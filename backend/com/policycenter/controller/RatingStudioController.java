package com.policycenter.controller;

import com.policycenter.gs.classes.rating.RatingStudioEngine;
import com.policycenter.model.RateTableFactor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rating")
@CrossOrigin(origins = "*")
public class RatingStudioController {

    @GetMapping("/factors")
    public List<RateTableFactor> getAllFactors() {
        return RatingStudioEngine.getAllFactors();
    }

    @PutMapping("/factors")
    public RateTableFactor updateFactor(@RequestBody Map<String, Object> payload) {
        String factorID = (String) payload.get("factorID");
        double factorValue = Double.parseDouble(payload.get("factorValue").toString());
        return RatingStudioEngine.updateFactor(factorID, factorValue);
    }
}
