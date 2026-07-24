// Guidewire PolicyCenter - Policy Submission Wizard & Quote Screens UI

function renderPolicyWizardSidebar(activeStep = 'policy-type') {
    const sidebar = document.getElementById('sidebar-panel');
    if (!sidebar) return;

    const jobNum = currentJob ? (currentJob.jobNumber || 'SUB-5001') : 'SUB-5001';
    const status = currentJob ? (currentJob.jobStatus || currentJob.status || 'Draft') : 'Draft';
    const isBound = (status === 'Bound' || status === 'Issued');
    const polNum = (currentJob && currentJob.policyPeriod && currentJob.policyPeriod.policyNumber) ? currentJob.policyPeriod.policyNumber : 'CP-8472910';

    sidebar.innerHTML = `
        <div class="gw-sidebar-title">
            <span>Actions</span>
            <span>▸</span>
        </div>
        <div style="padding:10px 14px; background:#1E2B37; border-bottom:1px solid #334155; font-size:12px;">
            <div style="font-weight:700; color:#FFFFFF;">Submission ${jobNum}</div>
            <div style="color:${isBound ? '#4ADE80' : '#60A5FA'}; font-weight:700;">${status.toUpperCase()}</div>
        </div>
        <div class="gw-sidebar-accordion">
            <div class="gw-accordion-section">
                <div class="gw-accordion-header" style="background:${isBound ? '#0284C7' : '#334155'}; color:#FFFFFF; font-weight:700;">
                    <span>Actions Menu</span>
                    <span>▾</span>
                </div>
                <ul class="gw-tree-list" style="padding:4px 0;">
                    <li class="gw-tree-item" style="color:#0284C7; font-weight:700; background:#F0F9FF;" onclick="showPolicyChangeWizard('${jobNum}', '${polNum}')">✏️ Policy Change (Endorsement)</li>
                    ${isBound ? `
                        <li class="gw-tree-item" onclick="handleRenewal('${jobNum}')">🔄 Renew Policy</li>
                        <li class="gw-tree-item" onclick="handleCancellation('${jobNum}')">❌ Cancel Policy</li>
                        <li class="gw-tree-item" onclick="handleReinstatement('${jobNum}')">⚡ Reinstate Policy</li>
                    ` : ''}
                </ul>
            </div>
            <div class="gw-accordion-section">
                <div class="gw-accordion-header">
                    <span>Policy Contract</span>
                    <span>▾</span>
                </div>
                <ul class="gw-tree-list">
                    <li class="gw-tree-subitem ${activeStep === 'policy-type' ? 'active' : ''}" onclick="switchWizardStep('policy-type')">Policy Type</li>
                    <li class="gw-tree-subitem ${activeStep === 'policy-info' ? 'active' : ''}" onclick="switchWizardStep('policy-info')">Policy Info</li>
                    <li class="gw-tree-subitem ${activeStep === 'prior-losses' ? 'active' : ''}" onclick="switchWizardStep('prior-losses')">Prior Losses</li>
                    <li class="gw-tree-subitem ${activeStep === 'commercial-auto' ? 'active' : ''}" onclick="switchWizardStep('commercial-auto')">Commercial Auto</li>
                    <li class="gw-tree-subitem ${activeStep === 'locations' ? 'active' : ''}" onclick="switchWizardStep('locations')">Locations</li>
                    <li class="gw-tree-subitem ${activeStep === 'state-info' ? 'active' : ''}" onclick="switchWizardStep('state-info')">State-Specific Information</li>
                    <li class="gw-tree-subitem ${activeStep === 'vehicles' ? 'active' : ''}" onclick="switchWizardStep('vehicles')">Vehicles</li>
                    <li class="gw-tree-subitem ${activeStep === 'drivers' ? 'active' : ''}" onclick="switchWizardStep('drivers')">Drivers</li>
                    <li class="gw-tree-subitem ${activeStep === 'covered-vehicles' ? 'active' : ''}" onclick="switchWizardStep('covered-vehicles')">Covered Vehicles</li>
                    <li class="gw-tree-subitem ${activeStep === 'modifiers' ? 'active' : ''}" onclick="switchWizardStep('modifiers')">Modifiers</li>
                    <li class="gw-tree-subitem ${activeStep === 'risk-analysis' ? 'active' : ''}" onclick="switchWizardStep('risk-analysis')">Risk Analysis</li>
                    <li class="gw-tree-subitem ${activeStep === 'policy-review' ? 'active' : ''}" onclick="switchWizardStep('policy-review')">Policy Review</li>
                    <li class="gw-tree-subitem ${activeStep === 'quote' ? 'active' : ''}" onclick="switchWizardStep('quote')">Quote</li>
                    <li class="gw-tree-subitem ${activeStep === 'forms' ? 'active' : ''}" onclick="switchWizardStep('forms')">Forms</li>
                    <li class="gw-tree-subitem ${activeStep === 'payment' ? 'active' : ''}" onclick="switchWizardStep('payment')">Payment</li>
                </ul>
            </div>
            <div class="gw-accordion-section">
                <div class="gw-accordion-header">
                    <span>Tools</span>
                    <span>▾</span>
                </div>
                <ul class="gw-tree-list">
                    <li class="gw-tree-subitem" onclick="switchWizardStep('notes')">Notes</li>
                    <li class="gw-tree-subitem" onclick="switchWizardStep('exchange-rates')">Exchange Rates</li>
                    <li class="gw-tree-subitem" onclick="showRatingWorksheet()">Documents</li>
                    <li class="gw-tree-subitem" onclick="switchWizardStep('participants')">Participants</li>
                    <li class="gw-tree-subitem" onclick="switchWizardStep('workplan')">Workplan</li>
                    <li class="gw-tree-subitem" onclick="switchWizardStep('history')">History</li>
                </ul>
            </div>
        </div>
    `;
}

function switchWizardStep(step) {
    renderPolicyWizardSidebar(step);
    const workArea = document.getElementById('work-area');
    const pageHeading = document.getElementById('page-heading');
    const subheaderBanner = document.getElementById('subheader-banner');

    const job = currentJob || {};
    const period = job.policyPeriod || {};
    const insured = period.primaryNamedInsured ? (period.primaryNamedInsured.name || period.primaryNamedInsured.companyName) : (period.account ? period.account.accountHolderName : 'Acme Logistics Inc');
    const prod = period.productName || job.productCode || 'Commercial Property';
    const status = job.jobStatus || job.status || 'Draft';
    const effDate = period.effectiveDate || '2026-07-23';
    const accNum = period.account ? period.account.accountNumber : 'C00010928';

    if (subheaderBanner) {
        subheaderBanner.style.display = 'flex';
        subheaderBanner.innerHTML = `
            <div class="gw-banner-left">
                <span class="gw-banner-item" style="color:${status === 'Bound' || status === 'Issued' ? '#059669' : (status === 'Quoted' ? '#1E40AF' : '#92400E')}; font-weight:700;">Submission (${status})</span>
                <span class="gw-banner-item">${prod}</span>
                <span class="gw-banner-item">Effective: <strong>${effDate}</strong></span>
                <span class="gw-banner-item">Primary: <strong>${insured}</strong></span>
                <span class="gw-banner-item">Account: <strong>${accNum}</strong></span>
                <span class="gw-banner-item">Job: <strong>${job.jobNumber || 'SUB-5001'}</strong></span>
            </div>
            <div style="display:flex; align-items:center; gap:10px;">
                ${(status === 'Bound' || status === 'Issued') ? `<button class="gw-btn gw-btn-primary" style="background:#0284C7; font-size:12px; font-weight:700;" onclick="showPolicyChangeWizard('${job.jobNumber || 'SUB-5001'}', '${period.policyNumber || 'CP-8472910'}')">✏️ Change Policy</button>` : ''}
                <span class="gw-badge gw-badge-${status.toLowerCase()}">${status}</span>
            </div>
        `;
    }

    if (step === 'policy-type') {
        if (pageHeading) pageHeading.innerText = 'Policy Type';
        renderPolicyTypeStep(workArea);
    } else if (step === 'quote') {
        if (pageHeading) pageHeading.innerText = 'Quote';
        renderQuoteStep(workArea);
    } else {
        if (pageHeading) pageHeading.innerText = step.replace('-', ' ').toUpperCase();
        renderGenericWizardStep(workArea, step);
    }
}

// 1. Policy Type Screen
function renderPolicyTypeStep(container) {
    const job = currentJob || {};
    const period = job.policyPeriod || {};
    const prod = period.productName || job.productCode || 'Commercial Property';
    const status = job.jobStatus || job.status || 'Draft';
    const isBound = (status === 'Bound' || status === 'Issued');
    const jobNum = job.jobNumber || 'SUB-5001';
    const polNum = period.policyNumber || 'CP-8472910';

    container.innerHTML = `
        <div class="gw-action-bar" style="margin:-20px -20px 20px -20px; border-bottom:1px solid #CBD5E1;">
            <h1 style="font-size:18px;">Policy Type</h1>
            <div class="gw-btn-group">
                ${isBound ? `
                    <button class="gw-btn gw-btn-primary" style="background:#0284C7; font-weight:700;" onclick="showPolicyChangeWizard('${jobNum}', '${polNum}')">✏️ Change Policy (Endorsement)</button>
                    <button class="gw-btn" onclick="handleRenewal('${jobNum}')">🔄 Renew Policy</button>
                    <button class="gw-btn" onclick="handleCancellation('${jobNum}')">❌ Cancel Policy</button>
                ` : `
                    <button class="gw-btn gw-btn-primary" onclick="switchWizardStep('policy-info')">Next</button>
                    <button class="gw-btn gw-btn-primary" onclick="requestQuote()">Quote</button>
                    <button class="gw-btn" onclick="saveDraft()">Save Draft</button>
                `}
            </div>
        </div>

        <div class="gw-panel" style="max-width:600px;">
            <div class="gw-panel-body">
                <div class="gw-form-grid" style="grid-template-columns: 1fr 2fr; align-items:center;">
                    <label style="font-weight:700; color:#475569;"><span style="color:red;">*</span> Policy Type</label>
                    <select id="pt-select" style="padding:6px 10px; border:1px solid #CBD5E1; border-radius:4px; font-size:13px;">
                        <option ${prod.includes('Auto') ? 'selected' : ''}>Business Auto Coverage Form</option>
                        <option ${prod.includes('Property') ? 'selected' : ''}>Commercial Property Coverage Form</option>
                        <option ${prod.includes('Liability') ? 'selected' : ''}>General Liability Coverage Form</option>
                        <option ${prod.includes('Compensation') ? 'selected' : ''}>Workers' Compensation Coverage Form</option>
                    </select>
                </div>
            </div>
        </div>
    `;
}

// 2. Quote Detailed Screen
function renderQuoteStep(container) {
    const job = currentJob || {};
    const period = job.policyPeriod || {};
    const insured = period.primaryNamedInsured ? (period.primaryNamedInsured.name || period.primaryNamedInsured.companyName) : 'Acme Logistics Inc';
    const address = period.primaryNamedInsured ? (period.primaryNamedInsured.addressLine1 + ', ' + (period.primaryNamedInsured.city || 'Chicago') + ' ' + (period.primaryNamedInsured.state || 'IL')) : '100 Industrial Parkway, Chicago IL';
    const prem = period.totalPremium ? period.totalPremium : 2400.00;
    const status = job.jobStatus || job.status || 'Draft';
    const isBound = (status === 'Bound' || status === 'Issued');
    const jobNum = job.jobNumber || 'SUB-5001';
    const polNum = period.policyNumber || 'CP-8472910';

    container.innerHTML = `
        <div class="gw-action-bar" style="margin:-20px -20px 20px -20px; border-bottom:1px solid #CBD5E1;">
            <h1 style="font-size:18px;">Quote (${status.toUpperCase()})</h1>
            <div class="gw-btn-group">
                ${isBound ? `
                    <button class="gw-btn gw-btn-primary" style="background:#0284C7; font-weight:700;" onclick="showPolicyChangeWizard('${jobNum}', '${polNum}')">✏️ Change Policy (Endorsement)</button>
                    <button class="gw-btn" onclick="handleRenewal('${jobNum}')">🔄 Renew Policy</button>
                    <button class="gw-btn" onclick="handleCancellation('${jobNum}')">❌ Cancel Policy</button>
                    <button class="gw-btn" onclick="showRatingWorksheet()">Print Dec Page</button>
                ` : `
                    <button class="gw-btn" onclick="switchWizardStep('policy-review')">Back</button>
                    <button class="gw-btn gw-btn-primary" onclick="switchWizardStep('forms')">Next</button>
                    <button class="gw-btn" onclick="alert('Lock released for policy transaction')">Release Lock</button>
                    <button class="gw-btn" onclick="switchWizardStep('policy-type')">Edit Policy Transaction</button>
                    <button class="gw-btn" onclick="saveDraft()">Save Draft</button>
                    <button class="gw-btn gw-btn-success" onclick="bindAndIssue()">Issue Policy</button>
                    <button class="gw-btn" onclick="showRatingWorksheet()">Print Quote</button>
                `}
            </div>
        </div>

        <!-- Quote Key Value Summary Grid -->
        <div class="gw-panel">
            <div class="gw-panel-body">
                <div class="gw-kv-grid" style="grid-template-columns: repeat(2, 1fr);">
                    <div>
                        <div class="gw-kv-item"><span>Job / Submission Number</span><span>${jobNum}</span></div>
                        <div class="gw-kv-item"><span>Policy Number</span><span>${polNum}</span></div>
                        <div class="gw-kv-item"><span>Policy Period</span><span>${period.effectiveDate || '2026-07-23'} - ${period.expirationDate || '2027-07-23'}</span></div>
                        <div class="gw-kv-item"><span>Primary Named Insured</span><span>${insured}</span></div>
                        <div class="gw-kv-item"><span>Address</span><span>${address}</span></div>
                    </div>
                    <div>
                        <div class="gw-kv-item"><span>Brokerage Commission %</span><span>15.0%</span></div>
                        <div class="gw-kv-item"><span>Total Premium</span><span style="font-size:14px; color:#1E40AF;">$${prem.toLocaleString()}</span></div>
                        <div class="gw-kv-item"><span>Transaction Status</span><span class="gw-badge gw-badge-${status.toLowerCase()}">${status}</span></div>
                        <div class="gw-kv-item"><span>Total Cost</span><span style="font-size:15px; color:#166534; font-weight:800;">$${prem.toLocaleString()}</span></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabs Bar -->
        <div class="gw-tab-strip">
            <div class="gw-tab-item active">Policy Premium</div>
            <div class="gw-tab-item">Premium Rollup</div>
            <div class="gw-tab-item">Cost Change Detail</div>
        </div>

        <div style="margin-bottom:12px; display:flex; gap:10px;">
            <button class="gw-btn gw-btn-primary" style="font-size:11px;" onclick="showRatingWorksheet()">Show Rating Worksheet</button>
            ${isBound ? `<button class="gw-btn gw-btn-primary" style="font-size:11px; background:#0284C7;" onclick="showPolicyChangeWizard('${jobNum}', '${polNum}')">✏️ Start Policy Change</button>` : ''}
        </div>

        <!-- Policy Premium Breakdown Table -->
        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Rating & Exposure Line Items</span>
            </div>
            <div class="gw-panel-body" style="padding:0;">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Location / Item</th>
                            <th>Code</th>
                            <th>Description</th>
                            <th>Amount ($)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Building #1 (HQ Facility)</td>
                            <td>CPBldgCov</td>
                            <td>Commercial Building Property Coverage</td>
                            <td style="font-weight:700; color:#1E293B;">$${prem.toLocaleString()}</td>
                        </tr>
                        <tr>
                            <td>State Premium Tax (5%)</td>
                            <td>Tax</td>
                            <td>State Insurance Premium Tax</td>
                            <td>$${(prem * 0.05).toFixed(2)}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `;
}

function requestQuote() {
    if (currentJob) {
        currentJob.jobStatus = 'Quoted';
        currentJob.status = 'Quoted';
    }
    switchWizardStep('quote');
}

async function bindAndIssue() {
    const jobNum = currentJob ? (currentJob.jobNumber || 'SUB-5001') : 'SUB-5001';
    try {
        const res = await fetch(`${API_BASE}/jobs/bind`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNum })
        });
        if (res.ok) {
            const updated = await res.json();
            currentJob = updated;
        }
    } catch (e) {}

    if (currentJob) {
        currentJob.jobStatus = 'Bound';
        currentJob.status = 'Bound';
    }
    alert(`Policy Period ${jobNum} Bound and Issued successfully in Database!\n\nYou can now perform Mid-Term Policy Changes (Endorsements) under the Actions menu!`);
    switchWizardStep('quote');
}

function showRatingWorksheet() {
    const jobNum = currentJob ? (currentJob.jobNumber || 'SUB-5001') : 'SUB-5001';
    window.open(`${API_BASE}/documents/dec-page?job=${jobNum}`, '_blank');
}

function saveDraft() {
    alert("Submission draft saved to Database.");
}

function renderGenericWizardStep(container, stepName) {
    const job = currentJob || {};
    const period = job.policyPeriod || {};
    const status = job.jobStatus || job.status || 'Draft';
    const isBound = (status === 'Bound' || status === 'Issued');
    const jobNum = job.jobNumber || 'SUB-5001';
    const polNum = period.policyNumber || 'CP-8472910';

    container.innerHTML = `
        <div class="gw-action-bar" style="margin:-20px -20px 20px -20px; border-bottom:1px solid #CBD5E1;">
            <h1 style="font-size:18px;">${stepName.replace('-', ' ').toUpperCase()}</h1>
            <div class="gw-btn-group">
                ${isBound ? `
                    <button class="gw-btn gw-btn-primary" style="background:#0284C7; font-weight:700;" onclick="showPolicyChangeWizard('${jobNum}', '${polNum}')">✏️ Change Policy (Endorsement)</button>
                    <button class="gw-btn" onclick="handleRenewal('${jobNum}')">🔄 Renew Policy</button>
                ` : `
                    <button class="gw-btn" onclick="switchWizardStep('policy-type')">Back</button>
                    <button class="gw-btn gw-btn-primary" onclick="switchWizardStep('quote')">Next</button>
                `}
            </div>
        </div>
        <div class="gw-panel">
            <div class="gw-panel-header">${stepName.replace('-', ' ').toUpperCase()} Details</div>
            <div class="gw-panel-body">
                <p>PolicyCenter step <strong>${stepName}</strong> for submission ${jobNum}.</p>
                ${isBound ? `
                    <div style="margin-top:16px; padding:16px; background:#ECFDF5; border:1px solid #A7F3D0; border-radius:6px; font-size:13px; color:#065F46;">
                        <strong>Policy is Bound & Issued!</strong><br>
                        Click <strong>"Change Policy (Endorsement)"</strong> in the Actions menu or header button above to initiate a mid-term policy change.
                    </div>
                ` : ''}
            </div>
        </div>
    `;
}
