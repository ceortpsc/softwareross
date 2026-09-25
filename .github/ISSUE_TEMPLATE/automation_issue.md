---
name: Automation / CI issue
about: Report an issue with CI, merge gate, or post-merge seeding
---

## Summary
Describe the CI / SOP automation issue or enhancement.

## Context
- Target branch:
- Related PR(s):
- Environment(s) affected:

## Expected Behavior
- CI auto-runs on every pull_request event.
- Merge gate blocks merging until all checks succeed.
- Post-merge DataLoader seeds/updates admin user from environment secrets.

## Current Behavior
Describe what is happening now.

## Steps to Reproduce
1.
2.
3.

## Logs / Evidence
- CI run URL:
- DataLoader logs:

## Acceptance Criteria
- [ ] CI auto-trigger confirmed
- [ ] Merge gate enforced
- [ ] Admin user seeded/updated correctly
- [ ] Dependency-upgrade workflow validated (if applicable)
