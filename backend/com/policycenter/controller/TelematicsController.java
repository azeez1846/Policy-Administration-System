package com.policycenter.controller;

import com.policycenter.model.TelematicsReading;
import com.policycenter.service.TelematicsStreamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/telematics")
@CrossOrigin(origins = "*")
public class TelematicsController {

    private final TelematicsStreamService streamService = TelematicsStreamService.getInstance();

    @GetMapping("/fleet")
    public Map<String, Object> getFleetAnalytics() {
        return streamService.getFleetAnalyticsSummary();
    }

    @GetMapping("/readings")
    public List<TelematicsReading> getLatestReadings() {
        return streamService.getAllRecentReadings();
    }

    @PostMapping("/stream")
    public TelematicsReading ingestPing(@RequestBody TelematicsReading reading) {
        return streamService.ingestReading(reading);
    }
}
