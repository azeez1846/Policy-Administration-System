package com.policycenter.repository;

import com.policycenter.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Persistent SQLite Relational Database Repository for Out-of-the-Box (OOTB) Guidewire PolicyCenter entities.
 */
public class PolicyCenterSqliteRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolicyCenterSqliteRepository.class);
    private static final String DB_URL = "jdbc:sqlite:" + getDatabaseFilePath() + "?journal_mode=WAL&synchronous=NORMAL&busy_timeout=5000";

    private static String getDatabaseFilePath() {
        return new java.io.File("policycenter.db").getAbsolutePath();
    }

    private static final PolicyCenterSqliteRepository INSTANCE = new PolicyCenterSqliteRepository();

    private PolicyCenterSqliteRepository() {
        initSchema();
    }

    public static PolicyCenterSqliteRepository getInstance() {
        return INSTANCE;
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Failed to load SQLite JDBC driver", e);
        }
        return DriverManager.getConnection(DB_URL);
    }

    private void initSchema() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            // 1. Contacts table
            stmt.execute("CREATE TABLE IF NOT EXISTS contacts (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "contact_type TEXT, " +
                    "name TEXT, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "company_name TEXT, " +
                    "tax_id TEXT, " +
                    "email TEXT, " +
                    "phone TEXT, " +
                    "work_phone TEXT, " +
                    "home_phone TEXT, " +
                    "fax_number TEXT, " +
                    "address_line1 TEXT, " +
                    "address_line2 TEXT, " +
                    "city TEXT, " +
                    "state TEXT, " +
                    "postal_code TEXT, " +
                    "country TEXT" +
                    ");");

            // 2. Account Locations table
            stmt.execute("CREATE TABLE IF NOT EXISTS account_locations (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "account_number TEXT, " +
                    "location_num INTEGER, " +
                    "location_name TEXT, " +
                    "address_line1 TEXT, " +
                    "address_line2 TEXT, " +
                    "city TEXT, " +
                    "state TEXT, " +
                    "postal_code TEXT, " +
                    "county TEXT, " +
                    "territory_code TEXT, " +
                    "geocode_status TEXT, " +
                    "FOREIGN KEY(account_number) REFERENCES accounts(account_number)" +
                    ");");

            // 3. Account Contacts table
            stmt.execute("CREATE TABLE IF NOT EXISTS account_contacts (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "account_id TEXT, " +
                    "contact_id TEXT, " +
                    "roles TEXT, " +
                    "FOREIGN KEY(account_id) REFERENCES accounts(account_number), " +
                    "FOREIGN KEY(contact_id) REFERENCES contacts(public_id)" +
                    ");");

            // 4. Accounts table
            stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_number TEXT PRIMARY KEY, " +
                    "public_id TEXT, " +
                    "account_status TEXT, " +
                    "industry_code TEXT, " +
                    "origination_date TEXT, " +
                    "preferred_coverage_currency TEXT, " +
                    "frozen INTEGER, " +
                    "account_holder_id TEXT, " +
                    "FOREIGN KEY(account_holder_id) REFERENCES contacts(public_id)" +
                    ");");

            // 5. Policy Terms table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_terms (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "term_number INTEGER, " +
                    "effective_date TEXT, " +
                    "expiration_date TEXT, " +
                    "term_status TEXT" +
                    ");");

            // 6. Policy Periods table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_periods (" +
                    "period_id TEXT PRIMARY KEY, " +
                    "public_id TEXT, " +
                    "status TEXT, " +
                    "product_code TEXT, " +
                    "product_name TEXT, " +
                    "quote_number TEXT, " +
                    "policy_number TEXT, " +
                    "effective_date TEXT, " +
                    "expiration_date TEXT, " +
                    "written_date TEXT, " +
                    "cancellation_date TEXT, " +
                    "currency TEXT, " +
                    "total_premium REAL, " +
                    "tax_and_fees REAL, " +
                    "total_cost REAL, " +
                    "account_number TEXT, " +
                    "insured_id TEXT" +
                    ");");

            // 7. Jobs table
            stmt.execute("CREATE TABLE IF NOT EXISTS jobs (" +
                    "job_number TEXT PRIMARY KEY, " +
                    "public_id TEXT, " +
                    "job_type TEXT, " +
                    "job_status TEXT, " +
                    "close_date TEXT, " +
                    "create_date TEXT, " +
                    "underwriter_id TEXT, " +
                    "producer_code TEXT, " +
                    "period_id TEXT, " +
                    "FOREIGN KEY(period_id) REFERENCES policy_periods(period_id)" +
                    ");");

            // 8. Policy Lines table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_lines (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "pattern_code TEXT, " +
                    "line_name TEXT" +
                    ");");

            // 9. Policy Locations table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_locations (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "location_num INTEGER, " +
                    "location_name TEXT, " +
                    "address_line1 TEXT, " +
                    "city TEXT, " +
                    "state TEXT, " +
                    "postal_code TEXT, " +
                    "building_count INTEGER, " +
                    "fire_protection_class TEXT, " +
                    "tax_location_code TEXT" +
                    ");");

            // 10. Buildings table
            stmt.execute("CREATE TABLE IF NOT EXISTS buildings (" +
                    "id TEXT PRIMARY KEY, " +
                    "fixed_id TEXT, " +
                    "branch_id TEXT, " +
                    "effective_date TEXT, " +
                    "expiration_date TEXT, " +
                    "change_type TEXT, " +
                    "public_id TEXT, " +
                    "period_id TEXT, " +
                    "building_num INTEGER, " +
                    "description TEXT, " +
                    "construction_type TEXT, " +
                    "year_built INTEGER, " +
                    "number_of_stories INTEGER, " +
                    "sprinklered INTEGER, " +
                    "alarm_type TEXT, " +
                    "fire_protection_class TEXT, " +
                    "building_limit REAL, " +
                    "contents_limit REAL" +
                    ");");

            // 11. Policy Vehicles table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_vehicles (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "vehicle_num INTEGER, " +
                    "vin TEXT, " +
                    "make TEXT, " +
                    "model TEXT, " +
                    "year INTEGER, " +
                    "use_type TEXT, " +
                    "cost_new REAL, " +
                    "garage_location_num INTEGER, " +
                    "license_state TEXT" +
                    ");");

            // 12. Policy Drivers table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_drivers (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "driver_num INTEGER, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "date_of_birth TEXT, " +
                    "license_number TEXT, " +
                    "license_state TEXT, " +
                    "number_of_violations INTEGER, " +
                    "good_driver_discount INTEGER" +
                    ");");

            // 13. Coverages table
            stmt.execute("CREATE TABLE IF NOT EXISTS coverages (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "target_id TEXT, " +
                    "pattern_code TEXT, " +
                    "name TEXT, " +
                    "limit_amount REAL, " +
                    "deductible_amount REAL, " +
                    "choice_value TEXT, " +
                    "direct_value REAL, " +
                    "currency TEXT, " +
                    "calculated_term_amount REAL" +
                    ");");

            // 14. Exclusions table
            stmt.execute("CREATE TABLE IF NOT EXISTS exclusions (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "target_id TEXT, " +
                    "pattern_code TEXT, " +
                    "name TEXT, " +
                    "description TEXT, " +
                    "exclusion_text TEXT" +
                    ");");

            // 15. Policy Conditions table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_conditions (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "target_id TEXT, " +
                    "pattern_code TEXT, " +
                    "name TEXT, " +
                    "condition_text TEXT" +
                    ");");

            // 16. Policy Additional Insureds table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_addl_insureds (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "contact_id TEXT, " +
                    "interest_type TEXT, " +
                    "certificate_required INTEGER" +
                    ");");

            // 17. Costs table
            stmt.execute("CREATE TABLE IF NOT EXISTS costs (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "cost_type TEXT, " +
                    "description TEXT, " +
                    "actual_amount REAL, " +
                    "actual_term_amount REAL, " +
                    "proration_factor REAL, " +
                    "charge_pattern TEXT, " +
                    "rate_amount REAL" +
                    ");");

            // 18. Policy Transactions table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_transactions (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "cost_id TEXT, " +
                    "posted_date TEXT, " +
                    "written_amount REAL, " +
                    "charged_amount REAL, " +
                    "eff_date TEXT, " +
                    "exp_date TEXT" +
                    ");");

            // 19. UW Issues table
            stmt.execute("CREATE TABLE IF NOT EXISTS uw_issues (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "issue_key TEXT, " +
                    "short_description TEXT, " +
                    "long_description TEXT, " +
                    "status TEXT, " +
                    "approval_blocking_level TEXT" +
                    ");");

            // 20. UW Authority Profiles table
            stmt.execute("CREATE TABLE IF NOT EXISTS uw_authority_profiles (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "profile_name TEXT, " +
                    "max_building_limit REAL, " +
                    "max_total_premium REAL" +
                    ");");

            // 21. Groups table
            stmt.execute("CREATE TABLE IF NOT EXISTS groups (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "group_name TEXT, " +
                    "group_type TEXT, " +
                    "supervisor_id TEXT" +
                    ");");

            // 22. Producer Codes table
            stmt.execute("CREATE TABLE IF NOT EXISTS producer_codes (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "code TEXT UNIQUE, " +
                    "description TEXT, " +
                    "status TEXT, " +
                    "commission_rate REAL" +
                    ");");

            // 23. Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "username TEXT UNIQUE, " +
                    "password TEXT, " +
                    "full_name TEXT, " +
                    "role TEXT, " +
                    "producer_code TEXT, " +
                    "email TEXT, " +
                    "department TEXT, " +
                    "authority_profile_id TEXT" +
                    ");");

            // 24. Policy Forms table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_forms (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "form_number TEXT, " +
                    "form_name TEXT, " +
                    "edition TEXT, " +
                    "inference_rule TEXT" +
                    ");");

            // 25. History table
            stmt.execute("CREATE TABLE IF NOT EXISTS history (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "event_timestamp TEXT, " +
                    "username TEXT, " +
                    "description TEXT, " +
                    "event_type TEXT" +
                    ");");

            // 26. Notes table
            stmt.execute("CREATE TABLE IF NOT EXISTS notes (" +
                    "public_id TEXT PRIMARY KEY, " +
                    "period_id TEXT, " +
                    "subject TEXT, " +
                    "body TEXT, " +
                    "author TEXT, " +
                    "topic TEXT, " +
                    "security_level TEXT" +
                    ");");

            // 27. Transactions
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "public_id TEXT PRIMARY KEY, policy_period_id TEXT, cost_id TEXT, amount REAL, charged INTEGER, written INTEGER, eff_date TEXT, exp_date TEXT, posted_date TEXT);");

            // 28. Payment Plans
            stmt.execute("CREATE TABLE IF NOT EXISTS payment_plans (" +
                    "public_id TEXT PRIMARY KEY, name TEXT, billing_method TEXT, down_payment_percent REAL, installment_fee REAL, number_of_installments INTEGER);");

            // 29. Modifiers
            stmt.execute("CREATE TABLE IF NOT EXISTS modifiers (" +
                    "public_id TEXT PRIMARY KEY, policy_period_id TEXT, pattern_code TEXT, rate_factor REAL, justification TEXT, minimum_factor REAL, maximum_factor REAL);");

            // 30. Audit Information
            stmt.execute("CREATE TABLE IF NOT EXISTS audit_informations (" +
                    "public_id TEXT PRIMARY KEY, policy_period_id TEXT, audit_type TEXT, audit_schedule_type TEXT, due_date TEXT, actual_audit_method TEXT, status TEXT);");

            // 31. UW Companies
            stmt.execute("CREATE TABLE IF NOT EXISTS uw_companies (" +
                    "public_id TEXT PRIMARY KEY, code TEXT, name TEXT, state TEXT, status TEXT);");

            // 32. UW Authority Grants
            stmt.execute("CREATE TABLE IF NOT EXISTS uw_authority_grants (" +
                    "public_id TEXT PRIMARY KEY, profile_id TEXT, issue_type TEXT, value_operand TEXT, reference_value TEXT, approved INTEGER);");

            // 33. Contingencies
            stmt.execute("CREATE TABLE IF NOT EXISTS contingencies (" +
                    "public_id TEXT PRIMARY KEY, policy_period_id TEXT, title TEXT, description TEXT, due_date TEXT, status TEXT, action TEXT);");

            // 34. Documents
            stmt.execute("CREATE TABLE IF NOT EXISTS documents (" +
                    "public_id TEXT PRIMARY KEY, account_number TEXT, job_id TEXT, name TEXT, doc_type TEXT, mime_type TEXT, status TEXT, url TEXT, date_created TEXT);");

            // 35. Form Patterns
            stmt.execute("CREATE TABLE IF NOT EXISTS form_patterns (" +
                    "public_id TEXT PRIMARY KEY, form_number TEXT, edition TEXT, description TEXT, inference_class TEXT, priority INTEGER);");

            // 36. WC Class Codes
            stmt.execute("CREATE TABLE IF NOT EXISTS wc_class_codes (" +
                    "public_id TEXT PRIMARY KEY, code TEXT, state TEXT, short_desc TEXT, base_rate REAL);");

            // 37. WC Employees
            stmt.execute("CREATE TABLE IF NOT EXISTS wc_employees (" +
                    "public_id TEXT PRIMARY KEY, policy_period_id TEXT, class_code TEXT, state TEXT, num_employees INTEGER, estimated_payroll REAL);");

            // 38. GL Class Codes
            stmt.execute("CREATE TABLE IF NOT EXISTS gl_class_codes (" +
                    "public_id TEXT PRIMARY KEY, code TEXT, sub_line TEXT, description TEXT, basis_type TEXT);");

            // 39. GL Exposures
            stmt.execute("CREATE TABLE IF NOT EXISTS gl_exposures (" +
                    "public_id TEXT PRIMARY KEY, policy_period_id TEXT, class_code TEXT, location_num INTEGER, exposure_amount REAL);");

            // 40. RI Risks
            stmt.execute("CREATE TABLE IF NOT EXISTS ri_risks (" +
                    "public_id TEXT PRIMARY KEY, policy_period_id TEXT, risk_number TEXT, total_insured_value REAL, probable_maximum_loss REAL, status TEXT);");

            // 41. RI Programs
            stmt.execute("CREATE TABLE IF NOT EXISTS ri_programs (" +
                    "public_id TEXT PRIMARY KEY, name TEXT, effective_date TEXT, expiration_date TEXT, status TEXT, single_risk_limit REAL);");

            // 42. RI Attachments
            stmt.execute("CREATE TABLE IF NOT EXISTS ri_attachments (" +
                    "public_id TEXT PRIMARY KEY, risk_id TEXT, treaty_id TEXT, attachment_type TEXT, ceded_share REAL);");

            // 43. Rate Books
            stmt.execute("CREATE TABLE IF NOT EXISTS rate_books (" +
                    "public_id TEXT PRIMARY KEY, book_code TEXT, book_name TEXT, book_edition TEXT, status TEXT, effective_date TEXT, policy_line TEXT);");

            // 44. Rate Routines
            stmt.execute("CREATE TABLE IF NOT EXISTS rate_routines (" +
                    "public_id TEXT PRIMARY KEY, routine_code TEXT, routine_name TEXT, policy_line TEXT, description TEXT, formula_expression TEXT);");

            // =====================================================
            // NEW OOTB ENTITIES (Tables 45–59)
            // =====================================================

            // 45. Activities table
            stmt.execute("CREATE TABLE IF NOT EXISTS activities (" +
                    "public_id TEXT PRIMARY KEY, subject TEXT, description TEXT, priority TEXT, status TEXT, " +
                    "activity_class TEXT, activity_pattern_id TEXT, assignee_id TEXT, target_id TEXT, target_type TEXT, " +
                    "due_date TEXT, completion_date TEXT, escalation_date TEXT, mandatory INTEGER, recurring INTEGER);");

            // 46. Activity Patterns table
            stmt.execute("CREATE TABLE IF NOT EXISTS activity_patterns (" +
                    "public_id TEXT PRIMARY KEY, code TEXT UNIQUE, subject TEXT, description TEXT, " +
                    "activity_class TEXT, priority TEXT, category TEXT, due_days_from_target INTEGER, " +
                    "escalation_days INTEGER, mandatory INTEGER, auto_assign INTEGER, recurring INTEGER, trigger_type TEXT);");

            // 47. Roles table
            stmt.execute("CREATE TABLE IF NOT EXISTS roles (" +
                    "public_id TEXT PRIMARY KEY, role_name TEXT UNIQUE, description TEXT, role_type TEXT, " +
                    "permissions TEXT, can_approve_uw_issues INTEGER, can_bind_policies INTEGER, " +
                    "can_cancel_policies INTEGER, can_view_financials INTEGER);");

            // 48. Organizations table
            stmt.execute("CREATE TABLE IF NOT EXISTS organizations (" +
                    "public_id TEXT PRIMARY KEY, name TEXT, org_type TEXT, parent_org_id TEXT, " +
                    "address TEXT, city TEXT, state TEXT, postal_code TEXT, phone TEXT, fein TEXT, " +
                    "status TEXT, license_number TEXT);");

            // 49. Producer Code Assignments table
            stmt.execute("CREATE TABLE IF NOT EXISTS producer_code_assignments (" +
                    "public_id TEXT PRIMARY KEY, user_id TEXT, producer_code_id TEXT, " +
                    "assignment_role TEXT, effective_date TEXT, expiration_date TEXT, active INTEGER, " +
                    "FOREIGN KEY(user_id) REFERENCES users(public_id), " +
                    "FOREIGN KEY(producer_code_id) REFERENCES producer_codes(public_id));");

            // 50. Regions table
            stmt.execute("CREATE TABLE IF NOT EXISTS regions (" +
                    "public_id TEXT PRIMARY KEY, region_code TEXT UNIQUE, region_name TEXT, region_type TEXT, " +
                    "states TEXT, uw_manager_id TEXT, catastrophe_exposed INTEGER, regulatory_zone TEXT);");

            // 51. Rate Table Factors table
            stmt.execute("CREATE TABLE IF NOT EXISTS rate_table_factors (" +
                    "public_id TEXT PRIMARY KEY, rate_book_id TEXT, table_name TEXT, " +
                    "factor_name TEXT, lookup_key TEXT, factor_value REAL, effective_date TEXT, " +
                    "FOREIGN KEY(rate_book_id) REFERENCES rate_books(public_id));");

            // 52. Policy Commissions table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_commissions (" +
                    "public_id TEXT PRIMARY KEY, period_id TEXT, cost_id TEXT, producer_code_id TEXT, " +
                    "commission_plan TEXT, commission_rate REAL, commission_amount REAL, " +
                    "role TEXT, payment_status TEXT, effective_date TEXT);");

            // 53. Tax Surcharges table
            stmt.execute("CREATE TABLE IF NOT EXISTS tax_surcharges (" +
                    "public_id TEXT PRIMARY KEY, period_id TEXT, tax_type TEXT, jurisdiction TEXT, " +
                    "tax_rate REAL, taxable_amount REAL, tax_amount REAL, description TEXT, " +
                    "overridden INTEGER, override_amount REAL);");

            // 54. Jurisdictions table
            stmt.execute("CREATE TABLE IF NOT EXISTS jurisdictions (" +
                    "public_id TEXT PRIMARY KEY, state_code TEXT UNIQUE, state_name TEXT, country TEXT, " +
                    "regulatory_body TEXT, file_and_use INTEGER, prior_approval INTEGER, " +
                    "default_premium_tax_rate REAL, nfip_participant INTEGER, residual_market TEXT, " +
                    "catastrophe_exposed INTEGER, timezone TEXT);");

            // 55. Product Models table
            stmt.execute("CREATE TABLE IF NOT EXISTS product_models (" +
                    "public_id TEXT PRIMARY KEY, product_code TEXT UNIQUE, product_name TEXT, " +
                    "product_abbrev TEXT, policy_line_pattern TEXT, available_jurisdictions TEXT, " +
                    "effective_date TEXT, expiration_date TEXT, status TEXT, " +
                    "renewal_enabled INTEGER, cancellation_enabled INTEGER, audit_enabled INTEGER, default_payment_plan TEXT);");

            // 56. Coverage Patterns table
            stmt.execute("CREATE TABLE IF NOT EXISTS coverage_patterns (" +
                    "public_id TEXT PRIMARY KEY, pattern_code TEXT UNIQUE, name TEXT, description TEXT, " +
                    "policy_line_pattern TEXT, coverable_type TEXT, default_limit REAL, default_deductible REAL, " +
                    "min_limit REAL, max_limit REAL, mandatory INTEGER, electable INTEGER, category TEXT);");

            // 57. Claim Details table
            stmt.execute("CREATE TABLE IF NOT EXISTS claim_details (" +
                    "public_id TEXT PRIMARY KEY, claim_number TEXT, policy_number TEXT, claim_status TEXT, " +
                    "loss_date TEXT, reported_date TEXT, closed_date TEXT, loss_cause TEXT, loss_type TEXT, " +
                    "adjuster_name TEXT, adjuster_phone TEXT, incurred_amount REAL, paid_amount REAL, " +
                    "reserve_amount REAL, subrogation INTEGER, subrogation_status TEXT, subrogation_recovery REAL, " +
                    "litigation INTEGER, litigation_status TEXT, fault_rating TEXT);");

            // 58. Audit Schedules table
            stmt.execute("CREATE TABLE IF NOT EXISTS audit_schedules (" +
                    "public_id TEXT PRIMARY KEY, period_id TEXT, audit_type TEXT, audit_method TEXT, " +
                    "scheduled_date TEXT, completion_date TEXT, status TEXT, auditor_name TEXT, " +
                    "auditor_company TEXT, estimated_premium REAL, audited_premium REAL, " +
                    "premium_adjustment REAL, notes TEXT);");

            // 59. Policy Holds table
            stmt.execute("CREATE TABLE IF NOT EXISTS policy_holds (" +
                    "public_id TEXT PRIMARY KEY, period_id TEXT, hold_type TEXT, reason TEXT, " +
                    "status TEXT, placed_date TEXT, released_date TEXT, placed_by_user_id TEXT, " +
                    "released_by_user_id TEXT, blocks_renewal INTEGER, blocks_endorsement INTEGER, " +
                    "blocks_cancellation INTEGER, blocks_reinstatement INTEGER);");

            // 60. Hazard Intelligence table (Marketplace Accelerator)
            stmt.execute("CREATE TABLE IF NOT EXISTS hazard_intelligence (" +
                    "id TEXT PRIMARY KEY, location_id TEXT, building_id TEXT, address_line TEXT, " +
                    "wildfire_score INTEGER, flood_zone TEXT, distance_to_coast_miles REAL, " +
                    "roof_condition_score REAL, hail_severity_index TEXT, risk_category TEXT, evaluated_at TEXT);");

            // 61. E-Signature Envelopes table (Marketplace Accelerator)
            stmt.execute("CREATE TABLE IF NOT EXISTS esignature_envelopes (" +
                    "id TEXT PRIMARY KEY, envelope_id TEXT UNIQUE, job_number TEXT, policy_number TEXT, " +
                    "signer_name TEXT, signer_email TEXT, document_type TEXT, status TEXT, " +
                    "sent_at TEXT, signed_at TEXT, document_id TEXT, download_url TEXT);");

            // Seed users if empty
            ResultSet userRs = stmt.executeQuery("SELECT COUNT(*) FROM users;");
            if (userRs.next() && userRs.getInt(1) == 0) {
                seedUsersInternal(conn);
            }

            // Seed initial data if accounts table is empty
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM accounts;");
            if (rs.next() && rs.getInt(1) == 0) {
                seedInitialData(conn);
            }

            // Seed extended entities if payment_plans empty
            ResultSet planRs = stmt.executeQuery("SELECT COUNT(*) FROM payment_plans;");
            if (planRs.next() && planRs.getInt(1) == 0) {
                seedExtendedOotbData(conn);
            }

            // Seed new OOTB entities (tables 45-59) if roles table is empty
            ResultSet rolesRs = stmt.executeQuery("SELECT COUNT(*) FROM roles;");
            if (rolesRs.next() && rolesRs.getInt(1) == 0) {
                seedNewOotbEntities(conn);
            }

            System.out.println("[SQLite DB] All 59 OOTB Guidewire PolicyCenter Sandbox Tables Initialized.");

        } catch (SQLException e) {
            LOGGER.error("Error initializing SQLite database schema", e);
        }
    }

    private void seedUsersInternal(Connection conn) throws SQLException {
        String sql = "INSERT INTO users (public_id, username, password, full_name, role, producer_code, email, department, authority_profile_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Underwriter
            pstmt.setString(1, "user-uw1");
            pstmt.setString(2, "su");
            pstmt.setString(3, "gw");
            pstmt.setString(4, "Super User (Senior UW)");
            pstmt.setString(5, "Underwriter");
            pstmt.setString(6, "PROD-1001");
            pstmt.setString(7, "su@guidewire.com");
            pstmt.setString(8, "Commercial Property LOB");
            pstmt.setString(9, "uw-profile-senior");
            pstmt.executeUpdate();

            // Producer
            pstmt.setString(1, "user-prod1");
            pstmt.setString(2, "producer");
            pstmt.setString(3, "gw");
            pstmt.setString(4, "John Agent (Agency Producer)");
            pstmt.setString(5, "Producer");
            pstmt.setString(6, "PROD-1001");
            pstmt.setString(7, "john.agent@brokerage.com");
            pstmt.setString(8, "Agency Distribution");
            pstmt.setString(9, "uw-profile-producer");
            pstmt.executeUpdate();
        }

        // Seed UW Authority Profiles
        String uwSql = "INSERT OR REPLACE INTO uw_authority_profiles (public_id, profile_name, max_building_limit, max_total_premium) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(uwSql)) {
            pstmt.setString(1, "uw-profile-senior");
            pstmt.setString(2, "Senior Commercial Underwriter");
            pstmt.setDouble(3, 10000000.0);
            pstmt.setDouble(4, 500000.0);
            pstmt.executeUpdate();
        }

        // Seed Producer Codes
        String pcSql = "INSERT OR REPLACE INTO producer_codes (public_id, code, description, status, commission_rate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(pcSql)) {
            pstmt.setString(1, "pc-1001");
            pstmt.setString(2, "PROD-1001");
            pstmt.setString(3, "Apex Global Insurance Brokers");
            pstmt.setString(4, "Active");
            pstmt.setDouble(5, 0.15);
            pstmt.executeUpdate();
        }

        // Seed Groups
        String grpSql = "INSERT OR REPLACE INTO groups (public_id, group_name, group_type, supervisor_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(grpSql)) {
            pstmt.setString(1, "grp-1");
            pstmt.setString(2, "Chicago Commercial Branch");
            pstmt.setString(3, "Branch");
            pstmt.setString(4, "user-uw1");
            pstmt.executeUpdate();
        }
    }

    private void seedInitialData(Connection conn) throws SQLException {
        Contact contact = new Contact("cont-101", "Company", "Acme Logistics Inc", "info@acmelogistics.com", "555-0192");
        contact.setCompanyName("Acme Logistics Inc");
        contact.setTaxID("12-3456789");
        contact.setAddressLine1("100 Industrial Parkway");
        contact.setCity("Chicago");
        contact.setState("IL");
        contact.setPostalCode("60601");
        saveContactInternal(conn, contact);

        Account account = new Account("acc-1001", "C00010928", contact, "Freight & Warehousing");
        saveAccountInternal(conn, account);

        // Account Location
        AccountLocation accLoc = new AccountLocation("accloc-1", 1, "Logistics Hub Alpha", "100 Industrial Parkway", "Chicago", "IL", "60601");
        saveAccountLocationInternal(conn, "C00010928", accLoc);

        PolicyPeriod period = new PolicyPeriod();
        period.setPublicID("period-1001");
        period.setPeriodID("1001");
        period.setProductCode("CommercialProperty");
        period.setProductName("Commercial Property");
        period.setQuoteNumber("Q-9018273");
        period.setAccount(account);
        period.setPrimaryNamedInsured(contact);

        // Policy Location & Building
        PolicyLocation polLoc = new PolicyLocation("polloc-1", 1, "Logistics Hub Alpha", "100 Industrial Parkway", "Chicago", "IL", "60601");
        Building bldg = new Building("bldg-1", 1, "Main Warehouse & Logistics Hub", "Joisted Masonry", 2012, 1500000.0, 500000.0);
        polLoc.addBuilding(bldg);
        period.addLocation(polLoc);

        PolicyLine propLine = new PolicyLine("line-101", "CommercialPropertyLine", "Commercial Property Line");
        propLine.addBuilding(bldg);
        period.addLine(propLine);

        // Coverages
        Coverage covBuilding = new Coverage("cov-1", "BuildingCov", "Building Coverage", 1500000.0, 5000.0);
        covBuilding.setCalculatedTermAmount(2400.0);
        saveCoverageInternal(conn, "bldg-1", covBuilding);

        // Cost
        Cost costBase = new Cost("cost-1", "BasePremium", "Commercial Property Base Premium", 2400.0);
        saveCostInternal(conn, "1001", costBase);
        period.setCosts(Collections.singletonList(costBase));

        // Form
        PolicyForm formCP = new PolicyForm("form-1", "CP 00 10", "Building and Personal Property Coverage Form", "10 12", "Mandatory CP Coverage");
        period.addForm(formCP);
        savePolicyFormInternal(conn, "1001", formCP);

        // Note
        Note note = new Note("note-1", "Underwriting Inspection", "Inspected premises on 2026-07-01. Sprinkler system certified.", "su", "Underwriting");
        period.addNote(note);
        saveNoteInternal(conn, "1001", note);

        savePolicyPeriodInternal(conn, period);

        Job submission = new Job("job-5001", "SUB-5001", "Submission", period);
        saveJobInternal(conn, submission);

        // Policy Term
        String termSql = "INSERT OR REPLACE INTO policy_terms (public_id, term_number, effective_date, expiration_date, term_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(termSql)) {
            pstmt.setString(1, "term-1");
            pstmt.setInt(2, 1);
            pstmt.setString(3, period.getEffectiveDate());
            pstmt.setString(4, period.getExpirationDate());
            pstmt.setString(5, "Active");
            pstmt.executeUpdate();
        }

        // Account Contact Join
        String acSql = "INSERT OR REPLACE INTO account_contacts (public_id, account_id, contact_id, roles) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(acSql)) {
            pstmt.setString(1, "acont-1");
            pstmt.setString(2, "C00010928");
            pstmt.setString(3, "cont-101");
            pstmt.setString(4, "PrimaryNamedInsured,AccountingContact");
            pstmt.executeUpdate();
        }

        // Policy Location
        String plocSql = "INSERT OR REPLACE INTO policy_locations (public_id, period_id, location_num, location_name, address_line1, city, state, postal_code, building_count, fire_protection_class, tax_location_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(plocSql)) {
            pstmt.setString(1, "polloc-1");
            pstmt.setString(2, "1001");
            pstmt.setInt(3, 1);
            pstmt.setString(4, "Logistics Hub Alpha");
            pstmt.setString(5, "100 Industrial Parkway");
            pstmt.setString(6, "Chicago");
            pstmt.setString(7, "IL");
            pstmt.setString(8, "60601");
            pstmt.setInt(9, 1);
            pstmt.setString(10, "Class 3");
            pstmt.setString(11, "IL-COOK-60601");
            pstmt.executeUpdate();
        }

        // Vehicle & Driver
        String vehSql = "INSERT OR REPLACE INTO policy_vehicles (public_id, period_id, vehicle_num, vin, make, model, year, use_type, cost_new, garage_location_num, license_state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(vehSql)) {
            pstmt.setString(1, "veh-1");
            pstmt.setString(2, "1001");
            pstmt.setInt(3, 1);
            pstmt.setString(4, "1FTFW1E84MKD90182");
            pstmt.setString(5, "Ford");
            pstmt.setString(6, "F-350 Super Duty");
            pstmt.setInt(7, 2023);
            pstmt.setString(8, "Commercial");
            pstmt.setDouble(9, 65000.0);
            pstmt.setInt(10, 1);
            pstmt.setString(11, "IL");
            pstmt.executeUpdate();
        }

        String drvSql = "INSERT OR REPLACE INTO policy_drivers (public_id, period_id, driver_num, first_name, last_name, date_of_birth, license_number, license_state, number_of_violations, good_driver_discount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(drvSql)) {
            pstmt.setString(1, "drv-1");
            pstmt.setString(2, "1001");
            pstmt.setInt(3, 1);
            pstmt.setString(4, "David");
            pstmt.setString(5, "Miller");
            pstmt.setString(6, "1982-08-24");
            pstmt.setString(7, "IL-D8910273");
            pstmt.setString(8, "IL");
            pstmt.setInt(9, 0);
            pstmt.setInt(10, 1);
            pstmt.executeUpdate();
        }

        // Exclusion & Condition
        String exclSql = "INSERT OR REPLACE INTO exclusions (public_id, target_id, pattern_code, name, description, exclusion_text) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(exclSql)) {
            pstmt.setString(1, "excl-1");
            pstmt.setString(2, "line-101");
            pstmt.setString(3, "NuclearHazardExcl");
            pstmt.setString(4, "Nuclear Hazard Exclusion");
            pstmt.setString(5, "Standard Commercial Property Exclusion");
            pstmt.setString(6, "Loss or damage caused directly or indirectly by nuclear hazard is excluded.");
            pstmt.executeUpdate();
        }

        String condSql = "INSERT OR REPLACE INTO policy_conditions (public_id, target_id, pattern_code, name, condition_text) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(condSql)) {
            pstmt.setString(1, "cond-1");
            pstmt.setString(2, "line-101");
            pstmt.setString(3, "ProtectiveSafeguardCond");
            pstmt.setString(4, "Protective Safeguards Condition");
            pstmt.setString(5, "Insured must maintain automatic sprinkler system in complete working order.");
            pstmt.executeUpdate();
        }

        // Additional Insured
        String addlSql = "INSERT OR REPLACE INTO policy_addl_insureds (public_id, period_id, contact_id, interest_type, certificate_required) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(addlSql)) {
            pstmt.setString(1, "addl-1");
            pstmt.setString(2, "1001");
            pstmt.setString(3, "cont-101");
            pstmt.setString(4, "LossPayee");
            pstmt.setInt(5, 1);
            pstmt.executeUpdate();
        }

        // Policy Transaction
        String txSql = "INSERT OR REPLACE INTO policy_transactions (public_id, cost_id, posted_date, written_amount, charged_amount, eff_date, exp_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(txSql)) {
            pstmt.setString(1, "tx-101");
            pstmt.setString(2, "cost-1");
            pstmt.setString(3, period.getWrittenDate());
            pstmt.setDouble(4, 2400.0);
            pstmt.setDouble(5, 2400.0);
            pstmt.setString(6, period.getEffectiveDate());
            pstmt.setString(7, period.getExpirationDate());
            pstmt.executeUpdate();
        }

        // UW Issue
        String uwISql = "INSERT OR REPLACE INTO uw_issues (public_id, period_id, issue_key, short_description, long_description, status, approval_blocking_level) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(uwISql)) {
            pstmt.setString(1, "uwi-1");
            pstmt.setString(2, "1001");
            pstmt.setString(3, "HighBuildingLimit");
            pstmt.setString(4, "Building limit exceeds $1.0M standard threshold");
            pstmt.setString(5, "Requires Senior Underwriter review for building limit > $1.0M");
            pstmt.setString(6, "Approved");
            pstmt.setString(7, "Bind");
            pstmt.executeUpdate();
        }

        // Log History
        History hist = new History("hist-1", "su", "Created Submission SUB-5001 for Account C00010928", "SubmissionCreated");
        saveHistoryInternal(conn, hist);
    }

    // CONTACT OPERATORS
    public void saveContact(Contact contact) {
        try (Connection conn = connect()) {
            saveContactInternal(conn, contact);
        } catch (SQLException e) {
            LOGGER.error("Error saving contact", e);
        }
    }

    private void saveContactInternal(Connection conn, Contact c) throws SQLException {
        String sql = "INSERT INTO contacts (public_id, contact_type, name, first_name, last_name, company_name, tax_id, email, phone, work_phone, home_phone, fax_number, address_line1, address_line2, city, state, postal_code, country) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(public_id) DO UPDATE SET " +
                "name=excluded.name, company_name=excluded.company_name, tax_id=excluded.tax_id, email=excluded.email, phone=excluded.phone, city=excluded.city, state=excluded.state;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getPublicID());
            pstmt.setString(2, c.getContactType());
            pstmt.setString(3, c.getName());
            pstmt.setString(4, c.getFirstName());
            pstmt.setString(5, c.getLastName());
            pstmt.setString(6, c.getCompanyName());
            pstmt.setString(7, c.getTaxID());
            pstmt.setString(8, c.getEmail());
            pstmt.setString(9, c.getPhone());
            pstmt.setString(10, c.getWorkPhone());
            pstmt.setString(11, c.getHomePhone());
            pstmt.setString(12, c.getFaxNumber());
            pstmt.setString(13, c.getAddressLine1());
            pstmt.setString(14, c.getAddressLine2());
            pstmt.setString(15, c.getCity());
            pstmt.setString(16, c.getState());
            pstmt.setString(17, c.getPostalCode());
            pstmt.setString(18, c.getCountry());
            pstmt.executeUpdate();
        }
    }

    public Contact getContact(String publicID) {
        String sql = "SELECT * FROM contacts WHERE public_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, publicID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Contact c = new Contact(rs.getString("public_id"), rs.getString("contact_type"), rs.getString("name"), rs.getString("email"), rs.getString("phone"));
                c.setFirstName(rs.getString("first_name"));
                c.setLastName(rs.getString("last_name"));
                c.setCompanyName(rs.getString("company_name"));
                c.setTaxID(rs.getString("tax_id"));
                c.setWorkPhone(rs.getString("work_phone"));
                c.setHomePhone(rs.getString("home_phone"));
                c.setFaxNumber(rs.getString("fax_number"));
                c.setAddressLine1(rs.getString("address_line1"));
                c.setAddressLine2(rs.getString("address_line2"));
                c.setCity(rs.getString("city"));
                c.setState(rs.getString("state"));
                c.setPostalCode(rs.getString("postal_code"));
                c.setCountry(rs.getString("country"));
                return c;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching contact", e);
        }
        return null;
    }

    // ACCOUNT OPERATORS
    public void saveAccount(Account account) {
        try (Connection conn = connect()) {
            saveAccountInternal(conn, account);
        } catch (SQLException e) {
            LOGGER.error("Error saving account", e);
        }
    }

    public void saveAccountLocation(String accountNumber, AccountLocation loc) {
        try (Connection conn = connect()) {
            saveAccountLocationInternal(conn, accountNumber, loc);
        } catch (SQLException e) {
            LOGGER.error("Error saving account location", e);
        }
    }

    private void saveAccountInternal(Connection conn, Account a) throws SQLException {
        if (a.getPublicID() == null || a.getPublicID().isEmpty()) {
            a.setPublicID("acc-" + System.currentTimeMillis());
        }
        if (a.getAccountStatus() == null || a.getAccountStatus().isEmpty()) {
            a.setAccountStatus("Active");
        }
        if (a.getOriginationDate() == null || a.getOriginationDate().isEmpty()) {
            a.setOriginationDate(java.time.LocalDate.now().toString());
        }
        if (a.getAccountHolder() != null) {
            if (a.getAccountHolder().getPublicID() == null || a.getAccountHolder().getPublicID().isEmpty()) {
                a.getAccountHolder().setPublicID("cont-" + System.currentTimeMillis());
            }
            saveContactInternal(conn, a.getAccountHolder());
        }
        String sql = "INSERT INTO accounts (account_number, public_id, account_status, industry_code, origination_date, preferred_coverage_currency, frozen, account_holder_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(account_number) DO UPDATE SET " +
                "public_id=excluded.public_id, account_status=excluded.account_status, industry_code=excluded.industry_code, preferred_coverage_currency=excluded.preferred_coverage_currency, frozen=excluded.frozen, account_holder_id=excluded.account_holder_id;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getAccountNumber());
            pstmt.setString(2, a.getPublicID());
            pstmt.setString(3, a.getAccountStatus());
            pstmt.setString(4, a.getIndustryCode());
            pstmt.setString(5, a.getOriginationDate());
            pstmt.setString(6, a.getPreferredCoverageCurrency());
            pstmt.setInt(7, a.isFrozen() ? 1 : 0);
            pstmt.setString(8, a.getAccountHolder() != null ? a.getAccountHolder().getPublicID() : null);
            pstmt.executeUpdate();
        }
    }

    private void saveAccountLocationInternal(Connection conn, String accountNumber, AccountLocation loc) throws SQLException {
        String sql = "INSERT OR REPLACE INTO account_locations (public_id, account_number, location_num, location_name, address_line1, address_line2, city, state, postal_code, county, territory_code, geocode_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loc.getPublicID());
            pstmt.setString(2, accountNumber);
            pstmt.setInt(3, loc.getLocationNum());
            pstmt.setString(4, loc.getLocationName());
            pstmt.setString(5, loc.getAddressLine1());
            pstmt.setString(6, loc.getAddressLine2());
            pstmt.setString(7, loc.getCity());
            pstmt.setString(8, loc.getState());
            pstmt.setString(9, loc.getPostalCode());
            pstmt.setString(10, loc.getCounty());
            pstmt.setString(11, loc.getTerritoryCode());
            pstmt.setString(12, loc.getGeocodeStatus());
            pstmt.executeUpdate();
        }
    }

    public Account getAccount(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Contact holder = getContact(rs.getString("account_holder_id"));
                Account account = new Account(rs.getString("public_id"), rs.getString("account_number"), holder, rs.getString("industry_code"));
                account.setAccountStatus(rs.getString("account_status"));
                account.setOriginationDate(rs.getString("origination_date"));
                account.setPreferredCoverageCurrency(rs.getString("preferred_coverage_currency"));
                account.setFrozen(rs.getInt("frozen") == 1);

                // Fetch policies
                String polSql = "SELECT policy_number FROM policy_periods WHERE account_number = ? AND policy_number IS NOT NULL";
                try (PreparedStatement polStmt = conn.prepareStatement(polSql)) {
                    polStmt.setString(1, accountNumber);
                    ResultSet polRs = polStmt.executeQuery();
                    while (polRs.next()) {
                        account.addPolicyNumber(polRs.getString("policy_number"));
                    }
                }
                return account;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching account", e);
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        List<Account> result = new ArrayList<>();
        List<String> accountNumbers = new ArrayList<>();
        String sql = "SELECT account_number FROM accounts ORDER BY rowid DESC LIMIT 50";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accountNumbers.add(rs.getString("account_number"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching all accounts", e);
        }
        for (String accNum : accountNumbers) {
            Account a = getAccount(accNum);
            if (a != null) result.add(a);
        }
        return result;
    }

    // POLICY PERIOD OPERATORS
    public void savePolicyPeriod(PolicyPeriod period) {
        try (Connection conn = connect()) {
            savePolicyPeriodInternal(conn, period);
        } catch (SQLException e) {
            LOGGER.error("Error saving policy period", e);
        }
    }

    private void savePolicyPeriodInternal(Connection conn, PolicyPeriod p) throws SQLException {
        if (p.getAccount() != null) {
            saveAccountInternal(conn, p.getAccount());
        }
        if (p.getPrimaryNamedInsured() != null) {
            saveContactInternal(conn, p.getPrimaryNamedInsured());
        }

        String sql = "INSERT INTO policy_periods (period_id, public_id, status, product_code, product_name, quote_number, policy_number, effective_date, expiration_date, written_date, cancellation_date, currency, total_premium, tax_and_fees, total_cost, account_number, insured_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(period_id) DO UPDATE SET " +
                "status=excluded.status, policy_number=excluded.policy_number, quote_number=excluded.quote_number, total_premium=excluded.total_premium, tax_and_fees=excluded.tax_and_fees, total_cost=excluded.total_cost;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getPeriodID());
            pstmt.setString(2, p.getPublicID());
            pstmt.setString(3, p.getStatus());
            pstmt.setString(4, p.getProductCode());
            pstmt.setString(5, p.getProductName());
            pstmt.setString(6, p.getQuoteNumber());
            pstmt.setString(7, p.getPolicyNumber());
            pstmt.setString(8, p.getEffectiveDate());
            pstmt.setString(9, p.getExpirationDate());
            pstmt.setString(10, p.getWrittenDate());
            pstmt.setString(11, p.getCancellationDate());
            pstmt.setString(12, p.getCurrency());
            pstmt.setDouble(13, p.getTotalPremium());
            pstmt.setDouble(14, p.getTaxAndFees());
            pstmt.setDouble(15, p.getTotalCost());
            pstmt.setString(16, p.getAccount() != null ? p.getAccount().getAccountNumber() : null);
            pstmt.setString(17, p.getPrimaryNamedInsured() != null ? p.getPrimaryNamedInsured().getPublicID() : null);
            pstmt.executeUpdate();
        }

        // Save Buildings & Lines
        for (PolicyLine line : p.getLines()) {
            savePolicyLineInternal(conn, p.getPeriodID(), line);
        }
        for (Cost cost : p.getCosts()) {
            saveCostInternal(conn, p.getPeriodID(), cost);
        }
        for (PolicyForm form : p.getForms()) {
            savePolicyFormInternal(conn, p.getPeriodID(), form);
        }
        for (Note note : p.getNotes()) {
            saveNoteInternal(conn, p.getPeriodID(), note);
        }
    }

    private void savePolicyLineInternal(Connection conn, String periodId, PolicyLine line) throws SQLException {
        String sql = "INSERT OR REPLACE INTO policy_lines (public_id, period_id, pattern_code, line_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, line.getPublicID());
            pstmt.setString(2, periodId);
            pstmt.setString(3, line.getPatternCode());
            pstmt.setString(4, line.getLineName());
            pstmt.executeUpdate();
        }
        for (Building b : line.getBuildings()) {
            saveBuildingInternal(conn, periodId, b);
        }
    }

    private void saveBuildingInternal(Connection conn, String periodId, Building b) throws SQLException {
        if (b.getBranchID() == null || "BRANCH-DEFAULT".equals(b.getBranchID())) {
            b.setBranchID(periodId);
        }
        String sql = "INSERT OR REPLACE INTO buildings (id, fixed_id, branch_id, effective_date, expiration_date, change_type, public_id, period_id, building_num, description, construction_type, year_built, number_of_stories, sprinklered, alarm_type, fire_protection_class, building_limit, contents_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getId());
            pstmt.setString(2, b.getFixedID());
            pstmt.setString(3, b.getBranchID());
            pstmt.setString(4, b.getEffectiveDate());
            pstmt.setString(5, b.getExpirationDate());
            pstmt.setString(6, b.getChangeType());
            pstmt.setString(7, b.getPublicID());
            pstmt.setString(8, periodId);
            pstmt.setInt(9, b.getBuildingNum());
            pstmt.setString(10, b.getDescription());
            pstmt.setString(11, b.getConstructionType());
            pstmt.setInt(12, b.getYearBuilt());
            pstmt.setInt(13, b.getNumberOfStories());
            pstmt.setInt(14, b.isSprinklered() ? 1 : 0);
            pstmt.setString(15, b.getAlarmType());
            pstmt.setString(16, b.getFireProtectionClass());
            pstmt.setDouble(17, b.getBuildingLimit());
            pstmt.setDouble(18, b.getContentsLimit());
            pstmt.executeUpdate();
        }
        for (Coverage c : b.getCoverages()) {
            saveCoverageInternal(conn, b.getPublicID(), c);
        }
    }

    private void saveCoverageInternal(Connection conn, String targetId, Coverage c) throws SQLException {
        String sql = "INSERT OR REPLACE INTO coverages (public_id, target_id, pattern_code, name, limit_amount, deductible_amount, choice_value, direct_value, currency, calculated_term_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getPublicID());
            pstmt.setString(2, targetId);
            pstmt.setString(3, c.getPatternCode());
            pstmt.setString(4, c.getName());
            pstmt.setDouble(5, c.getLimit());
            pstmt.setDouble(6, c.getDeductible());
            pstmt.setString(7, c.getChoiceValue());
            pstmt.setDouble(8, c.getDirectValue());
            pstmt.setString(9, c.getCurrency());
            pstmt.setDouble(10, c.getCalculatedTermAmount());
            pstmt.executeUpdate();
        }
    }

    private void saveCostInternal(Connection conn, String periodId, Cost cost) throws SQLException {
        String sql = "INSERT OR REPLACE INTO costs (public_id, period_id, cost_type, description, actual_amount, actual_term_amount, proration_factor, charge_pattern, rate_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cost.getPublicID());
            pstmt.setString(2, periodId);
            pstmt.setString(3, cost.getCostType());
            pstmt.setString(4, cost.getDescription());
            pstmt.setDouble(5, cost.getActualAmount());
            pstmt.setDouble(6, cost.getActualTermAmount());
            pstmt.setDouble(7, cost.getProrationFactor());
            pstmt.setString(8, cost.getChargePattern());
            pstmt.setDouble(9, cost.getRateAmount());
            pstmt.executeUpdate();
        }
    }

    private void savePolicyFormInternal(Connection conn, String periodId, PolicyForm form) throws SQLException {
        String sql = "INSERT OR REPLACE INTO policy_forms (public_id, period_id, form_number, form_name, edition, inference_rule) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, form.getPublicID());
            pstmt.setString(2, periodId);
            pstmt.setString(3, form.getFormNumber());
            pstmt.setString(4, form.getFormName());
            pstmt.setString(5, form.getEdition());
            pstmt.setString(6, form.getInferenceRule());
            pstmt.executeUpdate();
        }
    }

    private void saveNoteInternal(Connection conn, String periodId, Note note) throws SQLException {
        String sql = "INSERT OR REPLACE INTO notes (public_id, period_id, subject, body, author, topic, security_level) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, note.getPublicID());
            pstmt.setString(2, periodId);
            pstmt.setString(3, note.getSubject());
            pstmt.setString(4, note.getBody());
            pstmt.setString(5, note.getAuthor());
            pstmt.setString(6, note.getTopic());
            pstmt.setString(7, note.getSecurityLevel());
            pstmt.executeUpdate();
        }
    }

    private void saveHistoryInternal(Connection conn, History hist) throws SQLException {
        String sql = "INSERT OR REPLACE INTO history (public_id, event_timestamp, username, description, event_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hist.getPublicID());
            pstmt.setString(2, hist.getEventTimestamp());
            pstmt.setString(3, hist.getUsername());
            pstmt.setString(4, hist.getDescription());
            pstmt.setString(5, hist.getEventType());
            pstmt.executeUpdate();
        }
    }

    public PolicyPeriod getPolicyPeriod(String periodId) {
        String sql = "SELECT * FROM policy_periods WHERE period_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, periodId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PolicyPeriod p = new PolicyPeriod();
                p.setPeriodID(rs.getString("period_id"));
                p.setPublicID(rs.getString("public_id"));
                p.setStatus(rs.getString("status"));
                p.setProductCode(rs.getString("product_code"));
                p.setProductName(rs.getString("product_name"));
                p.setQuoteNumber(rs.getString("quote_number"));
                p.setPolicyNumber(rs.getString("policy_number"));
                p.setEffectiveDate(rs.getString("effective_date"));
                p.setExpirationDate(rs.getString("expiration_date"));
                p.setWrittenDate(rs.getString("written_date"));
                p.setCancellationDate(rs.getString("cancellation_date"));
                p.setCurrency(rs.getString("currency"));
                p.setTotalPremium(rs.getDouble("total_premium"));
                p.setTaxAndFees(rs.getDouble("tax_and_fees"));
                p.setTotalCost(rs.getDouble("total_cost"));
                p.setAccount(getAccount(rs.getString("account_number")));
                p.setPrimaryNamedInsured(getContact(rs.getString("insured_id")));

                // Load Line & Buildings
                String lineSql = "SELECT * FROM policy_lines WHERE period_id = ?";
                try (PreparedStatement lStmt = conn.prepareStatement(lineSql)) {
                    lStmt.setString(1, periodId);
                    ResultSet lRs = lStmt.executeQuery();
                    while (lRs.next()) {
                        PolicyLine line = new PolicyLine(lRs.getString("public_id"), lRs.getString("pattern_code"), lRs.getString("line_name"));
                        String bldgSql = "SELECT * FROM buildings WHERE period_id = ?";
                        try (PreparedStatement bStmt = conn.prepareStatement(bldgSql)) {
                            bStmt.setString(1, periodId);
                            ResultSet bRs = bStmt.executeQuery();
                            while (bRs.next()) {
                                Building b = new Building(bRs.getString("public_id"), bRs.getInt("building_num"), bRs.getString("description"), bRs.getString("construction_type"), bRs.getInt("year_built"), bRs.getDouble("building_limit"), bRs.getDouble("contents_limit"));
                                if (bRs.getString("id") != null) b.setId(bRs.getString("id"));
                                if (bRs.getString("fixed_id") != null) b.setFixedID(bRs.getString("fixed_id"));
                                if (bRs.getString("branch_id") != null) b.setBranchID(bRs.getString("branch_id"));
                                if (bRs.getString("effective_date") != null) b.setEffectiveDate(bRs.getString("effective_date"));
                                if (bRs.getString("expiration_date") != null) b.setExpirationDate(bRs.getString("expiration_date"));
                                if (bRs.getString("change_type") != null) b.setChangeType(bRs.getString("change_type"));
                                b.setNumberOfStories(bRs.getInt("number_of_stories"));
                                b.setSprinklered(bRs.getInt("sprinklered") == 1);
                                b.setAlarmType(bRs.getString("alarm_type"));
                                b.setFireProtectionClass(bRs.getString("fire_protection_class"));
                                line.addBuilding(b);
                                p.getBuildings().add(b);
                            }
                        }
                        p.addLine(line);
                    }
                }

                // Load Costs
                String costSql = "SELECT * FROM costs WHERE period_id = ?";
                try (PreparedStatement cStmt = conn.prepareStatement(costSql)) {
                    cStmt.setString(1, periodId);
                    ResultSet cRs = cStmt.executeQuery();
                    List<Cost> costList = new ArrayList<>();
                    while (cRs.next()) {
                        Cost c = new Cost(cRs.getString("public_id"), cRs.getString("cost_type"), cRs.getString("description"), cRs.getDouble("actual_amount"));
                        c.setActualTermAmount(cRs.getDouble("actual_term_amount"));
                        c.setProrationFactor(cRs.getDouble("proration_factor"));
                        c.setChargePattern(cRs.getString("charge_pattern"));
                        c.setRateAmount(cRs.getDouble("rate_amount"));
                        costList.add(c);
                    }
                    p.setCosts(costList);
                }

                // Load Forms
                String formSql = "SELECT * FROM policy_forms WHERE period_id = ?";
                try (PreparedStatement fStmt = conn.prepareStatement(formSql)) {
                    fStmt.setString(1, periodId);
                    ResultSet fRs = fStmt.executeQuery();
                    while (fRs.next()) {
                        PolicyForm f = new PolicyForm(fRs.getString("public_id"), fRs.getString("form_number"), fRs.getString("form_name"), fRs.getString("edition"), fRs.getString("inference_rule"));
                        p.addForm(f);
                    }
                }

                // Load Notes
                String noteSql = "SELECT * FROM notes WHERE period_id = ?";
                try (PreparedStatement nStmt = conn.prepareStatement(noteSql)) {
                    nStmt.setString(1, periodId);
                    ResultSet nRs = nStmt.executeQuery();
                    while (nRs.next()) {
                        Note n = new Note(nRs.getString("public_id"), nRs.getString("subject"), nRs.getString("body"), nRs.getString("author"), nRs.getString("topic"));
                        n.setSecurityLevel(nRs.getString("security_level"));
                        p.addNote(n);
                    }
                }

                return p;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching policy period", e);
        }
        return null;
    }

    public List<PolicyPeriod> getAllPolicies() {
        List<PolicyPeriod> list = new ArrayList<>();
        List<String> periodIds = new ArrayList<>();
        String sql = "SELECT period_id FROM policy_periods ORDER BY rowid DESC LIMIT 50";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                periodIds.add(rs.getString("period_id"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching all policies", e);
        }
        for (String pid : periodIds) {
            PolicyPeriod p = getPolicyPeriod(pid);
            if (p != null) list.add(p);
        }
        return list;
    }

    // JOB OPERATORS
    public void saveJob(Job job) {
        try (Connection conn = connect()) {
            saveJobInternal(conn, job);
        } catch (SQLException e) {
            LOGGER.error("Error saving job", e);
        }
    }

    private void saveJobInternal(Connection conn, Job job) throws SQLException {
        if (job.getPolicyPeriod() != null) {
            savePolicyPeriodInternal(conn, job.getPolicyPeriod());
        }
        String sql = "INSERT INTO jobs (job_number, public_id, job_type, job_status, close_date, create_date, underwriter_id, producer_code, period_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(job_number) DO UPDATE SET " +
                "job_status=excluded.job_status, close_date=excluded.close_date;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, job.getJobNumber());
            pstmt.setString(2, job.getPublicID());
            pstmt.setString(3, job.getJobType());
            pstmt.setString(4, job.getJobStatus());
            pstmt.setString(5, job.getCloseDate());
            pstmt.setString(6, job.getCreateDate());
            pstmt.setString(7, job.getUnderwriterID());
            pstmt.setString(8, job.getProducerCode());
            pstmt.setString(9, job.getPolicyPeriod() != null ? job.getPolicyPeriod().getPeriodID() : null);
            pstmt.executeUpdate();
        }
    }

    public Job getJob(String jobNumber) {
        String sql = "SELECT * FROM jobs WHERE job_number = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, jobNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PolicyPeriod period = getPolicyPeriod(rs.getString("period_id"));
                Job job = new Job(rs.getString("public_id"), rs.getString("job_number"), rs.getString("job_type"), period);
                job.setJobStatus(rs.getString("job_status"));
                job.setCloseDate(rs.getString("close_date"));
                job.setCreateDate(rs.getString("create_date"));
                job.setUnderwriterID(rs.getString("underwriter_id"));
                job.setProducerCode(rs.getString("producer_code"));
                return job;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching job", e);
        }
        return null;
    }

    public Job createSubmissionJob(String accountNumber, String prodCode) {
        Account acc = getAccount(accountNumber != null && !accountNumber.isEmpty() ? accountNumber : "C00010928");
        if (acc == null) {
            List<Account> allAccs = getAllAccounts();
            if (!allAccs.isEmpty()) {
                acc = allAccs.get(0);
            }
        }
        Contact insured = (acc != null && acc.getAccountHolder() != null) ? acc.getAccountHolder() : new Contact("cont-101", "Acme Enterprise", "Company", "info@acme.com", "555-0199");

        String periodID = "prd-" + (int)(1000 + Math.random() * 9000);
        PolicyPeriod period = new PolicyPeriod(periodID, acc, insured, java.time.LocalDate.now().toString(), java.time.LocalDate.now().plusYears(1).toString());
        period.setProductCode(prodCode != null ? prodCode : "CommercialProperty");
        period.setProductName("CommercialAuto".equalsIgnoreCase(prodCode) ? "Commercial Auto" : ("WorkersComp".equalsIgnoreCase(prodCode) ? "Workers' Compensation" : "Commercial Property"));
        period.setStatus("Draft");

        Building bldg = new Building("bldg-" + (int)(1000 + Math.random() * 9000), 1, "HQ Facility", "Joisted Masonry", 1000000.0, 250000.0);
        PolicyLine line = new PolicyLine("line-" + (int)(1000 + Math.random() * 9000), prodCode != null ? prodCode : "CommercialProperty", period.getProductName() + " Line");
        line.addBuilding(bldg);
        period.addLine(line);
        period.getBuildings().add(bldg);

        String jobNum = "SUB-" + (int)(10000 + Math.random() * 90000);
        Job job = new Job("job-" + jobNum, jobNum, "Submission", period);
        job.setJobStatus("Draft");
        job.setCreateDate(java.time.LocalDate.now().toString());
        job.setUnderwriterID("su");
        job.setProducerCode("PROD-1001");

        saveJob(job);
        return job;
    }

    public List<Job> getAllJobs() {
        List<Job> result = new ArrayList<>();
        List<String> jobNumbers = new ArrayList<>();
        String sql = "SELECT job_number FROM jobs ORDER BY rowid DESC LIMIT 50";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                jobNumbers.add(rs.getString("job_number"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching all jobs", e);
        }
        for (String jobNum : jobNumbers) {
            Job j = getJob(jobNum);
            if (j != null) result.add(j);
        }
        return result;
    }

    // USER OPERATORS
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User u = new User(rs.getString("public_id"), rs.getString("username"), rs.getString("password"), rs.getString("full_name"), rs.getString("role"), rs.getString("producer_code"));
                u.setEmail(rs.getString("email"));
                u.setDepartment(rs.getString("department"));
                u.setAuthorityProfileID(rs.getString("authority_profile_id"));
                return u;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching user by username", e);
        }
        return null;
    }

    private void seedExtendedOotbData(Connection conn) throws SQLException {
        // Payment Plans
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO payment_plans VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "plan-full"); ps.setString(2, "Full Pay"); ps.setString(3, "DirectBill"); ps.setDouble(4, 1.0); ps.setDouble(5, 0.0); ps.setInt(6, 1); ps.executeUpdate();
            ps.setString(1, "plan-4pay"); ps.setString(2, "Quarterly 4-Pay"); ps.setString(3, "DirectBill"); ps.setDouble(4, 0.25); ps.setDouble(5, 5.0); ps.setInt(6, 4); ps.executeUpdate();
            ps.setString(1, "plan-monthly"); ps.setString(2, "Monthly Installments"); ps.setString(3, "DirectBill"); ps.setDouble(4, 0.10); ps.setDouble(5, 3.0); ps.setInt(6, 12); ps.executeUpdate();
        }

        // UW Companies
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO uw_companies VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, "uwco-101"); ps.setString(2, "ACME_GEN"); ps.setString(3, "Acme General Insurance Company"); ps.setString(4, "IL"); ps.setString(5, "Active"); ps.executeUpdate();
            ps.setString(1, "uwco-102"); ps.setString(2, "APEX_IND"); ps.setString(3, "Apex Indemnity Corp"); ps.setString(4, "CA"); ps.setString(5, "Active"); ps.executeUpdate();
        }

        // Form Patterns
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO form_patterns VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "fp-101"); ps.setString(2, "CP 00 10"); ps.setString(3, "10 12"); ps.setString(4, "Building and Personal Property Coverage Form"); ps.setString(5, "com.policycenter.forms.CPBuildingInference"); ps.setInt(6, 10); ps.executeUpdate();
            ps.setString(1, "fp-102"); ps.setString(2, "IL 00 17"); ps.setString(3, "11 98"); ps.setString(4, "Common Policy Conditions"); ps.setString(5, "com.policycenter.forms.CommonConditionsInference"); ps.setInt(6, 1); ps.executeUpdate();
        }

        // WC Class Codes
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO wc_class_codes VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, "wc-8810"); ps.setString(2, "8810"); ps.setString(3, "IL"); ps.setString(4, "Clerical Office Employees NOC"); ps.setDouble(5, 0.35); ps.executeUpdate();
            ps.setString(1, "wc-5606"); ps.setString(2, "5606"); ps.setString(3, "IL"); ps.setString(4, "Contractor - Executive Supervisor"); ps.setDouble(5, 2.15); ps.executeUpdate();
        }

        // GL Class Codes
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO gl_class_codes VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, "gl-61212"); ps.setString(2, "61212"); ps.setString(3, "Premises/Operations"); ps.setString(4, "Warehouses - Private"); ps.setString(5, "Area (Sq Ft)"); ps.executeUpdate();
        }

        // Rate Books
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO rate_books VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "rb-2026-v1"); ps.setString(2, "CP-2026"); ps.setString(3, "Commercial Property Ratebook 2026"); ps.setString(4, "v1.0"); ps.setString(5, "Active"); ps.setString(6, "2026-01-01"); ps.setString(7, "CommercialProperty"); ps.executeUpdate();
        }

        // Rate Routines
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO rate_routines VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "rr-cp-base"); ps.setString(2, "CP_BASE_RATE_ROUTINE"); ps.setString(3, "CP Base Premium Formula"); ps.setString(4, "CommercialProperty"); ps.setString(5, "Base Rate x Construction Factor x Protection Class Factor"); ps.setString(6, "BaseRate * ConstructionMod * ProtectionClassMod"); ps.executeUpdate();
        }

        // Documents
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO documents VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "doc-101"); ps.setString(2, "C00010928"); ps.setString(3, "SUB-5001"); ps.setString(4, "Signed_Commercial_Application.pdf"); ps.setString(5, "Application"); ps.setString(6, "application/pdf"); ps.setString(7, "Approved"); ps.setString(8, "/api/documents/dec-page"); ps.setString(9, "2026-07-23"); ps.executeUpdate();
        }

        // Contingencies
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contingencies VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "ctg-101"); ps.setString(2, "period-5001"); ps.setString(3, "Central Station Fire Alarm Certificate"); ps.setString(4, "Insured must provide active alarm verification within 30 days of binding."); ps.setString(5, "2026-08-23"); ps.setString(6, "Pending"); ps.setString(7, "Cancel Policy"); ps.executeUpdate();
        }

        // Transactions
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO transactions VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, "tx-101"); ps.setString(2, "period-5001"); ps.setString(3, "cost-101"); ps.setDouble(4, 2400.00); ps.setInt(5, 1); ps.setInt(6, 1); ps.setString(7, "2026-01-01"); ps.setString(8, "2027-01-01"); ps.setString(9, "2026-07-23"); ps.executeUpdate();
        }
    }

    // OOTB ENTITIES SUMMARY MAP
    public Map<String, Integer> getEntityCountsMap() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        String[] tables = {
            "contacts", "account_locations", "account_contacts", "accounts", "policy_terms",
            "policy_periods", "jobs", "policy_lines", "policy_locations", "buildings",
            "policy_vehicles", "policy_drivers", "coverages", "exclusions", "policy_conditions",
            "policy_addl_insureds", "costs", "policy_transactions", "uw_issues", "uw_authority_profiles",
            "groups", "producer_codes", "users", "policy_forms", "history", "notes",
            "transactions", "payment_plans", "modifiers", "audit_informations", "uw_companies",
            "uw_authority_grants", "contingencies", "documents", "form_patterns", "wc_class_codes",
            "wc_employees", "gl_class_codes", "gl_exposures", "ri_risks", "ri_programs",
            "ri_attachments", "rate_books", "rate_routines",
            "activities", "activity_patterns", "roles", "organizations", "producer_code_assignments",
            "regions", "rate_table_factors", "policy_commissions", "tax_surcharges",
            "jurisdictions", "product_models", "coverage_patterns", "claim_details",
            "audit_schedules", "policy_holds"
        };
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table + ";");
                if (rs.next()) {
                    counts.put(table, rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error generating entity counts map", e);
        }
        return counts;
    }

    /**
     * Seeds reference data for the 15 new OOTB entities (tables 45–59).
     */
    private void seedNewOotbEntities(Connection conn) throws SQLException {
        // --- 47. Roles ---
        String roleSql = "INSERT INTO roles (public_id, role_name, description, role_type, permissions, can_approve_uw_issues, can_bind_policies, can_cancel_policies, can_view_financials) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(roleSql)) {
            insertRole(ps, "role-01", "Underwriter", "Reviews and underwrites commercial policies", "System", "view_policy,edit_policy,quote,approve_uw_issue", 1, 1, 0, 1);
            insertRole(ps, "role-02", "Senior Underwriter", "Senior authority with elevated bind limits", "System", "view_policy,edit_policy,quote,bind,approve_uw_issue,override_rating", 1, 1, 1, 1);
            insertRole(ps, "role-03", "UW Manager", "Manages underwriting team and authority escalations", "System", "view_policy,edit_policy,quote,bind,approve_uw_issue,override_rating,manage_team", 1, 1, 1, 1);
            insertRole(ps, "role-04", "Producer", "External agency producer with submission access", "System", "view_policy,create_submission,view_quotes", 0, 0, 0, 0);
            insertRole(ps, "role-05", "SysAdmin", "System administrator with full access", "System", "all", 1, 1, 1, 1);
            insertRole(ps, "role-06", "Auditor", "Premium audit specialist", "System", "view_policy,view_financials,audit", 0, 0, 0, 1);
            insertRole(ps, "role-07", "Claims Liaison", "ClaimCenter integration liaison", "System", "view_policy,view_claims,create_notes", 0, 0, 0, 0);
        }

        // --- 46. Activity Patterns ---
        String apSql = "INSERT INTO activity_patterns (public_id, code, subject, activity_class, priority, category, due_days_from_target, escalation_days, mandatory, auto_assign, recurring, trigger_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(apSql)) {
            ps.setString(1, "ap-01"); ps.setString(2, "verify_loss_runs"); ps.setString(3, "Verify Prior 3-Year Loss Runs");
            ps.setString(4, "Task"); ps.setString(5, "High"); ps.setString(6, "Underwriting");
            ps.setInt(7, 3); ps.setInt(8, 2); ps.setInt(9, 1); ps.setInt(10, 1); ps.setInt(11, 0); ps.setString(12, "Pre-Bind");
            ps.executeUpdate();

            ps.setString(1, "ap-02"); ps.setString(2, "order_inspection"); ps.setString(3, "Order Commercial Building Inspection");
            ps.setString(4, "Task"); ps.setString(5, "Urgent"); ps.setString(6, "Underwriting");
            ps.setInt(7, 5); ps.setInt(8, 3); ps.setInt(9, 1); ps.setInt(10, 1); ps.setInt(11, 0); ps.setString(12, "Pre-Quote");
            ps.executeUpdate();

            ps.setString(1, "ap-03"); ps.setString(2, "renewal_review"); ps.setString(3, "90-Day Renewal Review");
            ps.setString(4, "Approval"); ps.setString(5, "Normal"); ps.setString(6, "Renewal");
            ps.setInt(7, 90); ps.setInt(8, 14); ps.setInt(9, 1); ps.setInt(10, 1); ps.setInt(11, 1); ps.setString(12, "Renewal");
            ps.executeUpdate();

            ps.setString(1, "ap-04"); ps.setString(2, "premium_audit_notice"); ps.setString(3, "Send Premium Audit Notice");
            ps.setString(4, "Notification"); ps.setString(5, "Normal"); ps.setString(6, "Audit");
            ps.setInt(7, 30); ps.setInt(8, 7); ps.setInt(9, 0); ps.setInt(10, 0); ps.setInt(11, 0); ps.setString(12, "Post-Issue");
            ps.executeUpdate();
        }

        // --- 45. Activities ---
        String actSql = "INSERT INTO activities (public_id, subject, priority, status, activity_class, assignee_id, target_id, target_type, due_date, mandatory) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(actSql)) {
            ps.setString(1, "act-001"); ps.setString(2, "Verify Prior 3-Year Loss Runs"); ps.setString(3, "High"); ps.setString(4, "Open");
            ps.setString(5, "Task"); ps.setString(6, "user-uw1"); ps.setString(7, "SUB-5001"); ps.setString(8, "Job"); ps.setString(9, "2026-07-28"); ps.setInt(10, 1);
            ps.executeUpdate();
            ps.setString(1, "act-002"); ps.setString(2, "Order Commercial Building Inspection"); ps.setString(3, "Urgent"); ps.setString(4, "Open");
            ps.setString(5, "Task"); ps.setString(6, "user-uw1"); ps.setString(7, "SUB-5001"); ps.setString(8, "Job"); ps.setString(9, "2026-07-25"); ps.setInt(10, 1);
            ps.executeUpdate();
            ps.setString(1, "act-003"); ps.setString(2, "90-Day Renewal Review"); ps.setString(3, "Normal"); ps.setString(4, "Completed");
            ps.setString(5, "Approval"); ps.setString(6, "user-uw1"); ps.setString(7, "POL-001"); ps.setString(8, "PolicyPeriod"); ps.setString(9, "2026-10-01"); ps.setInt(10, 1);
            ps.executeUpdate();
        }

        // --- 48. Organizations ---
        String orgSql = "INSERT INTO organizations (public_id, name, org_type, status, city, state, phone, fein) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(orgSql)) {
            ps.setString(1, "org-01"); ps.setString(2, "Guidewire Mutual Insurance Company"); ps.setString(3, "Carrier"); ps.setString(4, "Active");
            ps.setString(5, "Chicago"); ps.setString(6, "IL"); ps.setString(7, "(312) 555-1000"); ps.setString(8, "36-1234567"); ps.executeUpdate();
            ps.setString(1, "org-02"); ps.setString(2, "Apex Global Insurance Agency"); ps.setString(3, "Agency"); ps.setString(4, "Active");
            ps.setString(5, "Chicago"); ps.setString(6, "IL"); ps.setString(7, "(312) 555-2000"); ps.setString(8, "36-9876543"); ps.executeUpdate();
            ps.setString(1, "org-03"); ps.setString(2, "Liberty Specialty MGA"); ps.setString(3, "MGA"); ps.setString(4, "Active");
            ps.setString(5, "New York"); ps.setString(6, "NY"); ps.setString(7, "(212) 555-3000"); ps.setString(8, "13-5551234"); ps.executeUpdate();
        }

        // --- 49. Producer Code Assignments ---
        String pcaSql = "INSERT INTO producer_code_assignments (public_id, user_id, producer_code_id, assignment_role, active) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(pcaSql)) {
            ps.setString(1, "pca-01"); ps.setString(2, "user-uw1"); ps.setString(3, "PROD-1001"); ps.setString(4, "Primary"); ps.setInt(5, 1); ps.executeUpdate();
            ps.setString(1, "pca-02"); ps.setString(2, "user-prod1"); ps.setString(3, "PROD-1001"); ps.setString(4, "Primary"); ps.setInt(5, 1); ps.executeUpdate();
        }

        // --- 50. Regions ---
        String regSql = "INSERT INTO regions (public_id, region_code, region_name, region_type, states, catastrophe_exposed) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(regSql)) {
            ps.setString(1, "reg-01"); ps.setString(2, "MW"); ps.setString(3, "Midwest"); ps.setString(4, "Zone"); ps.setString(5, "IL,IN,OH,MI,WI"); ps.setInt(6, 0); ps.executeUpdate();
            ps.setString(1, "reg-02"); ps.setString(2, "SE"); ps.setString(3, "Southeast"); ps.setString(4, "Zone"); ps.setString(5, "FL,GA,SC,NC,AL"); ps.setInt(6, 1); ps.executeUpdate();
            ps.setString(1, "reg-03"); ps.setString(2, "NE"); ps.setString(3, "Northeast"); ps.setString(4, "Zone"); ps.setString(5, "NY,NJ,CT,MA,PA"); ps.setInt(6, 0); ps.executeUpdate();
            ps.setString(1, "reg-04"); ps.setString(2, "SW"); ps.setString(3, "Southwest"); ps.setString(4, "Zone"); ps.setString(5, "TX,AZ,NM,OK"); ps.setInt(6, 1); ps.executeUpdate();
            ps.setString(1, "reg-05"); ps.setString(2, "WE"); ps.setString(3, "West"); ps.setString(4, "Zone"); ps.setString(5, "CA,OR,WA,NV,CO"); ps.setInt(6, 1); ps.executeUpdate();
        }

        // --- 54. Jurisdictions ---
        String jurSql = "INSERT INTO jurisdictions (public_id, state_code, state_name, country, default_premium_tax_rate, catastrophe_exposed, file_and_use, prior_approval) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(jurSql)) {
            ps.setString(1, "jur-IL"); ps.setString(2, "IL"); ps.setString(3, "Illinois"); ps.setString(4, "US"); ps.setDouble(5, 3.5); ps.setInt(6, 0); ps.setInt(7, 1); ps.setInt(8, 0); ps.executeUpdate();
            ps.setString(1, "jur-CA"); ps.setString(2, "CA"); ps.setString(3, "California"); ps.setString(4, "US"); ps.setDouble(5, 2.35); ps.setInt(6, 1); ps.setInt(7, 0); ps.setInt(8, 1); ps.executeUpdate();
            ps.setString(1, "jur-FL"); ps.setString(2, "FL"); ps.setString(3, "Florida"); ps.setString(4, "US"); ps.setDouble(5, 1.75); ps.setInt(6, 1); ps.setInt(7, 1); ps.setInt(8, 0); ps.executeUpdate();
            ps.setString(1, "jur-NY"); ps.setString(2, "NY"); ps.setString(3, "New York"); ps.setString(4, "US"); ps.setDouble(5, 3.6); ps.setInt(6, 0); ps.setInt(7, 0); ps.setInt(8, 1); ps.executeUpdate();
            ps.setString(1, "jur-TX"); ps.setString(2, "TX"); ps.setString(3, "Texas"); ps.setString(4, "US"); ps.setDouble(5, 1.6); ps.setInt(6, 1); ps.setInt(7, 1); ps.setInt(8, 0); ps.executeUpdate();
        }

        // --- 55. Product Models ---
        String pmSql = "INSERT INTO product_models (public_id, product_code, product_name, product_abbrev, policy_line_pattern, available_jurisdictions, status, renewal_enabled, cancellation_enabled, audit_enabled) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(pmSql)) {
            ps.setString(1, "pm-01"); ps.setString(2, "CommercialProperty"); ps.setString(3, "Commercial Property"); ps.setString(4, "CP"); ps.setString(5, "CPLine"); ps.setString(6, "ALL"); ps.setString(7, "Active"); ps.setInt(8, 1); ps.setInt(9, 1); ps.setInt(10, 0); ps.executeUpdate();
            ps.setString(1, "pm-02"); ps.setString(2, "CommercialAuto"); ps.setString(3, "Commercial Auto"); ps.setString(4, "CA"); ps.setString(5, "CALine"); ps.setString(6, "ALL"); ps.setString(7, "Active"); ps.setInt(8, 1); ps.setInt(9, 1); ps.setInt(10, 0); ps.executeUpdate();
            ps.setString(1, "pm-03"); ps.setString(2, "WorkersComp"); ps.setString(3, "Workers' Compensation"); ps.setString(4, "WC"); ps.setString(5, "WCLine"); ps.setString(6, "ALL"); ps.setString(7, "Active"); ps.setInt(8, 1); ps.setInt(9, 1); ps.setInt(10, 1); ps.executeUpdate();
            ps.setString(1, "pm-04"); ps.setString(2, "GeneralLiability"); ps.setString(3, "General Liability"); ps.setString(4, "GL"); ps.setString(5, "GLLine"); ps.setString(6, "ALL"); ps.setString(7, "Active"); ps.setInt(8, 1); ps.setInt(9, 1); ps.setInt(10, 1); ps.executeUpdate();
        }

        // --- 56. Coverage Patterns ---
        String cpSql = "INSERT INTO coverage_patterns (public_id, pattern_code, name, policy_line_pattern, coverable_type, default_limit, default_deductible, mandatory, category) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(cpSql)) {
            ps.setString(1, "cp-01"); ps.setString(2, "CPBldgCov"); ps.setString(3, "Building Coverage"); ps.setString(4, "CPLine"); ps.setString(5, "Building"); ps.setDouble(6, 500000); ps.setDouble(7, 1000); ps.setInt(8, 1); ps.setString(9, "Property"); ps.executeUpdate();
            ps.setString(1, "cp-02"); ps.setString(2, "CPBPPCov"); ps.setString(3, "Business Personal Property"); ps.setString(4, "CPLine"); ps.setString(5, "Building"); ps.setDouble(6, 100000); ps.setDouble(7, 500); ps.setInt(8, 0); ps.setString(9, "Property"); ps.executeUpdate();
            ps.setString(1, "cp-03"); ps.setString(2, "CPBIAndEECov"); ps.setString(3, "Business Income & Extra Expense"); ps.setString(4, "CPLine"); ps.setString(5, "Building"); ps.setDouble(6, 250000); ps.setDouble(7, 0); ps.setInt(8, 0); ps.setString(9, "Property"); ps.executeUpdate();
            ps.setString(1, "cp-04"); ps.setString(2, "CALiabCov"); ps.setString(3, "Auto Liability Coverage"); ps.setString(4, "CALine"); ps.setString(5, "Vehicle"); ps.setDouble(6, 1000000); ps.setDouble(7, 0); ps.setInt(8, 1); ps.setString(9, "Liability"); ps.executeUpdate();
            ps.setString(1, "cp-05"); ps.setString(2, "CACollCov"); ps.setString(3, "Auto Collision Coverage"); ps.setString(4, "CALine"); ps.setString(5, "Vehicle"); ps.setDouble(6, 50000); ps.setDouble(7, 500); ps.setInt(8, 0); ps.setString(9, "AutoPhysicalDamage"); ps.executeUpdate();
            ps.setString(1, "cp-06"); ps.setString(2, "CACompCov"); ps.setString(3, "Auto Comprehensive Coverage"); ps.setString(4, "CALine"); ps.setString(5, "Vehicle"); ps.setDouble(6, 50000); ps.setDouble(7, 250); ps.setInt(8, 0); ps.setString(9, "AutoPhysicalDamage"); ps.executeUpdate();
            ps.setString(1, "cp-07"); ps.setString(2, "GLCGLCov"); ps.setString(3, "Commercial General Liability"); ps.setString(4, "GLLine"); ps.setString(5, "Line"); ps.setDouble(6, 1000000); ps.setDouble(7, 0); ps.setInt(8, 1); ps.setString(9, "Liability"); ps.executeUpdate();
            ps.setString(1, "cp-08"); ps.setString(2, "WCStatCov"); ps.setString(3, "Workers Comp Statutory Coverage"); ps.setString(4, "WCLine"); ps.setString(5, "Line"); ps.setDouble(6, 0); ps.setDouble(7, 0); ps.setInt(8, 1); ps.setString(9, "Liability"); ps.executeUpdate();
        }

        // --- 52. Policy Commissions ---
        String comSql = "INSERT INTO policy_commissions (public_id, period_id, producer_code_id, commission_plan, commission_rate, commission_amount, role, payment_status) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(comSql)) {
            ps.setString(1, "comm-01"); ps.setString(2, "prd-001"); ps.setString(3, "PROD-1001"); ps.setString(4, "Standard"); ps.setDouble(5, 15.0); ps.setDouble(6, 3360.0); ps.setString(7, "Primary"); ps.setString(8, "Pending"); ps.executeUpdate();
        }

        // --- 53. Tax Surcharges ---
        String taxSql = "INSERT INTO tax_surcharges (public_id, period_id, tax_type, jurisdiction, tax_rate, taxable_amount, tax_amount, description, overridden) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(taxSql)) {
            ps.setString(1, "tax-01"); ps.setString(2, "prd-001"); ps.setString(3, "StatePremiumTax"); ps.setString(4, "IL"); ps.setDouble(5, 3.5); ps.setDouble(6, 22400.0); ps.setDouble(7, 784.0); ps.setString(8, "Illinois Premium Tax"); ps.setInt(9, 0); ps.executeUpdate();
            ps.setString(1, "tax-02"); ps.setString(2, "prd-001"); ps.setString(3, "FireTax"); ps.setString(4, "IL"); ps.setDouble(5, 1.0); ps.setDouble(6, 12000.0); ps.setDouble(7, 120.0); ps.setString(8, "Illinois Fire Marshal Tax"); ps.setInt(9, 0); ps.executeUpdate();
            ps.setString(1, "tax-03"); ps.setString(2, "prd-001"); ps.setString(3, "StampingFee"); ps.setString(4, "IL"); ps.setDouble(5, 0.25); ps.setDouble(6, 22400.0); ps.setDouble(7, 56.0); ps.setString(8, "Stamping Bureau Fee"); ps.setInt(9, 0); ps.executeUpdate();
        }

        // --- 57. Claim Details ---
        String clmSql = "INSERT INTO claim_details (public_id, claim_number, policy_number, claim_status, loss_date, reported_date, loss_cause, loss_type, adjuster_name, paid_amount, reserve_amount, incurred_amount, subrogation, subrogation_status, litigation, litigation_status, fault_rating) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(clmSql)) {
            ps.setString(1, "cd-01"); ps.setString(2, "CLM-90021"); ps.setString(3, "CP-3451127"); ps.setString(4, "Closed"); ps.setString(5, "2025-11-14"); ps.setString(6, "2025-11-15");
            ps.setString(7, "Water Pipe Burst"); ps.setString(8, "PropertyDamage"); ps.setString(9, "Sarah Mitchell"); ps.setDouble(10, 12500.0); ps.setDouble(11, 0.0); ps.setDouble(12, 12500.0);
            ps.setInt(13, 1); ps.setString(14, "Recovered"); ps.setInt(15, 0); ps.setString(16, "None"); ps.setString(17, "NotAtFault"); ps.executeUpdate();
            ps.setString(1, "cd-02"); ps.setString(2, "CLM-94810"); ps.setString(3, "POL-3764124"); ps.setString(4, "Open"); ps.setString(5, "2026-03-02"); ps.setString(6, "2026-03-03");
            ps.setString(7, "Fender Collision"); ps.setString(8, "AutoCollision"); ps.setString(9, "James Rodriguez"); ps.setDouble(10, 3200.0); ps.setDouble(11, 4500.0); ps.setDouble(12, 7700.0);
            ps.setInt(13, 0); ps.setString(14, "None"); ps.setInt(15, 0); ps.setString(16, "None"); ps.setString(17, "Partial"); ps.executeUpdate();
        }

        // --- 58. Audit Schedules ---
        String auSql = "INSERT INTO audit_schedules (public_id, period_id, audit_type, audit_method, scheduled_date, status, estimated_premium) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(auSql)) {
            ps.setString(1, "as-01"); ps.setString(2, "prd-001"); ps.setString(3, "Annual"); ps.setString(4, "Physical"); ps.setString(5, "2027-02-15"); ps.setString(6, "Scheduled"); ps.setDouble(7, 22400.0); ps.executeUpdate();
        }

        // --- 59. Policy Holds ---
        String phSql = "INSERT INTO policy_holds (public_id, period_id, hold_type, reason, status, placed_date, blocks_renewal, blocks_endorsement, blocks_cancellation, blocks_reinstatement) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(phSql)) {
            ps.setString(1, "ph-01"); ps.setString(2, "prd-001"); ps.setString(3, "Claims"); ps.setString(4, "Open claim CLM-94810 with active reserves"); ps.setString(5, "Active"); ps.setString(6, "2026-03-03");
            ps.setInt(7, 0); ps.setInt(8, 0); ps.setInt(9, 1); ps.setInt(10, 0); ps.executeUpdate();
        }

        // --- 51. Rate Table Factors ---
        String rtfSql = "INSERT INTO rate_table_factors (public_id, rate_book_id, table_name, factor_name, lookup_key, factor_value) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(rtfSql)) {
            ps.setString(1, "rtf-01"); ps.setString(2, "rb-cp-2026"); ps.setString(3, "ConstructionType"); ps.setString(4, "Frame"); ps.setString(5, "Frame"); ps.setDouble(6, 1.25); ps.executeUpdate();
            ps.setString(1, "rtf-02"); ps.setString(2, "rb-cp-2026"); ps.setString(3, "ConstructionType"); ps.setString(4, "Joisted Masonry"); ps.setString(5, "JM"); ps.setDouble(6, 1.00); ps.executeUpdate();
            ps.setString(1, "rtf-03"); ps.setString(2, "rb-cp-2026"); ps.setString(3, "ConstructionType"); ps.setString(4, "Fire Resistive"); ps.setString(5, "FR"); ps.setDouble(6, 0.85); ps.executeUpdate();
            ps.setString(1, "rtf-04"); ps.setString(2, "rb-cp-2026"); ps.setString(3, "ProtectionClass"); ps.setString(4, "Class 1-3"); ps.setString(5, "1-3"); ps.setDouble(6, 0.90); ps.executeUpdate();
            ps.setString(1, "rtf-05"); ps.setString(2, "rb-cp-2026"); ps.setString(3, "ProtectionClass"); ps.setString(4, "Class 4-6"); ps.setString(5, "4-6"); ps.setDouble(6, 1.00); ps.executeUpdate();
            ps.setString(1, "rtf-06"); ps.setString(2, "rb-cp-2026"); ps.setString(3, "ProtectionClass"); ps.setString(4, "Class 7-10"); ps.setString(5, "7-10"); ps.setDouble(6, 1.35); ps.executeUpdate();
        }

        LOGGER.info("Seeded reference data for 15 new OOTB entities (tables 45-59).");
    }

    private void insertRole(PreparedStatement ps, String id, String name, String desc, String type, String perms, int approve, int bind, int cancel, int viewFin) throws SQLException {
        ps.setString(1, id); ps.setString(2, name); ps.setString(3, desc); ps.setString(4, type);
        ps.setString(5, perms); ps.setInt(6, approve); ps.setInt(7, bind); ps.setInt(8, cancel); ps.setInt(9, viewFin);
        ps.executeUpdate();
    }

    // --- Marketplace Accelerator: HazardIntelligence DAO Methods ---
    public void saveHazardIntelligence(HazardIntelligence hi) {
        String sql = "INSERT OR REPLACE INTO hazard_intelligence (id, location_id, building_id, address_line, wildfire_score, flood_zone, distance_to_coast_miles, roof_condition_score, hail_severity_index, risk_category, evaluated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hi.getId());
            pstmt.setString(2, hi.getLocationId());
            pstmt.setString(3, hi.getBuildingId());
            pstmt.setString(4, hi.getAddressLine());
            pstmt.setInt(5, hi.getWildfireScore());
            pstmt.setString(6, hi.getFloodZone());
            pstmt.setDouble(7, hi.getDistanceToCoastMiles());
            pstmt.setDouble(8, hi.getRoofConditionScore());
            pstmt.setString(9, hi.getHailSeverityIndex());
            pstmt.setString(10, hi.getRiskCategory());
            pstmt.setString(11, hi.getEvaluatedAt() != null ? hi.getEvaluatedAt() : java.time.LocalDateTime.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error saving HazardIntelligence", e);
        }
    }

    public HazardIntelligence getHazardIntelligenceByLocation(String locationId) {
        String sql = "SELECT * FROM hazard_intelligence WHERE location_id = ? OR id = ? ORDER BY evaluated_at DESC LIMIT 1";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, locationId);
            pstmt.setString(2, locationId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                HazardIntelligence hi = new HazardIntelligence(
                        rs.getString("id"), rs.getString("location_id"), rs.getString("building_id"),
                        rs.getString("address_line"), rs.getInt("wildfire_score"), rs.getString("flood_zone"),
                        rs.getDouble("distance_to_coast_miles"), rs.getDouble("roof_condition_score"),
                        rs.getString("hail_severity_index"), rs.getString("risk_category")
                );
                hi.setEvaluatedAt(rs.getString("evaluated_at"));
                return hi;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching HazardIntelligence by locationId: " + locationId, e);
        }
        return null;
    }

    public List<HazardIntelligence> getAllHazardIntelligence() {
        List<HazardIntelligence> list = new ArrayList<>();
        String sql = "SELECT * FROM hazard_intelligence ORDER BY wildfire_score DESC LIMIT 50";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HazardIntelligence hi = new HazardIntelligence(
                        rs.getString("id"), rs.getString("location_id"), rs.getString("building_id"),
                        rs.getString("address_line"), rs.getInt("wildfire_score"), rs.getString("flood_zone"),
                        rs.getDouble("distance_to_coast_miles"), rs.getDouble("roof_condition_score"),
                        rs.getString("hail_severity_index"), rs.getString("risk_category")
                );
                hi.setEvaluatedAt(rs.getString("evaluated_at"));
                list.add(hi);
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching all HazardIntelligence records", e);
        }
        return list;
    }

    // --- Marketplace Accelerator: ESignatureEnvelope DAO Methods ---
    public void saveESignatureEnvelope(ESignatureEnvelope env) {
        String sql = "INSERT OR REPLACE INTO esignature_envelopes (id, envelope_id, job_number, policy_number, signer_name, signer_email, document_type, status, sent_at, signed_at, document_id, download_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, env.getId());
            pstmt.setString(2, env.getEnvelopeId());
            pstmt.setString(3, env.getJobNumber());
            pstmt.setString(4, env.getPolicyNumber());
            pstmt.setString(5, env.getSignerName());
            pstmt.setString(6, env.getSignerEmail());
            pstmt.setString(7, env.getDocumentType());
            pstmt.setString(8, env.getStatus());
            pstmt.setString(9, env.getSentAt());
            pstmt.setString(10, env.getSignedAt());
            pstmt.setString(11, env.getDocumentId());
            pstmt.setString(12, env.getDownloadUrl());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error saving ESignatureEnvelope", e);
        }
    }

    public ESignatureEnvelope getESignatureEnvelopeById(String envelopeId) {
        String sql = "SELECT * FROM esignature_envelopes WHERE envelope_id = ? OR id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, envelopeId);
            pstmt.setString(2, envelopeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ESignatureEnvelope env = new ESignatureEnvelope(
                        rs.getString("id"), rs.getString("envelope_id"), rs.getString("job_number"),
                        rs.getString("policy_number"), rs.getString("signer_name"), rs.getString("signer_email"),
                        rs.getString("document_type"), rs.getString("status"), rs.getString("sent_at")
                );
                env.setSignedAt(rs.getString("signed_at"));
                env.setDocumentId(rs.getString("document_id"));
                env.setDownloadUrl(rs.getString("download_url"));
                return env;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching ESignatureEnvelope by envelopeId: " + envelopeId, e);
        }
        return null;
    }

    public List<ESignatureEnvelope> getAllESignatureEnvelopes() {
        List<ESignatureEnvelope> list = new ArrayList<>();
        String sql = "SELECT * FROM esignature_envelopes ORDER BY sent_at DESC LIMIT 50";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ESignatureEnvelope env = new ESignatureEnvelope(
                        rs.getString("id"), rs.getString("envelope_id"), rs.getString("job_number"),
                        rs.getString("policy_number"), rs.getString("signer_name"), rs.getString("signer_email"),
                        rs.getString("document_type"), rs.getString("status"), rs.getString("sent_at")
                );
                env.setSignedAt(rs.getString("signed_at"));
                env.setDocumentId(rs.getString("document_id"));
                env.setDownloadUrl(rs.getString("download_url"));
                list.add(env);
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching all ESignatureEnvelopes", e);
        }
        return list;
    }

    public void saveDocument(Document doc) {
        String sql = "INSERT OR REPLACE INTO documents (public_id, account_number, job_id, name, doc_type, mime_type, status, url, date_created) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doc.getPublicID());
            pstmt.setString(2, doc.getAccountNumber());
            pstmt.setString(3, doc.getJobID());
            pstmt.setString(4, doc.getName());
            pstmt.setString(5, doc.getDocType());
            pstmt.setString(6, doc.getMimeType());
            pstmt.setString(7, doc.getStatus());
            pstmt.setString(8, doc.getUrl());
            pstmt.setString(9, doc.getDateCreated());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error saving Document", e);
        }
    }

    public Document getDocumentById(String docId) {
        String sql = "SELECT * FROM documents WHERE public_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, docId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Document(
                        rs.getString("public_id"), rs.getString("account_number"), rs.getString("job_id"),
                        rs.getString("name"), rs.getString("doc_type"), rs.getString("mime_type"),
                        rs.getString("status"), rs.getString("url"), rs.getString("date_created")
                );
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching Document by id: " + docId, e);
        }
        return null;
    }
}

