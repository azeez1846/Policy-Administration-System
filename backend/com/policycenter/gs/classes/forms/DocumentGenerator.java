package com.policycenter.gs.classes.forms;

import com.policycenter.model.*;

/**
 * Emulates Guidewire PolicyCenter Document Generator (PolicyDecPage.gs / DocumentTemplate.gs).
 * Generates official Policy Declarations (Dec Pages) and Quote Proposals.
 */
public class DocumentGenerator {

    public static String generatePolicyDecPageHtml(PolicyPeriod period) {
        if (period == null) return "<html><body><h1>Policy Period Not Found</h1></body></html>";

        String insuredName = period.getPrimaryNamedInsured() != null ? period.getPrimaryNamedInsured().getName() : "Acme Logistics Inc";
        String polNum = period.getPolicyNumber() != null ? period.getPolicyNumber() : "CP-8472910";
        String prodName = period.getProductName() != null ? period.getProductName() : "Commercial Property";

        StringBuilder formsSb = new StringBuilder();
        if (period.getForms() != null) {
            for (PolicyForm f : period.getForms()) {
                formsSb.append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", f.getFormNumber(), f.getFormName(), f.getEdition()));
            }
        }

        return String.format(
            "<!DOCTYPE html><html><head><title>Policy Declarations - %s</title>" +
            "<style>body{font-family:sans-serif; padding:40px; color:#1e293b;} .header{border-bottom:3px solid #0284c7; padding-bottom:12px;} .table{width:100%%; border-collapse:collapse; margin-top:20px;} .table th,.table td{border:1px solid #cbd5e1; padding:8px; text-align:left;} .table th{background:#f1f5f9;}</style></head>" +
            "<body>" +
            "<div class='header'><h2>GUIDEWIRE POLICYCENTER - COMMERCIAL INSURANCE POLICY DECLARATIONS</h2><p>Policy Number: <strong>%s</strong> | Status: <strong>%s</strong></p></div>" +
            "<div style='margin-top:20px;'><p><strong>Named Insured:</strong> %s</p><p><strong>Product Line:</strong> %s</p><p><strong>Policy Period:</strong> %s to %s</p></div>" +
            "<h3>PREMIUM SUMMARY</h3><p>Total Annual Premium: <strong>$%.2f</strong> | Taxes & Fees: <strong>$%.2f</strong> | Total Cost: <strong>$%.2f</strong></p>" +
            "<h3>ATTACHED FORMS & ENDORSEMENTS SCHEDULE</h3><table class='table'><thead><tr><th>Form Number</th><th>Form Title</th><th>Edition</th></tr></thead><tbody>%s</tbody></table>" +
            "</body></html>",
            polNum, polNum, period.getStatus(), insuredName, prodName, period.getEffectiveDate(), period.getExpirationDate(),
            period.getTotalPremium(), period.getTaxAndFees(), period.getTotalCost(), formsSb.toString()
        );
    }
}
