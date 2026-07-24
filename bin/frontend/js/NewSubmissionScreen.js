// Emulates Guidewire NewSubmission.pcf - Modal Screen for Initializing Submissions

function showNewSubmissionModal(accountNum) {
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

    const accOptions = accountsList.map(a => {
        const name = a.accountHolder ? a.accountHolder.name : (a.companyName || a.accountHolderName || 'Insured');
        const isSel = a.accountNumber === accountNum ? 'selected' : '';
        return `<option value="${a.accountNumber}" ${isSel}>${a.accountNumber} - ${name} (${a.industryCode || 'Commercial'})</option>`;
    }).join('');

    modalOverlay.innerHTML = `
        <div style="
            background: #FFFFFF; border-radius: 12px; width: 560px; max-width: 90vw;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            overflow: hidden; animation: modalFadeIn 0.2s ease-out;
        ">
            <div style="background: #1E293B; color: #FFFFFF; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center;">
                <div>
                    <h3 style="margin: 0; font-size: 18px; font-weight: 700;">Start New Policy Submission (NewSubmission.pcf)</h3>
                    <p style="margin: 2px 0 0 0; font-size: 12px; color: #94A3B8;">Select product line and term parameters to open submission wizard.</p>
                </div>
                <button onclick="closeNewSubmissionModal()" style="background: transparent; border: none; color: #94A3B8; font-size: 20px; cursor: pointer;">✕</button>
            </div>

            <form id="new-submission-form" style="padding: 24px;" onsubmit="handleNewSubmissionSubmit(event)">
                <div class="gw-form-grid" style="grid-template-columns: 1fr 1fr; gap: 16px;">
                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Insured Account *</label>
                        <select id="ns-account-num" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                            ${accOptions}
                        </select>
                    </div>

                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Product Line *</label>
                        <select id="ns-product-line" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;" onchange="updateSubmissionProductDefaults(this.value)">
                            <option value="Commercial Property">Commercial Property (Building & Content Coverage)</option>
                            <option value="Commercial Auto">Commercial Auto (Fleet Vehicles & Drivers)</option>
                            <option value="Personal Auto">Personal Auto (Private Passenger Vehicles)</option>
                        </select>
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Term Effective Date *</label>
                        <input type="date" id="ns-effective-date" value="2026-07-23" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Term Expiration Date *</label>
                        <input type="date" id="ns-expiration-date" value="2027-07-23" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Producer Organization</label>
                        <input type="text" id="ns-producer" value="Aon Commercial Risk Solutions" readonly style="width: 100%; padding: 8px 12px; background: #F8FAFC; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Initial Limit / Exposure ($)</label>
                        <input type="number" id="ns-limit" value="1000000" step="50000" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>
                </div>

                <div style="margin-top: 24px; display: flex; justify-content: flex-end; gap: 12px; border-top: 1px solid #E2E8F0; padding-top: 16px;">
                    <button type="button" onclick="closeNewSubmissionModal()" class="gw-btn" style="background: #F1F5F9; color: #475569; border: 1px solid #CBD5E1;">Cancel</button>
                    <button type="submit" class="gw-btn gw-btn-primary" style="padding: 8px 20px;">▶ Initialize Submission Wizard</button>
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
}

async function handleNewSubmissionSubmit(e) {
    e.preventDefault();
    const accNum = document.getElementById('ns-account-num').value;
    const prodLine = document.getElementById('ns-product-line').value;
    const limitVal = document.getElementById('ns-limit') ? document.getElementById('ns-limit').value : '1000000';
    const prodCode = prodLine === 'Commercial Auto' ? 'CommercialAuto' : (prodLine === 'Personal Auto' ? 'PersonalAuto' : 'CommercialProperty');

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
            await loadJobs();
            currentJob = newJob;
            currentStep = 1;
            closeNewSubmissionModal();
            switchMainTab('jobs');
            renderWizardStep(1);
        } else {
            const err = await res.json();
            alert('Error creating submission: ' + (err.error || 'Unknown error'));
        }
    } catch (err) {
        alert('Failed to start submission wizard: ' + err);
    }
}
