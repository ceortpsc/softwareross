Automations scaffold

This repository includes recommended automations. Summary and how to enable optional features:

Files created:
- .github/workflows/codeql-analysis.yml (CodeQL security analysis)
- .github/workflows/dependency-review.yml (dependency review on PR)
- .github/dependabot.yml (automated dependency updates)
- .github/workflows/ai-pr-assistant.yml (AI PR assistant — posts PR summaries, adds labels)
- .github/scripts/pr-assistant.js (script used by AI workflow; requires OPENAI_API_KEY)
- .github/release-drafter.yml and .github/workflows/auto-release.yml (release drafts)
- .github/workflows/branch-protection.yml (manual workflow to set branch protection — requires ADMIN_GITHUB_TOKEN)
- .github/ISSUE_TEMPLATE/*, .github/PULL_REQUEST_TEMPLATE.md, .github/CODEOWNERS

Required secrets to enable AI and admin features:
- OPENAI_API_KEY: required by AI PR assistant to generate summaries (add in repository Settings → Secrets)
- ADMIN_GITHUB_TOKEN: required to run the branch protection workflow (personal access token with admin:repo scope); store as a secret named ADMIN_GITHUB_TOKEN

Notes and next steps:
- The AI assistant uses OpenAI; the script uses a model placeholder gpt-4o-mini — change in .github/scripts/pr-assistant.js if needed.
- Branch protection cannot be applied by GITHUB_TOKEN in many orgs; use the branch-protection workflow with ADMIN_GITHUB_TOKEN and run manually.
- Review CODEOWNERS and replace @your-github-username with actual team/user handles.

If you want, the next actions are:
- Add OPENAI_API_KEY secret and test an AI PR by opening a PR
- Provide ADMIN_GITHUB_TOKEN and run the branch protection workflow manually
- Tweak CodeQL or add SAST/SCA providers like Snyk or Dependabot alerts
- Commit and push these changes (I can create the commit for you)
