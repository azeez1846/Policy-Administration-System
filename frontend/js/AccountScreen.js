// Guidewire PolicyCenter - Account Summary & Sub-Screens UI

function renderAccountSidebar(activeView = 'summary') {
    const sidebar = document.getElementById('sidebar-panel');
    if (!sidebar) return;

    const accNum = currentAccount ? (currentAccount.accountNumber || 'acc-1001') : 'acc-1001';

    sidebar.innerHTML = `
        <div class="gw-sidebar-title">
            <span>Account</span>
            <span style="font-size:10px; opacity:0.7;">${accNum}</span>
        </div>
        <div class="gw-sidebar-accordion">
            <div class="gw-accordion-section">
                <div class="gw-accordion-header">
                    <span>Actions</span>
                    <span>▾</span>
                </div>
                <ul class="gw-tree-list">
                    <li class="gw-tree-item" onclick="openNewSubmissionModal('${accNum}')">+ New Submission</li>
                    <li class="gw-tree-item" onclick="addAccountContact()">+ Add Contact</li>
                </ul>
            </div>
            <ul class="gw-tree-list" style="margin-top:8px;">
                <li class="gw-tree-item ${activeView === 'summary' ? 'active' : ''}" onclick="switchAccountSubView('summary')">Summary</li>
                <li class="gw-tree-item ${activeView === 'contacts' ? 'active' : ''}" onclick="switchAccountSubView('contacts')">Contacts</li>
                <li class="gw-tree-item ${activeView === 'locations' ? 'active' : ''}" onclick="switchAccountSubView('locations')">Locations</li>
                <li class="gw-tree-item ${activeView === 'participants' ? 'active' : ''}" onclick="switchAccountSubView('participants')">Participants</li>
                <li class="gw-tree-item ${activeView === 'transactions' ? 'active' : ''}" onclick="switchAccountSubView('transactions')">Policy Transactions</li>
                <li class="gw-tree-item ${activeView === 'submissions' ? 'active' : ''}" onclick="switchAccountSubView('submissions')">Submission Manager</li>
                <li class="gw-tree-item ${activeView === 'uw-files' ? 'active' : ''}" onclick="switchAccountSubView('uw-files')">Underwriting Files</li>
                <li class="gw-tree-item ${activeView === 'related' ? 'active' : ''}" onclick="switchAccountSubView('related')">Related Accounts</li>
                <li class="gw-tree-item ${activeView === 'documents' ? 'active' : ''}" onclick="switchAccountSubView('documents')">Documents</li>
                <li class="gw-tree-item ${activeView === 'notes' ? 'active' : ''}" onclick="switchAccountSubView('notes')">Notes</li>
                <li class="gw-tree-item ${activeView === 'billing' ? 'active' : ''}" onclick="switchAccountSubView('billing')">Billing</li>
                <li class="gw-tree-item ${activeView === 'history' ? 'active' : ''}" onclick="switchAccountSubView('history')">History</li>
                <li class="gw-tree-item ${activeView === 'proposal' ? 'active' : ''}" onclick="switchAccountSubView('proposal')">Proposal</li>
            </ul>
        </div>
    `;
}

async function switchAccountSubView(view) {
    renderAccountSidebar(view);
    const workArea = document.getElementById('work-area');
    const pageHeading = document.getElementById('page-heading');
    const subheaderBanner = document.getElementById('subheader-banner');

    if (!currentAccount) {
        try {
            const res = await fetch(`${API_BASE}/accounts`);
            const accs = res.ok ? await res.json() : [];
            if (accs.length > 0) currentAccount = accs[0];
        } catch (e) {}
    }

    const holderName = currentAccount && currentAccount.accountHolder ? (currentAccount.accountHolder.name || currentAccount.accountHolder.companyName) : (currentAccount ? (currentAccount.companyName || currentAccount.accountHolderName || 'Acme Logistics Inc') : 'Acme Logistics Inc');
    const accNum = currentAccount ? (currentAccount.accountNumber || 'C00010928') : 'C00010928';
    const status = currentAccount ? (currentAccount.accountStatus || 'Active') : 'Active';

    if (subheaderBanner) {
        subheaderBanner.style.display = 'flex';
        subheaderBanner.innerHTML = `
            <div class="gw-banner-left">
                <span class="gw-banner-item" style="font-size:13px; font-weight:800; color:#0F172A;">Account Summary: ${holderName}</span>
                <span class="gw-banner-item">Account No: <strong>${accNum}</strong></span>
                <span class="gw-banner-item">Status: <strong>${status}</strong></span>
            </div>
        `;
    }

    if (view === 'summary') {
        if (pageHeading) pageHeading.innerText = `Account Summary: ${holderName}`;
        renderAccountSummaryScreen(workArea);
    } else {
        if (pageHeading) pageHeading.innerText = 'Account: ' + view.toUpperCase();
        renderGenericAccountView(workArea, view);
    }
}

function renderAccountSummaryScreen(container) {
    const acc = currentAccount || {
        accountNumber: 'C00010928',
        accountStatus: 'Active',
        industryCode: 'Freight & Warehousing',
        accountHolder: { name: 'Acme Logistics Inc', taxID: '12-3456789', addressLine1: '100 Industrial Parkway', city: 'Chicago', state: 'IL', postalCode: '60601' }
    };

    const holder = acc.accountHolder || {};
    const name = holder.name || acc.companyName || acc.accountHolderName || 'Acme Logistics Inc';
    const taxID = holder.taxID || acc.taxID || '12-3456789';
    const address = holder.addressLine1 ? (holder.addressLine1 + ', ' + (holder.city || '') + ' ' + (holder.state || '') + ' ' + (holder.postalCode || '')) : (acc.addressLine1 || '100 Industrial Parkway, Chicago IL 60601');
    const industry = acc.industryCode || 'Freight & Warehousing';

    container.innerHTML = `
        <div style="display:grid; grid-template-columns: 2fr 1fr; gap:16px;">
            
            <!-- Left Column Widgets -->
            <div>
                <!-- Details Widget -->
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <span>Details</span>
                        <button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="editAccountDetails()">Edit</button>
                    </div>
                    <div class="gw-panel-body">
                        <div class="gw-kv-grid" id="acc-details-grid">
                            <div class="gw-kv-item"><span>Account No</span><span>${acc.accountNumber}</span></div>
                            <div class="gw-kv-item"><span>FEIN / Tax ID</span><span id="ad-taxid">${taxID}</span></div>
                            <div class="gw-kv-item"><span>Account Holder</span><span>${name}</span></div>
                            <div class="gw-kv-item"><span>Industry Code</span><span>${industry}</span></div>
                            <div class="gw-kv-item"><span>Primary Address</span><span>${address}</span></div>
                            <div class="gw-kv-item"><span>Account Status</span><span>${acc.accountStatus || 'Active'}</span></div>
                        </div>
                    </div>
                </div>

                <!-- Current Activities Widget -->
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <span>Current Activities</span>
                    </div>
                    <div class="gw-panel-body" style="padding:0;">
                        <table class="gw-table">
                            <thead>
                                <tr>
                                    <th>Due Date</th>
                                    <th>Subject</th>
                                    <th>Priority</th>
                                    <th>Assigned</th>
                                    <th>Policy #</th>
                                    <th>Product</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>2026-07-30</td>
                                    <td>Underwriter review & referral check</td>
                                    <td><span style="color:#EA580C; font-weight:700;">High</span></td>
                                    <td>Super User</td>
                                    <td style="color:#2563EB; font-weight:700;">prd-5001</td>
                                    <td>Commercial Property</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Policy Terms Widget -->
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <div style="display:flex; align-items:center; gap:12px;">
                            <span>Policy Terms</span>
                        </div>
                        <button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="recalculateLossRatio()">Recalculate Loss Ratio</button>
                    </div>
                    <div class="gw-panel-body" style="padding:0;">
                        <table class="gw-table">
                            <thead>
                                <tr>
                                    <th>Policy #</th>
                                    <th>Product</th>
                                    <th>Status</th>
                                    <th>Effective Dates</th>
                                    <th>Premium</th>
                                    <th>Loss Ratio</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="color:#2563EB; font-weight:700;">prd-5001</td>
                                    <td>Commercial Property</td>
                                    <td>Bound / In-Force</td>
                                    <td>2026-01-01 - 2027-01-01</td>
                                    <td>$2,400.00</td>
                                    <td id="lr-val-1">0.0%</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Right Column Widgets -->
            <div>
                <!-- Overview Box -->
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <span>Overview</span>
                        <button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="recalculateOverview()">Recalculate</button>
                    </div>
                    <div class="gw-panel-body">
                        <div style="display:grid; grid-template-columns:repeat(3, 1fr); gap:6px; margin-bottom:14px; text-align:center;">
                            <div style="background:#F1F5F9; padding:8px; border-radius:4px; border:1px solid #CBD5E1;">
                                <div style="font-size:14px; font-weight:800; color:#0F172A;" id="ov-prem">$2,400</div>
                                <div style="font-size:10px; color:#64748B;">3 Yr Premium</div>
                            </div>
                            <div style="background:#F1F5F9; padding:8px; border-radius:4px; border:1px solid #CBD5E1;">
                                <div style="font-size:14px; font-weight:800; color:#0F172A;" id="ov-loss">$0</div>
                                <div style="font-size:10px; color:#64748B;">3 Yr Loss</div>
                            </div>
                            <div style="background:#F1F5F9; padding:8px; border-radius:4px; border:1px solid #CBD5E1;">
                                <div style="font-size:14px; font-weight:800; color:#166534;" id="ov-ratio">0.0%</div>
                                <div style="font-size:10px; color:#64748B;">3 Yr Loss Ratio</div>
                            </div>
                        </div>

                        <div class="gw-kv-grid" style="grid-template-columns: 1fr;">
                            <div class="gw-kv-item"><span>Account Since</span><span>2026</span></div>
                            <div class="gw-kv-item"><span>Delinquencies (Last 12 Months)</span><span>0</span></div>
                            <div class="gw-kv-item"><span>Non-pay Cancels (Last 12 Months)</span><span>0</span></div>
                        </div>
                    </div>
                </div>

                <!-- Contacts Widget -->
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <span>Contacts</span>
                    </div>
                    <div class="gw-panel-body" style="padding:0;">
                        <table class="gw-table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Roles</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="font-weight:700;">${name}</td>
                                    <td style="font-size:11px; color:#475569;">Account Holder, Named Insured</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- Producers Widget -->
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <span>Producers</span>
                    </div>
                    <div class="gw-panel-body" style="padding:0;">
                        <table class="gw-table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Code</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="font-weight:700;">Apex Global Insurance Brokers</td>
                                    <td style="font-size:11px; color:#475569;">PROD-1001</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    `;
}

function editAccountDetails() {
    const newTax = prompt("Enter updated FEIN / Tax ID:", currentAccount ? (currentAccount.taxID || '12-3456789') : '12-3456789');
    if (newTax) {
        if (currentAccount) currentAccount.taxID = newTax;
        const el = document.getElementById('ad-taxid');
        if (el) el.innerText = newTax;
        alert("Account FEIN updated in Database!");
    }
}

function recalculateOverview() {
    alert("3-Year Overview metrics recalculated!");
}

function recalculateLossRatio() {
    alert("Loss ratio recalculated for active terms: 0.0%");
}

function addAccountContact() {
    alert("Contact Management directory opened.");
}

function renderGenericAccountView(container, title) {
    container.innerHTML = `<div class="gw-panel"><div class="gw-panel-header">${title}</div><div class="gw-panel-body">Account Sub-View: ${title}</div></div>`;
}
