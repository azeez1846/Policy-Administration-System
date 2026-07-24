package com.policycenter.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.policycenter.gs.classes.batch.*;
import com.policycenter.gs.classes.billing.*;
import com.policycenter.gs.classes.claims.*;
import com.policycenter.gs.classes.forms.*;
import com.policycenter.gs.classes.job.*;
import com.policycenter.gs.classes.rating.*;
import com.policycenter.gs.classes.reinsurance.*;
import com.policycenter.gs.classes.rules.*;
import com.policycenter.model.*;
import com.policycenter.repository.PolicyCenterSqliteRepository;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PolicyCenterServer {

    private static final int PORT = 8080;
    private static final PolicyCenterSqliteRepository repository = PolicyCenterSqliteRepository.getInstance();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/api/auth/login", new AuthHandler());
        server.createContext("/api/accounts", new AccountHandler());
        server.createContext("/api/jobs", new JobHandler());
        server.createContext("/api/policies/history", new PolicyHistoryHandler());
        server.createContext("/api/policies", new PolicyHandler());
        server.createContext("/api/entities", new EntitiesHandler());
        server.createContext("/api/uw", new UWHandler());
        server.createContext("/api/documents", new DocumentHandler());
        server.createContext("/api/billing", new BillingHandler());
        server.createContext("/api/batch", new BatchHandler());
        server.createContext("/api/claims", new ClaimsHandler());
        server.createContext("/api/rating", new RatingHandler());
        server.createContext("/api/reinsurance", new ReinsuranceHandler());
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(10));
        System.out.println("=================================================================");
        System.out.println(" Guidewire PolicyCenter Server started on http://localhost:" + PORT);
        System.out.println("=================================================================");
        server.start();
    }

    static class AuthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String username = extractJsonField(body, "username");
                String password = extractJsonField(body, "password");

                User user = repository.getUserByUsername(username);
                if (user != null && user.getPassword().equals(password)) {
                    String token = "token-" + user.getUsername() + "-" + System.currentTimeMillis();
                    String json = String.format(
                        "{\"status\":\"success\",\"token\":\"%s\",\"user\":{\"username\":\"%s\",\"fullName\":\"%s\",\"role\":\"%s\",\"producerCode\":\"%s\"}}",
                        token, user.getUsername(), esc(user.getFullName()), user.getRole(), user.getProducerCode()
                    );
                    sendJsonResponse(exchange, 200, json);
                } else {
                    sendJsonResponse(exchange, 401, "{\"status\":\"error\",\"message\":\"Invalid LDAP / System credentials\"}");
                }
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    static class AccountHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                Collection<Account> accounts = repository.getAllAccounts();
                sendJsonResponse(exchange, 200, toJsonAccounts(accounts));
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                Account account = parseAccountJson(body);
                repository.saveAccount(account);
                sendJsonResponse(exchange, 201, toJsonAccount(account));
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    static class JobHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if (path.endsWith("/quote") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                Job job = repository.getJob(jobNumber);

                if (job == null) {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                    return;
                }

                // If updated building limits provided in request, update period
                updatePeriodFromQuoteRequest(job.getPolicyPeriod(), body);

                SubmissionProcess process = new SubmissionProcess(job);
                try {
                    process.requestQuote();
                    repository.saveJob(job);
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } catch (Exception e) {
                    sendJsonResponse(exchange, 400, "{\"error\": \"" + e.getMessage() + "\"}");
                }
                return;
            }

            if (path.endsWith("/bind") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                Job job = repository.getJob(jobNumber);

                if (job == null) {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                    return;
                }

                SubmissionProcess process = new SubmissionProcess(job);
                try {
                    process.bindAndIssue();
                    repository.saveJob(job);
                    repository.savePolicyPeriod(job.getPolicyPeriod());
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } catch (Exception e) {
                    sendJsonResponse(exchange, 400, "{\"error\": \"" + e.getMessage() + "\"}");
                }
                return;
            }

            if (path.endsWith("/endorse") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                Job job = repository.getJob(jobNumber);
                if (job != null) {
                    PolicyChangeProcess pcp = new PolicyChangeProcess(job);
                    pcp.bindPolicyChange();
                    repository.saveJob(job);
                    repository.savePolicyPeriod(job.getPolicyPeriod());
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                }
                return;
            }

            if (path.endsWith("/renew") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                Job job = repository.getJob(jobNumber);
                if (job != null) {
                    RenewalProcess rp = new RenewalProcess(job);
                    rp.bindRenewal();
                    repository.saveJob(job);
                    repository.savePolicyPeriod(job.getPolicyPeriod());
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                }
                return;
            }

            if (path.endsWith("/cancel") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                String cancelType = extractJsonField(body, "cancelType");
                String reason = extractJsonField(body, "reason");
                Job job = repository.getJob(jobNumber);
                if (job != null) {
                    CancellationProcess cp = new CancellationProcess(job);
                    cp.cancelPolicy(cancelType == null ? "ProRata" : cancelType, null, reason == null ? "Customer Request" : reason);
                    repository.saveJob(job);
                    repository.savePolicyPeriod(job.getPolicyPeriod());
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                }
                return;
            }

            if (path.endsWith("/reinstate") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                Job job = repository.getJob(jobNumber);
                if (job != null) {
                    ReinstatementProcess rp = new ReinstatementProcess(job);
                    rp.reinstatePolicy("Payment Received / UW Approved");
                    repository.saveJob(job);
                    repository.savePolicyPeriod(job.getPolicyPeriod());
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                }
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                Collection<Job> jobs = repository.getAllJobs();
                sendJsonResponse(exchange, 200, toJsonJobs(jobs));
            } else if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                Job newJob = createNewSubmissionFromJson(body);
                repository.saveJob(newJob);
                sendJsonResponse(exchange, 201, toJsonJob(newJob));
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    static class PolicyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Collection<PolicyPeriod> policies = repository.getAllPolicies();
                sendJsonResponse(exchange, 200, toJsonPolicies(policies));
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String pathStr = exchange.getRequestURI().getPath();
            if ("/".equals(pathStr)) pathStr = "/index.html";

            Path filePath = Paths.get("frontend" + pathStr);
            if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
                filePath = Paths.get("frontend/index.html");
            }

            if (Files.exists(filePath)) {
                byte[] bytes = Files.readAllBytes(filePath);
                String mime = getMimeType(filePath.toString());
                exchange.getResponseHeaders().set("Content-Type", mime);
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } else {
                sendResponse(exchange, 404, "404 Not Found");
            }
        }
    }

    // JSON Helper Utilities
    private static void sendJsonResponse(HttpExchange exchange, int status, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private static String getMimeType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        return "text/plain";
    }

    private static String extractJsonField(String json, String field) {
        String key = "\"" + field + "\"";
        int idx = json.indexOf(key);
        if (idx == -1) return "";
        int start = json.indexOf(":", idx) + 1;
        int end1 = json.indexOf(",", start);
        int end2 = json.indexOf("}", start);
        int end = (end1 != -1 && end1 < end2) ? end1 : end2;
        if (end == -1) end = json.length();
        String val = json.substring(start, end).trim();
        if (val.startsWith("\"") && val.endsWith("\"")) {
            val = val.substring(1, val.length() - 1);
        }
        return val;
    }

    private static Account parseAccountJson(String body) {
        String accNum = "C" + System.currentTimeMillis() % 100000000L;
        String compName = extractJsonField(body, "companyName");
        if (compName.isEmpty()) compName = "New Insured Enterprise";
        
        Contact contact = new Contact("cont-" + System.currentTimeMillis(), "Company", compName, extractJsonField(body, "email"), extractJsonField(body, "phone"));
        contact.setCompanyName(compName);
        contact.setTaxID(extractJsonField(body, "taxID"));
        contact.setAddressLine1(extractJsonField(body, "addressLine1"));
        contact.setCity(extractJsonField(body, "city"));
        contact.setState(extractJsonField(body, "state"));
        contact.setPostalCode(extractJsonField(body, "postalCode"));

        return new Account("acc-" + System.currentTimeMillis(), accNum, contact, extractJsonField(body, "industryCode"));
    }

    private static Job createNewSubmissionFromJson(String body) {
        String accNum = extractJsonField(body, "accountNumber");
        Account account = repository.getAccount(accNum);
        if (account == null) {
            account = repository.getAllAccounts().iterator().next();
        }

        String productCode = extractJsonField(body, "productCode");
        if (productCode.isEmpty()) productCode = "CommercialProperty";

        PolicyPeriod period = new PolicyPeriod();
        period.setPublicID("period-" + System.currentTimeMillis());
        period.setPeriodID(String.valueOf(System.currentTimeMillis() % 100000L));
        period.setProductCode(productCode);
        period.setProductName("Commercial Property".equalsIgnoreCase(productCode) ? "Commercial Property" : "Personal Auto");
        period.setAccount(account);
        period.setPrimaryNamedInsured(account.getAccountHolder());

        PolicyLine propLine = new PolicyLine("line-" + System.currentTimeMillis(), productCode + "Line", period.getProductName() + " Line");
        
        String bldgDesc = extractJsonField(body, "buildingDescription");
        if (bldgDesc.isEmpty()) bldgDesc = "Commercial Facility #1";

        String constType = extractJsonField(body, "constructionType");
        if (constType.isEmpty()) constType = "Joisted Masonry";

        double bldgLimit = 1000000.0;
        try { bldgLimit = Double.parseDouble(extractJsonField(body, "buildingLimit")); } catch (NumberFormatException | NullPointerException ignored) {}

        double cntLimit = 250000.0;
        try { cntLimit = Double.parseDouble(extractJsonField(body, "contentsLimit")); } catch (NumberFormatException | NullPointerException ignored) {}

        Building bldg = new Building("bldg-" + System.currentTimeMillis(), 1, bldgDesc, constType, 2018, bldgLimit, cntLimit);
        propLine.addBuilding(bldg);
        period.addLine(propLine);

        String jobNum = "SUB-" + (System.currentTimeMillis() % 100000L);
        return new Job("job-" + System.currentTimeMillis(), jobNum, "Submission", period);
    }

    private static void updatePeriodFromQuoteRequest(PolicyPeriod period, String body) {
        if (period.getLines().isEmpty()) return;
        PolicyLine line = period.getLines().get(0);
        if (line.getBuildings().isEmpty()) return;

        Building bldg = line.getBuildings().get(0);
        String constType = extractJsonField(body, "constructionType");
        if (!constType.isEmpty()) bldg.setConstructionType(constType);

        try {
            String bl = extractJsonField(body, "buildingLimit");
            if (!bl.isEmpty()) bldg.setBuildingLimit(Double.parseDouble(bl));
        } catch (NumberFormatException | NullPointerException ignored) {}

        try {
            String cl = extractJsonField(body, "contentsLimit");
            if (!cl.isEmpty()) bldg.setContentsLimit(Double.parseDouble(cl));
        } catch (NumberFormatException | NullPointerException ignored) {}
    }

    // Manual JSON Serialization
    private static String toJsonAccounts(Collection<Account> accounts) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Account a : accounts) {
            if (!first) sb.append(",");
            sb.append(toJsonAccount(a));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    private static String toJsonAccount(Account a) {
        return String.format(
            "{\"publicID\":\"%s\",\"accountNumber\":\"%s\",\"accountStatus\":\"%s\",\"industryCode\":\"%s\",\"originationDate\":\"%s\",\"accountHolder\":%s}",
            a.getPublicID(), a.getAccountNumber(), a.getAccountStatus(), a.getIndustryCode(), a.getOriginationDate(), toJsonContact(a.getAccountHolder())
        );
    }

    private static String toJsonContact(Contact c) {
        if (c == null) return "null";
        return String.format(
            "{\"publicID\":\"%s\",\"name\":\"%s\",\"companyName\":\"%s\",\"taxID\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"addressLine1\":\"%s\",\"city\":\"%s\",\"state\":\"%s\",\"postalCode\":\"%s\"}",
            c.getPublicID(), esc(c.getName()), esc(c.getCompanyName()), esc(c.getTaxID()), esc(c.getEmail()), esc(c.getPhone()),
            esc(c.getAddressLine1()), esc(c.getCity()), esc(c.getState()), esc(c.getPostalCode())
        );
    }

    private static String toJsonJobs(Collection<Job> jobs) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Job j : jobs) {
            if (!first) sb.append(",");
            sb.append(toJsonJob(j));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    private static String toJsonJob(Job j) {
        return String.format(
            "{\"publicID\":\"%s\",\"jobNumber\":\"%s\",\"jobType\":\"%s\",\"closeDate\":\"%s\",\"policyPeriod\":%s}",
            j.getPublicID(), j.getJobNumber(), j.getJobType(), j.getCloseDate() == null ? "" : j.getCloseDate(), toJsonPolicyPeriod(j.getPolicyPeriod())
        );
    }

    private static String toJsonPolicies(Collection<PolicyPeriod> policies) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (PolicyPeriod p : policies) {
            if (!first) sb.append(",");
            sb.append(toJsonPolicyPeriod(p));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    private static String toJsonPolicyPeriod(PolicyPeriod p) {
        if (p == null) return "null";
        StringBuilder costsSb = new StringBuilder("[");
        if (p.getCosts() != null) {
            boolean f = true;
            for (Cost c : p.getCosts()) {
                if (!f) costsSb.append(",");
                costsSb.append(String.format("{\"costType\":\"%s\",\"description\":\"%s\",\"actualAmount\":%.2f}", c.getCostType(), esc(c.getDescription()), c.getActualAmount()));
                f = false;
            }
        }
        costsSb.append("]");

        StringBuilder bldgSb = new StringBuilder("[");
        if (!p.getLines().isEmpty() && !p.getLines().get(0).getBuildings().isEmpty()) {
            boolean f = true;
            for (Building b : p.getLines().get(0).getBuildings()) {
                if (!f) bldgSb.append(",");
                bldgSb.append(String.format("{\"buildingNum\":%d,\"description\":\"%s\",\"constructionType\":\"%s\",\"buildingLimit\":%.2f,\"contentsLimit\":%.2f}", b.getBuildingNum(), esc(b.getDescription()), b.getConstructionType(), b.getBuildingLimit(), b.getContentsLimit()));
                f = false;
            }
        }
        bldgSb.append("]");

        return String.format(
            "{\"periodID\":\"%s\",\"status\":\"%s\",\"productCode\":\"%s\",\"productName\":\"%s\",\"policyNumber\":\"%s\",\"effectiveDate\":\"%s\",\"expirationDate\":\"%s\",\"totalPremium\":%.2f,\"taxAndFees\":%.2f,\"totalCost\":%.2f,\"account\":%s,\"primaryNamedInsured\":%s,\"buildings\":%s,\"costs\":%s}",
            p.getPeriodID(), p.getStatus(), p.getProductCode(), p.getProductName(), p.getPolicyNumber() == null ? "" : p.getPolicyNumber(),
            p.getEffectiveDate(), p.getExpirationDate(), p.getTotalPremium(), p.getTaxAndFees(), p.getTotalCost(),
            toJsonAccount(p.getAccount()), toJsonContact(p.getPrimaryNamedInsured()), bldgSb.toString(), costsSb.toString()
        );
    }

    static class EntitiesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 204, "");
                return;
            }
            Map<String, Integer> counts = repository.getEntityCountsMap();
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                if (!first) sb.append(",");
                sb.append(String.format("\"%s\":%d", entry.getKey(), entry.getValue()));
                first = false;
            }
            sb.append("}");
            sendResponse(exchange, 200, sb.toString());
        }
    }

    static class UWHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if (path.endsWith("/evaluate") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                Job job = repository.getJob(jobNumber);
                if (job != null && job.getPolicyPeriod() != null) {
                    UWRulesEngine.evaluatePeriodRules(job.getPolicyPeriod());
                    repository.saveJob(job);
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                }
                return;
            }

            if (path.endsWith("/approve-issue") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                String issueKey = extractJsonField(body, "issueKey");
                Job job = repository.getJob(jobNumber);
                if (job != null && job.getPolicyPeriod() != null) {
                    UWRulesEngine.approveIssue(job.getPolicyPeriod(), issueKey, "su");
                    repository.saveJob(job);
                    sendJsonResponse(exchange, 200, toJsonJob(job));
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job or issue not found\"}");
                }
                return;
            }

            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    static class DocumentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String jobNumber = "SUB-5001";
            if (query != null && query.contains("job=")) {
                jobNumber = query.split("job=")[1].split("&")[0];
            }

            Job job = repository.getJob(jobNumber);
            PolicyPeriod period = job != null ? job.getPolicyPeriod() : null;
            if (period != null) {
                FormsInferenceEngine.inferPolicyForms(period);
            }

            if (path.endsWith("/dec-page")) {
                String html = DocumentGenerator.generatePolicyDecPageHtml(period);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                sendResponse(exchange, 200, html);
                return;
            }

            sendResponse(exchange, 404, "Document not found");
        }
    }

    static class BillingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if (path.endsWith("/installments") && "POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String jobNumber = extractJsonField(body, "jobNumber");
                String plan = extractJsonField(body, "plan");
                Job job = repository.getJob(jobNumber);
                if (job != null && job.getPolicyPeriod() != null) {
                    double totalCost = job.getPolicyPeriod().getTotalCost();
                    List<BillingSimulator.Installment> insts = BillingSimulator.generateInstallmentSchedule(totalCost, plan, job.getPolicyPeriod().getEffectiveDate());
                    double comm = BillingSimulator.calculateProducerCommission(totalCost, 0.15);

                    StringBuilder sb = new StringBuilder("{");
                    sb.append(String.format("\"totalCost\":%.2f,\"commission\":%.2f,\"plan\":\"%s\",\"installments\":[", totalCost, comm, plan));
                    for (int i = 0; i < insts.size(); i++) {
                        BillingSimulator.Installment ins = insts.get(i);
                        if (i > 0) sb.append(",");
                        sb.append(String.format("{\"num\":%d,\"date\":\"%s\",\"amount\":%.2f,\"desc\":\"%s\"}", ins.installmentNum, ins.dueDate, ins.amount, esc(ins.description)));
                    }
                    sb.append("]}");
                    sendJsonResponse(exchange, 200, sb.toString());
                } else {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Job not found\"}");
                }
                return;
            }

            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    static class BatchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equalsIgnoreCase(method) && path.endsWith("/jobs")) {
                List<BatchJob> jobs = BatchSchedulerEngine.getAllBatchJobs();
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < jobs.size(); i++) {
                    BatchJob j = jobs.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("{\"jobID\":\"%s\",\"processType\":\"%s\",\"status\":\"%s\",\"lastRunTime\":\"%s\",\"processedCount\":%d,\"failedCount\":%d}",
                        j.getJobID(), esc(j.getProcessType()), j.getStatus(), j.getLastRunTime(), j.getProcessedCount(), j.getFailedCount()));
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if ("POST".equalsIgnoreCase(method) && path.endsWith("/run")) {
                String body = readRequestBody(exchange);
                String processType = extractJsonField(body, "processType");
                if (processType.isEmpty()) processType = "RenewalNoticeBatch";
                BatchJob bj = BatchSchedulerEngine.runBatchJob(processType);
                String json = String.format("{\"jobID\":\"%s\",\"processType\":\"%s\",\"status\":\"%s\",\"lastRunTime\":\"%s\",\"processedCount\":%d,\"failedCount\":%d}",
                    bj.getJobID(), esc(bj.getProcessType()), bj.getStatus(), bj.getLastRunTime(), bj.getProcessedCount(), bj.getFailedCount());
                sendJsonResponse(exchange, 200, json);
                return;
            }

            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    static class ClaimsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                String query = exchange.getRequestURI().getQuery();
                String accountNumber = "ACC-1001";
                double earnedPremium = 2400.00;

                if (query != null) {
                    for (String param : query.split("&")) {
                        String[] kv = param.split("=");
                        if (kv.length == 2) {
                            if ("accountNumber".equals(kv[0])) accountNumber = kv[1];
                            if ("earnedPremium".equals(kv[0])) {
                                try { earnedPremium = Double.parseDouble(kv[1]); } catch (NumberFormatException | NullPointerException ignored) {}
                            }
                        }
                    }
                }

                List<Claim> claims = ClaimEngine.getClaimsForAccount(accountNumber);
                double lossRatio = ClaimEngine.calculate3YearLossRatio(accountNumber, earnedPremium);

                StringBuilder sb = new StringBuilder("{");
                sb.append(String.format("\"accountNumber\":\"%s\",\"lossRatioPercentage\":%.1f,\"claims\":[", accountNumber, lossRatio));
                for (int i = 0; i < claims.size(); i++) {
                    Claim c = claims.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("{\"publicID\":\"%s\",\"claimNumber\":\"%s\",\"accountNumber\":\"%s\",\"lossDate\":\"%s\",\"lossCause\":\"%s\",\"totalPaid\":%.2f,\"reserveAmount\":%.2f}",
                        c.getPublicID(), c.getClaimNumber(), c.getAccountNumber(), c.getLossDate(), esc(c.getCauseOfLoss()), c.getTotalPaid(), c.getReserveAmount()));
                }
                sb.append("]}");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if ("POST".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String accNum = extractJsonField(body, "accountNumber");
                String cause = extractJsonField(body, "lossCause");
                String lossDate = extractJsonField(body, "lossDate");
                double paid = 0.0, res = 0.0;
                try { paid = Double.parseDouble(extractJsonField(body, "totalPaid")); } catch (NumberFormatException | NullPointerException ignored) {}
                try { res = Double.parseDouble(extractJsonField(body, "reserveAmount")); } catch (NumberFormatException | NullPointerException ignored) {}

                Claim c = new Claim("clm-" + System.currentTimeMillis(), "CLM-" + (800000 + (int)(Math.random() * 90000)),
                    accNum.isEmpty() ? "ACC-1001" : accNum, lossDate.isEmpty() ? "2026-01-01" : lossDate, cause.isEmpty() ? "Property Loss" : cause, paid, res);
                ClaimEngine.addClaim(c);
                String json = String.format("{\"publicID\":\"%s\",\"claimNumber\":\"%s\",\"accountNumber\":\"%s\",\"lossDate\":\"%s\",\"lossCause\":\"%s\",\"totalPaid\":%.2f,\"reserveAmount\":%.2f}",
                    c.getPublicID(), c.getClaimNumber(), c.getAccountNumber(), c.getLossDate(), esc(c.getCauseOfLoss()), c.getTotalPaid(), c.getReserveAmount());
                sendJsonResponse(exchange, 200, json);
                return;
            }

            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    static class RatingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                List<RateTableFactor> factors = RatingStudioEngine.getAllFactors();
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < factors.size(); i++) {
                    RateTableFactor f = factors.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("{\"factorID\":\"%s\",\"productCode\":\"%s\",\"tableCode\":\"%s\",\"paramKey\":\"%s\",\"description\":\"%s\",\"factorValue\":%.2f}",
                        f.getFactorID(), f.getLineCode(), f.getTableCode(), f.getParamKey(), esc(f.getParamValue()), f.getFactorValue()));
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if ("PUT".equalsIgnoreCase(method)) {
                String body = readRequestBody(exchange);
                String factorID = extractJsonField(body, "factorID");
                double factorVal = 1.0;
                try { factorVal = Double.parseDouble(extractJsonField(body, "factorValue")); } catch (NumberFormatException | NullPointerException ignored) {}

                RateTableFactor f = RatingStudioEngine.updateFactor(factorID, factorVal);
                String json = String.format("{\"factorID\":\"%s\",\"productCode\":\"%s\",\"tableCode\":\"%s\",\"paramKey\":\"%s\",\"description\":\"%s\",\"factorValue\":%.2f}",
                    f.getFactorID(), f.getLineCode(), f.getTableCode(), f.getParamKey(), esc(f.getParamValue()), f.getFactorValue());
                sendJsonResponse(exchange, 200, json);
                return;
            }

            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    static class ReinsuranceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equalsIgnoreCase(method) && path.endsWith("/treaties")) {
                List<ReinsuranceTreaty> treaties = ReinsuranceEngine.getAllTreaties();
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < treaties.size(); i++) {
                    ReinsuranceTreaty t = treaties.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("{\"treatyID\":\"%s\",\"treatyName\":\"%s\",\"treatyType\":\"%s\",\"cededPercentage\":%.1f,\"attachmentPoint\":%.2f,\"reinsurerName\":\"%s\"}",
                        t.getTreatyID(), esc(t.getTreatyName()), t.getTreatyType(), t.getCededPercentage(), t.getAttachmentPoint(), esc(t.getReinsurerName())));
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if ("POST".equalsIgnoreCase(method) && path.endsWith("/calculate-cession")) {
                String body = readRequestBody(exchange);
                double grossPrem = 2400.00;
                double bldgLimit = 1000000.00;
                try { grossPrem = Double.parseDouble(extractJsonField(body, "grossPremium")); } catch (NumberFormatException | NullPointerException ignored) {}
                try { bldgLimit = Double.parseDouble(extractJsonField(body, "buildingLimit")); } catch (NumberFormatException | NullPointerException ignored) {}

                Map<String, Object> map = ReinsuranceEngine.calculateCession(grossPrem, bldgLimit);
                double totalCededPercentage = Double.parseDouble(map.get("totalCededPercentage").toString());
                double cededPremium = Double.parseDouble(map.get("cededPremium").toString());
                double netRetainedPremium = Double.parseDouble(map.get("netRetainedPremium").toString());

                List<ReinsuranceTreaty> treaties = ReinsuranceEngine.getAllTreaties();
                StringBuilder treatiesSb = new StringBuilder("[");
                for (int i = 0; i < treaties.size(); i++) {
                    ReinsuranceTreaty t = treaties.get(i);
                    if (i > 0) treatiesSb.append(",");
                    treatiesSb.append(String.format("{\"treatyID\":\"%s\",\"treatyName\":\"%s\",\"treatyType\":\"%s\",\"cededPercentage\":%.1f,\"attachmentPoint\":%.2f,\"reinsurerName\":\"%s\"}",
                        t.getTreatyID(), esc(t.getTreatyName()), t.getTreatyType(), t.getCededPercentage(), t.getAttachmentPoint(), esc(t.getReinsurerName())));
                }
                treatiesSb.append("]");

                String json = String.format("{\"grossPremium\":%.2f,\"buildingLimit\":%.2f,\"totalCededPercentage\":%.1f,\"cededPremium\":%.2f,\"netRetainedPremium\":%.2f,\"treaties\":%s}",
                    grossPrem, bldgLimit, totalCededPercentage, cededPremium, netRetainedPremium, treatiesSb.toString());
                sendJsonResponse(exchange, 200, json);
                return;
            }

            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    static class PolicyHistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                sendResponse(exchange, 204, "");
                return;
            }

            if ("GET".equalsIgnoreCase(method)) {
                String query = exchange.getRequestURI().getQuery();
                String policyNumber = "POL-88201";
                if (query != null && query.contains("policyNumber=")) {
                    policyNumber = query.split("policyNumber=")[1].split("&")[0];
                }
                List<PolicyVersion> versions = OOSEngine.getHistoryForPolicy(policyNumber);
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < versions.size(); i++) {
                    PolicyVersion v = versions.get(i);
                    if (i > 0) sb.append(",");
                    sb.append(String.format("{\"versionID\":\"%s\",\"policyNumber\":\"%s\",\"sequenceNumber\":%d,\"effectiveDate\":\"%s\",\"jobType\":\"%s\",\"description\":\"%s\",\"oos\":%b}",
                        v.getVersionID(), v.getPolicyNumber(), v.getSequenceNumber(), v.getEffectiveDate(), v.getJobType(), esc(v.getDescription()), v.isOOS()));
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if ("POST".equalsIgnoreCase(method) && path.endsWith("/oos-endorse")) {
                String body = readRequestBody(exchange);
                String policyNumber = extractJsonField(body, "policyNumber");
                String effDate = extractJsonField(body, "effectiveDate");
                String description = extractJsonField(body, "description");

                PolicyVersion v = OOSEngine.executeOOSEndorsement(policyNumber, effDate, description);
                String json = String.format("{\"versionID\":\"%s\",\"policyNumber\":\"%s\",\"sequenceNumber\":%d,\"effectiveDate\":\"%s\",\"jobType\":\"%s\",\"description\":\"%s\",\"oos\":%b}",
                    v.getVersionID(), v.getPolicyNumber(), v.getSequenceNumber(), v.getEffectiveDate(), v.getJobType(), esc(v.getDescription()), v.isOOS());
                sendJsonResponse(exchange, 200, json);
                return;
            }

            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private static String esc(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", " ");
    }
}
