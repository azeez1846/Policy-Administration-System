// Guidewire PolicyCenter - Policy Change (Endorsement) Transaction Wizard UI

let activePolicyChangeQuote = null;

async function showPolicyChangeWizard(jobNumber, policyNumber) {
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');
    const subheaderBanner = document.getElementById('subheader-banner');

    if (subheaderBanner) subheaderBanner.style.display = 'none';

    workArea.innerHTML = `<div style="padding:40px; text-align:center; color:#64748B;"><div class="gw-spinner"></div> Initializing Mid-Term Policy Change Transaction...</div>`;

    try {
        const res = await fetch(`${API_BASE}/policy-change/start`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                jobNumber: jobNumber || 'SUB-5001',
                policyNumber: policyNumber || 'CP-8472910',
                effectiveDate: new Date().toISOString().split('T')[0],
                changeReason: 'Increase Building Coverage Limit'
            })
        });

        const data = await res.json();
        const chgJobNum = data.jobNumber || jobNumber || 'CHG-84702';
        const polNum = data.policyNumber || policyNumber || 'CP-8472910';
        const period = data.policyPeriod || {};
        const bldg = (period.buildings && period.buildings.length > 0) ? period.buildings[0] : { buildingLimit: 1500000, contentsLimit: 250000, constructionType: 'Joisted Masonry' };

        if (heading) heading.innerText = `Policy Change (Endorsement) - ${chgJobNum}`;
        if (badge) {
            badge.innerText = 'DRAFT ENDORSEMENT';
            badge.className = 'gw-badge gw-badge-draft';
            badge.style.display = 'inline-block';
        }

        renderPolicyChangeWizardUI(workArea, chgJobNum, polNum, data, bldg);
    } catch (e) {
        workArea.innerHTML = `<div style="color:red; padding:20px;">Error starting Policy Change transaction: ${e}</div>`;
    }
}

function renderPolicyChangeWizardUI(container, chgJobNum, polNum, startData, bldg) {
    const today = new Date().toISOString().split('T')[0];

    container.innerHTML = `
        <!-- Policy Change Header Banner -->
        <div style="background: linear-gradient(135deg, #0F172A 0%, #1E293B 100%); color: #FFFFFF; padding: 20px 24px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
            <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px;">
                <div>
                    <div style="font-size:12px; text-transform:uppercase; letter-spacing:1px; color:#60A5FA; font-weight:700;">Guidewire PolicyCenter • Policy Change Transaction</div>
                    <h2 style="margin:4px 0 0 0; font-size:20px; font-weight:700; color:#FFFFFF;">Policy #${polNum} — Job #${chgJobNum}</h2>
                    <div style="font-size:13px; color:#94A3B8; margin-top:4px;">Insured: <strong>${startData.accountHolder || 'Acme Logistics Inc.'}</strong> | Product: <strong>Commercial Property</strong></div>
                </div>
                <div style="text-align:right;">
                    <span id="chg-status-pill" class="gw-badge gw-badge-draft" style="font-size:12px; padding:6px 14px;">DRAFT ENDORSEMENT</span>
                    <div style="font-size:11px; color:#CBD5E1; margin-top:6px;">Term: 2026-01-01 to 2027-01-01</div>
                </div>
            </div>
        </div>

        <!-- Wizard Navigation Tabs -->
        <div style="display:flex; gap:8px; border-bottom:2px solid #E2E8F0; margin-bottom:20px; overflow-x:auto;">
            <button class="gw-tab-btn active" id="btn-tab-1" onclick="switchChangeTab('tab-1')">1. Parameters & Reason</button>
            <button class="gw-tab-btn" id="btn-tab-2" onclick="switchChangeTab('tab-2')">2. Coverables & Property</button>
            <button class="gw-tab-btn" id="btn-tab-3" onclick="switchChangeTab('tab-3')">3. Limits & Deductibles</button>
            <button class="gw-tab-btn" id="btn-tab-4" onclick="switchChangeTab('tab-4')">4. Prorated Delta Rating</button>
            <button class="gw-tab-btn" id="btn-tab-5" onclick="switchChangeTab('tab-5')">5. UW Authority & OOS</button>
        </div>

        <!-- Tab 1: Transaction Parameters -->
        <div id="tab-1" class="chg-tab-content" style="display:block;">
            <div class="gw-panel">
                <div class="gw-panel-header">Policy Change Parameters & Effective Date</div>
                <div class="gw-panel-body">
                    <div class="gw-form-grid">
                        <div class="gw-field">
                            <label>Policy Number</label>
                            <input type="text" value="${polNum}" readonly style="background:#F8FAFC; color:#64748B;">
                        </div>
                        <div class="gw-field">
                            <label>Effective Date of Change *</label>
                            <input type="date" id="chg-eff-date" value="${today}">
                        </div>
                        <div class="gw-field">
                            <label>Reason for Change *</label>
                            <select id="chg-reason">
                                <option value="Increase Building Coverage Limit">Increase Building Coverage Limit</option>
                                <option value="Add Secondary Commercial Building">Add Secondary Commercial Building</option>
                                <option value="Update Address / Location Details">Update Address / Location Details</option>
                                <option value="Coverage Terms & Deductible Adjustment">Coverage Terms & Deductible Adjustment</option>
                                <option value="Add Additional Insured Blanket Endorsement">Add Additional Insured Blanket Endorsement</option>
                            </select>
                        </div>
                        <div class="gw-field">
                            <label>Initiated By Underwriter</label>
                            <input type="text" value="Super User (Senior Underwriter)" readonly style="background:#F8FAFC;">
                        </div>
                    </div>
                    <div style="margin-top:16px;">
                        <label style="font-size:12px; font-weight:600; color:#334155; display:block; margin-bottom:6px;">Detailed Endorsement Description & Underwriter Rationale</label>
                        <textarea id="chg-description" rows="3" style="width:100%; border:1px solid #CBD5E1; border-radius:6px; padding:8px; font-size:13px;" placeholder="Enter specific endorsement requested details, loss run audit confirmation, or property appraisal notes..."></textarea>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tab 2: Property & Coverables -->
        <div id="tab-2" class="chg-tab-content" style="display:none;">
            <div class="gw-panel">
                <div class="gw-panel-header">Building & Property Coverables Schedule</div>
                <div class="gw-panel-body">
                    <div class="gw-form-grid">
                        <div class="gw-field">
                            <label>Building #1 Name</label>
                            <input type="text" id="chg-bldg-name" value="HQ Commercial Facility">
                        </div>
                        <div class="gw-field">
                            <label>Construction Type</label>
                            <select id="chg-construction">
                                <option value="Joisted Masonry" ${bldg.constructionType === 'Joisted Masonry' ? 'selected' : ''}>Joisted Masonry (Factor: 1.20)</option>
                                <option value="Frame" ${bldg.constructionType === 'Frame' ? 'selected' : ''}>Frame (Factor: 1.45)</option>
                                <option value="Non-Combustible" ${bldg.constructionType === 'Non-Combustible' ? 'selected' : ''}>Non-Combustible (Factor: 1.00)</option>
                                <option value="Fire Resistive" ${bldg.constructionType === 'Fire Resistive' ? 'selected' : ''}>Fire Resistive (Factor: 0.75)</option>
                            </select>
                        </div>
                        <div class="gw-field">
                            <label>Building Coverage Limit ($) *</label>
                            <input type="number" id="chg-bldg-limit" value="${bldg.buildingLimit || 1500000}" step="50000">
                        </div>
                        <div class="gw-field">
                            <label>Business Personal Property (Contents) Limit ($)</label>
                            <input type="number" id="chg-contents-limit" value="${bldg.contentsLimit || 250000}" step="25000">
                        </div>
                        <div class="gw-field">
                            <label>Automatic Sprinkler Protection</label>
                            <select id="chg-sprinkler">
                                <option value="100%">100% Fully Sprinklered (NFPA 13 Standard)</option>
                                <option value="Partial">Partial Sprinklered</option>
                                <option value="None">None</option>
                            </select>
                        </div>
                        <div class="gw-field">
                            <label>Fire & Burglar Alarm Protection</label>
                            <select id="chg-alarm">
                                <option value="Central Station">Central Station Signal Alarm</option>
                                <option value="Local Alarm">Local Gong Alarm Only</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tab 3: Limits & Deductibles -->
        <div id="tab-3" class="chg-tab-content" style="display:none;">
            <div class="gw-panel">
                <div class="gw-panel-header">Policy Limits, Deductibles & Endorsements</div>
                <div class="gw-panel-body">
                    <div class="gw-form-grid">
                        <div class="gw-field">
                            <label>Property Deductible ($)</label>
                            <select id="chg-deductible">
                                <option value="1000">$1,000 Standard All Peril</option>
                                <option value="2500">$2,500 Per Occurrence</option>
                                <option value="5000">$5,000 High Deductible</option>
                            </select>
                        </div>
                        <div class="gw-field">
                            <label>General Liability Occurrence Limit ($)</label>
                            <select id="chg-gl-limit">
                                <option value="1000000">$1,000,000 / $2,000,000 Aggregate</option>
                                <option value="2000000">$2,000,000 / $4,000,000 Aggregate</option>
                            </select>
                        </div>
                    </div>
                    <div style="margin-top:16px;">
                        <label style="font-size:12px; font-weight:600; color:#334155; display:block; margin-bottom:8px;">Endorsement Clauses & Additional Insured Form</label>
                        <div style="display:flex; flex-direction:column; gap:8px; font-size:13px; color:#475569;">
                            <label><input type="checkbox" id="chk-addl-insured" checked> CG 20 10 04 13 - Additional Insured Owners, Lessees or Contractors</label>
                            <label><input type="checkbox" id="chk-waiver-sub" checked> CG 24 04 05 09 - Waiver of Transfer of Rights of Recovery (Waiver of Subrogation)</label>
                            <label><input type="checkbox" id="chk-equip-breakdown"> CP 10 33 - Equipment Breakdown Protection Extension</label>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tab 4: Prorated Financial Delta Rating -->
        <div id="tab-4" class="chg-tab-content" style="display:none;">
            <div class="gw-panel">
                <div class="gw-panel-header">Prorated Premium Rating Delta Engine</div>
                <div class="gw-panel-body">
                    <div id="chg-quote-preview-box">
                        <div style="padding:20px; text-align:center; color:#64748B; background:#F8FAFC; border-radius:6px; border:1px dashed #CBD5E1;">
                            Click <strong>"Calculate Delta Quote"</strong> below to execute mid-term rating engine and calculate prorated delta charges.
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tab 5: Underwriting Authority & OOS -->
        <div id="tab-5" class="chg-tab-content" style="display:none;">
            <div class="gw-panel">
                <div class="gw-panel-header">Underwriting Authority & Out-of-Sequence Evaluation</div>
                <div class="gw-panel-body">
                    <div id="chg-uw-checks-box">
                        <div style="padding:16px; background:#EFF6FF; border:1px solid #BFDBFE; border-radius:6px; font-size:13px; color:#1E40AF;">
                            <strong>Underwriting Authority Rules Engine:</strong><br>
                            • Building Limit Change Threshold: Up to $2,500,000 (Senior UW Authority)<br>
                            • Net Premium Delta Threshold: Up to $1,000 (Senior UW Authority)<br>
                            • Out-of-Sequence (OOS) Retroactive Buffer: 15 Days
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Action Control Buttons -->
        <div style="margin-top:24px; padding:16px; background:#FFFFFF; border:1px solid #E2E8F0; border-radius:8px; display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:12px; box-shadow:0 1px 3px rgba(0,0,0,0.05);">
            <div style="display:flex; gap:10px;">
                <button class="gw-btn gw-btn-primary" style="background:#0284C7;" onclick="executeCalculatePolicyChangeQuote('${chgJobNum}')">📊 Calculate Delta Quote</button>
                <button class="gw-btn" style="background:#475569; color:#fff;" onclick="openPolicyChangeACORDModal('${chgJobNum}')">📜 Preview Endorsement ACORD</button>
            </div>
            <div style="display:flex; gap:10px;">
                <button class="gw-btn gw-btn-success" style="background:#059669; font-size:14px; font-weight:700; padding:8px 20px;" onclick="executeBindPolicyChangeTransaction('${chgJobNum}')">⚡ Bind & Issue Policy Change</button>
                <button class="gw-btn" onclick="renderPoliciesTab()">Cancel Job</button>
            </div>
        </div>
    `;

    // Inject Tab CSS styling if not present
    if (!document.getElementById('chg-tab-style')) {
        const style = document.createElement('style');
        style.id = 'chg-tab-style';
        style.innerHTML = `
            .gw-tab-btn { background: transparent; border: none; padding: 10px 16px; font-size: 13px; font-weight: 600; color: #64748B; cursor: pointer; border-bottom: 3px solid transparent; transition: all 0.2s; }
            .gw-tab-btn:hover { color: #0284C7; }
            .gw-tab-btn.active { color: #0284C7; border-bottom-color: #0284C7; background: #F0F9FF; border-radius: 6px 6px 0 0; }
        `;
        document.head.appendChild(style);
    }
}

function switchChangeTab(tabId) {
    document.querySelectorAll('.chg-tab-content').forEach(el => el.style.display = 'none');
    document.querySelectorAll('.gw-tab-btn').forEach(el => el.classList.remove('active'));

    const activeEl = document.getElementById(tabId);
    if (activeEl) activeEl.style.display = 'block';

    const btnEl = document.getElementById('btn-' + tabId);
    if (btnEl) btnEl.classList.add('active');
}

async function executeCalculatePolicyChangeQuote(chgJobNum) {
    const bldgLimit = parseFloat(document.getElementById('chg-bldg-limit').value) || 1500000;
    const contentsLimit = parseFloat(document.getElementById('chg-contents-limit').value) || 250000;
    const construction = document.getElementById('chg-construction').value;
    const effDate = document.getElementById('chg-eff-date').value;
    const reason = document.getElementById('chg-reason').value;

    const previewBox = document.getElementById('chg-quote-preview-box');
    const uwChecksBox = document.getElementById('chg-uw-checks-box');
    const statusPill = document.getElementById('chg-status-pill');

    previewBox.innerHTML = `<div style="padding:20px; text-align:center; color:#0284C7;"><div class="gw-spinner"></div> Executing Mid-Term Rating Engine & Calculating Proration...</div>`;

    try {
        const res = await fetch(`${API_BASE}/policy-change/quote`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                jobNumber: chgJobNum,
                effectiveDate: effDate,
                changeReason: reason,
                buildingLimit: bldgLimit,
                contentsLimit: contentsLimit,
                constructionType: construction
            })
        });

        if (!res.ok) {
            previewBox.innerHTML = `<div style="color:red; padding:16px;">Error calculating quote</div>`;
            return;
        }

        const data = await res.json();
        activePolicyChangeQuote = data;

        if (statusPill) {
            statusPill.innerText = 'QUOTED ENDORSEMENT';
            statusPill.className = 'gw-badge gw-badge-quoted';
        }

        // Render Financial Delta Table
        previewBox.innerHTML = `
            <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:16px; margin-bottom:20px;">
                <div style="background:#F8FAFC; border:1px solid #E2E8F0; padding:16px; border-radius:8px;">
                    <div style="font-size:11px; text-transform:uppercase; color:#64748B; font-weight:700;">Prior Term Premium</div>
                    <div style="font-size:22px; font-weight:800; color:#1E293B;">$${data.priorAnnualPremium.toLocaleString('en-US', {minimumFractionDigits:2})}</div>
                </div>
                <div style="background:#F0F9FF; border:1px solid #BAE6FD; padding:16px; border-radius:8px;">
                    <div style="font-size:11px; text-transform:uppercase; color:#0369A1; font-weight:700;">New Annualized Premium</div>
                    <div style="font-size:22px; font-weight:800; color:#0284C7;">$${data.newAnnualPremium.toLocaleString('en-US', {minimumFractionDigits:2})}</div>
                </div>
                <div style="background:#ECFDF5; border:1px solid #A7F3D0; padding:16px; border-radius:8px;">
                    <div style="font-size:11px; text-transform:uppercase; color:#047857; font-weight:700;">Prorated Premium Delta</div>
                    <div style="font-size:22px; font-weight:800; color:#059669;">+ $${data.proratedDeltaPremium.toLocaleString('en-US', {minimumFractionDigits:2})}</div>
                </div>
                <div style="background:#FEF3C7; border:1px solid #FDE68A; padding:16px; border-radius:8px;">
                    <div style="font-size:11px; text-transform:uppercase; color:#B45309; font-weight:700;">Net Total Charge</div>
                    <div style="font-size:22px; font-weight:800; color:#D97706;">+ $${data.totalDeltaCharge.toLocaleString('en-US', {minimumFractionDigits:2})}</div>
                </div>
            </div>

            <table class="gw-table" style="margin-top:10px;">
                <thead>
                    <tr>
                        <th>Financial Line Item</th>
                        <th>Prior Annual ($)</th>
                        <th>New Annual ($)</th>
                        <th>Term Days Remaining</th>
                        <th>Proration Factor</th>
                        <th style="text-align:right;">Prorated Net Charge ($)</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Commercial Building #1 Premium</td>
                        <td>$2,200.00</td>
                        <td>$${data.newAnnualPremium.toLocaleString('en-US', {minimumFractionDigits:2})}</td>
                        <td>${data.remainingDays} / ${data.totalTermDays} days</td>
                        <td>${data.prorationFactor}</td>
                        <td style="text-align:right; font-weight:700; color:#059669;">+ $${data.proratedDeltaPremium.toLocaleString('en-US', {minimumFractionDigits:2})}</td>
                    </tr>
                    <tr>
                        <td>State Premium Tax & Fees (5%)</td>
                        <td>$110.00</td>
                        <td>$${(data.newAnnualPremium * 0.05).toFixed(2)}</td>
                        <td>${data.remainingDays} days</td>
                        <td>${data.prorationFactor}</td>
                        <td style="text-align:right; font-weight:700; color:#059669;">+ $${data.proratedTaxDelta.toLocaleString('en-US', {minimumFractionDigits:2})}</td>
                    </tr>
                    <tr style="background:#F8FAFC; font-weight:700;">
                        <td colspan="5">TOTAL NET ENDORSEMENT CHARGE TO INSURED</td>
                        <td style="text-align:right; font-size:16px; color:#0284C7;">+ $${data.totalDeltaCharge.toLocaleString('en-US', {minimumFractionDigits:2})}</td>
                    </tr>
                </tbody>
            </table>
        `;

        // Render UW Referral Issues & OOS Status
        let uwHtml = '';
        if (data.uwIssues && data.uwIssues.length > 0) {
            uwHtml = data.uwIssues.map(issue => `
                <div style="padding:12px 16px; background:#FEF2F2; border-left:4px solid #EF4444; border-radius:4px; margin-bottom:10px; font-size:13px;">
                    <div style="font-weight:700; color:#991B1B;">⚠️ UW REFERRAL ISSUE: ${issue.shortDescription}</div>
                    <div style="color:#B91C1C; font-size:12px; margin-top:2px;">Severity: ${issue.severity} | Requires Senior Underwriter / Manager Sign-off prior to Binding.</div>
                </div>
            `).join('');
        } else {
            uwHtml = `
                <div style="padding:12px 16px; background:#F0FDF4; border-left:4px solid #22C55E; border-radius:4px; font-size:13px; color:#15803D;">
                    ✅ <strong>Underwriting Referral Clear:</strong> Policy Change is within Senior Underwriter authority limits. Ready to Bind & Issue immediately!
                </div>
            `;
        }

        if (data.isOutOfSequence) {
            uwHtml += `
                <div style="padding:12px 16px; background:#FFFBEB; border-left:4px solid #F59E0B; border-radius:4px; margin-top:10px; font-size:13px; color:#B45309;">
                    ⚡ <strong>Out-Of-Sequence (OOS) Transaction:</strong> Change effective date is retroactive. PolicyCenter OOS Engine will apply slice calculations to subsequent transactions.
                </div>
            `;
        }

        if (uwChecksBox) uwChecksBox.innerHTML = uwHtml;

        // Switch automatically to Tab 4 (Rating Delta)
        switchChangeTab('tab-4');
    } catch (e) {
        previewBox.innerHTML = `<div style="color:red; padding:16px;">Failed to calculate quote: ${e}</div>`;
    }
}

async function executeBindPolicyChangeTransaction(chgJobNum) {
    if (!confirm(`Are you sure you want to BIND and ISSUE Policy Change Endorsement for Job ${chgJobNum}?`)) {
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/policy-change/bind`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: chgJobNum, boundBy: 'Super User' })
        });

        if (res.ok) {
            const data = await res.json();
            alert(`✅ POLICY CHANGE BOUND & ISSUED!\n\nPolicy Number: ${data.policyNumber}\nJob Number: ${data.jobNumber}\n\nEndorsement has been written to the active Policy Ledger & SQLite Database!`);
            if (typeof loadJobs === 'function') await loadJobs();
            if (typeof loadPolicies === 'function') await loadPolicies();
            if (typeof renderPoliciesTab === 'function') renderPoliciesTab();
        } else {
            alert('Failed to bind policy change transaction.');
        }
    } catch (e) {
        alert('Network error binding policy change: ' + e);
    }
}

function openPolicyChangeACORDModal(chgJobNum) {
    const url = `${API_BASE}/policy-change/document/${chgJobNum}`;
    window.open(url, '_blank', 'width=900,height=750');
}
