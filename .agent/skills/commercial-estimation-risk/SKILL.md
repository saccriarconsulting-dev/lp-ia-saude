---
name: commercial-estimation-risk
description: Effort estimation, complexity, and technical risk matrix with AI-assisted productivity metrics.
allowed-tools: Read, Glob, Grep
---

# Commercial Estimation & Risk (CrIAr Consulting)

> Methodology for estimating development effort and technical risks with safety buffers for learning curves.

## 🧮 The "Double-Safety" Estimation Logic

All estimates must follow this calculation to ensure zero-loss operations:

1. **Baseline Estimate:** Use international average metrics for AI-assisted development (e.g., GitHub Copilot / Cursor benchmarks).
2. **Complexity Multiplier:** Adjust for legacy, lack of docs, or niche tech.
3. **CrIAr Safety Buffer:** **MULTIPLY THE TOTAL BY 2 (100% buffer).**
    - *Rationale:* This covers the learning curve of the team, unforeseen environment issues, and ensures high quality without burnout or loss.

---

## 🛡️ Technical Risk Matrix (Veto Scenarios)

The SE identifies "Technical Poisons" that trigger a veto or radical re-pricing:

| Risk Signal | Severity | Action |
|-------------|----------|--------|
| **Hidden Legacy** | HIGH | Veto or phase 1 mandatory Discovery. |
| **Fragile APIs** | MEDIUM | Include 40% more effort in integration. |
| **Artificial Deadline** | HIGH | **VETO.** Quality and safety are P0. |
| **Compliance (LGPD)** | MEDIUM | Add mandatory SecOps review phase. |
| **New Tech Stack** | HIGH | Use the 2x buffer rule strictly. |

---

## 📋 Technical SOW Standard

When producing the SOW (Statement of Work), the SE must include:
- **Prerequisites:** What the client MUST provide (e.g., API access by day 5).
- **Assumptions:** What we assume is true (e.g., "The database is indexed").
- **Exclusions:** Explicit "What we ARE NOT doing".
- **Acceptance Criteria:** What defines a finished, successful task.

---

## 🛠️ Estimation Checklist

- [ ] Have I used AI-assisted benchmarks as a baseline?
- [ ] **IS THE ESTIMATE DOUBLED? (Global Rule).**
- [ ] Have I identified the **Critical Failure Points**?
- [ ] are the **Prerequisites** clearly documented?
- [ ] Does the **Delivery Team** have a realistic chance of success?
