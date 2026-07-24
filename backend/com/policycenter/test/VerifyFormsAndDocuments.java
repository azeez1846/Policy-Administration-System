package com.policycenter.test;

import com.policycenter.gs.classes.forms.*;
import com.policycenter.model.*;

import java.util.List;

public class VerifyFormsAndDocuments {
    public static void main(String[] args) {
        System.out.println("Testing Phase 4 Forms Inference & Document Generation Engine...");

        PolicyPeriod period = new PolicyPeriod();
        period.setProductCode("CommercialProperty");
        period.setProductName("Commercial Property");

        // 1. Infer Forms
        List<PolicyForm> forms = FormsInferenceEngine.inferPolicyForms(period);
        System.out.println("1. Forms Inferred -> Total Attached Forms: " + forms.size());
        for (PolicyForm f : forms) {
            System.out.println("   - [" + f.getFormNumber() + "] " + f.getFormName() + " (" + f.getEdition() + ")");
        }

        // 2. Generate Dec Page HTML Document
        String html = DocumentGenerator.generatePolicyDecPageHtml(period);
        System.out.println("2. Policy Dec Page Document Generated -> Length: " + html.length() + " bytes");

        if (forms.size() >= 3 && html.contains("POLICY DECLARATIONS")) {
            System.out.println("SUCCESS: Phase 4 Forms Inference & Document Generation Engine Verified Clean!");
        } else {
            System.err.println("FAILURE: Forms inference or document generator failed validation.");
            System.exit(1);
        }
    }
}
