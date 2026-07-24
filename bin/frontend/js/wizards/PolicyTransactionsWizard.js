// Guidewire PolicyCenter Policy Transactions Wizard (Policy Change, Renewal, Cancellation, Reinstatement)

async function handlePolicyChange(jobNumber) {
    if (!confirm(`Initiate Mid-Term Policy Change (Endorsement) for Job ${jobNumber}?`)) return;

    try {
        const res = await fetch(`${API_BASE}/jobs/endorse`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber })
        });
        if (res.ok) {
            alert('Mid-Term Endorsement processed and bound successfully!');
            loadJobs();
            loadPolicies();
        } else {
            const err = await res.json();
            alert('Error: ' + (err.error || 'Failed to process Policy Change'));
        }
    } catch (e) {
        alert('Network error processing Policy Change');
    }
}

async function handleRenewal(jobNumber) {
    if (!confirm(`Generate Renewal Quote and Issue Policy Term for Job ${jobNumber}?`)) return;

    try {
        const res = await fetch(`${API_BASE}/jobs/renew`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber })
        });
        if (res.ok) {
            alert('Policy Term Renewed and Issued successfully!');
            loadJobs();
            loadPolicies();
        } else {
            const err = await res.json();
            alert('Error: ' + (err.error || 'Failed to renew policy'));
        }
    } catch (e) {
        alert('Network error processing Renewal');
    }
}

function handleCancellation(jobNumber) {
    let overlay = document.getElementById('cancel-modal-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'cancel-modal-overlay';
        overlay.style.cssText = `
            position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
            background: rgba(15, 23, 42, 0.6); backdrop-filter: blur(4px);
            display: flex; justify-content: center; align-items: center; z-index: 9999;
        `;
        document.body.appendChild(overlay);
    }

    overlay.innerHTML = `
        <div style="background: #FFFFFF; border-radius: 12px; width: 480px; max-width: 90vw; overflow: hidden; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);">
            <div style="background: #991B1B; color: #FFFFFF; padding: 16px 20px; display: flex; justify-content: space-between; align-items: center;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700;">Initiate Policy Cancellation (${jobNumber})</h3>
                <button onclick="document.getElementById('cancel-modal-overlay').style.display='none'" style="background: transparent; border: none; color: #FCA5A5; font-size: 18px; cursor: pointer;">✕</button>
            </div>
            <form style="padding: 20px;" onsubmit="submitPolicyCancellation(event, '${jobNumber}')">
                <div style="margin-bottom: 16px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Cancellation Type *</label>
                    <select id="cancel-type" style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                        <option value="ProRata">ProRata (Pro-Rata refund for unused days)</option>
                        <option value="Flat">Flat (100% full refund from inception)</option>
                        <option value="ShortRate">ShortRate (Short-rate penalty deduction)</option>
                    </select>
                </div>
                <div style="margin-bottom: 20px;">
                    <label style="font-size: 12px; font-weight: 600; color: #334155; display: block; margin-bottom: 6px;">Cancellation Reason *</label>
                    <input type="text" id="cancel-reason" value="Customer Request - Sold Property" required style="width: 100%; padding: 8px 12px; border: 1px solid #CBD5E1; border-radius: 6px;">
                </div>
                <div style="display: flex; justify-content: flex-end; gap: 10px; border-top: 1px solid #E2E8F0; padding-top: 14px;">
                    <button type="button" onclick="document.getElementById('cancel-modal-overlay').style.display='none'" class="gw-btn">Close</button>
                    <button type="submit" class="gw-btn" style="background:#DC2626; color:#fff;">⚠️ Cancel Policy</button>
                </div>
            </form>
        </div>
    `;
    overlay.style.display = 'flex';
}

async function submitPolicyCancellation(e, jobNumber) {
    e.preventDefault();
    const cancelType = document.getElementById('cancel-type').value;
    const reason = document.getElementById('cancel-reason').value;

    try {
        const res = await fetch(`${API_BASE}/jobs/cancel`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber, cancelType: cancelType, reason: reason })
        });
        if (res.ok) {
            document.getElementById('cancel-modal-overlay').style.display = 'none';
            loadJobs();
            loadPolicies();
        } else {
            const err = await res.json();
            alert('Error: ' + (err.error || 'Failed to cancel policy'));
        }
    } catch (e) {
        alert('Network error cancelling policy');
    }
}

async function handleReinstatement(jobNumber) {
    if (!confirm(`Reinstate Cancelled Policy ${jobNumber}?`)) return;

    try {
        const res = await fetch(`${API_BASE}/jobs/reinstate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: jobNumber })
        });
        if (res.ok) {
            alert(`Policy ${jobNumber} Reinstated to active Bound status!`);
            loadJobs();
            loadPolicies();
        } else {
            const err = await res.json();
            alert('Error: ' + (err.error || 'Failed to reinstate policy'));
        }
    } catch (e) {
        alert('Network error reinstating policy');
    }
}
