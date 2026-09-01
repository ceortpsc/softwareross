SOP: Automated CI, Merge Gate, and Admin Seeding

Purpose:
Ensure PRs run deterministic CI, deny merging until required checks pass, and seed admin user only from environment-bound secrets after merge.

Directives:
- CI must run on every pull_request event (see .github/workflows/pr-ci-automation.yml).
- Branch protection must require these checks before merging.
- Post-merge, the deployment pipeline must execute DataLoader; DataLoader reads admin credentials from ADMIN_* secrets and seeds/updates admin user.
- No default weak admin is created in the absence of ADMIN_PASSWORD.

Required repository secrets:
- ADMIN_USERNAME
- ADMIN_PASSWORD
- ADMIN_ROLES
- JWT_SECRET

Emergency override: documented in "Emergency Procedures" (not intended for normal operations).

Compliance and audit:
- Retain CI logs for audit period.
- Record DataLoader seed events with timestamp and result.
