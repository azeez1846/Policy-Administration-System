package com.policycenter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/portal")
@CrossOrigin(origins = "*")
public class PortalController {

    @PostMapping("/quick-quote")
    public Map<String, Object> calculateQuickQuote(@RequestBody Map<String, String> payload) {
        String line = payload.getOrDefault("productLine", "Commercial Property");
        double limit = 1000000.0;
        try {
            limit = Double.parseDouble(payload.getOrDefault("limit", "1000000"));
        } catch (NumberFormatException ignored) {}

        double baseRate = "Commercial Auto".equalsIgnoreCase(line) ? 0.0035 : ("Workers Compensation".equalsIgnoreCase(line) ? 0.0042 : 0.0024);
        double premium = limit * baseRate;
        double tax = premium * 0.05;
        double total = premium + tax + 50.0;

        return Map.of(
            "quoteNumber", "QQ-" + (int)(10000 + Math.random() * 90000),
            "productLine", line,
            "estimatedLimit", limit,
            "estimatedPremium", Math.round(premium * 100.0) / 100.0,
            "taxAndFees", Math.round((tax + 50.0) * 100.0) / 100.0,
            "estimatedTotalCost", Math.round(total * 100.0) / 100.0,
            "status", "QUOTED"
        );
    }

    @GetMapping("/coi")
    public ResponseEntity<String> generateCOI() {
        String coiHtml = """
            <!DOCTYPE html>
            <html>
            <head><title>ACORD 25 - Certificate of Liability Insurance</title></head>
            <body style="font-family:sans-serif; padding:40px; background:#F8FAFC;">
                <div style="max-width:800px; margin:0 auto; background:#FFFFFF; border:2px solid #0F172A; padding:30px;">
                    <div style="display:flex; justify-content:space-between; border-bottom:2px solid #0F172A; padding-bottom:12px;">
                        <div><h2>ACORD™ CERTIFICATE OF LIABILITY INSURANCE</h2></div>
                        <div>DATE: <strong>2026-07-23</strong></div>
                    </div>
                    <div style="margin-top:20px; display:grid; grid-template-columns:1fr 1fr; gap:20px;">
                        <div>
                            <strong>PRODUCER:</strong><br>Apex Global Insurance Brokers<br>100 Financial Plaza, Suite 800<br>Chicago, IL 60606
                        </div>
                        <div>
                            <strong>INSURED:</strong><br>Titan Freight Logistics Inc<br>999 Commerce Blvd<br>Chicago, IL 60616
                        </div>
                    </div>
                    <div style="margin-top:24px;">
                        <table style="width:100%; border-collapse:collapse; border:1px solid #CBD5E1;" border="1" cellpadding="8">
                            <tr style="background:#E2E8F0;">
                                <th>TYPE OF INSURANCE</th>
                                <th>POLICY NUMBER</th>
                                <th>EFF DATE</th>
                                <th>EXP DATE</th>
                                <th>LIMITS</th>
                            </tr>
                            <tr>
                                <td>COMMERCIAL GENERAL LIABILITY</td>
                                <td>GL-9821034</td>
                                <td>2026-01-01</td>
                                <td>2027-01-01</td>
                                <td>$2,000,000 Each Occurrence</td>
                            </tr>
                            <tr>
                                <td>COMMERCIAL AUTO FLEET</td>
                                <td>POL-3764124</td>
                                <td>2026-07-23</td>
                                <td>2027-07-23</td>
                                <td>$1,000,000 Combined Single Limit</td>
                            </tr>
                        </table>
                    </div>
                    <div style="margin-top:30px; border-top:1px solid #CBD5E1; padding-top:12px; font-size:11px; color:#64748B;">
                        THIS CERTIFICATE IS ISSUED AS A MATTER OF INFORMATION ONLY AND CONFERS NO RIGHTS UPON THE CERTIFICATE HOLDER.
                    </div>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok().header("Content-Type", "text/html").body(coiHtml);
    }
}
