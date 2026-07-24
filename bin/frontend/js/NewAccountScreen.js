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
            background: #FFFFFF; border-radius: 12px; width: 560px; max-width: 90vw;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            overflow: hidden; animation: modalFadeIn 0.2s ease-out;
        ">
            <div style="background: #1E293B; color: #FFFFFF; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center;">
                <div>
                    <h3 style="margin: 0; font-size: 18px; font-weight: 700;">Create New Account (NewAccount.pcf)</h3>
                    <p style="margin: 2px 0 0 0; font-size: 12px; color: #94A3B8;">Enter insured company details and primary location information.</p>
                </div>
                <button onclick="closeNewAccountModal()" style="background: transparent; border: none; color: #94A3B8; font-size: 20px; cursor: pointer;">✕</button>
            </div>

            <form id="new-account-form" style="padding: 24px;" onsubmit="handleNewAccountSubmit(event)">
                <div class="gw-form-grid" style="grid-template-columns: 1fr 1fr; gap: 16px;">
                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Company / Named Insured *</label>
                        <input type="text" id="na-company-name" required placeholder="e.g. Apex Logistics Inc" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Industry Code *</label>
                        <select id="na-industry-code" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                            <option value="Freight & Warehousing">Freight & Warehousing (4841)</option>
                            <option value="Commercial Retail">Commercial Retail (4411)</option>
                            <option value="IT Services & Consulting">IT Services & Consulting (5415)</option>
                            <option value="General Construction">General Construction (2362)</option>
                            <option value="Healthcare & Life Sciences">Healthcare & Life Sciences (6211)</option>
                        </select>
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">FEIN / Tax ID *</label>
                        <input type="text" id="na-tax-id" required placeholder="e.g. 98-7654321" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>

                    <div class="gw-field" style="grid-column: span 2;">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">Primary Location Address *</label>
                        <input type="text" id="na-address" required placeholder="e.g. 100 Enterprise Way, Suite 400" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">City *</label>
                        <input type="text" id="na-city" required placeholder="e.g. Chicago" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                    </div>

                    <div class="gw-field">
                        <label style="font-weight: 600; font-size: 12px; color: #334155;">State & Zip *</label>
                        <div style="display: flex; gap: 8px;">
                            <input type="text" id="na-state" required placeholder="IL" style="width: 60px; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                            <input type="text" id="na-zip" required placeholder="60601" style="flex: 1; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                        </div>
                    </div>
                </div>

                <div style="margin-top: 24px; display: flex; justify-content: flex-end; gap: 12px; border-top: 1px solid #E2E8F0; padding-top: 16px;">
                    <button type="button" onclick="closeNewAccountModal()" class="gw-btn" style="background: #F1F5F9; color: #475569; border: 1px solid #CBD5E1;">Cancel</button>
                    <button type="submit" class="gw-btn gw-btn-primary" style="padding: 8px 20px;">✓ Create Account</button>
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
            await loadAccounts();
            closeNewAccountModal();
            renderAccountsTab();
        } else {
            alert('Failed to create account.');
        }
    } catch (err) {
        alert('Failed to create account: ' + err);
    }
}
