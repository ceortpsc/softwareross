const fs = require('fs')
const path = require('path')
const { Octokit } = require('@octokit/rest')

async function main() {
  const githubEventPath = process.env.GITHUB_EVENT_PATH
  if (!githubEventPath || !fs.existsSync(githubEventPath)) {
    console.error('GITHUB_EVENT_PATH not found')
    process.exit(0)
  }

  const event = JSON.parse(fs.readFileSync(githubEventPath, 'utf8'))
  const pr = event.pull_request
  if (!pr) {
    console.log('No pull request data')
    return
  }

  const token = process.env.GITHUB_TOKEN
  if (!token) {
    console.error('GITHUB_TOKEN not provided')
    process.exit(1)
  }

  const openaiKey = process.env.OPENAI_API_KEY
  if (!openaiKey) {
    console.log('OPENAI_API_KEY not set — skipping OpenAI summary')
  }

  const octokit = new Octokit({ auth: token })
  const { owner, repo } = event.repository
  const prNumber = pr.number

  // Simple labeler heuristics
  const labels = []
  if (pr.title.match(/fix|bug/i)) labels.push('bug')
  if (pr.title.match(/feat|feature|add/i)) labels.push('feature')

  if (labels.length) {
    await octokit.issues.addLabels({ owner: owner.name || owner.login, repo: repo.name || repo, issue_number: prNumber, labels })
    console.log('Added labels:', labels)
  }

  // Generate a short summary via OpenAI if key is present
  let summary = null
  if (openaiKey) {
    try {
      const prompt = `Summarize this PR for reviewers:\nTitle: ${pr.title}\nBody: ${pr.body || '<no body>'}\nList quick review points and possible risks.`
      const resp = await fetch('https://api.openai.com/v1/chat/completions', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${openaiKey}`
        },
        body: JSON.stringify({
          model: 'gpt-4o-mini',
          messages: [{ role: 'user', content: prompt }],
          max_tokens: 300
        })
      })
      const j = await resp.json()
      summary = j?.choices?.[0]?.message?.content?.trim()
    } catch (e) {
      console.error('OpenAI call failed', e.message)
    }
  }

  // Post summary comment
  let body = 'Automated PR assistant summary.'
  if (summary) body = `**AI PR Summary**\n\n${summary}`
  else body = '**Automated PR assistant**\n\nNo AI key configured; add OPENAI_API_KEY to enable summaries.'

  await octokit.issues.createComment({ owner: owner.name || owner.login, repo: repo.name || repo, issue_number: prNumber, body })
  console.log('Posted PR assistant comment')
}

main().catch(err => { console.error(err); process.exit(1) })
