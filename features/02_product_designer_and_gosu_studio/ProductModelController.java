package com.policycenter.controller;

import com.policycenter.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/product-model")
@CrossOrigin(origins = "*")
public class ProductModelController {

    @GetMapping("/rate-books")
    public List<RateBook> getRateBooks() {
        List<RateBook> list = new ArrayList<>();
        list.add(new RateBook("rb-2026-v1", "CP-2026", "Commercial Property Ratebook 2026", "v1.0", "Active", "2026-01-01", "CommercialProperty"));
        return list;
    }

    @GetMapping("/rate-routines")
    public List<RateRoutine> getRateRoutines() {
        List<RateRoutine> list = new ArrayList<>();
        list.add(new RateRoutine("rr-cp-base", "CP_BASE_RATE_ROUTINE", "CP Base Premium Formula", "CommercialProperty", "Base Rate x Construction Factor x Protection Class Factor", "BaseRate * ConstructionMod * ProtectionClassMod"));
        return list;
    }
}
