// Emulates Guidewire FormsScreen.pcf / PolicyFormsLV.pcf

function renderFormsStep(container, period) {
    const forms = period.forms || [
        { formNumber: 'IL 00 17', formName: 'Common Policy Conditions', edition: '11 98', inferenceRule: 'Mandatory Commercial Lines' },
        { formNumber: 'CP 00 10', formName: 'Building and Personal Property Coverage Form', edition: '10 12', inferenceRule: 'Commercial Property LOB' },
        { formNumber: 'CP 10 30', formName: 'Causes of Loss - Special Form', edition: '10 12', inferenceRule: 'Special Form Coverage' }
    ];

    let formRows = forms.map(f => `
        <tr>
            <td><strong>${f.formNumber}</strong></td>
            <td>${f.formName}</td>
            <td>${f.edition}</td>
            <td><span class="gw-badge gw-badge-bound">${f.inferenceRule}</span></td>
        </tr>
    `).join('');

    const polNum = period.policyNumber || (currentJob ? currentJob.jobNumber : 'SUB-5001');

    container.innerHTML = `
        <div class="gw-panel" style="margin-bottom:20px;">
            <div class="gw-panel-header">
                <span>Inferred Forms & Endorsements Schedule (FormsScreen.pcf)</span>
                <button class="gw-btn gw-btn-primary" style="padding:4px 10px; font-size:11px;" onclick="window.open('${API_BASE}/documents/dec-page?job=${polNum}', '_blank')">📄 View Policy Dec Page</button>
            </div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Form Number</th>
                            <th>Form Title</th>
                            <th>Edition</th>
                            <th>Inference Rule Source</th>
                        </tr>
                    </thead>
                    <tbody>${formRows}</tbody>
                </table>
            </div>
        </div>
    `;
}
