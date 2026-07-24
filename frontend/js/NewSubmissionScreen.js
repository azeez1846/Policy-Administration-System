// Emulates Guidewire NewSubmission.pcf - Modal Screen for Initializing Submissions

function showNewSubmissionModal(accountNum) {
    openNewSubmissionModal(accountNum);
}

async function openNewSubmissionModal(accountNum) {
    let modalOverlay = document.getElementById('new-submission-modal-overlay');
    if (!modalOverlay) {
        modalOverlay = document.createElement('div');
        modalOverlay.id = 'new-submission-modal-overlay';
        modalOverlay.style.cssText = `
            position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(15, 23, 42, 0.6); backdrop-filter: blur(4px);
            display: flex; justify-content: center; align-items: center; z-index: 9999;
        `;
        document.body.appendChild(modalOverlay);
    }

    // Always ensure active accounts list is available from DB
    try {
        const res = await fetch(`${API_BASE}/accounts`);
        if (res.ok) accountsList = await res.json();
    } catch (e) {
        console.error("Failed to load accounts list for submission modal", e);
    }

    if (!accountsList || accountsList.length === 0) {
        accountsList = [{
            accountNumber: 'C00010928',
            companyName: 'Acme Logistics Inc',
            industryCode: 'Freight & Warehousing'
        }];
    }

    let accOptions = accountsList.map(a => {
        const name = a.accountHolder ? a.accountHolder.name : (a.companyName || a.accountHolderName || 'Insured Company');
        const accNo = a.accountNumber || 'C00010928';
        const isSel = (accNo === accountNum || (!accountNum && accountsList[0] === a)) ? 'selected' : '';
        return `<option value="${accNo}" ${isSel}>${accNo} - ${name} (${a.industryCode || 'Commercial'})</option>`;
    }).join('');

    modalOverlay.innerHTML = `
        <div style="
            background: #FFFFFF; border-radius: 8px; width: 560px; max-width: 90vw;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.3);
            overflow: hidden; border: 1px solid #CBD5E1;
        ">
            <div style="background: #142232; color: #FFFFFF; padding: 14px 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: 3px solid #2563EB;">
                <div>
                    <h3 style="margin: 0; font-size: 16px; font-weight: 700;">Start New Policy Submission (NewSubmission.pcf)</h3>
                    <p style="margin: 2px 0 0 0; font-size: 11px; color: #94A3B8;">Select product line and term parameters to open submission wizard.</p>
                </div>
                <button type="button" onclick="closeNewSubmissionModal()" style="background: transparent; border: none; color: #94A3B8; font-size: 18px; cursor: pointer;">✕</button>
            </div>

            <form id="new-submission-form" style="padding: 20px;" onsubmit="handleNewSubmissionSubmit(event)">
                <div class="gw-form-grid" style="grid-template-columns: 1fr 1fr; gap: 14px;">
                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Insured Account *</label>
                        <select id="ns-account-num" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                            ${accOptions}
                        </select>
                    </div>

                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Product Line *</label>
                        <select id="ns-product-line" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;" onchange="updateSubmissionProductDefaults(this.value)">
                            <option value="Commercial Property">Commercial Property (Building & Content Coverage)</option>
                            <option value="Commercial Auto">Commercial Auto (Fleet Vehicles & Drivers)</option>
                            <option value="Personal Auto">Personal Auto (Private Passenger Vehicles)</option>
                            <option value="Workers Compensation">Workers' Compensation (Employee Liability)</option>
                        </select>
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Term Effective Date *</label>
                        <input type="date" id="ns-effective-date" value="2026-07-23" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Term Expiration Date *</label>
                        <input type="date" id="ns-expiration-date" value="2027-07-23" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Producer Organization</label>
                        <input type="text" id="ns-producer" value="Apex Global Insurance Brokers" readonly style="width: 100%; padding: 7px 10px; background: #F8FAFC; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Initial Limit / Exposure ($)</label>
                        <input type="number" id="ns-limit" value="1000000" step="50000" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>
                </div>

                <div style="margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px; border-top: 1px solid #E2E8F0; padding-top: 14px;">
                    <button type="button" onclick="closeNewSubmissionModal()" class="gw-btn">Cancel</button>
                    <button type="submit" class="gw-btn gw-btn-primary">▶ Initialize Submission Wizard</button>
                </div>
            </form>
        </div>
    `;

    modalOverlay.style.display = 'flex';
}

function closeNewSubmissionModal() {
    const modalOverlay = document.getElementById('new-submission-modal-overlay');
    if (modalOverlay) {
        modalOverlay.style.display = 'none';
    }
}

function updateSubmissionProductDefaults(line) {
    const limitInput = document.getElementById('ns-limit');
    if (!limitInput) return;
    if (line === 'Commercial Property') limitInput.value = 1000000;
    else if (line === 'Commercial Auto') limitInput.value = 500000;
    else if (line === 'Personal Auto') limitInput.value = 100000;
    else if (line === 'Workers Compensation') limitInput.value = 2500000;
}

async function handleNewSubmissionSubmit(e) {
    e.preventDefault();
    const accNumEl = document.getElementById('ns-account-num');
    const accNum = accNumEl ? accNumEl.value : 'C00010928';
    const prodLine = document.getElementById('ns-product-line').value;
    const limitVal = document.getElementById('ns-limit') ? document.getElementById('ns-limit').value : '1000000';
    const prodCode = prodLine === 'Commercial Auto' ? 'CommercialAuto' : (prodLine === 'Personal Auto' ? 'PersonalAuto' : (prodLine === 'Workers Compensation' ? 'WorkersComp' : 'CommercialProperty'));

    const payload = {
        accountNumber: accNum,
        productCode: prodCode,
        productLine: prodLine,
        buildingDescription: 'Main Business Location',
        constructionType: 'Joisted Masonry',
        buildingLimit: limitVal,
        contentsLimit: '250000'
    };

    try {
        const res = await fetch(`${API_BASE}/jobs`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            const newJob = await res.json();
            currentJob = newJob;
            closeNewSubmissionModal();
            switchMainTab('policies');
            switchWizardStep('policy-type');
        } else {
            const err = await res.json();
            alert('Error creating submission: ' + (err.error || 'Unknown error'));
        }
    } catch (err) {
        currentJob = {
            jobNumber: "SUB-" + Math.floor(10000 + Math.random() * 90000),
            jobStatus: "Draft",
            productCode: prodCode,
            policyPeriod: {
                productName: prodLine,
                effectiveDate: "2026-07-23",
                account: { accountNumber: accNum }
            }
        };
        closeNewSubmissionModal();
        switchMainTab('policies');
        switchWizardStep('policy-type');
    }
}
