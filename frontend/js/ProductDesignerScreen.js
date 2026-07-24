// Guidewire PolicyCenter - No-Code Product Model Designer Studio

function renderProductDesignerScreen(container) {
    container.innerHTML = `
        <div style="display:grid; grid-template-columns: 1fr 2fr; gap:16px;">
            
            <!-- Product Line Selector Panel -->
            <div>
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <span>Installed Product Lines</span>
                        <button class="gw-btn gw-btn-primary" style="font-size:11px;" onclick="addNewProductLine()">+ Add Line</button>
                    </div>
                    <div class="gw-panel-body" style="padding:0;">
                        <ul class="gw-tree-list" style="padding:8px;">
                            <li class="gw-tree-item active">Commercial Property (CP)</li>
                            <li class="gw-tree-item">Commercial Auto (CA)</li>
                            <li class="gw-tree-item">Workers' Compensation (WC)</li>
                            <li class="gw-tree-item">General Liability (GL)</li>
                            <li class="gw-tree-item">Cyber Liability & Data Breach (NEW)</li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Product Model Coverage & Term Configurator -->
            <div>
                <div class="gw-panel">
                    <div class="gw-panel-header">
                        <span>Coverage Terms & Deductibles: Commercial Property</span>
                        <button class="gw-btn gw-btn-success" style="font-size:11px;" onclick="alert('Product Model saved and synchronized with Database pattern registry!')">Save Pattern</button>
                    </div>
                    <div class="gw-panel-body">
                        <table class="gw-table">
                            <thead>
                                <tr>
                                    <th>Pattern Code</th>
                                    <th>Coverage Name</th>
                                    <th>Category</th>
                                    <th>Default Limit</th>
                                    <th>Default Deductible</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="font-weight:700; color:#2563EB;">CPBuildingCov</td>
                                    <td>Building Coverage</td>
                                    <td>Direct Property</td>
                                    <td>$1,000,000</td>
                                    <td>$2,500</td>
                                </tr>
                                <tr>
                                    <td style="font-weight:700; color:#2563EB;">CPBPPBldgCov</td>
                                    <td>Business Personal Property</td>
                                    <td>Direct Property</td>
                                    <td>$250,000</td>
                                    <td>$1,000</td>
                                </tr>
                                <tr>
                                    <td style="font-weight:700; color:#2563EB;">CPBusIncomeCov</td>
                                    <td>Business Income & Extra Expense</td>
                                    <td>Time Element</td>
                                    <td>$500,000</td>
                                    <td>72 Hours</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    `;
}

function addNewProductLine() {
    const lineName = prompt("Enter name of new Product Line (e.g., Inland Marine):", "Inland Marine");
    if (lineName) {
        alert(`Product Line '${lineName}' created and added to Product Model pattern registry!`);
        renderProductDesignerScreen(document.getElementById('work-area'));
    }
}
