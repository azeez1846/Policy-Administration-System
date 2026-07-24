package com.policycenter.controller;

import com.policycenter.model.Account;
import com.policycenter.model.AccountLocation;
import com.policycenter.model.Contact;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    @GetMapping
    public List<Account> getAllAccounts() {
        return repository.getAllAccounts();
    }

    @GetMapping("/{accountNumber}")
    public Account getAccount(@PathVariable String accountNumber) {
        return repository.getAccount(accountNumber);
    }

    @PostMapping
    public Account createAccount(@RequestBody Map<String, String> payload) {
        String companyName = payload.getOrDefault("companyName", payload.getOrDefault("name", "New Insured Company"));
        String industryCode = payload.getOrDefault("industryCode", "Commercial");
        String taxID = payload.getOrDefault("taxID", "00-0000000");
        String address = payload.getOrDefault("addressLine1", "100 Main St");
        String city = payload.getOrDefault("city", "Chicago");
        String state = payload.getOrDefault("state", "IL");
        String zip = payload.getOrDefault("postalCode", "60601");
        String email = payload.getOrDefault("email", "info@company.com");
        String phone = payload.getOrDefault("phone", "(555) 019-2831");

        String accNum = payload.get("accountNumber");
        if (accNum == null || accNum.isEmpty()) {
            accNum = "C" + (int)(10000000 + Math.random() * 90000000);
        }

        String contactId = "cont-" + System.currentTimeMillis();
        Contact holder = new Contact(contactId, companyName, "Company", email, phone);
        holder.setCompanyName(companyName);
        holder.setTaxID(taxID);
        holder.setAddressLine1(address);
        holder.setCity(city);
        holder.setState(state);
        holder.setPostalCode(zip);

        String accId = "acc-" + System.currentTimeMillis();
        Account account = new Account(accId, accNum, holder, industryCode);
        account.setAccountStatus("Active");

        AccountLocation loc = new AccountLocation("loc-" + System.currentTimeMillis(), 1, "Primary Location", address, city, state, zip);
        account.getLocations().add(loc);

        repository.saveAccount(account);
        repository.saveAccountLocation(accNum, loc);

        return account;
    }
}
