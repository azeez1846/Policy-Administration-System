package com.policycenter.service;

import com.policycenter.model.CatastropheMoratorium;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CatastropheMoratoriumService {

    private static final CatastropheMoratoriumService INSTANCE = new CatastropheMoratoriumService();
    private final Map<String, CatastropheMoratorium> moratoriumMap = new ConcurrentHashMap<>();

    public CatastropheMoratoriumService() {
        initDefaultMoratoriums();
    }

    public static CatastropheMoratoriumService getInstance() {
        return INSTANCE;
    }

    private void initDefaultMoratoriums() {
        if (moratoriumMap.isEmpty()) {
            CatastropheMoratorium m1 = new CatastropheMoratorium(
                "moratorium-101",
                "Hurricane Milton FL South Coast Moratorium",
                "Hurricane",
                25.7617, -80.1918,
                75.0,
                LocalDate.now().minusDays(2).toString(),
                LocalDate.now().plusDays(10).toString(),
                "ACTIVE",
                "su",
                true, true
            );

            CatastropheMoratorium m2 = new CatastropheMoratorium(
                "moratorium-102",
                "Southern California Wildfire Moratorium",
                "Wildfire",
                34.0522, -118.2437,
                50.0,
                LocalDate.now().minusDays(5).toString(),
                LocalDate.now().plusDays(15).toString(),
                "ACTIVE",
                "underwriter",
                false, true
            );

            moratoriumMap.put(m1.getId(), m1);
            moratoriumMap.put(m2.getId(), m2);
        }
    }

    public List<CatastropheMoratorium> getAllMoratoriums() {
        return new ArrayList<>(moratoriumMap.values());
    }

    public List<CatastropheMoratorium> getActiveMoratoriums() {
        List<CatastropheMoratorium> active = new ArrayList<>();
        for (CatastropheMoratorium m : moratoriumMap.values()) {
            if ("ACTIVE".equalsIgnoreCase(m.getStatus())) {
                active.add(m);
            }
        }
        return active;
    }

    public CatastropheMoratorium addMoratorium(CatastropheMoratorium m) {
        if (m.getId() == null || m.getId().trim().isEmpty()) {
            m.setId("moratorium-" + System.currentTimeMillis());
        }
        if (m.getStatus() == null || m.getStatus().trim().isEmpty()) {
            m.setStatus("ACTIVE");
        }
        if (m.getEffectiveDate() == null) {
            m.getEffectiveDate();
            m.setEffectiveDate(LocalDate.now().toString());
        }
        moratoriumMap.put(m.getId(), m);
        return m;
    }

    public boolean liftMoratorium(String id) {
        CatastropheMoratorium m = moratoriumMap.get(id);
        if (m != null) {
            m.setStatus("LIFTED");
            return true;
        }
        return false;
    }

    /**
     * Calculates Haversine distance in miles between two lat/lng points.
     */
    public static double calculateDistanceMiles(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3958.8 * c; // Radius of Earth in miles
    }

    public List<CatastropheMoratorium> findViolatingMoratoriums(double lat, double lon) {
        List<CatastropheMoratorium> violations = new ArrayList<>();
        for (CatastropheMoratorium m : getActiveMoratoriums()) {
            double dist = calculateDistanceMiles(lat, lon, m.getLat(), m.getLng());
            if (dist <= m.getRadiusMiles()) {
                violations.add(m);
            }
        }
        return violations;
    }
}
