---
name: lgpd-compliance
description: Technical security requirements for Brazilian LGPD (Lei Geral de Proteção de Dados).
allowed-tools: Read, Glob, Grep
---

# LGPD Compliance

> Technical auditing framework for ensuring system compliance with LGPD (Brazil's Data Protection Law).

## 🛡️ Technical Control Categories

| Category | LGPD Requirement | Technical Check |
|----------|------------------|-----------------|
| **Privacy by Design** | Art. 46 | Minimal data collection, automated disposal, encryption. |
| **Data Protection** | Art. 46 | AES-256 for data at rest, TLS 1.3 for data in transit. |
| **Anonymization** | Art. 12 | Irreversible masking for analytics data (Hash + Salt). |
| **Consent Management** | Art. 7, 8 | Logs of consent (who, when, purpose). granular opt-ins. |
| **Access Control** | Art. 46 | RBAC (Role-Based Access Control) + MFA for admins. |
| **Audit Logging** | Art. 37 | Immutabile logs of access to PII (Personally Identifiable Information). |
| **Data Portability** | Art. 18 | Mechanism to export user data in machine-readable format. |

---

## 🔍 Technical Audit Checklist

### 1. Data Inventory (PII)
- [ ] List all database columns containing PII (Names, CPF, Email, IP, Cookies).
- [ ] Identify third-party data transfers (SDKs, APIs, Cloud Providers).

### 2. Encryption (Storage & Transit)
- [ ] Verify SSL/TLS version (Reject < 1.2).
- [ ] Check DB encryption for tables with sensitive data.
- [ ] Audit secret management (No secrets in environment variables or code).

### 3. Data Retention & Disposal
- [ ] Verify exists a script or process for automatic data deletion after purpose ends.
- [ ] Check logs for accidental PII storage (e.g., email in URL params).

### 4. Incident Response Readiness
- [ ] Verify logging for unauthorized access attempts.
- [ ] Check for data breach notification mechanism.

---

## 🛠️ Audit Commands

| Command | Purpose |
|---------|---------|
| `grep -rI "cpf" .` | Identify potential CPF handling areas |
| `grep -rI "email" .` | Identify PII collection points |
| `grep -rI "log.d" .` | Check for accidental PII logging in Android |
| `grep -rI "localStorage" .` | Check for PII in browser storage |

---

> **Note:** LGPD is not just technical; it requires legal documents (Privacy Policy, Terms of Use). This skill focuses on the **technical enforcement** of these policies.
