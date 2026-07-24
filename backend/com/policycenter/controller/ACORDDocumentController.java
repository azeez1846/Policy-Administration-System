package com.policycenter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class ACORDDocumentController {

    @GetMapping("/acord125")
    public ResponseEntity<String> getACORD125Document(@RequestParam(defaultValue = "C00010928") String accountNumber) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head><title>ACORD 125 - Commercial Insurance Application</title></head>
            <body style="font-family:sans-serif; padding:40px; background:#F8FAFC; color:#0F172A;">
                <div style="max-width:850px; margin:0 auto; background:#FFFFFF; border:2px solid #0F172A; padding:30px;">
                    <div style="display:flex; justify-content:space-between; border-bottom:3px solid #0F172A; padding-bottom:12px;">
                        <div>
                            <h2 style="margin:0;">ACORD™ 125</h2>
                            <span style="font-size:12px; color:#475569;">COMMERCIAL INSURANCE APPLICATION</span>
                        </div>
                        <div style="text-align:right; font-size:12px;">
                            DATE: <strong>2026-07-23</strong><br>
                            ACCOUNT #: <strong>{{ACCOUNT_NUMBER}}</strong>
                        </div>
                    </div>

                    <div style="margin-top:20px; display:grid; grid-template-columns: 1fr 1fr; gap:20px; font-size:13px;">
                        <div style="border:1px solid #CBD5E1; padding:12px; border-radius:4px;">
                            <strong style="color:#2563EB;">PRODUCER:</strong><br>
                            Apex Global Insurance Agency<br>
                            100 Financial Plaza, Suite 800<br>
                            Chicago, IL 60606<br>
                            Phone: (555) 019-2831
                        </div>
                        <div style="border:1px solid #CBD5E1; padding:12px; border-radius:4px;">
                            <strong style="color:#2563EB;">NAMED INSURED:</strong><br>
                            Acme Logistics & Distribution Inc<br>
                            100 Industrial Parkway<br>
                            Chicago, IL 60601<br>
                            FEIN: 12-3456789
                        </div>
                    </div>

                    <div style="margin-top:24px;">
                        <h4 style="margin-bottom:8px; border-bottom:1px solid #CBD5E1; padding-bottom:4px;">LINES OF BUSINESS REQUESTED</h4>
                        <table style="width:100%; border-collapse:collapse; font-size:12px;" border="1" cellpadding="6">
                            <tr style="background:#F1F5F9;">
                                <th>LINE OF BUSINESS</th>
                                <th>INDICATED LIMIT</th>
                                <th>DEDUCTIBLE</th>
                                <th>STATUS</th>
                            </tr>
                            <tr>
                                <td>Commercial Property</td>
                                <td>$1,000,000 Building / $250,000 BPP</td>
                                <td>$2,500</td>
                                <td>BOUND</td>
                            </tr>
                            <tr>
                                <td>Commercial Auto Fleet</td>
                                <td>$1,000,000 Combined Single Limit</td>
                                <td>$1,000</td>
                                <td>BOUND</td>
                            </tr>
                            <tr>
                                <td>Workers' Compensation</td>
                                <td>$500,000 / $500,000 / $500,000</td>
                                <td>$0</td>
                                <td>QUOTED</td>
                            </tr>
                        </table>
                    </div>

                    <div style="margin-top:40px; border-top:2px solid #0F172A; padding-top:12px; font-size:10px; color:#64748B; text-align:center;">
                        ACORD 125 (2016/03) © ACORD CORPORATION 1993-2016. ALL RIGHTS RESERVED. GUIDEWIRE POLICYCENTER™ AUTHORIZED SYSTEM OF RECORD.
                    </div>
                </div>
            </body>
            </html>
            """.replace("{{ACCOUNT_NUMBER}}", accountNumber);
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
    }

    @GetMapping("/acord140")
    public ResponseEntity<String> getACORD140Document(@RequestParam(defaultValue = "C00010928") String accountNumber) {
        String html = """
            <!DOCTYPE html>
            <html>
            <head><title>ACORD 140 - Property Section Schedule</title></head>
            <body style="font-family:sans-serif; padding:40px; background:#F8FAFC; color:#0F172A;">
                <div style="max-width:850px; margin:0 auto; background:#FFFFFF; border:2px solid #0F172A; padding:30px;">
                    <div style="display:flex; justify-content:space-between; border-bottom:3px solid #0F172A; padding-bottom:12px;">
                        <div>
                            <h2 style="margin:0;">ACORD™ 140</h2>
                            <span style="font-size:12px; color:#475569;">PROPERTY SECTION SCHEDULE & PREMISES INFORMATION</span>
                        </div>
                        <div style="text-align:right; font-size:12px;">
                            DATE: <strong>2026-07-23</strong><br>
                            ACCOUNT #: <strong>{{ACCOUNT_NUMBER}}</strong>
                        </div>
                    </div>

                    <div style="margin-top:24px;">
                        <table style="width:100%; border-collapse:collapse; font-size:12px;" border="1" cellpadding="8">
                            <tr style="background:#F1F5F9;">
                                <th>PREM # / BLDG #</th>
                                <th>STREET ADDRESS</th>
                                <th>CONSTRUCTION TYPE</th>
                                <th>YEAR BUILT</th>
                                <th>BUILDING LIMIT</th>
                                <th>SPRINKLERED</th>
                            </tr>
                            <tr>
                                <td>1 / 1</td>
                                <td>100 Industrial Parkway, Chicago, IL</td>
                                <td>Joisted Masonry</td>
                                <td>2005</td>
                                <td>$1,000,000</td>
                                <td>YES (Central Station)</td>
                            </tr>
                            <tr>
                                <td>1 / 2</td>
                                <td>102 Industrial Parkway, Chicago, IL</td>
                                <td>Frame Construction</td>
                                <td>2012</td>
                                <td>$450,000</td>
                                <td>YES (Local Alarm)</td>
                            </tr>
                        </table>
                    </div>

                    <div style="margin-top:40px; border-top:2px solid #0F172A; padding-top:12px; font-size:10px; color:#64748B; text-align:center;">
                        ACORD 140 (2016/03) © ACORD CORPORATION 1993-2016. GUIDEWIRE POLICYCENTER™ OFFICIAL PROPERTY SCHEDULE.
                    </div>
                </div>
            </body>
            </html>
            """.replace("{{ACCOUNT_NUMBER}}", accountNumber);
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
    }
}
