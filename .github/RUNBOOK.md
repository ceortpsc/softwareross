Runbook: PR → CI → Merge Gate → Auto‑Seed → Optional Upgrade

1) PR Event
- PR opened/updated/reopened triggers pr-ci-automation.yml which builds backend and frontend.

2) CI Monitoring
- Automation watches GitHub Actions for check-suite results. If failures occur, fix and push; CI auto-runs.

3) Merge Gate
- Configure branch protection on main to require the PR-CI checks and any other policies. Merge blocked until checks succeed.

4) Post‑Merge Auto‑Seed
- On push to main, post-merge-automation.yml runs; deployments must run DataLoader which uses ADMIN_* secrets to seed/update admin.

5) Optional Dependency Upgrade
- If enabled, auto-upgrade workflows run after successful merge; they create PRs for dependency bumps for human review.

6) Verification
- After merge, verify admin user exists using the application login and audit DataLoader logs.
