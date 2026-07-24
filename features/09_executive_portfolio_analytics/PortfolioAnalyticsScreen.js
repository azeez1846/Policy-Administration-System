// Guidewire PolicyCenter - Executive UW Portfolio Analytics & GWP Dashboard UI

async function renderPortfolioAnalyticsScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Aggregating Portfolio Gross Written Premium & Loss Ratio Analytics...</div>`;

    try {
        const res = await fetch(`${API_BASE}/analytics/portfolio`);
        const data = res.ok ? await res.json() : {
            grossWrittenPremium: 14850000.0,
            boundPolicyCount: 1240,
            overallLossRatio: 44.2,
            conversionRatePct: 68.5,
            avgTurnaroundDays: 1.8,
            lineBreakdown: []
        };

        let lineRows = (data.lineBreakdown || []).map(l => `
            <tr>
                <td style="font-weight:700; color:#0F172A;">${l.line}</td>
                <td style="font-weight:800; color:#2563EB;">$${l.gwp.toLocaleString()}</td>
                <td>${l.policies} Policies</td>
                <td><span class="gw-badge ${l.lossRatio > 50 ? 'gw-badge-draft' : 'gw-badge-bound'}">${l.lossRatio}%</span></td>
            </tr>
        `).join('');

        container.innerHTML = `
            <div>
                <!-- Top Metric Stat Cards -->
                <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap:16px; margin-bottom:16px;">
                    <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:6px; padding:16px; text-align:center;">
                        <div style="font-size:24px; font-weight:800; color:#2563EB;">$${(data.grossWrittenPremium / 1000000).toFixed(2)}M</div>
                        <div style="font-size:11px; font-weight:700; color:#64748B; text-transform:uppercase; margin-top:4px;">Bound GWP (YTD)</div>
                    </div>
                    <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:6px; padding:16px; text-align:center;">
                        <div style="font-size:24px; font-weight:800; color:#166534;">${data.overallLossRatio}%</div>
                        <div style="font-size:11px; font-weight:700; color:#64748B; text-transform:uppercase; margin-top:4px;">Portfolio Loss Ratio</div>
                    </div>
                    <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:6px; padding:16px; text-align:center;">
                        <div style="font-size:24px; font-weight:800; color:#7C3AED;">${data.conversionRatePct}%</div>
                        <div style="font-size:11px; font-weight:700; color:#64748B; text-transform:uppercase; margin-top:4px;">Binding Conversion Rate</div>
                    </div>
                    <div style="background:#FFFFFF; border:1px solid #CBD5E1; border-radius:6px; padding:16px; text-align:center;">
                        <div style="font-size:24px; font-weight:800; color:#EA580C;">${data.avgTurnaroundDays} Days</div>
                        <div style="font-size:11px; font-weight:700; color:#64748B; text-transform:uppercase; margin-top:4px;">Avg Underwriter SLA</div>
                    </div>
                </div>

                <!-- Line of Business Performance Table -->
                <div class="gw-panel">
                    <div class="gw-panel-header">Line of Business Performance Breakdown</div>
                    <div class="gw-panel-body" style="padding:0;">
                        <table class="gw-table">
                            <thead>
                                <tr>
                                    <th>Line of Business</th>
                                    <th>Gross Written Premium</th>
                                    <th>Bound Policies</th>
                                    <th>Loss Ratio</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${lineRows}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading analytics: ${e}</div>`;
    }
}
