package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-assist")
@CrossOrigin(origins = "*")
public class AIAssistController {

    @PostMapping("/chat")
    public Map<String, Object> processAIChat(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "").toLowerCase();
        Map<String, Object> response = new HashMap<>();

        String reply;
        if (message.contains("account") || message.contains("titan") || message.contains("acme")) {
            reply = "Account **C00010928** (Acme Logistics Inc) is currently **Active** with 2 bound policies: Commercial Property (CP-3451127) and Commercial Auto Fleet (POL-3764124). Total In-Force Premium: $22,400.00.";
        } else if (message.contains("submission") || message.contains("sub") || message.contains("quote")) {
            reply = "Submission **SUB-5001** for Commercial Property is in **Draft** status. It has an AI Risk Score of **42.5** (Standard UW Review). Building #1 limit is $1,000,000.00.";
        } else if (message.contains("rule") || message.contains("gosu") || message.contains("referral")) {
            reply = "PolicyCenter enforces 3 active Gosu UW referral rules:\n1. Building limit > $1.0M requires Senior UW referral (Blocks Bind).\n2. Buildings constructed prior to 1980 require structural wiring review (Blocks Quote).\n3. Unsprinklered commercial premises trigger a 10% underwriting surcharge.";
        } else if (message.contains("rate") || message.contains("premium") || message.contains("irpm")) {
            reply = "Rating Engine is utilizing Rate Book **RB-2026-IL** (Commercial Property v4.2). Current base rate: $0.24 per $100 TIV. Applied IRPM Schedule Credit: -15.0% for Superior Maintenance.";
        } else {
            reply = "I am **Guidewire Assist AI**. How can I assist you with Account C00010928, Submission SUB-5001, Gosu Rules, or Rating Worksheets today?";
        }

        response.put("reply", reply);
        response.put("timestamp", java.time.LocalTime.now().toString().substring(0, 5));
        return response;
    }
}
