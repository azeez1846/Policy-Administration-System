// Guidewire PolicyCenter - Batch Process Monitor & WorkQueue Studio UI (BatchProcessLV.pcf)

let currentBatchLogs = [];

function renderBatchSchedulerScreen(container) {
    container.innerHTML = `
        <!-- Batch Studio Header Banner -->
        <div style="background: linear-gradient(135deg, #0F172A 0%, #1E293B 100%); color: #FFFFFF; padding: 20px 24px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1);">
            <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:16px;">
                <div>
                    <div style="font-size:12px; text-transform:uppercase; letter-spacing:1px; color:#60A5FA; font-weight:700;">Guidewire PolicyCenter • WorkQueue & Batch Processor Studio</div>
                    <h2 style="margin:4px 0 0 0; font-size:20px; font-weight:700; color:#FFFFFF;">Automated Batch Process Monitor (BatchProcessLV.pcf)</h2>
                    <div style="font-size:13px; color:#94A3B8; margin-top:4px;">Execute & Audit Background Policy WorkQueues: Automated Policy Changes, Renewal Notices, and Expirations</div>
                </div>
                <div style="text-align:right;">
                    <span class="gw-badge gw-badge-bound" style="font-size:12px; padding:6px 14px;">WORKQUEUE ENGINE ACTIVE</span>
                </div>
            </div>
        </div>

        <!-- Featured Auto PolicyChange Batch Card -->
        <div class="gw-panel" style="border: 2px solid #0284C7; background: #F0F9FF;">
            <div class="gw-panel-header" style="background:#0284C7; color:#FFFFFF; display:flex; justify-content:space-between; align-items:center;">
                <span>⚡ Automated Mid-Term Policy Change Batch (AutoPolicyChangeBatchProcess.gs)</span>
                <span class="gw-badge" style="background:#0369A1; color:#FFF;">FEATURED BATCH JOB</span>
            </div>
            <div class="gw-panel-body">
                <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:20px;">
                    <div style="max-width:650px;">
                        <h4 style="margin:0 0 6px 0; color:#0369A1; font-size:16px;">Automatic Bulk Policy Change Endorsement Processor</h4>
                        <p style="margin:0; font-size:13px; color:#334155; line-height:1.5;">
                            Scans all active <strong>Bound</strong> and <strong>Issued</strong> policy periods in the database.
                            Initiates a mid-term <strong>Policy Change (Endorsement)</strong> job for each policy, applies automatic building coverage limit adjustments (+ $250,000 inflation factor), calculates prorated delta premium, and issues the endorsement automatically.
                        </p>
                    </div>
                    <div>
                        <button class="gw-btn gw-btn-success" style="background:#059669; font-size:15px; font-weight:700; padding:12px 24px; box-shadow:0 4px 6px -1px rgba(0,0,0,0.1);" onclick="triggerAutoPolicyChangeBatch()">
                            ▶ Run Auto PolicyChange Batch Process
                        </button>
                    </div>
                </div>

                <!-- Batch Execution Stats Summary Box -->
                <div id="batch-stats-summary-box" style="margin-top:20px; display:none;">
                    <!-- Populated dynamically after batch execution -->
                </div>
            </div>
        </div>

        <!-- WorkQueue Batch Jobs Summary Table -->
        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Guidewire WorkQueue Batch Status Catalog (BatchProcessLV.pcf)</span>
            </div>
            <div class="gw-panel-body">
                <div id="batch-jobs-table-container">Loading batch processes...</div>
            </div>
        </div>

        <!-- Audit Execution Log Table -->
        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Auto PolicyChange Batch Audit Logs & Endorsement Records</span>
            </div>
            <div class="gw-panel-body" style="padding:0;">
                <div id="batch-audit-log-container">
                    <div style="padding:20px; text-align:center; color:#94A3B8;">No batch audit logs loaded. Click "Run Auto PolicyChange Batch Process" to execute batch and inspect issued endorsements.</div>
                </div>
            </div>
        </div>
    `;

    loadBatchJobsData();
}

async function loadBatchJobsData() {
    const container = document.getElementById('batch-jobs-table-container');
    if (!container) return;

    try {
        const res = await fetch(`${API_BASE}/batch/jobs`);
        if (res.ok) {
            const jobs = await res.json();
            let rows = jobs.map(j => `
                <tr>
                    <td><strong style="color:#0F172A;">${j.processType}</strong></td>
                    <td><span class="gw-badge gw-badge-${j.status.toLowerCase() === 'completed' ? 'bound' : 'draft'}">${j.status.toUpperCase()}</span></td>
                    <td>${j.lastRunTime || 'Not yet executed'}</td>
                    <td><strong style="color:#059669;">${j.processedCount} Records</strong></td>
                    <td><span style="color:${j.failedCount > 0 ? '#DC2626' : '#64748B'};">${j.failedCount} Errors</span></td>
                    <td style="text-align:right;">
                        <button class="gw-btn gw-btn-primary" style="padding:4px 10px; font-size:11px;" onclick="triggerBatchRun('${j.processType}')">▶ Execute Batch</button>
                    </td>
                </tr>
            `).join('');

            container.innerHTML = `
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Batch Process Name</th>
                            <th>Status</th>
                            <th>Last Run Timestamp</th>
                            <th>Processed Items</th>
                            <th>Failed Items</th>
                            <th style="text-align:right;">Actions</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }
    } catch (e) {
        container.innerHTML = `<div style="color:#DC2626; padding:12px;">Failed to load batch process status: ${e}</div>`;
    }
}

async function triggerAutoPolicyChangeBatch() {
    const summaryBox = document.getElementById('batch-stats-summary-box');
    const logContainer = document.getElementById('batch-audit-log-container');

    if (summaryBox) {
        summaryBox.style.display = 'block';
        summaryBox.innerHTML = `
            <div style="padding:16px; background:#EFF6FF; border:1px solid #BFDBFE; border-radius:6px; color:#1E40AF; text-align:center; font-weight:600;">
                <div class="gw-spinner"></div> Executing Auto PolicyChange Batch Processor... Scanning Bound/Issued Policies & Rating Prorated Deltas...
            </div>
        `;
    }

    try {
        const res = await fetch(`${API_BASE}/batch/auto-policy-change/run`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        });

        if (res.ok) {
            const data = await res.json();
            currentBatchLogs = data.executionLogs || [];

            // Update stats summary box
            if (summaryBox) {
                summaryBox.style.display = 'block';
                summaryBox.innerHTML = `
                    <div style="padding:16px; background:#ECFDF5; border:1px solid #A7F3D0; border-radius:6px;">
                        <h4 style="margin:0 0 10px 0; color:#065F46; font-size:15px;">✅ Batch Execution Complete — Auto PolicyChange Batch Process</h4>
                        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:16px;">
                            <div>
                                <div style="font-size:11px; text-transform:uppercase; color:#047857; font-weight:700;">Policies Scanned</div>
                                <div style="font-size:22px; font-weight:800; color:#065F46;">${data.policiesScanned}</div>
                            </div>
                            <div>
                                <div style="font-size:11px; text-transform:uppercase; color:#047857; font-weight:700;">Endorsements Issued</div>
                                <div style="font-size:22px; font-weight:800; color:#059669;">${data.endorsementsIssued}</div>
                            </div>
                            <div>
                                <div style="font-size:11px; text-transform:uppercase; color:#047857; font-weight:700;">Total Prorated Premium Written</div>
                                <div style="font-size:22px; font-weight:800; color:#0284C7;">+ $${(data.totalProratedPremiumWritten || 0).toLocaleString('en-US', {minimumFractionDigits:2})}</div>
                            </div>
                            <div>
                                <div style="font-size:11px; text-transform:uppercase; color:#047857; font-weight:700;">Execution Time</div>
                                <div style="font-size:14px; font-weight:700; color:#334155; margin-top:4px;">${data.startTime}</div>
                            </div>
                        </div>
                    </div>
                `;
            }

            // Update audit log table
            if (logContainer && currentBatchLogs.length > 0) {
                let logRows = currentBatchLogs.map((log, idx) => `
                    <tr>
                        <td><strong>${idx + 1}</strong></td>
                        <td style="color:#0284C7; font-weight:700;">${log.policyNumber}</td>
                        <td>${log.parentJobNumber}</td>
                        <td><strong style="color:#059669;">${log.endorsementJobNumber}</strong></td>
                        <td>${log.insuredName}</td>
                        <td>$${(log.priorBuildingLimit || 0).toLocaleString()}</td>
                        <td><strong style="color:#0284C7;">$${(log.endorsedBuildingLimit || 0).toLocaleString()}</strong> (+ $250,000)</td>
                        <td style="font-weight:700; color:#059669;">+ $${(log.proratedDeltaPremium || 0).toFixed(2)}</td>
                        <td><span class="gw-badge gw-badge-bound">BOUND & ISSUED</span></td>
                    </tr>
                `).join('');

                logContainer.innerHTML = `
                    <table class="gw-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Policy Number</th>
                                <th>Parent Job</th>
                                <th>Endorsement Job #</th>
                                <th>Insured Name</th>
                                <th>Prior Building Limit ($)</th>
                                <th>Endorsed Building Limit ($)</th>
                                <th>Prorated Delta Premium ($)</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>${logRows}</tbody>
                    </table>
                `;
            }

            await loadBatchJobsData();
            if (typeof loadJobs === 'function') await loadJobs();
            if (typeof loadPolicies === 'function') await loadPolicies();
        }
    } catch (e) {
        if (summaryBox) summaryBox.innerHTML = `<div style="color:red; padding:12px;">Failed to run batch process: ${e}</div>`;
    }
}

async function triggerBatchRun(processType) {
    if ("AutoPolicyChangeBatch".equalsIgnoreCase(processType) || "AutoPolicyChangeBatch" === processType) {
        triggerAutoPolicyChangeBatch();
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/batch/run`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ processType: processType })
        });
        if (res.ok) {
            const data = await res.json();
            alert(`✅ Batch Job [${processType}] executed successfully!\nProcessed ${data.processedCount || 10} records.`);
            loadBatchJobsData();
        }
    } catch (e) {
        alert("Failed to trigger batch job execution.");
    }
}
