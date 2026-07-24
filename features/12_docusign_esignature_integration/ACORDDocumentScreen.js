// Guidewire PolicyCenter - ACORD 125/140 & Policy Binder Document Viewer UI with DocuSign Accelerator Integration

async function renderACORDDocumentScreen(container) {
    let envelopesHtml = '';
    try {
        const res = await fetch(`${API_BASE}/esignature/envelopes`);
        const envelopes = res.ok ? await res.json() : [];
        envelopesHtml = envelopes.map(env => `
            <tr style="border-bottom:1px solid #E2E8F0; font-size:12px;">
                <td style="padding:8px; font-weight:700; color:#1E40AF;">${env.envelopeId || env.id}</td>
                <td style="padding:8px;">${env.signerName} &lt;${env.signerEmail}&gt;</td>
                <td style="padding:8px; font-weight:600;">${env.documentType}</td>
                <td style="padding:8px;">
                    <span class="gw-badge ${env.status === 'Completed' ? 'gw-badge-bound' : 'gw-badge-draft'}">${env.status}</span>
                </td>
                <td style="padding:8px; font-size:11px; color:#64748B;">${env.sentAt ? env.sentAt.substring(0, 16).replace('T', ' ') : ''}</td>
                <td style="padding:8px; text-align:right;">
                    ${env.status !== 'Completed' ? `
                        <button class="gw-btn gw-btn-primary" style="padding:2px 8px; font-size:10px;" onclick="simulateDocuSignWebhook('${env.envelopeId}')">⚡ Simulate Sign Webhook</button>
                    ` : `
                        <span style="color:#059669; font-weight:700;">✓ Auto-Attached (pc_document)</span>
                    `}
                </td>
            </tr>
        `).join('');
    } catch (e) {
        console.error('Error fetching envelopes:', e);
    }

    container.innerHTML = `
        <div style="display:grid; grid-template-columns: 1fr 3fr; gap:16px;">
            
            <!-- Document Picker Sidebar -->
            <div class="gw-panel">
                <div class="gw-panel-header">Document Library</div>
                <div class="gw-panel-body" style="padding:0;">
                    <ul class="gw-tree-list" style="padding:8px;">
                        <li class="gw-tree-item active" onclick="loadDocumentFrame('acord125')">📄 ACORD 125 (Commercial App)</li>
                        <li class="gw-tree-item" onclick="loadDocumentFrame('acord140')">🏢 ACORD 140 (Property Schedule)</li>
                        <li class="gw-tree-item" onclick="loadDocumentFrame('coi')">📜 ACORD 25 (Certificate of Ins)</li>
                        <li class="gw-tree-item" onclick="loadDocumentFrame('dec')">📋 Policy Dec Page (CP-3451127)</li>
                    </ul>
                </div>
            </div>

            <!-- Printable Document Frame & Accelerator Bar -->
            <div>
                <div class="gw-panel">
                    <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                        <span id="doc-title-header">ACORD 125 Commercial Application</span>
                        <div class="gw-btn-group">
                            <button class="gw-btn gw-btn-primary" style="font-size:11px;" onclick="sendDocuSignEnvelopePrompt()">✍️ Send for DocuSign E-Signature</button>
                            <button class="gw-btn" style="font-size:11px;" onclick="printActiveDocument()">🖨️ Print / Save PDF</button>
                        </div>
                    </div>
                    <div class="gw-panel-body" style="padding:0;">
                        <iframe id="doc-preview-iframe" src="${API_BASE}/documents/acord125" style="width:100%; height:450px; border:none;"></iframe>
                    </div>
                </div>

                <!-- Marketplace Accelerator: DocuSign Envelopes Ledger -->
                <div class="gw-panel" style="margin-top:16px; border-left:4px solid #4F46E5;">
                    <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                        <span>✍️ Guidewire Marketplace Accelerator: DocuSign E-Signature Ledger</span>
                        <span class="gw-badge gw-badge-bound">Live Webhook Synced</span>
                    </div>
                    <div class="gw-panel-body" style="padding:0; max-height:220px; overflow-y:auto;">
                        <table style="width:100%; border-collapse:collapse;">
                            <thead style="background:#F8FAFC; font-size:11px; text-transform:uppercase; color:#475569; border-bottom:1px solid #E2E8F0;">
                                <tr>
                                    <th style="padding:8px; text-align:left;">Envelope ID</th>
                                    <th style="padding:8px; text-align:left;">Signer Details</th>
                                    <th style="padding:8px; text-align:left;">Document Type</th>
                                    <th style="padding:8px; text-align:left;">Status</th>
                                    <th style="padding:8px; text-align:left;">Sent At</th>
                                    <th style="padding:8px; text-align:right;">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${envelopesHtml || '<tr><td colspan="6" style="padding:16px; text-align:center; color:#94A3B8;">No active e-signature envelopes found.</td></tr>'}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    `;
}

function loadDocumentFrame(docType) {
    const iframe = document.getElementById('doc-preview-iframe');
    const header = document.getElementById('doc-title-header');
    if (!iframe || !header) return;

    if (docType === 'acord140') {
        header.innerText = 'ACORD 140 Property Section Schedule';
        iframe.src = `${API_BASE}/documents/acord140`;
    } else if (docType === 'coi') {
        header.innerText = 'ACORD 25 Certificate of Liability Insurance';
        iframe.src = `${API_BASE}/portal/coi`;
    } else if (docType === 'dec') {
        header.innerText = 'Commercial Policy Declarations (CP-3451127)';
        iframe.src = `${API_BASE}/documents/dec-page`;
    } else {
        header.innerText = 'ACORD 125 Commercial Application';
        iframe.src = `${API_BASE}/documents/acord125`;
    }
}

function printActiveDocument() {
    const iframe = document.getElementById('doc-preview-iframe');
    if (iframe && iframe.contentWindow) {
        iframe.contentWindow.print();
    }
}

async function sendDocuSignEnvelopePrompt() {
    const email = prompt("Enter Signer Email Address for DocuSign E-Signature Delivery:", "jane.doe@acmelogistics.com");
    if (!email) return;

    try {
        const res = await fetch(`${API_BASE}/esignature/send`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                jobNumber: 'SUB-5001',
                policyNumber: 'POL-3451127',
                signerName: 'Jane Doe (Insured Principal)',
                signerEmail: email,
                documentType: 'ACORD 125 Commercial Insurance Application'
            })
        });
        const data = await res.json();
        alert('✍️ DocuSign Envelope Dispatched!\nEnvelope UUID: ' + data.envelope.envelopeId + '\nSigner: ' + email + '\nStatus: Sent');
        location.reload();
    } catch (e) {
        alert('Failed to dispatch envelope: ' + e);
    }
}

async function simulateDocuSignWebhook(envelopeId) {
    try {
        const res = await fetch(`${API_BASE}/esignature/webhook/complete/${envelopeId}`, {
            method: 'POST'
        });
        const data = await res.json();
        alert('⚡ DocuSign Webhook Received!\nEnvelope: ' + envelopeId + '\nStatus: Completed\nAuto-Attached to Guidewire Entity: pc_document (' + data.document.publicID + ')');
        location.reload();
    } catch (e) {
        alert('Webhook simulation failed: ' + e);
    }
}
