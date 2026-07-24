package com.policycenter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*")
public class ActivityController {

    private static final List<Map<String, Object>> ACTIVITIES = new ArrayList<>();
    private static final List<Map<String, Object>> NOTES = new ArrayList<>();

    static {
        ACTIVITIES.add(Map.of("id", "act-101", "subject", "Verify Prior 3-Year Loss Runs", "priority", "High", "assignedTo", "su", "dueDate", "2026-07-25", "status", "Open", "targetNumber", "SUB-5001"));
        ACTIVITIES.add(Map.of("id", "act-102", "subject", "Order Commercial Building Engineering Inspection", "priority", "Urgent", "assignedTo", "underwriter", "dueDate", "2026-07-24", "status", "Open", "targetNumber", "C00010928"));
        ACTIVITIES.add(Map.of("id", "act-103", "subject", "Review Schedule Rating Modification Surcharge", "priority", "Medium", "assignedTo", "su", "dueDate", "2026-07-28", "status", "Completed", "targetNumber", "SUB-5001"));

        NOTES.add(Map.of("id", "note-1", "author", "su", "date", "2026-07-23 10:15", "topic", "Underwriting Review", "body", "Reviewed high building limit ($1.0M). Structural wiring inspected and verified updated in 2018. Approved for binding."));
        NOTES.add(Map.of("id", "note-2", "author", "producer", "date", "2026-07-22 16:30", "topic", "Customer Communication", "body", "Insured confirmed installation of Central Station automatic sprinkler alarm."));
    }

    @GetMapping
    public List<Map<String, Object>> getActivities() {
        return ACTIVITIES;
    }

    @PostMapping
    public Map<String, Object> createActivity(@RequestBody Map<String, String> payload) {
        Map<String, Object> act = new HashMap<>();
        act.put("id", "act-" + System.currentTimeMillis());
        act.put("subject", payload.getOrDefault("subject", "New Activity"));
        act.put("priority", payload.getOrDefault("priority", "Medium"));
        act.put("assignedTo", payload.getOrDefault("assignedTo", "su"));
        act.put("dueDate", payload.getOrDefault("dueDate", java.time.LocalDate.now().plusDays(3).toString()));
        act.put("status", "Open");
        act.put("targetNumber", payload.getOrDefault("targetNumber", "C00010928"));
        ACTIVITIES.add(0, act);
        return act;
    }

    @GetMapping("/notes")
    public List<Map<String, Object>> getNotes() {
        return NOTES;
    }

    @PostMapping("/notes")
    public Map<String, Object> createNote(@RequestBody Map<String, String> payload) {
        Map<String, Object> note = new HashMap<>();
        note.put("id", "note-" + System.currentTimeMillis());
        note.put("author", payload.getOrDefault("author", "su"));
        note.put("date", java.time.LocalDateTime.now().toString().substring(0, 16).replace("T", " "));
        note.put("topic", payload.getOrDefault("topic", "Underwriting Note"));
        note.put("body", payload.getOrDefault("body", ""));
        NOTES.add(0, note);
        return note;
    }
}
