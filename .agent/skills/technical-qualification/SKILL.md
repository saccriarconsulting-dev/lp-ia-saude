---
name: technical-qualification
description: Technical qualification matrix, OSINT account research, and CRM logging for SDR/BDR roles.
allowed-tools: Read, Glob, Grep, search_web, read_url_content
---

# Technical Qualification & Research (SDR/BDR)

> Methodology for identifying technical fit and qualifying leads before they reach the Commercial Director.

## 🔍 Account Research (OSINT Técnico)

Use free tools and MCP integrations (Search/Scraping) to identify signals:

| Signal | Method | Indicator |
|--------|--------|-----------|
| **Stack Empregada** | Search "vagas [Empresa]", BuildWith (Free), LinkedIn. | "Procurando Dev Kotlin" → Foco Android Nativo. |
| **Maturidade Digital** | Review of App Store / Play Store. | "Last update 2 years ago" → Potential Legacy/Sustentação. |
| **Porte da TI** | LinkedIn search (People > IT roles). | Small team → Squad demand. Big team → Alocação/Body shop. |
| **Infra/Cloud** | Job descriptions / Search. | Mention of AWS/Azure → Cloud consulting opportunity. |

---

## 🛡️ The "Fit or Veto" Matrix (CrIAr Standard)

A lead must be **QUALIFIED** only if it passes the Technical Effort vs. Risk test:

| Criterion | High Risk (DISCARD ❌) | Healthy Fit (PROCEED ✅) |
|-----------|-------------------------|--------------------------|
| **Learning Curve** | Tech we don't know and would take > 1 month to master. | Tech we master (Android, Security, Cloud). |
| **Project Success** | High risk of failure due to missing expertise. | High confidence in successful delivery. |
| **Integrations** | Unknown proprietary legacy systems with no docs. | Standard Rest APIs, GraphQL, well-known ERPs. |
| **Demand Type** | Consulting for a domain we hate or avoid. | Core domains (Android, Security, AI). |

---

## 📋 CRM Logging Standard (HubSpot Free)

When logging a qualified lead, include these "Technical Logs":
- **Pain Point:** (Project, Support, Integration, or Allocation).
- **Observed Stack:** (e.g., Kotlin, RxJava, Legacy Java).
- **Urgency Signal:** (Why now? Deadline? Tech debt?).
- **Complexity Signal:** (Multiple stakeholders, legacy code, unregulated environment).
- **Stakeholder Map:** (Decisor técnico vs. Decisor de negócio).

---

## 🛠️ Qualification Checklist

- [ ] Does the lead have a **Technical Pain** we can solve?
- [ ] Is the **Learning Curve** acceptable for our current team?
- [ ] Can we guarantee **Project Success** based on our experts' skills?
- [ ] Is the lead looking for **Value** or just the lowest price? (Avoid "Exploratory Budget" only).
