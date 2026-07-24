package com.policycenter.test;

import com.policycenter.repository.PolicyCenterSqliteRepository;
import java.util.Map;

public class VerifyOOTBEntities {
    public static void main(String[] args) {
        System.out.println("Executing OOTB Guidewire PolicyCenter Entity Verification Test...");
        PolicyCenterSqliteRepository repo = PolicyCenterSqliteRepository.getInstance();

        Map<String, Integer> counts = repo.getEntityCountsMap();
        System.out.println("\n------------------------------------------------------------");
        System.out.println(" OOTB Guidewire PolicyCenter Sandbox Entity Table Record Counts:");
        System.out.println("------------------------------------------------------------");

        int totalTables = 0;
        int totalRecords = 0;

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            System.out.printf(" %-28s : %d records\n", entry.getKey(), entry.getValue());
            totalTables++;
            totalRecords += entry.getValue();
        }

        System.out.println("------------------------------------------------------------");
        System.out.printf(" Total OOTB Tables Verified : %d\n", totalTables);
        System.out.printf(" Total Seed Records         : %d\n", totalRecords);
        System.out.println("------------------------------------------------------------");

        if (totalTables == 26) {
            System.out.println("SUCCESS: All 26 OOTB PolicyCenter Sandbox tables verified clean!");
        } else {
            System.err.println("FAILURE: Table count mismatch! Expected 26, found " + totalTables);
            System.exit(1);
        }
    }
}
