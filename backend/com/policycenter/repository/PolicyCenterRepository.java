package com.policycenter.repository;

import com.policycenter.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory Datastore representing Guidewire PolicyCenter DB tables.
 */
public class PolicyCenterRepository {

    private static final PolicyCenterRepository INSTANCE = new PolicyCenterRepository();

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<String, Job> jobs = new ConcurrentHashMap<>();
    private final Map<String, PolicyPeriod> policies = new ConcurrentHashMap<>();

    private PolicyCenterRepository() {
        seedSampleData();
    }

    public static PolicyCenterRepository getInstance() {
        return INSTANCE;
    }

    private void seedSampleData() {
        // Create Sample Contact & Account
        Contact contact = new Contact("cont-101", "Company", "Acme Logistics Inc", "info@acmelogistics.com", "555-0192");
        contact.setFirstName("Acme");
        contact.setLastName("Logistics");
        contact.setCompanyName("Acme Logistics Inc");
        contact.setTaxID("12-3456789");
        contact.setAddressLine1("100 Industrial Parkway");
        contact.setCity("Chicago");
        contact.setState("IL");
        contact.setPostalCode("60601");

        Account account = new Account("acc-1001", "C00010928", contact, "Freight & Warehousing");
        accounts.put(account.getAccountNumber(), account);

        // Create Seed Policy Period & Job
        PolicyPeriod period = new PolicyPeriod();
        period.setPublicID("period-1001");
        period.setPeriodID("1001");
        period.setProductCode("CommercialProperty");
        period.setProductName("Commercial Property");
        period.setAccount(account);
        period.setPrimaryNamedInsured(contact);

        PolicyLine propLine = new PolicyLine("line-101", "CommercialPropertyLine", "Commercial Property Line");
        Building bldg = new Building("bldg-1", 1, "Main Warehouse & Logistics Hub", "Joisted Masonry", 2012, 1500000.0, 500000.0);
        propLine.addBuilding(bldg);
        period.addLine(propLine);

        Job submission = new Job("job-5001", "SUB-5001", "Submission", period);
        jobs.put(submission.getJobNumber(), submission);
    }

    public Collection<Account> getAllAccounts() { return accounts.values(); }
    public Account getAccount(String accountNumber) { return accounts.get(accountNumber); }
    public void saveAccount(Account account) { accounts.put(account.getAccountNumber(), account); }

    public Collection<Job> getAllJobs() { return jobs.values(); }
    public Job getJob(String jobNumber) { return jobs.get(jobNumber); }
    public void saveJob(Job job) { jobs.put(job.getJobNumber(), job); }

    public Collection<PolicyPeriod> getAllPolicies() { return policies.values(); }
    public PolicyPeriod getPolicyByNumber(String policyNumber) { return policies.get(policyNumber); }
    public void savePolicy(PolicyPeriod period) {
        if (period.getPolicyNumber() != null) {
            policies.put(period.getPolicyNumber(), period);
        }
    }
}
