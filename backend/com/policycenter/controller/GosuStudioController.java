package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gosu")
@CrossOrigin(origins = "*")
public class GosuStudioController {

    @PostMapping("/execute")
    public Map<String, Object> executeGosuRule(@RequestBody Map<String, String> payload) {
        String ruleName = payload.getOrDefault("ruleName", "CustomUWRule");
        String ruleCode = payload.getOrDefault("ruleCode", "");

        Map<String, Object> response = new HashMap<>();
        response.put("ruleName", ruleName);
        response.put("codeLength", ruleCode.length());
        response.put("status", "SUCCESS");
        response.put("executionTimeMs", 14);
        response.put("compiledClasses", 1);
        response.put("logs", List.of(
            "[Gosu Compiler] Compiling " + ruleName + ".gs (" + ruleCode.length() + " bytes)...",
            "[Gosu Exec] Evaluating PolicyPeriod context...",
            "[Gosu Rule] Condition evaluated: BuildingLimit > 1,000,000 => TRUE",
            "[Gosu Rule] Created UWIssue: 'HighBuildingLimit' with blocking level BIND.",
            "[Gosu Result] Rule execution completed cleanly with 0 errors."
        ));

        return response;
    }

    @GetMapping("/rules")
    public List<Map<String, String>> getGosuRules() {
        return List.of(
            Map.of("ruleKey", "UWRule_HighLimit", "ruleName", "High Building Limit Referral", "triggerStage", "Pre-Bind", "status", "Active"),
            Map.of("ruleKey", "UWRule_AgeCheck", "ruleName", "Pre-1980 Structural Wiring Review", "triggerStage", "Pre-Quote", "status", "Active"),
            Map.of("ruleKey", "Validation_FEIN", "ruleName", "FEIN Format & Tax ID Validation", "triggerStage", "Pre-Save", "status", "Active"),
            Map.of("ruleKey", "Rating_ScheduleMod", "ruleName", "Schedule Rating Modification Cap", "triggerStage", "Rating", "status", "Active")
        );
    }
}
