# Semantic Release Setup Guide

This document explains how semantic-release is configured for automated version management and releases in the GymPlanner project.

## Overview

semantic-release automates the entire package release workflow including:
- Determining the next version number based on commit messages
- Generating release notes and CHANGELOG
- Building and packaging the XCFramework
- Creating GitHub releases
- Updating version files

## How It Works

### 1. Commit Analysis

When you push commits to `main` or `develop`:

```bash
git commit -m "feat(chat): add WebSocket support"
git push origin main
```

semantic-release analyzes commits since the last release using [Conventional Commits](https://www.conventionalcommits.org/):

| Commit Type | Example | Version Bump |
|-------------|---------|--------------|
| `fix:` | `fix(booking): correct timezone` | Patch (1.0.1) |
| `feat:` | `feat(chat): add reactions` | Minor (1.1.0) |
| `feat!:` or `BREAKING CHANGE:` | `feat!: new auth API` | Major (2.0.0) |
| `docs:`, `chore:`, etc. | `docs: update README` | No release |

### 2. Version Determination

semantic-release calculates the next version using [Semantic Versioning](https://semver.org/):

- **Major (X.0.0)**: Breaking changes (`feat!:` or `BREAKING CHANGE:`)
- **Minor (0.X.0)**: New features (`feat:`)
- **Patch (0.0.X)**: Bug fixes (`fix:`)

### 3. Release Process

Once the version is determined, semantic-release:

1. **Updates version files** (`version.properties`, `build.gradle.kts`)
2. **Generates CHANGELOG.md** from commit messages
3. **Builds XCFramework** using `build-xcframework.sh`
4. **Creates GitHub Release** with:
   - Release notes
   - XCFramework zip
   - Swift Package manifest
5. **Commits version updates** back to the repository
6. **Comments on related issues/PRs** with release info

## Configuration

### .releaserc.json

The main configuration file defines:

```json
{
  "branches": ["main", {"name": "develop", "prerelease": "beta"}],
  "plugins": [...]
}
```

**Branches:**
- `main` - Production releases (1.0.0, 1.1.0, etc.)
- `develop` - Beta releases (1.0.0-beta.1, 1.0.0-beta.2, etc.)

**Plugins:**

1. **@semantic-release/commit-analyzer** - Analyzes commits to determine release type
2. **@semantic-release/release-notes-generator** - Generates release notes
3. **@semantic-release/changelog** - Updates CHANGELOG.md
4. **@semantic-release/exec** - Runs custom scripts (version update, XCFramework build)
5. **@semantic-release/github** - Creates GitHub releases and uploads assets
6. **@semantic-release/git** - Commits updated files back to repository

### GitHub Actions Workflow

**File:** `.github/workflows/release.yml`

Triggers on push to `main` or `develop`:

```yaml
on:
  push:
    branches:
      - main
      - develop
```

Required permissions:
- `contents: write` - Create releases and commit changes
- `issues: write` - Comment on issues
- `pull-requests: write` - Comment on PRs

### Version Update Script

**File:** `scripts/update-version.sh`

Updates version across the project:
- `version.properties` - Main version file
- `Package.swift` - Swift Package manifest
- `README.md` - Documentation

## Usage

### Standard Workflow

1. **Create a feature branch:**
   ```bash
   git checkout -b feat/websocket-chat
   ```

2. **Make changes and commit with conventional format:**
   ```bash
   git add .
   git commit -m "feat(chat): add WebSocket real-time messaging"
   ```

3. **Push and create PR:**
   ```bash
   git push origin feat/websocket-chat
   # Create PR to main
   ```

4. **Merge PR to main:**
   - semantic-release automatically runs
   - Determines this is a minor release (new feature)
   - Bumps version from 1.0.0 → 1.1.0
   - Builds XCFramework
   - Creates GitHub release

### Beta Releases

Push to `develop` for beta versions:

```bash
git checkout develop
git merge feat/websocket-chat
git push origin develop
```

This creates a beta release: `1.1.0-beta.1`

### Emergency Patch

For critical fixes:

```bash
git checkout -b fix/critical-crash
# Make fix
git commit -m "fix(booking): prevent crash on null trainer"
git push origin fix/critical-crash
# Merge to main via PR
```

Result: `1.1.0 → 1.1.1` (patch release)

### Breaking Changes

For incompatible API changes:

```bash
git commit -m "feat(api)!: change authentication to OAuth 2.0

BREAKING CHANGE: Basic authentication is no longer supported.
All API clients must migrate to OAuth 2.0."
```

Result: `1.1.1 → 2.0.0` (major release)

## Commit Message Format

### Structure

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Examples

**Feature (Minor):**
```
feat(ios): add XCFramework distribution

- Automated builds via GitHub Actions
- Swift Package Manager support
- Checksum generation for SPM
```

**Bug Fix (Patch):**
```
fix(chat): resolve WebSocket connection timeout

Increased timeout from 5s to 30s to handle slower networks.

Closes #42
```

**Breaking Change (Major):**
```
feat(auth)!: migrate to Firebase Authentication

BREAKING CHANGE: Custom authentication backend is deprecated.
Update your app to use Firebase Auth SDK.

Migration guide: docs/MIGRATION.md
```

**Documentation (No Release):**
```
docs(README): add semantic-release documentation

Added comprehensive guide for automated releases.
```

## Version Files

### version.properties

Auto-generated file with current version:

```properties
version=1.2.3
versionCode=202512220000
```

**DO NOT** edit manually - managed by semantic-release.

### CHANGELOG.md

Auto-generated changelog with all releases:

```markdown
## [1.2.0] - 2024-12-22

### Features
- **chat**: add WebSocket support (#42)
- **ios**: add XCFramework distribution

### Bug Fixes
- **booking**: correct timezone calculation (#38)
```

## Troubleshooting

### No Release Created

**Problem:** Pushed to main but no release was created.

**Solutions:**
1. Check commits follow conventional format
2. Verify commits have releasable types (`feat`, `fix`, etc.)
3. Ensure previous release/tag exists
4. Check GitHub Actions logs

### Wrong Version Bump

**Problem:** Expected minor but got patch release.

**Solution:** Check commit type:
- Use `feat:` for new features (minor)
- Use `fix:` for bug fixes (patch)
- Use `feat!:` or `BREAKING CHANGE:` for breaking changes (major)

### Build Failed

**Problem:** semantic-release succeeded but XCFramework build failed.

**Solutions:**
1. Check `scripts/update-version.sh` is executable: `chmod +x scripts/update-version.sh`
2. Verify `build-xcframework.sh` works locally
3. Check GitHub Actions logs for Gradle errors
4. Ensure macOS runner has Xcode installed

### Git Push Failed

**Problem:** semantic-release can't push commits back.

**Solutions:**
1. Ensure `GITHUB_TOKEN` has `contents: write` permission
2. Check branch protection rules allow bot commits
3. Add `[skip ci]` exception to branch protection

## Advanced Configuration

### Custom Release Rules

Add to `.releaserc.json`:

```json
{
  "plugins": [
    ["@semantic-release/commit-analyzer", {
      "releaseRules": [
        {"type": "perf", "release": "patch"},
        {"scope": "experimental", "release": false}
      ]
    }]
  ]
}
```

### Skip CI for Version Commits

Already configured in `.releaserc.json`:

```json
{
  "plugins": [
    ["@semantic-release/git", {
      "message": "chore(release): ${nextRelease.version} [skip ci]"
    }]
  ]
}
```

### Custom Assets

Add more files to GitHub release:

```json
{
  "plugins": [
    ["@semantic-release/github", {
      "assets": [
        {"path": "path/to/asset.zip", "label": "Asset Label"}
      ]
    }]
  ]
}
```

## Best Practices

1. **Always use conventional commits** - Ensures proper versioning
2. **Write descriptive commit messages** - Improves generated release notes
3. **Reference issues/PRs** - Use `Closes #123` in commit body
4. **Test locally first** - Run `./build-xcframework.sh` before pushing
5. **Use scopes** - Makes changelog more organized (`feat(chat):`, `fix(booking):`)
6. **Squash PRs** - Keep main branch history clean with meaningful commits
7. **Document breaking changes** - Always explain migration path

## Monitoring

### GitHub Actions

View releases in progress:
1. Go to **Actions** tab
2. Select **Release** workflow
3. Click on running workflow

### Release History

View all releases:
1. Go to **Releases** section
2. See version history, assets, and release notes

### CHANGELOG

Read `CHANGELOG.md` for formatted release history.

## Manual Override (Emergency)

If semantic-release fails, you can release manually:

```bash
# Build XCFramework
./build-xcframework.sh 1.2.3

# Create tag
git tag v1.2.3
git push origin v1.2.3

# Upload to GitHub Releases manually
# Update CHANGELOG.md manually
```

## Resources

- [semantic-release Documentation](https://semantic-release.gitbook.io/)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)
- [Keep a Changelog](https://keepachangelog.com/)
- [Angular Commit Convention](https://github.com/angular/angular/blob/master/CONTRIBUTING.md#commit)

## Support

For issues with semantic-release:
1. Check GitHub Actions logs
2. Review commit messages format
3. Verify configuration in `.releaserc.json`
4. Consult [CONTRIBUTING.md](../CONTRIBUTING.md)
5. Open an issue with logs and configuration

