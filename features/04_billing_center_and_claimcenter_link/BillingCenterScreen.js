// Guidewire PolicyCenter - Billing Center & Payments UI

async function renderBillingCenterScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading Billing Schedules & Installment Ledger...</div>`;

    try {
        const res = await fetch(`${API_BASE}/billing/installment-schedule?plan=12Pay&totalCost=2400`);
        const data = res.ok ? await res.json() : {
            paymentPlan: "12Pay",
            totalCost: 2400.0,
            installments: [
                { installmentNum: 1, dueDate: "2026-07-15", amount: 200.0, status: "Paid" },
                { installmentNum: 2, dueDate: "2026-08-15", amount: 200.0, status: "Scheduled" }
            ]
        };

        let rows = data.installments.map(i => `
            <tr>
                <td>#${i.installmentNum}</td>
                <td>${i.dueDate}</td>
                <td style="font-weight:700; color:#0F172A;">$${i.amount.toFixed(2)}</td>
                <td><span class="gw-badge ${i.status === 'Paid' ? 'gw-badge-bound' : 'gw-badge-quoted'}">${i.status}</span></td>
                <td><button class="gw-btn" style="padding:2px 8px; font-size:11px;" ${i.status === 'Paid' ? 'disabled' : ''} onclick="alert('Processing payment for Installment #${i.installmentNum}')">Process Payment</button></td>
            </tr>
        `).join('');

        container.innerHTML = `
            <div class="gw-panel">
                <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                    <span>Billing & Installment Plan Schedule (${data.paymentPlan})</span>
                    <div style="font-size:13px; font-weight:800; color:#1E40AF;">Total Cost: $${data.totalCost.toLocaleString()}</div>
                </div>
                <div class="gw-panel-body" style="padding:0;">
                    <table class="gw-table">
                        <thead>
                            <tr>
                                <th>Installment #</th>
                                <th>Due Date</th>
                                <th>Amount Due</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${rows}
                        </tbody>
                    </table>
                </div>
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading billing ledger: ${e}</div>`;
    }
}
