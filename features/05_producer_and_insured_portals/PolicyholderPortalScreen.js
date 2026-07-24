// Guidewire PolicyCenter - Insured Customer Self-Service Portal UI

function renderPolicyholderPortalScreen(container) {
    container.innerHTML = `
        <div style="display:grid; grid-template-columns: 2fr 1fr; gap:16px;">
            
            <!-- Active Policies Overview -->
            <div>
                <div class="gw-panel">
                    <div class="gw-panel-header">My Policy Portfolio</div>
                    <div class="gw-panel-body" style="padding:0;">
                        <table class="gw-table">
                            <thead>
                                <tr>
                                    <th>Policy Number</th>
                                    <th>Product Line</th>
                                    <th>Effective Term</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="color:#2563EB; font-weight:700;">POL-3764124</td>
                                    <td>Commercial Auto Fleet</td>
                                    <td>2026-07-23 - 2027-07-23</td>
                                    <td><span class="gw-badge gw-badge-bound">Bound</span></td>
                                    <td><button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="window.open('${API_BASE}/documents/dec-page', '_blank')">Download Dec Page</button></td>
                                </tr>
                                <tr>
                                    <td style="color:#2563EB; font-weight:700;">CP-3451127</td>
                                    <td>Commercial Property</td>
                                    <td>2026-07-23 - 2027-07-23</td>
                                    <td><span class="gw-badge gw-badge-bound">Bound</span></td>
                                    <td><button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="window.open('${API_BASE}/portal/coi', '_blank')">Request COI</button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Customer Self-Service Quick Links -->
            <div>
                <div class="gw-panel">
                    <div class="gw-panel-header">Self-Service Actions</div>
                    <div class="gw-panel-body">
                        <button class="gw-btn gw-btn-primary" style="width:100%; margin-bottom:10px; padding:8px;" onclick="window.open('${API_BASE}/portal/coi', '_blank')">📄 Certificate of Insurance (ACORD 25)</button>
                        <button class="gw-btn" style="width:100%; margin-bottom:10px; padding:8px;" onclick="alert('Policy Change request submitted to Underwriting!')">✏️ Request Policy Change (Endorsement)</button>
                        <button class="gw-btn" style="width:100%; padding:8px;" onclick="switchMainTab('billing')">💳 View Billing Statements & Auto-Pay</button>
                    </div>
                </div>
            </div>

        </div>
    `;
}
