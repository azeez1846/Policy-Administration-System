// Guidewire PolicyCenter - Activity Task & Underwriting Note Management UI

async function renderActivityTaskScreen(container) {
    container.innerHTML = `<div style="padding:20px; color:#64748B;">Loading Underwriting Tasks & Internal Notes...</div>`;

    try {
        const [actRes, noteRes] = await Promise.all([
            fetch(`${API_BASE}/activities`),
            fetch(`${API_BASE}/activities/notes`)
        ]);

        const activities = actRes.ok ? await actRes.json() : [];
        const notes = noteRes.ok ? await noteRes.json() : [];

        let actRows = activities.map(a => `
            <tr>
                <td style="font-weight:700; color:#0F172A;">${a.subject}</td>
                <td><span class="gw-badge ${a.priority === 'Urgent' ? 'gw-badge-draft' : (a.priority === 'High' ? 'gw-badge-quoted' : 'gw-badge-bound')}">${a.priority}</span></td>
                <td>${a.assignedTo}</td>
                <td>${a.dueDate}</td>
                <td><span class="gw-badge ${a.status === 'Open' ? 'gw-badge-draft' : 'gw-badge-bound'}">${a.status}</span></td>
                <td><button class="gw-btn" style="padding:2px 8px; font-size:11px;" onclick="alert('Task marked completed!')">Complete</button></td>
            </tr>
        `).join('');

        let noteCards = notes.map(n => `
            <div style="background:#F8FAFC; border:1px solid #CBD5E1; border-radius:6px; padding:12px; margin-bottom:10px;">
                <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:6px;">
                    <strong style="color:#2563EB; font-size:12px;">${n.topic}</strong>
                    <span style="font-size:11px; color:#64748B;">${n.author} • ${n.date}</span>
                </div>
                <div style="font-size:12px; color:#334155; line-height:1.4;">${n.body}</div>
            </div>
        `).join('');

        container.innerHTML = `
            <div style="display:grid; grid-template-columns: 2fr 1fr; gap:16px;">
                
                <!-- Left Activity Tasks Panel -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <span>📋 Underwriting Activities & Follow-up Tasks</span>
                            <button class="gw-btn gw-btn-primary" style="font-size:11px;" onclick="createNewActivityPrompt()">+ Create Activity</button>
                        </div>
                        <div class="gw-panel-body" style="padding:0;">
                            <table class="gw-table">
                                <thead>
                                    <tr>
                                        <th>Subject</th>
                                        <th>Priority</th>
                                        <th>Assigned To</th>
                                        <th>Due Date</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${actRows}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Right Internal Notes Feed Panel -->
                <div>
                    <div class="gw-panel">
                        <div class="gw-panel-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <span>📝 Underwriting Notes</span>
                            <button class="gw-btn gw-btn-primary" style="font-size:11px;" onclick="createNewNotePrompt()">+ Add Note</button>
                        </div>
                        <div class="gw-panel-body" style="max-height:420px; overflow-y:auto;">
                            ${noteCards}
                        </div>
                    </div>
                </div>

            </div>
        `;
    } catch (e) {
        container.innerHTML = `<div style="color:red; padding:20px;">Error loading activity tasks: ${e}</div>`;
    }
}

async function createNewActivityPrompt() {
    const subject = prompt("Enter Activity Subject:", "Verify Commercial Auto Vehicle Vin Registration");
    if (subject) {
        await fetch(`${API_BASE}/activities`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ subject: subject, priority: "High" })
        });
        renderActivityTaskScreen(document.getElementById('work-area'));
    }
}

async function createNewNotePrompt() {
    const body = prompt("Enter Underwriting Note Body:", "Verified financial credit score and prior loss run history clean.");
    if (body) {
        await fetch(`${API_BASE}/activities/notes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ topic: "UW Assessment Note", body: body })
        });
        renderActivityTaskScreen(document.getElementById('work-area'));
    }
}
