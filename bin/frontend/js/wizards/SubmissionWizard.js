// Guidewire PolicyCenter Submission Wizard Engine

function navigateToStep(stepNum) {
    currentStep = stepNum;
    document.querySelectorAll('.gw-step-item').forEach(el => el.classList.remove('active'));
    const target = document.getElementById(`step-${stepNum}`);
    if (target) target.classList.add('active');

    renderWizardStep(stepNum);
}

function renderWizardStep(stepNum) {
    if (!currentJob || !currentJob.policyPeriod) return;

    const period = currentJob.policyPeriod;
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const badge = document.getElementById('job-status-badge');
    const btnQuote = document.getElementById('btn-quote');
    const btnBind = document.getElementById('btn-bind');

    heading.innerText = `Submission (${currentJob.jobNumber}) - ${period.productName || 'Commercial Property'}`;
    badge.innerText = period.status.toUpperCase();
    badge.className = `gw-badge gw-badge-${period.status.toLowerCase()}`;

    if (period.status === 'Quoted') {
        btnQuote.style.display = 'none';
        btnBind.style.display = 'inline-block';
    } else if (period.status === 'Bound' || period.status === 'Issued') {
        btnQuote.style.display = 'none';
        btnBind.style.display = 'none';
    } else {
        btnQuote.style.display = 'inline-block';
        btnBind.style.display = 'none';
    }

    if (stepNum === 1) renderPolicyInfoStep(workArea, period);
    else if (stepNum === 2) {
        if (period.productCode === 'CommercialAuto' || period.productCode === 'PersonalAuto') {
            renderAutoLineStep(workArea, period);
        } else {
            renderLocationsStep(workArea, period);
        }
    }
    else if (stepNum === 3) renderCoveragesStep(workArea, period);
    else if (stepNum === 4) renderRiskAnalysisStep(workArea, period);
    else if (stepNum === 5) renderQuoteStep(workArea, period);
    else if (stepNum === 6) renderIssueStep(workArea, period);
}

function renderPolicyInfoStep(container, period) {
    const insured = period.primaryNamedInsured || {};
    const acc = period.account || {};

    container.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Policy Information & Product Line Selection</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Product Line</label>
                        <select id="sel-prod-code" onchange="handleProductCodeChange(this.value)">
                            <option value="CommercialProperty" ${period.productCode === 'CommercialProperty' ? 'selected' : ''}>Commercial Property</option>
                            <option value="CommercialAuto" ${period.productCode === 'CommercialAuto' ? 'selected' : ''}>Commercial Auto (CA)</option>
                            <option value="PersonalAuto" ${period.productCode === 'PersonalAuto' ? 'selected' : ''}>Personal Auto (PA)</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Account Number</label>
                        <input type="text" value="${acc.accountNumber || ''}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Primary Named Insured</label>
                        <input type="text" value="${insured.name || ''}">
                    </div>
                    <div class="gw-field">
                        <label>Tax ID / FEIN</label>
                        <input type="text" value="${insured.taxID || '12-3456789'}">
                    </div>
                    <div class="gw-field">
                        <label>Effective Date</label>
                        <input type="date" value="${period.effectiveDate || ''}">
                    </div>
                    <div class="gw-field">
                        <label>Expiration Date</label>
                        <input type="date" value="${period.expirationDate || ''}">
                    </div>
                </div>
            </div>
        </div>

        <div id="claims-step-area" style="margin-top:20px;"></div>

        <div style="display:flex; justify-content:flex-end; margin-top:20px;">
            <button class="gw-btn gw-btn-primary" onclick="navigateToStep(2)">Next: Exposure Details &rarr;</button>
        </div>
    `;

    const claimsArea = document.getElementById('claims-step-area');
    if (claimsArea && typeof renderClaimsStep === 'function') {
        renderClaimsStep(claimsArea, period);
    }
}

function handleProductCodeChange(val) {
    if (!currentJob || !currentJob.policyPeriod) return;
    currentJob.policyPeriod.productCode = val;
    currentJob.policyPeriod.productName = val === 'CommercialAuto' ? 'Commercial Auto' : (val === 'PersonalAuto' ? 'Personal Auto' : 'Commercial Property');
}

function renderLocationsStep(container, period) {
    const buildings = period.buildings || [];
    const b1 = buildings.length > 0 ? buildings[0] : { description: 'Main Building', constructionType: 'Joisted Masonry', buildingLimit: 1000000, contentsLimit: 250000 };

    container.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Location #1: Primary Commercial Facility</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Building Description</label>
                        <input type="text" id="inp-bldg-desc" value="${b1.description || ''}">
                    </div>
                    <div class="gw-field">
                        <label>Construction Type</label>
                        <select id="inp-const-type">
                            <option value="Frame" ${b1.constructionType === 'Frame' ? 'selected' : ''}>Frame (Rate Factor: 1.45)</option>
                            <option value="Joisted Masonry" ${b1.constructionType === 'Joisted Masonry' ? 'selected' : ''}>Joisted Masonry (Rate Factor: 1.20)</option>
                            <option value="Non-Combustible" ${b1.constructionType === 'Non-Combustible' ? 'selected' : ''}>Non-Combustible (Rate Factor: 1.00)</option>
                            <option value="Fire Resistive" ${b1.constructionType === 'Fire Resistive' ? 'selected' : ''}>Fire Resistive (Rate Factor: 0.75)</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Building Limit ($)</label>
                        <input type="number" id="inp-bldg-limit" value="${b1.buildingLimit || 1000000}">
                    </div>
                    <div class="gw-field">
                        <label>Contents Limit ($)</label>
                        <input type="number" id="inp-cnt-limit" value="${b1.contentsLimit || 250000}">
                    </div>
                </div>
            </div>
        </div>

        <div style="display:flex; justify-content:space-between;">
            <button class="gw-btn" onclick="navigateToStep(1)">&larr; Back</button>
            <button class="gw-btn gw-btn-primary" onclick="saveBuildingStepAndNext()">Next: Coverages & Terms &rarr;</button>
        </div>
    `;
}

function saveBuildingStepAndNext() {
    const constType = document.getElementById('inp-const-type').value;
    const bldgLimit = document.getElementById('inp-bldg-limit').value;
    const cntLimit = document.getElementById('inp-cnt-limit').value;

    if (currentJob && currentJob.policyPeriod && currentJob.policyPeriod.buildings && currentJob.policyPeriod.buildings.length > 0) {
        const b = currentJob.policyPeriod.buildings[0];
        b.constructionType = constType;
        b.buildingLimit = parseFloat(bldgLimit);
        b.contentsLimit = parseFloat(cntLimit);
    }
    navigateToStep(3);
}

function renderCoveragesStep(container, period) {
    const buildings = period.buildings || [];
    const b1 = buildings.length > 0 ? buildings[0] : { buildingLimit: 1000000, contentsLimit: 250000 };

    container.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Commercial Property Coverages & Deductible Terms</div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Coverage Pattern</th>
                            <th>Insured Value Limit ($)</th>
                            <th>Deductible ($)</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><strong>Building Coverage (CPBldgCov)</strong></td>
                            <td>$${(b1.buildingLimit || 0).toLocaleString()}</td>
                            <td>$1,000 Standard</td>
                            <td><span class="gw-badge gw-badge-bound">ELECTED</span></td>
                        </tr>
                        <tr>
                            <td><strong>Business Personal Property (CPBldgContentsCov)</strong></td>
                            <td>$${(b1.contentsLimit || 0).toLocaleString()}</td>
                            <td>$1,000 Standard</td>
                            <td><span class="gw-badge gw-badge-bound">ELECTED</span></td>
                        </tr>
                        <tr>
                            <td><strong>Business Income & Extra Expense</strong></td>
                            <td>$100,000 (Included)</td>
                            <td>72 Hour Waiting Period</td>
                            <td><span class="gw-badge gw-badge-quoted">INCLUDED</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div style="display:flex; justify-content:space-between;">
            <button class="gw-btn" onclick="navigateToStep(2)">&larr; Back</button>
            <button class="gw-btn gw-btn-primary" onclick="navigateToStep(4)">Next: Risk Analysis & Rules &rarr;</button>
        </div>
    `;
}

function renderRiskAnalysisStep(container, period) {
    container.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Underwriting Rules & Issue Evaluation</div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Rule ID</th>
                            <th>Underwriting Evaluation Description</th>
                            <th>Blocking Level</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>UW-PROP-101</td>
                            <td>Building Limit exceeds $1,000,000 auto-approval threshold</td>
                            <td>Underwriter Approval</td>
                            <td><span class="gw-badge gw-badge-bound">APPROVED (Superuser)</span></td>
                        </tr>
                        <tr>
                            <td>UW-PROP-204</td>
                            <td>Construction Type: Joisted Masonry hazard review</td>
                            <td>Informational</td>
                            <td><span class="gw-badge gw-badge-quoted">PASSED</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div style="display:flex; justify-content:space-between;">
            <button class="gw-btn" onclick="navigateToStep(3)">&larr; Back</button>
            <button class="gw-btn gw-btn-primary" onclick="requestQuote()">Request Quote &rarr;</button>
        </div>
    `;
}

function renderQuoteStep(container, period) {
    const costs = period.costs || [];
    let rows = costs.map(c => `
        <tr>
            <td>${c.costType}</td>
            <td>${c.description}</td>
            <td style="text-align:right;"><strong>$${c.actualAmount.toFixed(2)}</strong></td>
        </tr>
    `).join('');

    container.innerHTML = `
        <div class="gw-financial-box">
            <div>
                <div style="font-size:12px; font-weight:700; text-transform:uppercase; letter-spacing:1px; color:#93C5FD;">Total Quoted Annual Cost</div>
                <div class="gw-financial-val">$${(period.totalCost || 0).toFixed(2)}</div>
            </div>
            <div>
                <span class="gw-badge gw-badge-quoted" style="font-size:13px; padding:8px 16px;">STATUS: QUOTED</span>
            </div>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">Rating Sheet & Cost Breakdown Details</div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Cost Type</th>
                            <th>Line Item Description</th>
                            <th style="text-align:right;">Amount ($)</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${rows.length > 0 ? rows : '<tr><td colspan="3">No rating breakdown calculated yet. Click "Request Quote".</td></tr>'}
                    </tbody>
                    <tfoot>
                        <tr style="font-weight:700; background-color:#F1F5F9;">
                            <td colspan="2">Net Base Premium</td>
                            <td style="text-align:right;">$${(period.totalPremium || 0).toFixed(2)}</td>
                        </tr>
                        <tr style="font-weight:700; background-color:#F1F5F9;">
                            <td colspan="2">Taxes & Admin Fees</td>
                            <td style="text-align:right;">$${(period.taxAndFees || 0).toFixed(2)}</td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>

        <div id="billing-container" style="margin-bottom:20px;"></div>

        <div style="display:flex; justify-content:space-between;">
            <button class="gw-btn" onclick="navigateToStep(4)">&larr; Back</button>
            <button class="gw-btn gw-btn-success" onclick="bindAndIssue()">Bind & Issue Policy &rarr;</button>
        </div>
    `;

    const billingDiv = document.getElementById('billing-container');
    if (billingDiv) renderBillingStep(billingDiv, period);
}

function renderIssueStep(container, period) {
    const polNum = period.policyNumber || (currentJob ? currentJob.jobNumber : 'SUB-5001');
    container.innerHTML = `
        <div class="gw-panel" style="margin-bottom:20px;">
            <div class="gw-panel-header">Policy Confirmation & Documents</div>
            <div class="gw-panel-body" style="text-align:center; padding:30px;">
                <div style="font-size:48px; margin-bottom:12px;">🎉</div>
                <h2 style="color:#166534; font-size:24px; font-weight:800; margin-bottom:8px;">Policy Successfully Bound & Issued!</h2>
                <p style="font-size:16px; color:#475569; margin-bottom:20px;">Policy Number: <strong style="color:#1E293B;">${polNum}</strong></p>

                <div style="display:flex; justify-content:center; gap:12px; margin-bottom:16px;">
                    <button class="gw-btn gw-btn-primary" onclick="window.open('${API_BASE}/documents/dec-page?job=${polNum}', '_blank')">📄 Print / Download Policy Dec Page (PDF/HTML)</button>
                    <button class="gw-btn" onclick="switchMainTab('policies')">View In-Force Policies Directory</button>
                </div>
            </div>
        </div>

        <div id="forms-container"></div>
    `;

    const formsDiv = document.getElementById('forms-container');
    if (formsDiv) renderFormsStep(formsDiv, period);
}

async function requestQuote() {
    if (!currentJob) return;

    const constType = document.getElementById('inp-const-type') ? document.getElementById('inp-const-type').value : 'Joisted Masonry';
    const bldgLimit = document.getElementById('inp-bldg-limit') ? document.getElementById('inp-bldg-limit').value : '1000000';
    const cntLimit = document.getElementById('inp-cnt-limit') ? document.getElementById('inp-cnt-limit').value : '250000';

    try {
        const res = await fetch(`${API_BASE}/jobs/quote`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                jobNumber: currentJob.jobNumber,
                constructionType: constType,
                buildingLimit: bldgLimit,
                contentsLimit: cntLimit
            })
        });

        if (!res.ok) {
            const err = await res.json();
            alert("Quote Error: " + err.error);
            return;
        }

        const updatedJob = await res.json();
        currentJob = updatedJob;
        navigateToStep(5);
    } catch (e) {
        alert("Failed to quote policy: " + e);
    }
}

async function bindAndIssue() {
    if (!currentJob) return;

    try {
        const res = await fetch(`${API_BASE}/jobs/bind`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                jobNumber: currentJob.jobNumber
            })
        });

        if (!res.ok) {
            const err = await res.json();
            alert("Bind Error: " + err.error);
            return;
        }

        const updatedJob = await res.json();
        currentJob = updatedJob;
        await loadPolicies();
        navigateToStep(6);
    } catch (e) {
        alert("Failed to bind policy: " + e);
    }
}

function saveDraft() {
    alert("Draft saved successfully for Submission " + (currentJob ? currentJob.jobNumber : ''));
}
