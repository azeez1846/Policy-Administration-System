package com.policycenter.repository;

import com.policycenter.model.PolicyPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyPeriodRepository extends JpaRepository<PolicyPeriod, String> {
    Optional<PolicyPeriod> findByPolicyNumber(String policyNumber);
    List<PolicyPeriod> findByStatus(String status);
}
