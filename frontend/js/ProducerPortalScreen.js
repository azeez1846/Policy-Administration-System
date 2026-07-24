// Guidewire PolicyCenter - Producer / Broker Quick-Quote Portal UI

function renderProducerPortalScreen(container) {
    container.innerHTML = `
        <div style="display:grid; grid-template-columns: 1fr 1fr; gap:16px;">
            
            <!-- Producer 60-Second Quick Quote Form -->
            <div class="gw-panel">
                <div class="gw-panel-header">
                    <span>⚡ Producer 60-Second Quick Quote</span>
                </div>
                <div class="gw-panel-body">
                    <form onsubmit="handleQuickQuoteSubmit(event)">
                        <div style="margin-bottom:12px;">
                            <label style="font-size:11px; font-weight:700; color:#475569;">PRODUCT LINE</label>
                            <select id="qq-line" style="width:100%; padding:8px; border:1px solid #CBD5E1; border-radius:4px; font-size:13px;">
                                <option value="Commercial Property">Commercial Property</option>
                                <option value="Commercial Auto">Commercial Auto Fleet</option>
                                <option value="Workers Compensation">Workers' Compensation</option>
                            </select>
                        </div>

                        <div style="margin-bottom:12px;">
                            <label style="font-size:11px; font-weight:700; color:#475569;">EXPOSURE / COVERAGE LIMIT ($)</label>
                            <input type="number" id="qq-limit" value="1000000" step="50000" style="width:100%; padding:8px; border:1px solid #CBD5E1; border-radius:4px; font-size:13px;" required>
                        </div>

                        <div style="margin-bottom:16px;">
                            <label style="font-size:11px; font-weight:700; color:#475569;">PRIMARY STATE</label>
                            <input type="text" id="qq-state" value="IL" style="width:100%; padding:8px; border:1px solid #CBD5E1; border-radius:4px; font-size:13px;" required>
                        </div>

                        <button type="submit" class="gw-btn gw-btn-primary" style="width:100%; padding:10px;">⚡ Generate Instant Producer Quote</button>
                    </form>
                </div>
            </div>

            <!-- Instant Quote Result Card -->
            <div class="gw-panel">
                <div class="gw-panel-header">
                    <span>Quick Quote Summary</span>
                </div>
                <div class="gw-panel-body" id="qq-result-panel">
                    <div style="text-align:center; padding:40px; color:#94A3B8;">
                        Fill in parameters on the left to generate an instant quote.
                    </div>
                </div>
            </div>

        </div>
    `;
}

async function handleQuickQuoteSubmit(e) {
    e.preventDefault();
    const line = document.getElementById('qq-line').value;
    const limit = document.getElementById('qq-limit').value;
    const panel = document.getElementById('qq-result-panel');

    panel.innerHTML = `<div style="color:#64748B;">Calculating instant producer rate...</div>`;

    try {
        const res = await fetch(`${API_BASE}/portal/quick-quote`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productLine: line, limit: limit })
        });
        const data = await res.json();

        panel.innerHTML = `
            <div style="text-align:center; padding:10px;">
                <span class="gw-badge gw-badge-quoted">${data.status}</span>
                <div style="font-size:12px; color:#64748B; margin-top:6px;">Quote Reference: <strong>${data.quoteNumber}</strong></div>
                <div style="font-size:32px; font-weight:800; color:#166534; margin:12px 0;">$${data.estimatedTotalCost.toLocaleString()}</div>
                <div style="font-size:11px; color:#475569;">Estimated Annual Premium: $${data.estimatedPremium.toLocaleString()} + Taxes & Fees: $${data.taxAndFees}</div>
                
                <div style="margin-top:20px; display:flex; gap:10px; justify-content:center;">
                    <button class="gw-btn gw-btn-success" onclick="openNewSubmissionModal()">Convert to Full Submission</button>
                    <button class="gw-btn" onclick="window.open('${API_BASE}/portal/coi', '_blank')">Download COI (ACORD 25)</button>
                </div>
            </div>
        `;
    } catch (err) {
        panel.innerHTML = `<div style="color:red;">Error generating quick quote: ${err}</div>`;
    }
}
