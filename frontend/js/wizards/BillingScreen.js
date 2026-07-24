// Emulates Guidewire BillingScreen.pcf / PaymentPlanDV.pcf

function renderBillingStep(container, period) {
    const total = period.totalCost || 2467.50;
    const comm = total * 0.15;

    container.innerHTML = `
        <div class="gw-panel" style="margin-bottom:20px;">
            <div class="gw-panel-header">Payment Plan & Billing Center Integration (BillingScreen.pcf)</div>
            <div class="gw-panel-body">
                <div class="gw-form-grid">
                    <div class="gw-field">
                        <label>Payment Plan Option</label>
                        <select id="sel-pay-plan" onchange="fetchBillingInstallments(this.value)">
                            <option value="FullPay">Full Pay (100% due at inception)</option>
                            <option value="FourPay">Quarterly (25% downpayment + 3 installments)</option>
                            <option value="TwelvePay" selected>Monthly (10% downpayment + 11 installments)</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Billing Method</label>
                        <select>
                            <option>Direct Bill (Invoice Insured)</option>
                            <option>Agency Bill (Broker Statement)</option>
                        </select>
                    </div>
                    <div class="gw-field">
                        <label>Total Annual Policy Cost</label>
                        <input type="text" value="$${total.toFixed(2)}" readonly>
                    </div>
                    <div class="gw-field">
                        <label>Producer Commission (15%)</label>
                        <input type="text" value="$${comm.toFixed(2)}" readonly>
                    </div>
                </div>
            </div>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">Installment Schedule Preview</div>
            <div class="gw-panel-body">
                <div id="billing-schedule-table">Loading installment schedule...</div>
            </div>
        </div>
    `;

    fetchBillingInstallments('TwelvePay');
}

async function fetchBillingInstallments(plan) {
    if (!currentJob) return;
    try {
        const res = await fetch(`${API_BASE}/billing/installments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ jobNumber: currentJob.jobNumber, plan: plan })
        });
        if (res.ok) {
            const data = await res.json();
            const rows = data.installments.map(ins => `
                <tr>
                    <td>Installment #${ins.num}</td>
                    <td>${ins.date}</td>
                    <td>${ins.desc}</td>
                    <td style="text-align:right; font-weight:600;">$${ins.amount.toFixed(2)}</td>
                </tr>
            `).join('');

            document.getElementById('billing-schedule-table').innerHTML = `
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Due Date</th>
                            <th>Description</th>
                            <th style="text-align:right;">Amount ($)</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }
    } catch (e) {
        console.error("Error fetching billing installments", e);
    }
}
