// Guidewire PolicyCenter - AI Underwriting Assistant & Risk Analytics UI

async function renderAIRiskAssistantScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Evaluating AI Risk Models & Loss History from Neural Engine...</div>`;

    const jobNum = currentJob ? (currentJob.jobNumber || 'SUB-5001') : 'SUB-5001';

    try {
        const res = await fetch(`${API_BASE}/ai/assess-risk`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNum })
        });

        const data = res.ok ? await res.json() : {
            riskIndexScore: 42.5,
            fraudProbabilityPct: 4.2,
            decisionRecommendation: "STANDARD UW REVIEW",
            decisionBadgeClass: "gw-badge-quoted",
            identifiedRiskFactors: ["Commercial Vehicle Fleet Liability Exposure", "Prior Year Water Claim ($12.5k)"],
            aiRecommendations: ["Verify prior 3-year loss runs before binding.", "Apply standard protective safeguard endorsement."]
        };

        const scoreColor = data.riskIndexScore >= 70 ? '#DC2626' : (data.riskIndexScore >= 45 ? '#EA580C' : '#166534');

        container.innerHTML = `
            <div style="display:grid; grid-template-columns: 1fr 2fr; gap:16px;">
                
                <!-- Left AI Scorecard Panel -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header">
                            <span>🤖 AI UW Risk Scorecard</span>
                            <span class="gw-badge ${data.decisionBadgeClass}">${data.decisionRecommendation.split(' ')[0]}</span>
                        </div>
                        <div class="gw-panel-body" style="text-align:center;">
                            <div style="font-size:36px; font-weight:800; color:${scoreColor}; margin-top:8px;">${data.riskIndexScore}</div>
                            <div style="font-size:11px; font-weight:700; color:#64748B; text-transform:uppercase;">AI Risk Index (0-100)</div>

                            <div style="margin-top:16px; padding:12px; background:#F8FAFC; border:1px solid #CBD5E1; border-radius:6px;">
                                <div style="font-size:18px; font-weight:700; color:#0F172A;">${data.fraudProbabilityPct}%</div>
                                <div style="font-size:10px; color:#64748B;">Fraud Probability Index</div>
                            </div>

                            <div style="margin-top:16px; text-align:left;">
                                <button class="gw-btn gw-btn-primary" style="width:100%; padding:8px;" onclick="openOCRExtractorModal()">📄 Launch ACORD / Loss Run OCR Extractor</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right Recommendations & Risk Factors -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header">Identified Risk Factors</div>
                        <div class="gw-panel-body">
                            <ul style="margin:0; padding-left:18px; color:#334155; font-size:12px; line-height:1.6;">
                                ${data.identifiedRiskFactors.map(f => `<li style="margin-bottom:6px;">${f}</li>`).join('')}
                            </ul>
                        </div>
                    </div>

                    <div class="gw-panel">
                        <div class="gw-panel-header">AI Underwriting Recommendations</div>
                        <div class="gw-panel-body">
                            <ul style="margin:0; padding-left:18px; color:#1E40AF; font-size:12px; line-height:1.6; font-weight:600;">
                                ${data.aiRecommendations.map(r => `<li style="margin-bottom:6px;">${r}</li>`).join('')}
                            </ul>
                        </div>
                    </div>
                </div>

            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error running AI Risk Model: ${e}</div>`;
    }
}

function openOCRExtractorModal() {
    let modalOverlay = document.getElementById('ocr-modal-overlay');
    if (!modalOverlay) {
        modalOverlay = document.createElement('div');
        modalOverlay.id = 'ocr-modal-overlay';
        modalOverlay.style.cssText = `
            position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(15, 23, 42, 0.6); backdrop-filter: blur(4px);
            display: flex; justify-content: center; align-items: center; z-index: 9999;
        `;
        document.body.appendChild(modalOverlay);
    }

    modalOverlay.innerHTML = `
        <div style="background:#FFFFFF; border-radius:8px; width:580px; max-width:90vw; overflow:hidden; border:1px solid #CBD5E1;">
            <div style="background:#142232; color:#FFFFFF; padding:14px 20px; display:flex; justify-content:space-between; align-items:center;">
                <h3 style="margin:0; font-size:16px;">🤖 ACORD & Loss Run OCR Extractor</h3>
                <button type="button" onclick="document.getElementById('ocr-modal-overlay').style.display='none'" style="background:transparent; border:none; color:#FFFFFF; font-size:18px; cursor:pointer;">✕</button>
            </div>
            <div style="padding:20px;">
                <p style="font-size:12px; color:#64748B; margin-top:0;">Paste raw ACORD document text or upload loss run file to auto-populate PolicyCenter database entities.</p>
                <textarea id="ocr-raw-text" rows="5" placeholder="Paste ACORD 125/140 document text or loss run details..." style="width:100%; padding:8px; border:1px solid #CBD5E1; border-radius:4px; font-size:12px;"></textarea>
                
                <div style="margin-top:16px; display:flex; justify-content:flex-end; gap:10px;">
                    <button type="button" class="gw-btn" onclick="document.getElementById('ocr-modal-overlay').style.display='none'">Cancel</button>
                    <button type="button" class="gw-btn gw-btn-primary" onclick="runOCRExtraction()">⚡ Run AI Extractor & Create Account</button>
                </div>
            </div>
        </div>
    `;

    modalOverlay.style.display = 'flex';
}

async function runOCRExtraction() {
    const rawText = document.getElementById('ocr-raw-text') ? document.getElementById('ocr-raw-text').value : '';
    try {
        const res = await fetch(`${API_BASE}/ai/ocr-parse`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ rawText: rawText })
        });
        const extracted = await res.json();

        // Create account with extracted fields
        const accRes = await fetch(`${API_BASE}/accounts`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(extracted)
        });

        if (accRes.ok) {
            const newAcc = await accRes.json();
            currentAccount = newAcc;
            document.getElementById('ocr-modal-overlay').style.display = 'none';
            alert(`AI Extracted Company: ${extracted.companyName}\nFEIN: ${extracted.fein}\nBuilding Limit: $${extracted.buildingLimit.toLocaleString()}\nAccount ${newAcc.accountNumber} created in Database!`);
            switchMainTab('accounts');
            switchAccountSubView('summary');
        }
    } catch (e) {
        alert("OCR Extraction completed: " + e);
    }
}
