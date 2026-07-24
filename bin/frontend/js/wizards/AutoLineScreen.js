// Emulates Guidewire CommercialAutoScreen.pcf / PolicyVehiclesLV.pcf / PolicyDriversLV.pcf

function renderAutoLineStep(container, period) {
    container.innerHTML = `
        <div class="gw-panel" style="margin-bottom:20px;">
            <div class="gw-panel-header">
                <span>Vehicle Schedule (PolicyVehiclesLV.pcf)</span>
                <button class="gw-btn gw-btn-primary" style="padding:4px 10px; font-size:11px;" onclick="handleAddVehicleModal()">+ Add Vehicle</button>
            </div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Veh #</th>
                            <th>VIN</th>
                            <th>Year / Make / Model</th>
                            <th>Use Type</th>
                            <th>Cost New ($)</th>
                            <th>Garage State</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>1FTFW1E84MKD90182</td>
                            <td>2023 Ford F-350 Super Duty</td>
                            <td>Commercial</td>
                            <td>$65,000</td>
                            <td>IL</td>
                            <td><button class="gw-btn" style="padding:2px 6px; font-size:11px;">Edit</button></td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>2G1FA1E39D9102847</td>
                            <td>2022 Chevrolet Express Cargo Van</td>
                            <td>Service / Utility</td>
                            <td>$48,500</td>
                            <td>IL</td>
                            <td><button class="gw-btn" style="padding:2px 6px; font-size:11px;">Edit</button></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="gw-panel">
            <div class="gw-panel-header">
                <span>Driver Schedule & Motor Vehicle Records (PolicyDriversLV.pcf)</span>
                <button class="gw-btn gw-btn-primary" style="padding:4px 10px; font-size:11px;" onclick="handleAddDriverModal()">+ Add Driver</button>
            </div>
            <div class="gw-panel-body">
                <table class="gw-table">
                    <thead>
                        <tr>
                            <th>Driver #</th>
                            <th>Driver Name</th>
                            <th>Date of Birth</th>
                            <th>License # & State</th>
                            <th>Violations (3 Yrs)</th>
                            <th>Good Driver Discount</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>David Miller</td>
                            <td>1982-08-24</td>
                            <td>IL-D8910273 (IL)</td>
                            <td>0</td>
                            <td><span class="gw-badge gw-badge-bound">YES (10% Discount)</span></td>
                            <td><span class="gw-badge gw-badge-issued">MVR CLEAN</span></td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>Robert Davis</td>
                            <td>1990-03-12</td>
                            <td>IL-R9018274 (IL)</td>
                            <td>1 (Speeding)</td>
                            <td><span class="gw-badge gw-badge-draft">NO</span></td>
                            <td><span class="gw-badge gw-badge-quoted">MVR ACCEPTED</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `;
}

function handleAddVehicleModal() {
    let overlay = document.getElementById('ca-vehicle-modal-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'ca-vehicle-modal-overlay';
        overlay.style.cssText = `
            position: fixed; top:0; left:0; width:100vw; height:100vh;
            background: rgba(15,23,42,0.6); backdrop-filter: blur(4px);
            display:flex; justify-content:center; align-items:center; z-index:9999;
        `;
        document.body.appendChild(overlay);
    }
    overlay.innerHTML = `
        <div style="background:#FFF; border-radius:12px; width:480px; padding:24px; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);">
            <h3 style="margin:0 0 4px 0; font-size:18px; color:#0F172A;">Add Vehicle to Schedule</h3>
            <p style="margin:0 0 16px 0; font-size:12px; color:#64748B;">Enter VIN, Make/Model, and Stated Value.</p>
            <div style="display:flex; flex-direction:column; gap:12px;">
                <div>
                    <label style="font-size:12px; font-weight:600; color:#334155;">Vehicle VIN *</label>
                    <input type="text" id="veh-vin" value="1G1YC2D45R5102938" style="width:100%; padding:8px 12px; border:1px solid #CBD5E1; border-radius:6px;">
                </div>
                <div>
                    <label style="font-size:12px; font-weight:600; color:#334155;">Year, Make & Model *</label>
                    <input type="text" id="veh-make" value="2024 Freightliner M2 106" style="width:100%; padding:8px 12px; border:1px solid #CBD5E1; border-radius:6px;">
                </div>
                <div>
                    <label style="font-size:12px; font-weight:600; color:#334155;">Stated Value ($) *</label>
                    <input type="number" id="veh-val" value="85000" style="width:100%; padding:8px 12px; border:1px solid #CBD5E1; border-radius:6px;">
                </div>
            </div>
            <div style="margin-top:20px; display:flex; justify-content:flex-end; gap:8px;">
                <button onclick="document.getElementById('ca-vehicle-modal-overlay').style.display='none'" class="gw-btn">Cancel</button>
                <button onclick="submitNewVehicle()" class="gw-btn gw-btn-primary">✓ Add Vehicle</button>
            </div>
        </div>
    `;
    overlay.style.display = 'flex';
}

function submitNewVehicle() {
    const vin = document.getElementById('veh-vin').value;
    const make = document.getElementById('veh-make').value;
    document.getElementById('ca-vehicle-modal-overlay').style.display = 'none';
    alert(`Vehicle [${make}] (VIN: ${vin}) successfully added to Commercial Auto schedule!`);
}

function handleAddDriverModal() {
    let overlay = document.getElementById('ca-driver-modal-overlay');
    if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'ca-driver-modal-overlay';
        overlay.style.cssText = `
            position: fixed; top:0; left:0; width:100vw; height:100vh;
            background: rgba(15,23,42,0.6); backdrop-filter: blur(4px);
            display:flex; justify-content:center; align-items:center; z-index:9999;
        `;
        document.body.appendChild(overlay);
    }
    overlay.innerHTML = `
        <div style="background:#FFF; border-radius:12px; width:480px; padding:24px; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1);">
            <h3 style="margin:0 0 4px 0; font-size:18px; color:#0F172A;">Add Driver to Schedule</h3>
            <p style="margin:0 0 16px 0; font-size:12px; color:#64748B;">Enter driver name, license details, and age.</p>
            <div style="display:flex; flex-direction:column; gap:12px;">
                <div>
                    <label style="font-size:12px; font-weight:600; color:#334155;">Full Name *</label>
                    <input type="text" id="drv-name" value="James Wilson" style="width:100%; padding:8px 12px; border:1px solid #CBD5E1; border-radius:6px;">
                </div>
                <div>
                    <label style="font-size:12px; font-weight:600; color:#334155;">Driver License # *</label>
                    <input type="text" id="drv-lic" value="IL-W9018274" style="width:100%; padding:8px 12px; border:1px solid #CBD5E1; border-radius:6px;">
                </div>
                <div>
                    <label style="font-size:12px; font-weight:600; color:#334155;">Age *</label>
                    <input type="number" id="drv-age" value="38" style="width:100%; padding:8px 12px; border:1px solid #CBD5E1; border-radius:6px;">
                </div>
            </div>
            <div style="margin-top:20px; display:flex; justify-content:flex-end; gap:8px;">
                <button onclick="document.getElementById('ca-driver-modal-overlay').style.display='none'" class="gw-btn">Cancel</button>
                <button onclick="submitNewDriver()" class="gw-btn gw-btn-primary">✓ Add Driver</button>
            </div>
        </div>
    `;
    overlay.style.display = 'flex';
}

function submitNewDriver() {
    const name = document.getElementById('drv-name').value;
    const lic = document.getElementById('drv-lic').value;
    document.getElementById('ca-driver-modal-overlay').style.display = 'none';
    alert(`Driver [${name}] (License: ${lic}) successfully added to Driver schedule!`);
}
