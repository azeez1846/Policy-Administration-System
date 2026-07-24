// Emulates Guidewire NewAccount.pcf - Modal Screen for Creating New Accounts

function showNewAccountModal() {
    openNewAccountModal();
}

function openNewAccountModal() {
    let modalOverlay = document.getElementById('new-account-modal-overlay');
    if (!modalOverlay) {
        modalOverlay = document.createElement('div');
        modalOverlay.id = 'new-account-modal-overlay';
        modalOverlay.style.cssText = `
            position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(15, 23, 42, 0.6); backdrop-filter: blur(4px);
            display: flex; justify-content: center; align-items: center; z-index: 9999;
        `;
        document.body.appendChild(modalOverlay);
    }

    modalOverlay.innerHTML = `
        <div style="
            background: #FFFFFF; border-radius: 8px; width: 540px; max-width: 90vw;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.3);
            overflow: hidden; border: 1px solid #CBD5E1;
        ">
            <div style="background: #142232; color: #FFFFFF; padding: 14px 20px; display: flex; justify-content: space-between; align-items: center; border-bottom: 3px solid #2563EB;">
                <div>
                    <h3 style="margin: 0; font-size: 16px; font-weight: 700;">Create New Account (NewAccount.pcf)</h3>
                    <p style="margin: 2px 0 0 0; font-size: 11px; color: #94A3B8;">Enter insured company details and primary location information.</p>
                </div>
                <button type="button" onclick="closeNewAccountModal()" style="background: transparent; border: none; color: #94A3B8; font-size: 18px; cursor: pointer;">✕</button>
            </div>

            <form id="new-account-form" style="padding: 20px;" onsubmit="handleNewAccountSubmit(event)">
                <div class="gw-form-grid" style="grid-template-columns: 1fr 1fr; gap: 14px;">
                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Company / Named Insured *</label>
                        <input type="text" id="na-company-name" required placeholder="e.g. Apex Logistics Inc" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Industry Code *</label>
                        <select id="na-industry-code" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                            <option value="Freight & Warehousing">Freight & Warehousing (4841)</option>
                            <option value="Commercial Retail">Commercial Retail (4411)</option>
                            <option value="IT Services & Consulting">IT Services & Consulting (5415)</option>
                            <option value="General Construction">General Construction (2362)</option>
                            <option value="Healthcare & Life Sciences">Healthcare & Life Sciences (6211)</option>
                        </select>
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">FEIN / Tax ID *</label>
                        <input type="text" id="na-tax-id" required placeholder="e.g. 98-7654321" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>

                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">Primary Location Address *</label>
                        <input type="text" id="na-address" required placeholder="e.g. 100 Enterprise Way, Suite 400" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">City *</label>
                        <input type="text" id="na-city" required placeholder="e.g. Chicago" style="width: 100%; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 700; font-size: 11px; color: #334155;">State & Zip *</label>
                        <div style="display: flex; gap: 8px;">
                            <input type="text" id="na-state" required placeholder="IL" style="width: 50px; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                            <input type="text" id="na-zip" required placeholder="60601" style="flex: 1; padding: 7px 10px; border: 1px solid #CBD5E1; border-radius: 4px; font-size:13px;">
                        </div>
                    </div>
                </div>

                <div style="margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px; border-top: 1px solid #E2E8F0; padding-top: 14px;">
                    <button type="button" onclick="closeNewAccountModal()" class="gw-btn">Cancel</button>
                    <button type="submit" class="gw-btn gw-btn-primary">✓ Create Account</button>
                </div>
            </form>
        </div>
    `;

    modalOverlay.style.display = 'flex';
}

function closeNewAccountModal() {
    const modalOverlay = document.getElementById('new-account-modal-overlay');
    if (modalOverlay) {
        modalOverlay.style.display = 'none';
    }
}

async function handleNewAccountSubmit(e) {
    e.preventDefault();
    const compName = document.getElementById('na-company-name').value;
    const indCode = document.getElementById('na-industry-code').value;
    const taxID = document.getElementById('na-tax-id').value;
    const address = document.getElementById('na-address').value;
    const city = document.getElementById('na-city').value;
    const state = document.getElementById('na-state').value;
    const zip = document.getElementById('na-zip').value;

    const payload = {
        companyName: compName,
        industryCode: indCode,
        taxID: taxID,
        addressLine1: address,
        city: city,
        state: state,
        postalCode: zip,
        email: 'contact@' + compName.toLowerCase().replace(/[^a-z0-9]/g, '') + '.com',
        phone: '(555) 012-3456'
    };

    try {
        const res = await fetch(`${API_BASE}/accounts`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            const newAcc = await res.json();
            currentAccount = newAcc;
            if (!accountsList) accountsList = [];
            accountsList.unshift(newAcc);
            closeNewAccountModal();
            switchMainTab('accounts');
            switchAccountSubView('summary');
        } else {
            closeNewAccountModal();
            switchMainTab('accounts');
            switchAccountSubView('summary');
        }
    } catch (err) {
        closeNewAccountModal();
        switchMainTab('accounts');
        switchAccountSubView('summary');
    }
}
