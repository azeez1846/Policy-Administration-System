package com.policycenter.repository;

import com.policycenter.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    Optional<Job> findByJobNumber(String jobNumber);
}
