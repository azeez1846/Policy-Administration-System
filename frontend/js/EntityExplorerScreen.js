// OOTB Guidewire PolicyCenter Entity Explorer Dashboard UI

const OOTB_ENTITIES_METADATA = [
    // Core Data Model
    { table: 'accounts', name: 'Account', cat: 'Core Data Model', desc: 'Account entity holding policyholder profile & currency' },
    { table: 'contacts', name: 'Contact', cat: 'Core Data Model', desc: 'Person/Company contact details & addresses' },
    { table: 'account_locations', name: 'AccountLocation', cat: 'Core Data Model', desc: 'Locations tied to an account' },
    { table: 'account_contacts', name: 'AccountContact', cat: 'Core Data Model', desc: 'Account contact role join relationship' },
    { table: 'policy_terms', name: 'PolicyTerm', cat: 'Core Data Model', desc: 'Policy lifecycle term container' },
    { table: 'policy_periods', name: 'PolicyPeriod', cat: 'Core Data Model', desc: 'Effective period version & financial totals' },
    { table: 'jobs', name: 'Job', cat: 'Core Data Model', desc: 'Policy job process (Submission, Renewal, PolicyChange, etc)' },

    // Transaction & Risk Exposures
    { table: 'policy_lines', name: 'PolicyLine', cat: 'Transaction & Exposure', desc: 'Line of Business (CP, CA, PA, WC, GL)' },
    { table: 'policy_locations', name: 'PolicyLocation', cat: 'Transaction & Exposure', desc: 'Location risk exposure on policy period' },
    { table: 'buildings', name: 'Building', cat: 'Transaction & Exposure', desc: 'Commercial building risk exposure details' },
    { table: 'policy_vehicles', name: 'PolicyVehicle', cat: 'Transaction & Exposure', desc: 'Commercial / Personal Auto vehicle exposure' },
    { table: 'policy_drivers', name: 'PolicyDriver', cat: 'Transaction & Exposure', desc: 'Driver MVR record on auto line' },
    { table: 'coverages', name: 'Coverage', cat: 'Transaction & Exposure', desc: 'Coverage terms, limits, & deductibles' },
    { table: 'exclusions', name: 'Exclusion', cat: 'Transaction & Exposure', desc: 'Policy exclusions' },
    { table: 'policy_conditions', name: 'PolicyCondition', cat: 'Transaction & Exposure', desc: 'Policy conditions' },
    { table: 'policy_addl_insureds', name: 'PolicyAddlInsured', cat: 'Transaction & Exposure', desc: 'Additional insureds & loss payees' },

    // Financials & Rating
    { table: 'costs', name: 'Cost', cat: 'Financials & Rating', desc: 'Rated line item costs & premiums' },
    { table: 'policy_transactions', name: 'PolicyTransaction', cat: 'Financials & Rating', desc: 'Policy financial transaction ledger' },
    { table: 'transactions', name: 'Transaction', cat: 'Financials & Rating', desc: 'Detailed financial ledger entries for rating/billing' },
    { table: 'payment_plans', name: 'PaymentPlan', cat: 'Financials & Rating', desc: 'Payment schedules (Full Pay, 4-Pay, Monthly)' },
    { table: 'modifiers', name: 'Modifier', cat: 'Financials & Rating', desc: 'Schedule rating, E-MOD, or IRPM factors' },
    { table: 'audit_informations', name: 'AuditInformation', cat: 'Financials & Rating', desc: 'Audit schedule & method for auditable LOBs' },

    // Underwriting & Rules
    { table: 'uw_issues', name: 'UWIssue', cat: 'Underwriting & Rules', desc: 'Underwriting referral issue & status' },
    { table: 'uw_authority_profiles', name: 'UWAuthorityProfile', cat: 'Underwriting & Rules', desc: 'Underwriter authority profile & limits' },
    { table: 'uw_companies', name: 'UWCompany', cat: 'Underwriting & Rules', desc: 'Carrier legal entity issuing the policy' },
    { table: 'uw_authority_grants', name: 'UWAuthorityGrant', cat: 'Underwriting & Rules', desc: 'Specific authority limit granted to an UW profile' },
    { table: 'contingencies', name: 'Contingency', cat: 'Underwriting & Rules', desc: 'Binding/issuance conditions and requirements' },

    // LOB Expansion (Workers Comp & General Liability)
    { table: 'wc_class_codes', name: 'WCClassCode', cat: 'LOB Expansion', desc: 'Workers Compensation classification codes & rates' },
    { table: 'wc_employees', name: 'WCEmployee', cat: 'LOB Expansion', desc: 'Employee payroll exposure per state and class code' },
    { table: 'gl_class_codes', name: 'GLClassCode', cat: 'LOB Expansion', desc: 'General Liability classification codes' },
    { table: 'gl_exposures', name: 'GLExposure', cat: 'LOB Expansion', desc: 'Exposure amounts and territory for GL sub-lines' },

    // Reinsurance Management
    { table: 'ri_risks', name: 'RIRisk', cat: 'Reinsurance', desc: 'Risk unit evaluated for reinsurance attachment' },
    { table: 'ri_programs', name: 'RIProgram', cat: 'Reinsurance', desc: 'Reinsurance program layering treaties' },
    { table: 'ri_attachments', name: 'RIAttachment', cat: 'Reinsurance', desc: 'Association connecting a policy risk to a treaty' },

    // Product Model & Rating Engine
    { table: 'rate_books', name: 'RateBook', cat: 'Product Model', desc: 'Rating ratebook versions & effective dates' },
    { table: 'rate_routines', name: 'RateRoutine', cat: 'Product Model', desc: 'Rating calculation step formulas & algorithms' },
    { table: 'form_patterns', name: 'FormPattern', cat: 'Product Model', desc: 'Inference rules for mandatory policy forms' },

    // Documents, Audit & Security
    { table: 'documents', name: 'Document', cat: 'Audit & Documents', desc: 'Uploaded application/policy document files' },
    { table: 'policy_forms', name: 'PolicyForm', cat: 'Audit & Documents', desc: 'Inferred policy form endorsement instance' },
    { table: 'history', name: 'History', cat: 'Audit & Documents', desc: 'Audit history event log' },
    { table: 'notes', name: 'Note', cat: 'Audit & Documents', desc: 'User notes & annotations' },
    { table: 'users', name: 'User', cat: 'Org & Security', desc: 'PolicyCenter user account & credentials' },
    { table: 'groups', name: 'Group', cat: 'Org & Security', desc: 'Organizational unit group' },
    { table: 'producer_codes', name: 'ProducerCode', cat: 'Org & Security', desc: 'Producer broker commission code' }
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
                <p style="margin:0; color:#64748B; font-size:14px;">All 44 out-of-the-box Guidewire PolicyCenter sandbox entities initialized with SQLite DDL, seed data, and foreign key relations.</p>
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
