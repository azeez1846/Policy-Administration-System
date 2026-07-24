package com.policycenter.controller;

import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST API Controller: Entity Explorer & Catalog Browser
 *
 * Provides GET endpoints to browse and query all 59 OOTB PolicyCenter entity
 * tables. Returns row counts and catalog schema details.
 */
@RestController
@RequestMapping("/api/entities")
@CrossOrigin(origins = "*")
public class EntityExplorerController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    /**
     * GET /api/entities
     * Returns entity counts map for all 59 OOTB tables.
     */
    @GetMapping
    public Map<String, Integer> getEntityCounts() {
        return repository.getEntityCountsMap();
    }

    /**
     * GET /api/entities/catalog
     * Returns a structured catalog of all 59 OOTB entity types grouped by domain.
     */
    @GetMapping("/catalog")
    public List<Map<String, Object>> getEntityCatalog() {
        List<Map<String, Object>> catalog = new ArrayList<>();

        catalog.add(group("Core Account & Contact", List.of(
            entity("Contact", "contacts", "Account holders, insured persons, and third-party contacts"),
            entity("Account", "accounts", "Insurance accounts with industry code and status"),
            entity("AccountLocation", "account_locations", "Physical locations associated with accounts"),
            entity("AccountContact", "account_contacts", "Contact-to-account relationship with roles")
        )));

        catalog.add(group("Policy Lifecycle", List.of(
            entity("PolicyTerm", "policy_terms", "Policy term periods with effective/expiration dates"),
            entity("PolicyPeriod", "policy_periods", "The core policy transaction unit — quotes, policies, renewals"),
            entity("Job", "jobs", "Submissions, renewals, cancellations, endorsements, reinstatements"),
            entity("PolicyLine", "policy_lines", "Lines of business on a policy (CP, CA, WC, GL)"),
            entity("PolicyVersion", "policy_versions", "Versioned snapshots of policy period changes")
        )));

        catalog.add(group("Risk & Coverable Objects", List.of(
            entity("PolicyLocation", "policy_locations", "Insured premises with fire protection and territory"),
            entity("Building", "buildings", "Commercial buildings with construction, sprinkler, and alarm details"),
            entity("PolicyVehicle", "policy_vehicles", "Insured vehicles with VIN, make, model, and garage location"),
            entity("PolicyDriver", "policy_drivers", "Insured drivers with license and violation history")
        )));

        catalog.add(group("Coverage, Exclusions & Conditions", List.of(
            entity("Coverage", "coverages", "Coverage selections with limits, deductibles, and term amounts"),
            entity("Exclusion", "exclusions", "Policy exclusion endorsements"),
            entity("PolicyCondition", "policy_conditions", "Policy conditions and subjectivities"),
            entity("PolicyAddlInsured", "policy_addl_insureds", "Additional insured parties with interest types"),
            entity("CoveragePattern", "coverage_patterns", "Master catalog of available coverage types")
        )));

        catalog.add(group("Rating & Financial", List.of(
            entity("Cost", "costs", "Rated premium cost components"),
            entity("PolicyTransaction", "policy_transactions", "Written/charged transaction amounts"),
            entity("Transaction", "transactions", "Financial transaction postings"),
            entity("Modifier", "modifiers", "Schedule rating modifiers and experience mods"),
            entity("RateBook", "rate_books", "Published rate book editions by line"),
            entity("RateRoutine", "rate_routines", "Rating algorithm routines and formulas"),
            entity("RateTableFactor", "rate_table_factors", "Factor lookup tables for rating calculations"),
            entity("PolicyCommission", "policy_commissions", "Producer commission rates and amounts"),
            entity("TaxSurcharge", "tax_surcharges", "State premium taxes and regulatory surcharges"),
            entity("PaymentPlan", "payment_plans", "Billing payment plan configurations")
        )));

        catalog.add(group("Underwriting", List.of(
            entity("UWIssue", "uw_issues", "Underwriting issues that block or inform decisions"),
            entity("UWAuthorityProfile", "uw_authority_profiles", "UW authority limits by profile"),
            entity("UWAuthorityGrant", "uw_authority_grants", "Specific authority grants for issue types"),
            entity("UWCompany", "uw_companies", "Underwriting company entities"),
            entity("Contingency", "contingencies", "Policy contingencies requiring action before binding")
        )));

        catalog.add(group("Activity & Workflow", List.of(
            entity("Activity", "activities", "UW tasks, approval items, and system-generated activities"),
            entity("ActivityPattern", "activity_patterns", "Templates for auto-generating activities"),
            entity("Note", "notes", "Internal underwriting notes and communication"),
            entity("History", "history", "Audit trail of policy events and user actions"),
            entity("Document", "documents", "Attached documents (ACORD forms, inspections, photos)")
        )));

        catalog.add(group("Organization & Security", List.of(
            entity("User", "users", "System users with roles and authority profiles"),
            entity("Role", "roles", "Security roles controlling permissions"),
            entity("Group", "groups", "Organizational groups and team structures"),
            entity("Organization", "organizations", "Carrier, agency, and MGA organizations"),
            entity("ProducerCode", "producer_codes", "Producer/agent codes with commission rates"),
            entity("ProducerCodeAssignment", "producer_code_assignments", "User-to-ProducerCode assignments"),
            entity("Region", "regions", "Geographic UW regions and territory zones")
        )));

        catalog.add(group("Product & Jurisdiction", List.of(
            entity("ProductModel", "product_models", "Insurance product configurations (CP, CA, WC, GL)"),
            entity("Jurisdiction", "jurisdictions", "State regulatory rules and filing requirements"),
            entity("FormPattern", "form_patterns", "Policy form templates and inference rules"),
            entity("PolicyForm", "policy_forms", "Attached forms on a policy period")
        )));

        catalog.add(group("Workers' Comp & General Liability", List.of(
            entity("WCClassCode", "wc_class_codes", "Workers' Comp class codes with base rates"),
            entity("WCEmployee", "wc_employees", "WC employee classifications and payroll"),
            entity("GLClassCode", "gl_class_codes", "General Liability classification codes"),
            entity("GLExposure", "gl_exposures", "GL exposure units by location and class")
        )));

        catalog.add(group("Reinsurance", List.of(
            entity("RIRisk", "ri_risks", "Reinsurance risk records with TIV and PML"),
            entity("RIProgram", "ri_programs", "Reinsurance treaty programs"),
            entity("RIAttachment", "ri_attachments", "RI cession attachments to risks")
        )));

        catalog.add(group("Claims & Audit", List.of(
            entity("ClaimDetail", "claim_details", "Extended claim records with adjuster and subrogation"),
            entity("AuditInformation", "audit_informations", "Legacy audit tracking records"),
            entity("AuditSchedule", "audit_schedules", "Premium audit scheduling and completion"),
            entity("PolicyHold", "policy_holds", "Holds preventing policy transactions")
        )));

        return catalog;
    }

    private Map<String, Object> group(String groupName, List<Map<String, String>> entities) {
        Map<String, Object> g = new LinkedHashMap<>();
        g.put("group", groupName);
        g.put("entityCount", entities.size());
        g.put("entities", entities);
        return g;
    }

    private Map<String, String> entity(String name, String table, String description) {
        return Map.of("entityName", name, "tableName", table, "description", description);
    }
}
