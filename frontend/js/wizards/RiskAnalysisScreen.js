// Emulates Guidewire RiskAnalysisScreen.pcf / UWIssuesLV.pcf

function renderRiskAnalysisStep(container, period) {
    const issues = period.uwIssues || [];
    
    let issueRows = issues.map(iss => {
        const isOpen = iss.status === 'Open';
        const isApproved = iss.status === 'Approved';
        const badgeClass = isApproved ? 'gw-badge-bound' : (isOpen ? 'gw-badge-draft' : 'gw-badge-quoted');
        
        return `
            <tr>
                <td><strong>${iss.issueKey || 'UWIssue'}</strong></td>
                <td>${iss.shortDescription || ''}</td>
                <td><span class="gw-badge" style="background:#FEF3C7; color:#92400E;">BLOCK ${iss.approvalBlockingLevel ? iss.approvalBlockingLevel.toUpperCase() : 'QUOTE'}</span></td>
                <td><span class="gw-badge ${badgeClass}">${iss.status ? iss.status.toUpperCase() : 'OPEN'}</span></td>
                <td>
                    ${isOpen ? `
                        <button class="gw-btn gw-btn-success" style="padding:4px 10px; font-size:11px;" onclick="handleApproveUWIssue('${iss.issueKey}')">Approve Issue</button>
                    ` : `
                        <span style="color:#059669; font-weight:700; font-size:12px;">✓ Authorized by Senior UW</span>
                    `}
                </td>
            </tr>
        `;
    }).join('');

    container.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Underwriting Risk Analysis & Referral Issues (RiskAnalysisScreen.pcf)</span>
                <button class="gw-btn" style="padding:4px 10px; font-size:11px;" onclick="handleEvaluateUWRules()">Re-Run UW Rules</button>
            </div>
            <div class="gw-panel-body">
                <p style="font-size:13px; color:#475569; margin-bottom:16px;">
                    Underwriting rules evaluate policy risks and generate referral issues. Open issues with <strong>BLOCK QUOTE</strong> or <strong>BLOCK BIND</strong> must be approved by a Senior Underwriter before proceeding.
                </p>

                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Issue Key</th>
                            <th>Description</th>
                            <th>Blocking Level</th>
                            <th>Status</th>
                            <th>Underwriter Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${issueRows.length > 0 ? issueRows : '<tr><td colspan="5" style="color:#059669; font-weight:600;">✓ No underwriting issues triggered. Risk evaluation clear.</td></tr>'}
                    </tbody>
                </table>
            </div>
        </div>
    `;
}

async function handleEvaluateUWRules() {
    if (!currentJob) return;
    try {
        const res = await fetch(`${API_BASE}/uw/evaluate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: currentJob.jobNumber })
        });
        if (res.ok) {
            currentJob = await res.json();
            renderWizardStep(4);
        }
    } catch (e) {
        console.error("Failed to evaluate UW rules", e);
    }
}

async function handleApproveUWIssue(issueKey) {
    if (!currentJob) return;
    try {
        const res = await fetch(`${API_BASE}/uw/approve-issue`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: currentJob.jobNumber, issueKey: issueKey })
        });
        if (res.ok) {
            currentJob = await res.json();
            renderWizardStep(4);
        } else {
            alert('Failed to approve underwriting issue');
        }
    } catch (e) {
        alert('Error approving issue');
    }
}
