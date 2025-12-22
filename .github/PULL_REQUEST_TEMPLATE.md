---
name: Pull Request
about: Create a pull request to contribute to the project
title: ''
labels: ''
assignees: ''
---

## Description

<!-- Provide a brief description of your changes -->

## Type of Change

<!-- Mark the appropriate option with an 'x' -->

- [ ] 🐛 Bug fix (patch) - `fix:`
- [ ] ✨ New feature (minor) - `feat:`
- [ ] 💥 Breaking change (major) - `feat!:` or `BREAKING CHANGE:`
- [ ] 📝 Documentation update - `docs:`
- [ ] 🎨 Code style/formatting - `style:`
- [ ] ♻️ Code refactoring - `refactor:`
- [ ] ⚡ Performance improvement - `perf:`
- [ ] ✅ Test update - `test:`
- [ ] 🔧 Build/CI changes - `build:` or `ci:`
- [ ] 🔨 Chore/maintenance - `chore:`

## Scope

<!-- What area does this change affect? (e.g., chat, booking, ios, android) -->

Scope: `______`

## Commit Message Preview

<!-- Preview how your squash commit message should look -->

```
type(scope): brief description

- Detailed change 1
- Detailed change 2

Closes #issue_number
```

**Example:**
```
feat(chat): add WebSocket real-time messaging

- Implemented WebSocket client using Ktor
- Added message subscription and publishing
- Updated UI to display real-time updates

Closes #42
```

## Changes Made

<!-- List the specific changes in this PR -->

- 
- 
- 

## Testing

<!-- Describe how you tested your changes -->

- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed
- [ ] Screenshot tests updated (if UI changes)

## Screenshots (if applicable)

<!-- Add screenshots for UI changes -->

| Before | After |
|--------|-------|
|        |       |

## Breaking Changes

<!-- If this is a breaking change, describe the impact and migration path -->

**BREAKING CHANGE:** 

Migration guide:
- Step 1
- Step 2

## Checklist

- [ ] My code follows the project's code style
- [ ] I have performed a self-review of my code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] Any dependent changes have been merged and published
- [ ] I have followed [Conventional Commits](https://www.conventionalcommits.org/) format
- [ ] I have updated CHANGELOG.md if needed

## Related Issues

<!-- Link related issues -->

Closes #
Relates to #

## Additional Context

<!-- Add any other context about the PR here -->

---

## For Reviewers

### semantic-release Impact

This PR will trigger a **[patch/minor/major]** release when merged to `main`.

- **Version bump:** `x.x.x` → `x.x.x`
- **Release notes:** Will be auto-generated from squash commit message

### Review Checklist

- [ ] Commit message follows conventional commits format
- [ ] Changes are properly scoped
- [ ] Breaking changes are clearly documented
- [ ] Tests are adequate
- [ ] Documentation is updated

