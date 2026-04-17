---
name: commit
description: Create a conventional commit for this repo
allowed-tools: Bash(git *)
---

Create a conventional commit following Conventional Commits format.

Use these prefixes:
- `feat:` — new feature (minor version bump)
- `fix:` — bug fix (patch version bump)
- `BREAKING CHANGE` in footer — major version bump

Run `npm run commit` if the user wants the interactive wizard, otherwise stage and commit directly using `git commit -m`.