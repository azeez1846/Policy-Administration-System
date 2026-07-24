// Emulates Guidewire BatchProcessLV.pcf / WorkQueue Batch Processing Monitor

function renderBatchSchedulerScreen(container) {
    container.innerHTML = `
        <div style="margin-bottom:20px; display:flex; justify-content:space-between; align-items:center;">
            <div>
                <h2 style="margin:0; font-size:22px; color:#0f172a; font-weight:700;">Batch Process Monitor & WorkQueue (BatchProcessLV.pcf)</h2>
                <p style="margin:4px 0 0 0; color:#64748b; font-size:13px;">Manage automated background WorkQueue tasks: Renewal Notice generation, Expiration Processing, and UW Escalations.</p>
            </div>
            <div>
                <button class="gw-btn gw-btn-primary" style="margin-right:8px;" onclick="triggerBatchRun('RenewalNoticeBatch')">▶ Run Renewal Batch</button>
                <button class="gw-btn" style="margin-right:8px;" onclick="triggerBatchRun('PolicyExpirationBatch')">▶ Run Expiration Batch</button>
                <button class="gw-btn" onclick="triggerBatchRun('UWEscalationBatch')">▶ Run UW Escalation</button>
            </div>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Guidewire WorkQueue Batch Status (BatchProcessLV.pcf)</span>
                <span class="gw-badge gw-badge-bound">SPRING BOOT WORKQUEUE</span>
            </div>
            <div class="gw-panel-body">
                <div id="batch-jobs-table-container">Loading batch jobs...</div>
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
                    <td><strong>${j.processType}</strong></td>
                    <td><span class="gw-badge gw-badge-bound">${j.status.toUpperCase()}</span></td>
                    <td>${j.lastRunTime}</td>
                    <td><strong style="color:#059669;">${j.processedCount} Records</strong></td>
                    <td><span style="color:${j.failedCount > 0 ? '#dc2626' : '#64748b'};">${j.failedCount} Errors</span></td>
                    <td>
                        <button class="gw-btn gw-btn-primary" style="padding:3px 8px; font-size:11px;" onclick="triggerBatchRun('${j.processType}')">▶ Execute Batch</button>
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
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>${rows}</tbody>
                </table>
            `;
        }
    } catch (e) {
        container.innerHTML = `<div style="color:#dc2626; padding:12px;">Failed to load batch process status.</div>`;
    }
}

async function triggerBatchRun(processType) {
    try {
        const res = await fetch(`${API_BASE}/batch/run`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ processType: processType })
        });
        if (res.ok) {
            const data = await res.json();
            alert(`Batch Job [${data.processType}] executed successfully! Processed ${data.processedCount} records.`);
            loadBatchJobsData();
        }
    } catch (e) {
        alert("Failed to trigger batch job execution.");
    }
}
