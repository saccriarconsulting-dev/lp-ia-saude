---
name: technical-solution-design
description: High-level architecture design, component mapping, and implementation sequence for pre-sales.
allowed-tools: Read, Glob, Grep
---

# Technical Solution Design (CrIAr Consulting)

> Methodology for designing high-level technical solutions that are both feasible and commercially attractive.

## 🏗️ High-Level Architecture (Mermaid.js)

Always provide a visual representation using Mermaid.js syntax.

### Component Mapping
Identify and document:
- **Core Components:** Engines, Databases, Mobile Apps.
- **Interfaces:** APIs, Gateways, Webhooks.
- **External Systems:** Third-party services, legacy ERPs.

---

## 📋 Implementation Sequence (Phases)

Never propose a monolith. Break down into:
1. **MVP (Phase 1):** Value-first core functionality.
2. **Expansion (Phase 2):** Secondary features/integrations.
3. **Optimization (Phase 3):** Performance and scaling.

---

## 🛡️ Boundaries & Limits

Explicitly document the solution limits:
- **Scalability Limit:** "Supports up to 10k concurrent users."
- **Integration Limit:** "Covers REST API only; manual file import excluded."
- **Responsibility:** "Client provides the cloud environment."

---

## 🛠️ Design Checklist

- [ ] Is the architecture **Clean** and **Scalable**?
- [ ] Does it follow the **Operational Security** rules of CrIAr?
- [ ] have the **Third-party Dependencies** been explicitly mapped?
- [ ] are the **Component Responsibilities** (CrIAr vs Client) clear?
- [ ] Does the **Mermaid diagram** accurately reflect the text proposal?
