// Guidewire PolicyCenter - Desktop Screens (My Summary, My Submissions, My Activities, etc.)

function renderDesktopSidebar(activeView = 'summary') {
    const sidebar = document.getElementById('sidebar-panel');
    if (!sidebar) return;

    sidebar.innerHTML = `
        <div class="gw-sidebar-title">
            <span>Desktop</span>
            <span style="font-size:10px; opacity:0.7;">PolicyCenter</span>
        </div>
        <div class="gw-sidebar-accordion">
            <div class="gw-accordion-section">
                <div class="gw-accordion-header">
                    <span>Actions</span>
                    <span>▾</span>
                </div>
                <ul class="gw-tree-list">
                    <li class="gw-tree-item" onclick="openNewAccountModal()">+ New Account</li>
                    <li class="gw-tree-item" onclick="openNewSubmissionModal()">+ New Submission</li>
                    <li class="gw-tree-item" onclick="showPolicyChangeWizard('SUB-5001', 'CP-8472910')">✏️ Change Policy (Endorsement)</li>
                </ul>
            </div>
            <ul class="gw-tree-list" style="margin-top:8px;">
                <li class="gw-tree-item ${activeView === 'summary' ? 'active' : ''}" onclick="switchDesktopSubView('summary')">Summary</li>
                <li class="gw-tree-item ${activeView === 'activities' ? 'active' : ''}" onclick="switchDesktopSubView('activities')">My Activities</li>
                <li class="gw-tree-item ${activeView === 'cross-suite' ? 'active' : ''}" onclick="switchDesktopSubView('cross-suite')">Cross-Suite Activities</li>
                <li class="gw-tree-item ${activeView === 'accounts' ? 'active' : ''}" onclick="switchDesktopSubView('accounts')">My Accounts</li>
                <li class="gw-tree-item ${activeView === 'submissions' ? 'active' : ''}" onclick="switchDesktopSubView('submissions')">My Submissions</li>
                <li class="gw-tree-item ${activeView === 'renewals' ? 'active' : ''}" onclick="switchDesktopSubView('renewals')">My Renewals</li>
                <li class="gw-tree-item ${activeView === 'other-transactions' ? 'active' : ''}" onclick="switchDesktopSubView('other-transactions')">Other Policy Transactions</li>
                <li class="gw-tree-item ${activeView === 'queues' ? 'active' : ''}" onclick="switchDesktopSubView('queues')">My Queues</li>
                <li class="gw-tree-item ${activeView === 'uploaded-docs' ? 'active' : ''}" onclick="switchDesktopSubView('uploaded-docs')">Uploaded Documents</li>
            </ul>
        </div>
    `;
}

async function switchDesktopSubView(view) {
    renderDesktopSidebar(view);
    const workArea = document.getElementById('work-area');
    const pageHeading = document.getElementById('page-heading');
    const subheaderBanner = document.getElementById('subheader-banner');

    if (subheaderBanner) {
        subheaderBanner.style.display = 'none';
    }

    if (view === 'summary') {
        if (pageHeading) pageHeading.innerText = 'My Summary';
        await renderMySummaryScreen(workArea);
    } else if (view === 'submissions') {
        if (pageHeading) pageHeading.innerText = 'My Submissions';
        await renderMySubmissionsScreen(workArea);
    } else if (view === 'accounts') {
        if (pageHeading) pageHeading.innerText = 'My Accounts';
        await renderMyAccountsScreen(workArea);
    } else if (view === 'activities') {
        if (pageHeading) pageHeading.innerText = 'My Activities';
        renderMyActivitiesScreen(workArea);
    } else {
        if (pageHeading) pageHeading.innerText = 'Desktop: ' + view.toUpperCase();
        renderGenericDesktopView(workArea, view);
    }
}

// 1. My Summary Screen (Dynamic DB data)
async function renderMySummaryScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading Summary metrics from Database...</div>`;

    try {
        let jobs = [];
        let accounts = [];
        try {
            const jobsRes = await fetch(`${API_BASE}/jobs`);
            if (jobsRes.ok) jobs = await jobsRes.json();
        } catch (e) {
            console.warn('Jobs fetch warning:', e);
        }
        try {
            const accsRes = await fetch(`${API_BASE}/accounts`);
            if (accsRes.ok) accounts = await accsRes.json();
        } catch (e) {
            console.warn('Accounts fetch warning:', e);
        }

        const totalSubmissions = jobs.filter(j => j.jobType === 'Submission' || !j.jobType).length;
        const totalRenewals = jobs.filter(j => j.jobType === 'Renewal').length;
        const totalChanges = jobs.filter(j => j.jobType === 'PolicyChange').length;
        const totalCancels = jobs.filter(j => j.jobType === 'Cancellation').length;

        let submissionsRows = jobs.map(j => {
            const period = j.policyPeriod || {};
            const insured = period.primaryNamedInsured ? (period.primaryNamedInsured.name || period.primaryNamedInsured.companyName) : (period.account ? period.account.accountHolderName : 'Insured');
            const prod = period.productName || j.productCode || 'Commercial Property';
            const status = j.jobStatus || j.status || 'Draft';
            const effDate = period.effectiveDate || '2026-07-23';

            return `
                <tr style="cursor:pointer;" onclick="openJobByNumber('${j.jobNumber}')">
                    <td style="color:#2563EB; font-weight:700;">${j.jobNumber || 'SUB-5001'}</td>
                    <td>${insured}</td>
                    <td>${effDate}</td>
                    <td><span class="gw-badge gw-badge-${status.toLowerCase()}">${status}</span></td>
                    <td>-</td>
                    <td>$${period.totalPremium ? period.totalPremium.toLocaleString() : '2,400'}</td>
                    <td>${prod}</td>
                    <td>${j.underwriterID || 'Super User'}</td>
                </tr>
            `;
        }).join('');

        if (!submissionsRows) {
            submissionsRows = `<tr><td colspan="8" style="text-align:center; color:#94A3B8; padding:16px;">No submissions found in Database.</td></tr>`;
        }

        container.innerHTML = `
            <!-- Overview Stat Cards Widget -->
            <div class="gw-panel">
                <div class="gw-panel-header">
                    <span>Overview</span>
                </div>
                <div class="gw-panel-body">
                    <div class="gw-stat-row">
                        <div class="gw-stat-card">
                            <div class="gw-stat-num">${accounts.length}</div>
                            <div class="gw-stat-label">Active Accounts</div>
                        </div>
                        <div class="gw-stat-card">
                            <div class="gw-stat-num">${totalSubmissions}</div>
                            <div class="gw-stat-label">Submissions</div>
                        </div>
                        <div class="gw-stat-card">
                            <div class="gw-stat-num">${totalChanges}</div>
                            <div class="gw-stat-label">Change Requests</div>
                        </div>
                        <div class="gw-stat-card">
                            <div class="gw-stat-num">${totalRenewals}</div>
                            <div class="gw-stat-label">Renewals</div>
                        </div>
                        <div class="gw-stat-card">
                            <div class="gw-stat-num">${totalCancels}</div>
                            <div class="gw-stat-label">Cancellations</div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- My Activities Table Widget -->
            <div class="gw-panel">
                <div class="gw-panel-header">
                    <div style="display:flex; align-items:center; gap:12px;">
                        <span>My Activities</span>
                    </div>
                </div>
                <div class="gw-panel-body" style="padding:0;">
                    <table class="gw-table">
                        <thead>
                            <tr>
                                <th style="width:30px;"><input type="checkbox"></th>
                                <th>Due Date</th>
                                <th>Subject</th>
                                <th>Priority</th>
                                <th>Status</th>
                                <th>Account Holder</th>
                                <th>Product</th>
                                <th style="text-align:right;">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><input type="checkbox"></td>
                                <td>2026-07-30</td>
                                <td>Underwriting Review & Exposure Assessment</td>
                                <td><span style="color:#EA580C; font-weight:700;">High</span></td>
                                <td>Open</td>
                                <td>Acme Logistics Inc</td>
                                <td>Commercial Property</td>
                                <td style="text-align:right;"><button class="gw-btn gw-btn-primary" style="padding:2px 8px; font-size:11px;" onclick="completeActivity(this)">Complete</button></td>
                            </tr>
                            <tr>
                                <td><input type="checkbox"></td>
                                <td>2026-08-05</td>
                                <td>Verify Alarm Certificate & Risk Analysis</td>
                                <td><span style="color:#2563EB; font-weight:700;">Medium</span></td>
                                <td>Open</td>
                                <td>Apex Freight Corp</td>
                                <td>Commercial Auto</td>
                                <td style="text-align:right;"><button class="gw-btn gw-btn-primary" style="padding:2px 8px; font-size:11px;" onclick="completeActivity(this)">Complete</button></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- My Submissions Widget -->
            <div class="gw-panel">
                <div class="gw-panel-header">
                    <div style="display:flex; align-items:center; gap:12px;">
                        <span>My Submissions (Database Records)</span>
                    </div>
                </div>
                <div class="gw-panel-body" style="padding:0;">
                    <table class="gw-table">
                        <thead>
                            <tr>
                                <th>Transaction #</th>
                                <th>Primary Insured</th>
                                <th>Effective Date</th>
                                <th>Status</th>
                                <th>Quote Needed</th>
                                <th>Premium</th>
                                <th>Product</th>
                                <th>Producer</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${submissionsRows}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading summary from Database: ${e}</div>`;
    }
}

// 2. My Submissions Full Screen (Dynamic DB data)
async function renderMySubmissionsScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading Submissions from Database...</div>`;

    try {
        const res = await fetch(`${API_BASE}/jobs`);
        const jobs = res.ok ? await res.json() : [];

        let rows = jobs.map(j => {
            const period = j.policyPeriod || {};
            const insured = period.primaryNamedInsured ? (period.primaryNamedInsured.name || period.primaryNamedInsured.companyName) : (period.account ? period.account.accountHolderName : 'Insured');
            const prod = period.productName || j.productCode || 'Commercial Property';
            const status = j.jobStatus || j.status || 'Draft';
            const effDate = period.effectiveDate || '2026-07-23';

            return `
                <tr style="cursor:pointer;" onclick="openJobByNumber('${j.jobNumber}')">
                    <td style="font-weight:700; color:#0F172A;">${insured}</td>
                    <td>${effDate}</td>
                    <td>No</td>
                    <td style="color:#2563EB; font-weight:700;">${j.jobNumber || 'SUB-5001'}</td>
                    <td>${j.jobType || 'Submission'}</td>
                    <td><span class="gw-badge gw-badge-${status.toLowerCase()}">${status}</span></td>
                    <td>${status === 'Bound' || status === 'Issued' ? 'Yes' : 'No'}</td>
                    <td>${prod}</td>
                    <td>${j.underwriterID || 'Super User'}</td>
                </tr>
            `;
        }).join('');

        if (!rows) {
            rows = `<tr><td colspan="9" style="text-align:center; color:#94A3B8; padding:16px;">No submission transactions in Database. Click "+ New Submission" to create one.</td></tr>`;
        }

        container.innerHTML = `
            <div class="gw-panel">
                <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                    <span>My Submissions (${jobs.length} Transactions)</span>
                    <button class="gw-btn gw-btn-primary" onclick="openNewSubmissionModal()">+ New Submission</button>
                </div>
                <div class="gw-panel-body" style="padding:0;">
                    <table class="gw-table">
                        <thead>
                            <tr>
                                <th>Primary Insured</th>
                                <th>Effective Date</th>
                                <th>Quote Needed</th>
                                <th>Transaction #</th>
                                <th>Type</th>
                                <th>Status</th>
                                <th>Issued</th>
                                <th>Product</th>
                                <th>Underwriter</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${rows}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading submissions: ${e}</div>`;
    }
}

// 3. My Accounts Screen (Dynamic DB data)
async function renderMyAccountsScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading Accounts from Database...</div>`;

    try {
        const res = await fetch(`${API_BASE}/accounts`);
        const accounts = res.ok ? await res.json() : [];

        let rows = accounts.map(a => `
            <tr style="cursor:pointer;" onclick="selectAccount('${a.accountNumber}')">
                <td style="color:#2563EB; font-weight:700;">${a.accountNumber}</td>
                <td style="font-weight:700;">${a.accountHolder ? a.accountHolder.name : (a.companyName || 'Insured')}</td>
                <td>${a.industryCode || 'Commercial'}</td>
                <td>${a.accountHolder ? (a.accountHolder.city + ', ' + a.accountHolder.state) : (a.city ? a.city + ', ' + a.state : 'IL')}</td>
                <td><span class="gw-badge gw-badge-bound">${a.accountStatus || 'Active'}</span></td>
                <td><button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="event.stopPropagation(); openNewSubmissionModal('${a.accountNumber}')">+ New Submission</button></td>
            </tr>
        `).join('');

        if (!rows) {
            rows = `<tr><td colspan="6" style="text-align:center; color:#94A3B8; padding:16px;">No accounts in Database. Click "+ New Account" to create one.</td></tr>`;
        }

        container.innerHTML = `
            <div class="gw-panel">
                <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                    <span>My Accounts (${accounts.length} Accounts in Database)</span>
                    <button class="gw-btn gw-btn-primary" onclick="openNewAccountModal()">+ New Account</button>
                </div>
                <div class="gw-panel-body" style="padding:0;">
                    <table class="gw-table">
                        <thead>
                            <tr>
                                <th>Account #</th>
                                <th>Insured Company Name</th>
                                <th>Industry</th>
                                <th>Location</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${rows}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading accounts: ${e}</div>`;
    }
}

function renderMyActivitiesScreen(container) {
    container.innerHTML = `<div class="gw-panel"><div class="gw-panel-header">My Activities</div><div class="gw-panel-body">Activity Queue System</div></div>`;
}

function renderGenericDesktopView(container, title) {
    container.innerHTML = `<div class="gw-panel"><div class="gw-panel-header">${title}</div><div class="gw-panel-body">Desktop View: ${title}</div></div>`;
}

function completeActivity(btn) {
    const tr = btn.closest('tr');
    if (tr) {
        tr.style.opacity = '0.5';
        btn.disabled = true;
        btn.innerText = '✓ Done';
    }
}
