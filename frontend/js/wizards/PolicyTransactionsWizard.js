// Guidewire PolicyCenter Policy Transactions Wizard Handler

async function handlePolicyChange(jobNumber, policyNumber) {
    if (typeof showPolicyChangeWizard === 'function') {
        showPolicyChangeWizard(jobNumber, policyNumber);
    } else {
        alert('Policy Change Wizard is loading...');
    }
}

async function handleRenewal(jobNumber, policyNumber) {
    if (typeof showRenewalScreen === 'function') {
        showRenewalScreen(jobNumber, policyNumber);
    } else {
        alert('Renewal Wizard is loading...');
    }
}

function handleCancellation(jobNumber, policyNumber) {
    if (typeof showCancellationScreen === 'function') {
        showCancellationScreen(jobNumber, policyNumber);
    } else {
        alert('Cancellation Wizard is loading...');
    }
}

async function handleReinstatement(jobNumber, policyNumber) {
    if (typeof showReinstatementScreen === 'function') {
        showReinstatementScreen(jobNumber, policyNumber);
    } else {
        alert('Reinstatement Wizard is loading...');
    }
}
