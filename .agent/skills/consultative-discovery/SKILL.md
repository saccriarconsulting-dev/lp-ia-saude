---
name: consultative-discovery
description: Methodology of investigating problems, mapping architecture, and translating technology into business value.
allowed-tools: Read, Glob, Grep
---

# Consultative Discovery (CrIAr Consulting)

> Advanced methodology for identifying the root cause of client problems and mapping technical solutions to business outcomes.

## 🕵️ Discovery Framework (Symptom vs. Cause)

When investigating a lead, the AE must differentiate:

| Symptom (Client says) | Root Cause (AE discovers) | Delivery Model |
|-----------------------|---------------------------|----------------|
| "Our app is slow." | Performance leaks, bad architecture. | Assessment + Refactoring. |
| "We need more devs." | Scalability issues, process gaps. | Dedicated Squad. |
| "Integrations fail often." | Fragile APIs, no error handling. | Project (Integration Phase). |
| "We want AI." | Lack of data strategy or automation. | AI Discovery. |

---

## 🏗️ Architecture Mapping (Executive Level)

Map the environment before proposing:
- **Frontend/Mobile:** Native, Cross-platform, Web.
- **Backend:** Node, Python, Java, Go.
- **Integrations:** REST, SOAP, GraphQL, gRPC.
- **Environment:** Cloud (AWS/GCP/Azure) or On-premise.
- **Restrictions:** Regulatory (LGPD), legacy code, limited APIs.

---

## 💎 Value Proposition (Translating Tech to $$$)

Translate features into **Business Benefits**:

| Technical Feature | Business Benefit |
|-------------------|------------------|
| TDD / Unit Testing | Reduction of Rework (Lower Cost). |
| CI/CD Pipeline | High Predictability & Speed to Market. |
| Secure Insets / MASVS | Brand Protection & Compliance (LGPD). |
| Micro-animations / UX | Customer Retention & Engagement. |
| Refactoring Legacy | Reduction of Maintenance Overhead. |

---

## 🛠️ Discovery Checklist

- [ ] Have I identified the **Problem Owner** (Business) and the **Technical Owner**?
- [ ] Do I understand the **Integration Impact** on other systems?
- [ ] is the **Success Metric** (KPI) clear for the client? (e.g., "Reduce loading time by 50%").
- [ ] Have I mapped the **Operational Restrictions** (e.g., "Cannot change the DB schema")?
