package com.policycenter.controller;

import com.policycenter.model.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/lob")
@CrossOrigin(origins = "*")
public class LOBController {

    @GetMapping("/wc-class-codes")
    public List<WCClassCode> getWCClassCodes() {
        List<WCClassCode> list = new ArrayList<>();
        list.add(new WCClassCode("wc-8810", "8810", "IL", "Clerical Office Employees NOC", 0.35));
        list.add(new WCClassCode("wc-5606", "5606", "IL", "Contractor - Executive Supervisor", 2.15));
        return list;
    }

    @GetMapping("/gl-class-codes")
    public List<GLClassCode> getGLClassCodes() {
        List<GLClassCode> list = new ArrayList<>();
        list.add(new GLClassCode("gl-61212", "61212", "Premises/Operations", "Warehouses - Private", "Area (Sq Ft)"));
        return list;
    }
}
