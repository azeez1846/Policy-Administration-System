package com.policycenter.controller;

import com.policycenter.model.PolicyPeriod;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    @GetMapping
    public List<PolicyPeriod> getAllPolicies() {
        return repository.getAllPolicies();
    }
}
