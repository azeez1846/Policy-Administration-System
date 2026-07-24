package com.policycenter.gs.classes.forms;

import com.policycenter.model.PolicyForm;
import com.policycenter.model.PolicyPeriod;

import java.util.ArrayList;
import java.util.List;

/**
 * Emulates Guidewire PolicyCenter Forms Inference Engine (FormsInferenceEngine.gs).
 * Automatically infers and attaches policy forms and endorsements based on product line & coverages.
 */
public class FormsInferenceEngine {

    public static List<PolicyForm> inferPolicyForms(PolicyPeriod period) {
        List<PolicyForm> inferredForms = new ArrayList<>();
        if (period == null) return inferredForms;

        // Form 1: Mandatory Common Policy Conditions for all Commercial Lines
        PolicyForm il0017 = new PolicyForm(
            "form-il0017",
            "IL 00 17",
            "Common Policy Conditions",
            "11 98",
            "Mandatory Common Policy Conditions for Commercial Lines"
        );
        inferredForms.add(il0017);

        if ("CommercialAuto".equalsIgnoreCase(period.getProductCode()) || "PersonalAuto".equalsIgnoreCase(period.getProductCode())) {
            // Form 2: Business Auto Coverage Form
            PolicyForm ca0001 = new PolicyForm(
                "form-ca0001",
                "CA 00 01",
                "Business Auto Coverage Form",
                "10 13",
                "Inferred for Commercial Auto Line of Business"
            );
            inferredForms.add(ca0001);
        } else {
            // Form 2: Building and Personal Property Coverage Form
            PolicyForm cp0010 = new PolicyForm(
                "form-cp0010",
                "CP 00 10",
                "Building and Personal Property Coverage Form",
                "10 12",
                "Inferred for Commercial Property Line of Business"
            );
            inferredForms.add(cp0010);

            // Form 3: Causes of Loss - Special Form
            PolicyForm cp1030 = new PolicyForm(
                "form-cp1030",
                "CP 10 30",
                "Causes of Loss - Special Form",
                "10 12",
                "Inferred for Commercial Property Special Form Coverage"
            );
            inferredForms.add(cp1030);
        }

        // Attach inferred forms to period
        for (PolicyForm form : inferredForms) {
            boolean exists = false;
            for (PolicyForm existing : period.getForms()) {
                if (existing.getFormNumber().equalsIgnoreCase(form.getFormNumber())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                period.addForm(form);
            }
        }

        return period.getForms();
    }
}
