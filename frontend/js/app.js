// Guidewire PolicyCenter Core Frontend Controller

const API_BASE = 'http://localhost:8080/api';

let currentUser = null;
let currentJob = null;
let currentAccount = null;
let accountsList = [];
let jobsList = [];
let currentTab = 'desktop';

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
        switchMainTab('desktop');
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
        switchMainTab('desktop');
    } catch (err) {
        // Fallback for offline demo mode
        currentUser = { username: u, fullName: u === 'su' ? 'Super User (Senior UW)' : u, role: 'Underwriter', producerCode: 'PROD-1001' };
        sessionStorage.setItem('gw_session_token', 'demo-token-123');
        sessionStorage.setItem('gw_user', JSON.stringify(currentUser));
        document.getElementById('login-modal').style.display = 'none';
        renderUserHeader();
        switchMainTab('desktop');
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
        <span><strong>${currentUser.username}</strong> (${currentUser.fullName})</span>
        <button class="gw-btn" style="padding: 2px 8px; font-size: 11px; margin-left: 8px;" onclick="handleLogout()">Logout</button>
    `;
}

function switchMainTab(tab) {
    currentTab = tab;
    document.querySelectorAll('.gw-tab').forEach(el => el.classList.remove('active'));
    
    const tabMap = {
        'desktop': 'tab-desktop',
        'accounts': 'tab-accounts',
        'policies': 'tab-policies',
        'ai-uw': 'tab-ai-uw',
        'product-studio': 'tab-product-studio',
        'gosu': 'tab-gosu',
        'gis': 'tab-gis',
        'billing': 'tab-billing',
        'broker-portal': 'tab-broker-portal',
        'acord': 'tab-acord',
        'activities': 'tab-activities',
        'analytics': 'tab-analytics',
        'irpm': 'tab-irpm',
        'entities': 'tab-entities',
        'batch': 'tab-batch'
    };

    const tabEl = document.getElementById(tabMap[tab] || `tab-${tab}`);
    if (tabEl) tabEl.classList.add('active');

    const sidebar = document.getElementById('sidebar-panel');
    const subheaderBanner = document.getElementById('subheader-banner');
    const workArea = document.getElementById('work-area');
    const pageHeading = document.getElementById('page-heading');
    sidebar.style.display = 'flex';

    if (tab === 'desktop') {
        switchDesktopSubView('summary');
    } else if (tab === 'accounts') {
        switchAccountSubView('summary');
    } else if (tab === 'policies') {
        switchWizardStep('policy-type');
    } else if (tab === 'ai-uw') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '🤖 AI Underwriting Assistant & Risk Analytics';
        if (typeof renderAIRiskAssistantScreen === 'function') renderAIRiskAssistantScreen(workArea);
    } else if (tab === 'product-studio') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '⚡ No-Code Product Model Designer Studio';
        if (typeof renderProductDesignerScreen === 'function') renderProductDesignerScreen(workArea);
    } else if (tab === 'gosu') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '💻 Gosu Business Rule Studio & Compiler';
        if (typeof renderGosuRuleStudioScreen === 'function') renderGosuRuleStudioScreen(workArea);
    } else if (tab === 'gis') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '🗺️ GIS Risk Heatmap & Catastrophe Exposure';
        if (typeof renderGISRiskMapScreen === 'function') renderGISRiskMapScreen(workArea);
    } else if (tab === 'billing') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '💳 Billing Center & ClaimCenter Loss History';
        if (typeof renderBillingCenterScreen === 'function') renderBillingCenterScreen(workArea);
    } else if (tab === 'broker-portal') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '🌐 Producer / Broker Quick-Quote Portal';
        if (typeof renderProducerPortalScreen === 'function') renderProducerPortalScreen(workArea);
    } else if (tab === 'acord') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '📄 ACORD 125/140 & Policy Binder Document Studio';
        if (typeof renderACORDDocumentScreen === 'function') renderACORDDocumentScreen(workArea);
    } else if (tab === 'activities') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '📋 Underwriting Activity Tasks & Internal Notes';
        if (typeof renderActivityTaskScreen === 'function') renderActivityTaskScreen(workArea);
    } else if (tab === 'analytics') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '📈 Executive UW Portfolio Analytics & GWP Dashboard';
        if (typeof renderPortfolioAnalyticsScreen === 'function') renderPortfolioAnalyticsScreen(workArea);
    } else if (tab === 'irpm') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '⚡ Schedule Rating Modification (IRPM) & Factor Override Studio';
        if (typeof renderIRPMScreen === 'function') renderIRPMScreen(workArea);
    } else if (tab === 'entities') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        renderEntityExplorerScreenWrapper();
    } else if (tab === 'batch') {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = '⚙️ Guidewire WorkQueue & Batch Process Studio';
        if (typeof renderBatchSchedulerScreen === 'function') renderBatchSchedulerScreen(workArea);
    } else {
        if (subheaderBanner) subheaderBanner.style.display = 'none';
        if (pageHeading) pageHeading.innerText = tab.toUpperCase();
        workArea.innerHTML = `<div class="gw-panel"><div class="gw-panel-header">${tab.toUpperCase()}</div><div class="gw-panel-body">PolicyCenter ${tab} module</div></div>`;
    }
}

function renderEntityExplorerScreenWrapper() {
    const pageHeading = document.getElementById('page-heading');
    if (pageHeading) pageHeading.innerText = 'OOTB Guidewire PolicyCenter Entity Explorer';
    const workArea = document.getElementById('work-area');
    workArea.innerHTML = '<div id="entities-view">Loading 44 OOTB Sandbox Entities...</div>';
    if (typeof renderEntityExplorerTab === 'function') {
        renderEntityExplorerTab();
    }
}

async function openJobByNumber(jobNum) {
    try {
        const res = await fetch(`${API_BASE}/jobs/${jobNum}`);
        if (res.ok) {
            currentJob = await res.json();
        }
    } catch (e) {}
    switchMainTab('policies');
}

async function selectAccount(accNum) {
    try {
        const res = await fetch(`${API_BASE}/accounts/${accNum}`);
        if (res.ok) {
            currentAccount = await res.json();
        }
    } catch (e) {}
    switchMainTab('accounts');
}

function handleQuickSearch(e) {
    if (e.key === 'Enter') {
        const query = e.target.value;
        if (query) {
            alert(`Searching PolicyCenter directory for: ${query}`);
        }
    }
}
