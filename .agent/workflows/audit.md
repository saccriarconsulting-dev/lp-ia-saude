# Workflow: Security & Compliance Audit (/audit)

This workflow coordinates a complete security and compliance audit using the SecOps Consultant agent.

## Phase 1: Context Gathering
- **Target**: Ask the user which project or directory to audit.
- **Scope**: Define if it's Web, Android, or both.
- **Tool**: `secops-consultant` reads `PROJECT.md` to align with CrIAr standards.

## Phase 2: Automated Scanning
- Trigger `vulnerability-scanner` for automated checks.
- Trigger `mobile-security-masvs` for Android-specific checks.
- Trigger `lgpd-compliance` for data protection checks.

## Phase 3: Expert Analysis
- The `secops-consultant` analyzes the scan results.
- Focus on business impact and risk prioritization.

## Phase 4: Reporting
- Generate a file `auditoria_seguranca.md` in the target directory (or artifacts).
- Follow the Consulting Standard defined in `secops-consultant.md`.

## Phase 5: Debrief
- Summarize the top 3 critical findings to the user.
- Suggest immediate remediation steps.
