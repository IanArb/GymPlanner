# Conventional Commits Guide

This project uses [Conventional Commits](https://www.conventionalcommits.org/) for automated versioning with semantic-release.

## Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type

The type must be one of the following:

| Type | Description | Version Bump |
|------|-------------|--------------|
| `feat` | A new feature | Minor (0.x.0) |
| `fix` | A bug fix | Patch (0.0.x) |
| `docs` | Documentation only changes | Patch (0.0.x) |
| `style` | Code style changes (formatting, etc.) | Patch (0.0.x) |
| `refactor` | Code refactoring | Patch (0.0.x) |
| `perf` | Performance improvements | Patch (0.0.x) |
| `test` | Adding or updating tests | No release |
| `build` | Build system or dependencies | No release |
| `ci` | CI configuration changes | No release |
| `chore` | Other changes that don't modify src | No release |
| `revert` | Reverts a previous commit | Depends |

### Breaking Changes

To trigger a **major version bump** (x.0.0), add `BREAKING CHANGE:` in the footer or use `!` after the type:

```
feat!: remove deprecated API
```

or

```
feat: add new authentication system

BREAKING CHANGE: Old authentication method is no longer supported
```

## Examples

### Feature (Minor Release - 0.1.0)

```
feat(chat): add message reactions

- Users can now react to messages with emojis
- Reactions are displayed below each message
```

### Bug Fix (Patch Release - 0.0.1)

```
fix(booking): correct time zone calculation

Fixed an issue where booking times were displayed in wrong timezone
for users in different regions.

Fixes #123
```

### Breaking Change (Major Release - 1.0.0)

```
feat(api)!: change authentication flow

BREAKING CHANGE: The authentication endpoint now requires OAuth 2.0
instead of basic authentication. Update your API clients accordingly.
```

### Documentation (Patch Release - 0.0.1)

```
docs(README): update installation instructions

Added Swift Package Manager installation guide for iOS developers
```

### Multiple Types

```
feat(ios): add XCFramework distribution
fix(android): resolve crash on booking screen

- Implemented automated XCFramework builds
- Fixed null pointer exception in BookingViewModel
- Added GitHub Actions workflow for releases
```

## Scopes

Common scopes in this project:

- `android` - Android-specific changes
- `ios` - iOS-specific changes
- `shared` - Shared KMP module
- `chat` - Chat feature
- `booking` - Booking feature
- `dashboard` - Dashboard feature
- `auth` - Authentication
- `api` - API/backend integration
- `ci` - CI/CD workflows
- `deps` - Dependencies

## Release Process

### Automatic Release (Recommended)

1. **Make changes and commit with conventional format:**
   ```bash
   git add .
   git commit -m "feat(chat): add WebSocket support"
   ```

2. **Push to main or develop:**
   ```bash
   git push origin main
   ```

3. **semantic-release automatically:**
   - Analyzes commits since last release
   - Determines next version number
   - Generates CHANGELOG.md
   - Updates version files
   - Builds XCFramework
   - Creates GitHub release
   - Publishes artifacts

### Manual Release (Emergency)

If you need to release manually:

```bash
# Create a version tag
git tag v1.2.3
git push origin v1.2.3
```

## Version Numbers

Following [Semantic Versioning](https://semver.org/):

- **MAJOR** (1.0.0) - Breaking changes
- **MINOR** (0.1.0) - New features (backwards compatible)
- **PATCH** (0.0.1) - Bug fixes (backwards compatible)

### Pre-releases

Commits to `develop` branch create beta releases:

```
1.0.0-beta.1
1.0.0-beta.2
```

## Commit Message Tips

### DO ✅

```bash
git commit -m "feat(booking): add calendar view"
git commit -m "fix(chat): resolve message ordering issue"
git commit -m "docs: update API documentation"
git commit -m "refactor(dashboard): simplify state management"
```

### DON'T ❌

```bash
git commit -m "updates"
git commit -m "fixed stuff"
git commit -m "WIP"
git commit -m "changes"
```

## Skipping Release

To skip CI/CD pipeline (no release):

```bash
git commit -m "chore: update .gitignore [skip ci]"
```

## Troubleshooting

### No release created

Check that:
- Commits follow conventional format
- You're pushing to `main` or `develop` branch
- Previous release exists (semantic-release needs a base)
- GitHub token has correct permissions

### Wrong version bump

Ensure your commit type is correct:
- Use `fix` for patches
- Use `feat` for minor releases
- Use `BREAKING CHANGE` for major releases

## Tools

### Commitizen (Optional)

For interactive commit messages:

```bash
npm install -g commitizen cz-conventional-changelog
git cz
```

### Commitlint (Optional)

To validate commit messages:

```bash
npm install -g @commitlint/cli @commitlint/config-conventional
```

## Resources

- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
- [semantic-release Documentation](https://semantic-release.gitbook.io/)
- [Angular Commit Guidelines](https://github.com/angular/angular/blob/master/CONTRIBUTING.md#commit)

