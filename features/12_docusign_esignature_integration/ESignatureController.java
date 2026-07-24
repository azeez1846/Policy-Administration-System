package com.policycenter.controller;

import com.policycenter.model.Document;
import com.policycenter.model.ESignatureEnvelope;
import com.policycenter.repository.PolicyCenterSqliteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Guidewire Marketplace Accelerator Controller: DocuSign E-Signature Integration
 */
@RestController
@RequestMapping("/api/esignature")
@CrossOrigin(origins = "*")
public class ESignatureController {

    private final PolicyCenterSqliteRepository repo = PolicyCenterSqliteRepository.getInstance();

    @PostMapping("/send")
    public Map<String, Object> sendEnvelope(@RequestBody Map<String, String> payload) {
        String id = "env-" + (1000 + new Random().nextInt(9000));
        String envelopeId = "docusign-uuid-" + UUID.randomUUID().toString().substring(0, 8);
        String jobNumber = payload.getOrDefault("jobNumber", "SUB-5001");
        String policyNumber = payload.getOrDefault("policyNumber", "POL-3451127");
        String signerName = payload.getOrDefault("signerName", "Jane Doe");
        String signerEmail = payload.getOrDefault("signerEmail", "jane.doe@acmelogistics.com");
        String documentType = payload.getOrDefault("documentType", "ACORD 125 Policy Binder");

        String now = java.time.LocalDateTime.now().toString();

        ESignatureEnvelope envelope = new ESignatureEnvelope(
                id, envelopeId, jobNumber, policyNumber, signerName, signerEmail, documentType, "Sent", now
        );
        envelope.setDownloadUrl("/api/esignature/document/" + envelopeId);

        repo.saveESignatureEnvelope(envelope);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "E-Signature envelope successfully dispatched to signer: " + signerEmail);
        response.put("envelope", envelope);
        return response;
    }

    @GetMapping("/status/{envelopeId}")
    public Map<String, Object> getEnvelopeStatus(@PathVariable String envelopeId) {
        Map<String, Object> response = new HashMap<>();
        ESignatureEnvelope env = repo.getESignatureEnvelopeById(envelopeId);
        if (env != null) {
            // Simulate progression if envelope is still Sent
            if ("Sent".equalsIgnoreCase(env.getStatus())) {
                env.setStatus("Delivered");
                repo.saveESignatureEnvelope(env);
            }
            response.put("found", true);
            response.put("envelope", env);
        } else {
            response.put("found", false);
            response.put("message", "Envelope not found: " + envelopeId);
        }
        return response;
    }

    @PostMapping("/webhook/complete/{envelopeId}")
    public Map<String, Object> triggerWebhookCompletion(@PathVariable String envelopeId) {
        Map<String, Object> response = new HashMap<>();
        ESignatureEnvelope env = repo.getESignatureEnvelopeById(envelopeId);
        if (env != null) {
            String now = java.time.LocalDateTime.now().toString();
            env.setStatus("Completed");
            env.setSignedAt(now);

            // Auto-create & attach signed Document to Guidewire Document Library (pc_document)
            String docId = "doc-signed-" + UUID.randomUUID().toString().substring(0, 6);
            env.setDocumentId(docId);
            repo.saveESignatureEnvelope(env);

            Document doc = new Document(
                    docId,
                    "C00010928",
                    env.getJobNumber(),
                    "Signed " + env.getDocumentType() + " (" + env.getSignerName() + ")",
                    "ACORD Form",
                    "application/pdf",
                    "FinalSigned",
                    env.getDownloadUrl(),
                    now
            );

            repo.saveDocument(doc);

            response.put("status", "SUCCESS");
            response.put("message", "Envelope completed via DocuSign webhook callback. Signed document auto-attached to pc_document.");
            response.put("envelope", env);
            response.put("document", doc);
        } else {
            response.put("status", "ERROR");
            response.put("message", "Envelope not found: " + envelopeId);
        }
        return response;
    }

    @GetMapping("/envelopes")
    public List<ESignatureEnvelope> getAllEnvelopes() {
        List<ESignatureEnvelope> list = repo.getAllESignatureEnvelopes();
        if (list.isEmpty()) {
            // Seed sample envelope
            ESignatureEnvelope sample = new ESignatureEnvelope(
                    "env-1001", "docusign-uuid-sample01", "SUB-5001", "POL-3451127",
                    "John Smith (Insured Officer)", "john.smith@acme.com", "ACORD 125 Commercial Insurance Application",
                    "Completed", java.time.LocalDateTime.now().minusDays(1).toString()
            );
            sample.setSignedAt(java.time.LocalDateTime.now().minusHours(2).toString());
            sample.setDocumentId("doc-signed-sample01");
            sample.setDownloadUrl("/api/esignature/document/docusign-uuid-sample01");
            repo.saveESignatureEnvelope(sample);
            list.add(sample);
        }
        return list;
    }
}
