// Guidewire PolicyCenter Core Frontend Controller

const API_BASE = 'http://localhost:8080/api';

let currentUser = null;
let currentJob = null;
let currentAccount = null;
let accountsList = [];
let jobsList = [];
let policiesList = [];
let currentStep = 1;
let currentTab = 'jobs';

document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
});

function checkAuth() {
    const sessionToken = sessionStorage.getItem('gw_session_token');
    const storedUser = sessionStorage.getItem('gw_user');

    if (sessionToken && storedUser) {
        currentUser = JSON.parse(storedUser);
        document.getElementById('login-modal').style.display = 'none';
        renderUserHeader();
        loadAccounts();
        loadJobs();
        loadPolicies();
    } else {
        document.getElementById('login-modal').style.display = 'flex';
    }
}

function fillCredentials(u, p) {
    document.getElementById('login-username').value = u;
    document.getElementById('login-password').value = p;
}

async function handleLoginSubmit(e) {
    e.preventDefault();
    const u = document.getElementById('login-username').value;
    const p = document.getElementById('login-password').value;
    const errBox = document.getElementById('login-error-msg');

    errBox.style.display = 'none';

    try {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: u, password: p })
        });

        if (!res.ok) {
            const err = await res.json();
            errBox.innerText = err.message || "Invalid LDAP credentials";
            errBox.style.display = 'block';
            return;
        }

        const data = await res.json();
        sessionStorage.setItem('gw_session_token', data.token);
        sessionStorage.setItem('gw_user', JSON.stringify(data.user));
        currentUser = data.user;

        document.getElementById('login-modal').style.display = 'none';
        renderUserHeader();
        loadAccounts();
        loadJobs();
        loadPolicies();
    } catch (err) {
        errBox.innerText = "Connection error: " + err;
        errBox.style.display = 'block';
    }
}

function handleLogout() {
    sessionStorage.removeItem('gw_session_token');
    sessionStorage.removeItem('gw_user');
    currentUser = null;
    document.getElementById('login-modal').style.display = 'flex';
}

function renderUserHeader() {
    if (!currentUser) return;
    const header = document.getElementById('user-header-info');
    header.innerHTML = `
        <span>User: <strong>${currentUser.username} (${currentUser.fullName})</strong></span>
        <span style="color: #64748B;">|</span>
        <span>Role: <strong style="color: #60A5FA;">${currentUser.role}</strong></span>
        <span style="color: #64748B;">|</span>
        <span>Org Code: <strong>${currentUser.producerCode}</strong></span>
        <button class="gw-btn" style="padding: 4px 10px; font-size: 11px; margin-left: 8px;" onclick="handleLogout()">Logout</button>
    `;
}

function switchMainTab(tab) {
    currentTab = tab;
    document.querySelectorAll('.gw-tab').forEach(el => el.classList.remove('active'));
    const tabEl = document.getElementById(`tab-${tab}`);
    if (tabEl) {
        tabEl.classList.add('active');
    }

    const sidebar = document.getElementById('sidebar-panel');
    const workArea = document.getElementById('work-area');
    const heading = document.getElementById('page-heading');
    const actionBtns = document.getElementById('action-buttons');

    if (tab === 'jobs') {
        sidebar.style.display = 'flex';
        actionBtns.style.display = 'flex';
        if (currentJob) {
            renderWizardStep(currentStep);
        } else if (jobsList.length > 0) {
            currentJob = jobsList[0];
            renderWizardStep(1);
        }
    } else {
        sidebar.style.display = 'none';
        actionBtns.style.display = 'none';
        
        if (tab === 'accounts') {
            heading.innerText = 'Accounts File Directory';
            renderAccountsTab();
        } else if (tab === 'policies') {
            heading.innerText = 'Active Policies File Directory';
            renderPoliciesTab();
        } else if (tab === 'rating') {
            heading.innerText = 'Product Model Rating Matrix Studio';
            const area = document.getElementById('work-area');
            if (typeof renderRatingStudioScreen === 'function') {
                renderRatingStudioScreen(area);
            }
        } else if (tab === 'reinsurance') {
            heading.innerText = 'Reinsurance Management & Risk Cession';
            const area = document.getElementById('work-area');
            if (typeof renderReinsuranceScreen === 'function') {
                renderReinsuranceScreen(area);
            }
        } else if (tab === 'desktop') {
            heading.innerText = 'Underwriter Desktop & Activities';
            renderDesktopTab();
        } else if (tab === 'entities') {
            heading.innerText = 'OOTB Guidewire PolicyCenter Entity Explorer';
            const area = document.getElementById('work-area');
            area.innerHTML = '<div id="entities-view">Loading 26 OOTB Sandbox Entities...</div>';
            renderEntityExplorerTab();
        } else if (tab === 'admin') {
            heading.innerText = 'PolicyCenter System Administration';
            renderAdminTab();
        }
    }
}

async function loadAccounts() {
    try {
        const res = await fetch(`${API_BASE}/accounts`);
        accountsList = await res.json();
    } catch (e) {
        console.error("Failed to load accounts", e);
    }
}

async function loadJobs() {
    try {
        const res = await fetch(`${API_BASE}/jobs`);
        jobsList = await res.json();
        if (jobsList.length > 0 && !currentJob) {
            currentJob = jobsList[0];
            renderWizardStep(1);
        }
    } catch (e) {
        console.error("Failed to load jobs", e);
    }
}

async function loadPolicies() {
    try {
        const res = await fetch(`${API_BASE}/policies`);
        policiesList = await res.json();
    } catch (e) {
        console.error("Failed to load policies", e);
    }
}

function renderAccountsTab() {
    const area = document.getElementById('work-area');
    let rows = accountsList.map(a => `
        <tr>
            <td><strong>${a.accountNumber}</strong></td>
            <td>${a.accountHolder ? a.accountHolder.name : 'N/A'}</td>
            <td>${a.industryCode || 'Commercial'}</td>
            <td>${a.accountHolder ? a.accountHolder.city + ', ' + a.accountHolder.state : ''}</td>
            <td><span class="gw-badge gw-badge-bound">${a.accountStatus}</span></td>
            <td><button class="gw-btn" onclick="startSubmissionForAccount('${a.accountNumber}')">New Submission</button></td>
        </tr>
    `).join('');

    area.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Account Search & Summary Directory</span>
                <button class="gw-btn gw-btn-primary" onclick="showNewAccountModal()">+ Create New Account</button>
            </div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Account #</th>
                            <th>Insured / Company Name</th>
                            <th>Industry Code</th>
                            <th>Primary Location</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            </div>
        </div>
    `;
}

function renderPoliciesTab() {
    const area = document.getElementById('work-area');
    let rows = jobsList.map(j => {
        const p = j.policyPeriod || {};
        const isCancelled = p.status === 'Cancelled';
        const isBound = p.status === 'Bound' || p.status === 'Issued';
        return `
        <tr>
            <td><strong>${p.policyNumber || j.jobNumber}</strong></td>
            <td>${p.primaryNamedInsured ? p.primaryNamedInsured.name : (p.account ? p.account.accountNumber : 'N/A')}</td>
            <td>${p.productName || 'Commercial Property'}</td>
            <td>${p.effectiveDate || ''} to ${p.expirationDate || ''}</td>
            <td>$${p.totalCost ? p.totalCost.toLocaleString() : '0'}</td>
            <td><span class="gw-badge gw-badge-${p.status ? p.status.toLowerCase() : 'draft'}">${p.status || 'Draft'}</span></td>
            <td>
                <button class="gw-btn" style="padding:4px 8px; font-size:11px;" onclick="showPolicyHistoryScreen('${p.policyNumber || 'POL-88201'}')">📜 OOS History</button>
                ${isBound ? `
                    <button class="gw-btn" style="padding:4px 8px; font-size:11px;" onclick="showPolicyChangeScreen('${j.jobNumber}')">Policy Change</button>
                    <button class="gw-btn gw-btn-primary" style="padding:4px 8px; font-size:11px;" onclick="showRenewalScreen('${j.jobNumber}')">Renew Policy</button>
                    <button class="gw-btn" style="padding:4px 8px; font-size:11px; background:#ef4444; color:#fff;" onclick="showCancellationScreen('${j.jobNumber}')">Cancel Policy</button>
                ` : ''}
                ${isCancelled ? `
                    <button class="gw-btn gw-btn-success" style="padding:4px 8px; font-size:11px;" onclick="showReinstatementScreen('${j.jobNumber}')">Reinstate Policy</button>
                ` : ''}
            </td>
        </tr>
        `;
    }).join('');

    area.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Active In-Force Policies & Transaction Management</div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Policy / Job #</th>
                            <th>Named Insured</th>
                            <th>Product Line</th>
                            <th>Term Effective Dates</th>
                            <th>Total Premium</th>
                            <th>Status</th>
                            <th>Policy Actions</th>
                        </tr>
                    </thead>
                    <tbody>${rows.length > 0 ? rows : '<tr><td colspan="7">No bound policies yet. Complete a submission wizard to bind a policy.</td></tr>'}</tbody>
                </table>
            </div>
        </div>
    `;
}

function renderDesktopTab() {
    const area = document.getElementById('work-area');
    area.innerHTML = `
        <div class="gw-panel">
            <div class="gw-panel-header">Underwriter Work Queue & Open Activities</div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Priority</th>
                            <th>Subject / Activity</th>
                            <th>Related Job</th>
                            <th>Due Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><span class="gw-badge gw-badge-draft">HIGH</span></td>
                            <td>Review High Value Building Limit ($1,500,000)</td>
                            <td>SUB-5001 (Acme Logistics)</td>
                            <td>Today</td>
                            <td>Open</td>
                        </tr>
                        <tr>
                            <td><span class="gw-badge gw-badge-quoted">NORMAL</span></td>
                            <td>Verify Tax ID and FEIN Documentation</td>
                            <td>SUB-5001</td>
                            <td>In 2 Days</td>
                            <td>Pending</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `;
}

function renderAdminTab() {
    const area = document.getElementById('work-area');
    if (typeof renderBatchSchedulerScreen === 'function') {
        renderBatchSchedulerScreen(area);
    }
}

function showNewAccountModal() {
    if (typeof openNewAccountModal === 'function') {
        openNewAccountModal();
    }
}

function startSubmissionForAccount(accNum) {
    if (typeof showNewSubmissionModal === 'function') {
        showNewSubmissionModal(accNum);
    }
}

function showPolicyHistoryScreen(polNum) {
    const area = document.getElementById('work-area');
    if (typeof renderPolicyHistoryScreen === 'function') {
        renderPolicyHistoryScreen(area, polNum);
    }
}
