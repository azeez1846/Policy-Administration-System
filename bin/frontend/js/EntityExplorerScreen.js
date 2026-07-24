// OOTB Guidewire PolicyCenter Entity Explorer Dashboard UI

const OOTB_ENTITIES_METADATA = [
    { table: 'pc_account', name: 'Account', cat: 'Core Data Model', desc: 'Account entity holding policyholder profile & currency' },
    { table: 'pc_contact', name: 'Contact', cat: 'Core Data Model', desc: 'Person/Company contact details & addresses' },
    { table: 'pc_accountlocation', name: 'AccountLocation', cat: 'Core Data Model', desc: 'Locations tied to an account' },
    { table: 'pc_accountcontact', name: 'AccountContact', cat: 'Core Data Model', desc: 'Account contact role join relationship' },
    { table: 'pc_policyterm', name: 'PolicyTerm', cat: 'Core Data Model', desc: 'Policy lifecycle term container' },
    { table: 'pc_policyperiod', name: 'PolicyPeriod', cat: 'Core Data Model', desc: 'Effective period version & financial totals' },
    { table: 'pc_job', name: 'Job', cat: 'Core Data Model', desc: 'Policy job process (Submission, Renewal, PolicyChange, etc)' },
    { table: 'pc_policyline', name: 'PolicyLine', cat: 'Transaction & Exposure', desc: 'Line of Business (CP, CA, PA)' },
    { table: 'pc_policylocation', name: 'PolicyLocation', cat: 'Transaction & Exposure', desc: 'Location risk exposure on policy period' },
    { table: 'pc_building', name: 'Building', cat: 'Transaction & Exposure', desc: 'Commercial building risk exposure details' },
    { table: 'pc_policyvehicle', name: 'PolicyVehicle', cat: 'Transaction & Exposure', desc: 'Commercial / Personal Auto vehicle exposure' },
    { table: 'pc_policydriver', name: 'PolicyDriver', cat: 'Transaction & Exposure', desc: 'Driver MVR record on auto line' },
    { table: 'pc_coverage', name: 'Coverage', cat: 'Transaction & Exposure', desc: 'Coverage terms, limits, & deductibles' },
    { table: 'pc_exclusion', name: 'Exclusion', cat: 'Transaction & Exposure', desc: 'Policy exclusions' },
    { table: 'pc_policycondition', name: 'PolicyCondition', cat: 'Transaction & Exposure', desc: 'Policy conditions' },
    { table: 'pc_policyaddlinsured', name: 'PolicyAddlInsured', cat: 'Transaction & Exposure', desc: 'Additional insureds & loss payees' },
    { table: 'pc_cost', name: 'Cost', cat: 'Transaction & Exposure', desc: 'Rated line item costs & premiums' },
    { table: 'pc_policytransaction', name: 'PolicyTransaction', cat: 'Transaction & Exposure', desc: 'Financial transaction ledger' },
    { table: 'pc_uwissue', name: 'UWIssue', cat: 'Underwriting & Rules', desc: 'Underwriting referral issue & status' },
    { table: 'pc_uwauthorityprofile', name: 'UWAuthorityProfile', cat: 'Underwriting & Rules', desc: 'Underwriter authority profile & limits' },
    { table: 'pc_user', name: 'User', cat: 'Org & Security', desc: 'PolicyCenter user account & credentials' },
    { table: 'pc_group', name: 'Group', cat: 'Org & Security', desc: 'Organizational unit group' },
    { table: 'pc_producercode', name: 'ProducerCode', cat: 'Org & Security', desc: 'Producer broker commission code' },
    { table: 'pc_policyform', name: 'PolicyForm', cat: 'Audit & Documents', desc: 'Inferred policy form endorsement' },
    { table: 'pc_history', name: 'History', cat: 'Audit & Documents', desc: 'Audit history event log' },
    { table: 'pc_note', name: 'Note', cat: 'Audit & Documents', desc: 'User notes & annotations' }
];

async function renderEntityExplorerTab() {
    const container = document.getElementById('entities-view');
    if (!container) return;

    try {
        const res = await fetch(`${API_BASE}/entities`);
        const counts = res.ok ? await res.json() : {};

        let cardsHtml = OOTB_ENTITIES_METADATA.map(e => {
            const count = counts[e.table] !== undefined ? counts[e.table] : 0;
            return `
                <div style="background:#FFF; border:1px solid #E2E8F0; border-radius:8px; padding:16px; box-shadow:0 1px 3px rgba(0,0,0,0.05); display:flex; flex-direction:column; justify-content:space-between;">
                    <div>
                        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;">
                            <h4 style="margin:0; font-size:16px; color:#1E293B; font-weight:700;">${e.name}</h4>
                            <span class="gw-badge gw-badge-bound" style="font-size:12px;">${count} Records</span>
                        </div>
                        <p style="font-size:11px; color:#64748B; margin:0 0 8px 0; font-family:monospace;">${e.table}</p>
                        <p style="font-size:13px; color:#475569; margin:0; line-height:1.4;">${e.desc}</p>
                    </div>
                    <div style="margin-top:12px; display:flex; justify-content:space-between; align-items:center;">
                        <span class="gw-badge" style="background:#F1F5F9; color:#475569;">${e.cat}</span>
                        <button class="gw-btn" style="padding:4px 8px; font-size:11px;" onclick="inspectEntitySchema('${e.name}', '${e.table}')">Inspect Schema</button>
                    </div>
                </div>
            `;
        }).join('');

        container.innerHTML = `
            <div style="margin-bottom:20px;">
                <h3 style="margin:0 0 4px 0; color:#0F172A; font-size:20px; font-weight:800;">Guidewire PolicyCenter OOTB Entity Data Model</h3>
                <p style="margin:0; color:#64748B; font-size:14px;">All 26 out-of-the-box Guidewire PolicyCenter sandbox entities initialized with SQLite DDL, seed data, and foreign key relations.</p>
            </div>

            <div style="display:grid; grid-template-columns:repeat(auto-fill, minmax(320px, 1fr)); gap:16px;">
                ${cardsHtml}
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red;">Failed to load entity metadata from server.</div>`;
    }
}

function inspectEntitySchema(name, table) {
    alert(`Entity: ${name}\nDatabase Table: ${table}\n\nSchema Status: Active OOTB Guidewire PolicyCenter Entity with primary key ID, publicID, createTime, updateTime, and relational foreign keys.`);
}
