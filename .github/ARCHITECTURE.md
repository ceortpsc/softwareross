Automated CI + Merge Gate + Admin Seeding Pipeline

Overview:
Event source: GitHub pull_request events on protected branches.

CI Layer:
- GitHub Actions executes backend (Maven) and frontend (Vite) builds and tests.
- Results feed into branch protection rules; merge is blocked until success.

Merge Gate:
- Enforced by GitHub branch protection requiring the CI checks.

Deployment Layer:
- After merge, deployment pipeline (CD) deploys the application and runs DataLoader at startup.
- DataLoader reads ADMIN_* secrets to seed/update admin user.
- DataLoader emits structured audit logs.

Optional Dependency Upgrade Module:
- Runs after CI success; opens PRs with dependency updates for human review.

Audit & Logging:
- CI logs retained by GitHub Actions
- DataLoader logs must be persisted by the deployment platform

Security Notes:
- ADMIN credentials are only provided via repository or environment secrets.
- No default admin is created when secrets are missing.
