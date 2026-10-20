# GitHub Actions CI/CD Workflows for Android

This repository is configured with two automated GitHub Actions workflows under `.github/workflows/`:
1. **Pull Request Check (`pr-check.yml`)**: Two parallel jobs on every pull request, push to `main`/`master`, or manual trigger — a **Lint & Style** job (`ktlint` style check + Android Lint for debug and release variants) and a **Run Unit Tests & Coverage** job (unit tests, JaCoCo report generation, and minimum 30% coverage verification with automated Markdown step summaries).
2. **Release Build & Publish (`release.yml`)**: Runs a pre-flight verification gate (`verify` job), then builds, signs, and automatically publishes release-ready APK/AAB artifacts with release notes to GitHub Releases. Gracefully handles unconfigured signing secrets by generating unsigned builds rather than crashing.

Below is the documentation on how these workflows are structured and configured.

---

## 1. Workflows Setup

### A. Pull Request & Push Check (`pr-check.yml`)
Located at `.github/workflows/pr-check.yml`:

```yaml
name: Pull Request Check

on:
  pull_request:
    branches:
      - main
      - master
  push:
    branches:
      - main
      - master
  workflow_dispatch:

concurrency:
  group: pr-check-${{ github.event.pull_request.number || github.ref }}
  cancel-in-progress: ${{ github.event_name == 'pull_request' }}

permissions:
  contents: read

jobs:
  lint:
    name: Lint & Style
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4

      - name: Validate Gradle Wrapper
        uses: gradle/actions/wrapper-validation@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '17'
          cache: 'gradle'

      - name: Grant Execute Permission to Gradlew
        run: chmod +x gradlew

      - name: Run Style Check (ktlint)
        run: ./gradlew ktlintCheck

      - name: Run Android Lint (debug + release)
        run: ./gradlew lintDebug lintRelease

      - name: Upload Lint Reports
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: lint-reports
          path: |
            app/build/reports/lint-results-*
            app/build/reports/ktlint/
          if-no-files-found: ignore
          retention-days: 7

      - name: Generate Lint Step Summary
        if: always()
        run: |
          echo "### 🎨 Code Quality & Lint Summary" >> $GITHUB_STEP_SUMMARY
          echo "" >> $GITHUB_STEP_SUMMARY
          echo "| Check | Status |" >> $GITHUB_STEP_SUMMARY
          echo "| :--- | :--- |" >> $GITHUB_STEP_SUMMARY
          echo "| **ktlint (Code Style)** | ${{ job.status == 'success' && '✅ Passed' || '⚠️ Check logs' }} |" >> $GITHUB_STEP_SUMMARY
          echo "| **Android Lint (debug + release)** | ${{ job.status == 'success' && '✅ Passed (0 warnings/errors)' || '⚠️ Check reports' }} |" >> $GITHUB_STEP_SUMMARY

  test:
    name: Run Unit Tests & Coverage
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4

      - name: Validate Gradle Wrapper
        uses: gradle/actions/wrapper-validation@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '17'
          cache: 'gradle'

      - name: Grant Execute Permission to Gradlew
        run: chmod +x gradlew

      - name: Run Unit Tests & JaCoCo Coverage
        run: ./gradlew testDebugUnitTest jacocoTestReport jacocoTestCoverageVerification

      - name: Upload Test Report
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: unit-test-report
          path: app/build/reports/tests/testDebugUnitTest/
          if-no-files-found: ignore
          retention-days: 7

      - name: Upload Coverage Report
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: jacoco-coverage-report
          path: app/build/reports/jacoco/jacocoTestReport/
          if-no-files-found: ignore
          retention-days: 7

      - name: Generate Test & Coverage Step Summary
        if: always()
        run: |
          python3 -c '
          import xml.etree.ElementTree as ET
          import glob, os

          total_tests = 0
          failures = 0
          errors = 0
          skipped = 0
          duration = 0.0

          for xml_file in glob.glob("app/build/test-results/testDebugUnitTest/TEST-*.xml"):
              try:
                  tree = ET.parse(xml_file)
                  root = tree.getroot()
                  total_tests += int(root.attrib.get("tests", 0))
                  failures += int(root.attrib.get("failures", 0))
                  errors += int(root.attrib.get("errors", 0))
                  skipped += int(root.attrib.get("skipped", 0))
                  duration += float(root.attrib.get("time", 0.0))
              except Exception:
                  pass

          passed = total_tests - failures - errors - skipped

          cov_instructions = "N/A"
          cov_branches = "N/A"
          cov_lines = "N/A"

          jacoco_file = "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
          if os.path.exists(jacoco_file):
              try:
                  tree = ET.parse(jacoco_file)
                  root = tree.getroot()
                  for counter in root.findall("counter"):
                      c_type = counter.attrib.get("type")
                      missed = int(counter.attrib.get("missed", 0))
                      covered = int(counter.attrib.get("covered", 0))
                      total = missed + covered
                      if total > 0:
                          pct = f"{(covered / total) * 100:.1f}% ({covered}/{total})"
                          if c_type == "INSTRUCTION":
                              cov_instructions = pct
                          elif c_type == "BRANCH":
                              cov_branches = pct
                          elif c_type == "LINE":
                              cov_lines = pct
              except Exception:
                  pass

          summary = f"""### 🧪 Test & JaCoCo Coverage Summary

| Metric | Result |
| :--- | :--- |
| **Total Tests** | `{total_tests}` |
| **Passed** | `✅ {passed}` |
| **Failures** | `{"❌ " + str(failures) if failures > 0 else "0"}` |
| **Errors** | `{"💥 " + str(errors) if errors > 0 else "0"}` |
| **Skipped** | `{skipped}` |
| **Duration** | `{duration:.2f}s` |
| **Instruction Coverage** | `{cov_instructions}` |
| **Branch Coverage** | `{cov_branches}` |
| **Line Coverage** | `{cov_lines}` |
"""

          summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
          if summary_path:
              with open(summary_path, "a") as f:
                  f.write(summary)
          '
```

> **Why `lintDebug lintRelease` and not just `lint`?**
> In AGP 8.7 the aggregate `lint` task only wires up the **debug** variant. Release-only
> checks such as `MonochromeLauncherIcon` are therefore invisible to `./gradlew lint`,
> even though `checkReleaseBuilds = true` means they gate the release build. Naming both
> variants explicitly keeps CI and the local release build honest.

---

### B. Release Build, Sign, and Auto-Publish (`release.yml`)
Located at `.github/workflows/release.yml`:

```yaml
name: Release Build & Publish

on:
  push:
    branches:
      - main
      - master
  workflow_dispatch:
    inputs:
      version_name:
        description: 'Version name override (e.g. 1.1 or leave empty to use baseVersionName)'
        required: false
        default: ''
      is_draft:
        description: 'Publish release as draft'
        type: boolean
        required: false
        default: false
      is_prerelease:
        description: 'Publish release as pre-release'
        type: boolean
        required: false
        default: false

concurrency:
  group: release-${{ github.ref }}
  cancel-in-progress: false

permissions:
  contents: write

jobs:
  verify:
    name: Pre-release Quality Gate
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4

      - name: Validate Gradle Wrapper
        uses: gradle/actions/wrapper-validation@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '17'
          cache: 'gradle'

      - name: Grant Execute Permission to Gradlew
        run: chmod +x gradlew

      - name: Run Style Check (ktlint)
        run: ./gradlew ktlintCheck

      - name: Run Android Lint (debug + release)
        run: ./gradlew lintDebug lintRelease

      - name: Run Unit Tests
        run: ./gradlew testDebugUnitTest

  build:
    name: Build, Sign & Release
    needs: [verify]
    runs-on: ubuntu-latest
    permissions:
      contents: write

    steps:
      - name: Checkout Source Code
        uses: actions/checkout@v4

      - name: Validate Gradle Wrapper
        uses: gradle/actions/wrapper-validation@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'zulu'
          java-version: '17'
          cache: 'gradle'

      - name: Grant Execute Permission to Gradlew
        run: chmod +x gradlew

      - name: Extract Version Name
        id: get_version
        run: |
          if [ -n "${{ github.event.inputs.version_name }}" ]; then
            VERSION_NAME="${{ github.event.inputs.version_name }}"
          else
            VERSION_NAME=$(sed -n 's/.*baseVersionName = "\(.*\)".*/\1/p' app/build.gradle.kts | tr -d '[:space:]')
          fi
          echo "version=$VERSION_NAME" >> $GITHUB_OUTPUT
          echo "FULL_VERSION=${VERSION_NAME}.${{ github.run_number }}" >> $GITHUB_ENV

      - name: Build Release APK & Bundle
        run: ./gradlew assembleRelease bundleRelease -PversionCode=${{ github.run_number }} -PversionName=${{ env.FULL_VERSION }}

      - name: Gather Build Artifacts
        run: |
          mkdir -p app/build/outputs/release
          cp app/build/outputs/apk/release/*.apk app/build/outputs/release/
          cp app/build/outputs/bundle/release/*.aab app/build/outputs/release/

      - name: Check Signing Secrets
        id: check_secrets
        run: |
          if [ -n "${{ secrets.SIGNING_KEY }}" ] && [ -n "${{ secrets.KEY_STORE_PASSWORD }}" ] && [ -n "${{ secrets.KEY_PASSWORD }}" ] && [ -n "${{ secrets.ALIAS }}" ]; then
            echo "has_secrets=true" >> $GITHUB_OUTPUT
          else
            echo "has_secrets=false" >> $GITHUB_OUTPUT
            echo "::warning::Signing secrets not configured. Building unsigned artifacts."
          fi

      - name: Sign Artifacts
        if: steps.check_secrets.outputs.has_secrets == 'true'
        uses: r0adkll/sign-android-release@v1
        id: sign_app
        with:
          releaseDirectory: app/build/outputs/release
          signingKeyBase64: ${{ secrets.SIGNING_KEY }}
          alias: ${{ secrets.ALIAS }}
          keyStorePassword: ${{ secrets.KEY_STORE_PASSWORD }}
          keyPassword: ${{ secrets.KEY_PASSWORD }}
        env:
          BUILD_TOOLS_VERSION: "35.0.0"

      - name: Prepare Artifacts for Release
        run: |
          if [ "${{ steps.check_secrets.outputs.has_secrets }}" == "true" ]; then
            if [ -f app/build/outputs/release/app-release-unsigned-signed.apk ]; then
              mv app/build/outputs/release/app-release-unsigned-signed.apk app/build/outputs/release/hackerrank-v${{ env.FULL_VERSION }}.apk
            elif [ -f app/build/outputs/release/app-release-signed.apk ]; then
              mv app/build/outputs/release/app-release-signed.apk app/build/outputs/release/hackerrank-v${{ env.FULL_VERSION }}.apk
            fi
            mv app/build/outputs/release/app-release.aab app/build/outputs/release/hackerrank-v${{ env.FULL_VERSION }}.aab
          else
            find app/build/outputs/release -name "*.apk" -exec mv {} app/build/outputs/release/hackerrank-v${{ env.FULL_VERSION }}-unsigned.apk \;
            find app/build/outputs/release -name "*.aab" -exec mv {} app/build/outputs/release/hackerrank-v${{ env.FULL_VERSION }}-unsigned.aab \;
          fi

      - name: Upload Release Binaries to Artifacts
        uses: actions/upload-artifact@v4
        with:
          name: release-binaries-v${{ env.FULL_VERSION }}
          path: |
            app/build/outputs/release/hackerrank-v*
          retention-days: 14

      - name: Create GitHub Release
        uses: softprops/action-gh-release@v2
        with:
          tag_name: v${{ env.FULL_VERSION }}
          name: Release v${{ env.FULL_VERSION }}
          files: |
            app/build/outputs/release/hackerrank-v*
          draft: ${{ github.event.inputs.is_draft || false }}
          prerelease: ${{ github.event.inputs.is_prerelease || false }}
          generate_release_notes: true
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Release Step Summary
        run: |
          echo "### 🚀 Release v${{ env.FULL_VERSION }} Published" >> $GITHUB_STEP_SUMMARY
          echo "" >> $GITHUB_STEP_SUMMARY
          echo "| Property | Value |" >> $GITHUB_STEP_SUMMARY
          echo "| :--- | :--- |" >> $GITHUB_STEP_SUMMARY
          echo "| **Version** | `v${{ env.FULL_VERSION }}` |" >> $GITHUB_STEP_SUMMARY
          echo "| **Version Code** | `${{ github.run_number }}` |" >> $GITHUB_STEP_SUMMARY
          echo "| **Signed** | ${{ steps.check_secrets.outputs.has_secrets == 'true' && '✅ Yes' || '⚠️ No (Unsigned)' }} |" >> $GITHUB_STEP_SUMMARY
          echo "| **Draft** | `${{ github.event.inputs.is_draft || false }}` |" >> $GITHUB_STEP_SUMMARY
          echo "| **Pre-release** | `${{ github.event.inputs.is_prerelease || false }}` |" >> $GITHUB_STEP_SUMMARY
```

---

## 2. Key Enhancements & Features

### 🛡️ Supply Chain & Wrapper Security
Both workflows include `gradle/actions/wrapper-validation@v4` to verify the checksum of `gradle/wrapper/gradle-wrapper.jar` before running any tasks, preventing execution of compromised wrappers.

### 📊 JaCoCo Code Coverage & Step Summaries
- **Tasks**: `./gradlew testDebugUnitTest jacocoTestReport jacocoTestCoverageVerification` generates XML & HTML reports and enforces a minimum 30% instruction coverage gate.
- **Exclusions**: Generated Dagger/Hilt injection classes and the ASM-transformed Application class (`HackerRankApp`) are filtered out to prevent bytecode mismatch errors.
- **Visual Feedback**: GitHub Action run summaries automatically render Markdown tables breaking down passed/failed test counts and exact instruction/branch/line coverage metrics.

### 🚦 Pre-release Quality Gate
The release workflow runs a dedicated `verify` job before `build`, confirming that code passes formatting (`ktlintCheck`), Android Lint for debug and release, and all unit tests prior to generating release tags or uploading binaries.

### 🔐 Safe Release Signing Fallback
If signing secrets are not yet registered in GitHub repository settings, the build does not crash. It logs a warning, tags the binaries with `-unsigned`, and publishes the unsigned APK/AAB or draft release so workflow execution remains resilient.

---

## 3. GitHub Secrets Configuration

For the **Release Workflow** to sign your app binaries, generate a release keystore (`.jks` file) and define these 4 secrets under **Repository Settings** ➔ **Secrets and variables** ➔ **Actions**:

| Secret Name | Description / Extraction Method |
|---|---|
| `SIGNING_KEY` | The Base64-encoded string of your keystore file. Convert it using:<br>`[Convert]::ToBase64String([IO.File]::ReadAllBytes("your-key.jks"))` |
| `ALIAS` | The key alias name you defined when creating the keystore. |
| `KEY_STORE_PASSWORD` | The password for the keystore file. |
| `KEY_PASSWORD` | The private key password. |

---

## 4. Branch Protection Rules

To prevent code from being merged when lint or tests fail:
1. Go to your GitHub repository -> **Settings** ➔ **Branches**.
2. Click **Add branch protection rule** (or Edit your existing rule).
3. Set the **Branch name pattern** to `main` (or `master`).
4. Check the box **Require status checks to pass before merging**.
5. Search for and select both status checks: **`Lint & Style`** and **`Run Unit Tests & Coverage`**.
6. Save the changes.

---

## 5. Local Parity

The opt-in pre-commit hook runs fast checks locally before pushing:

```bash
git config core.hooksPath .githooks
```

To reproduce the full CI checks locally, run:

```bash
# Check code style and Android Lint
./gradlew ktlintCheck lintDebug lintRelease

# Run unit tests and generate coverage reports
./gradlew testDebugUnitTest jacocoTestReport jacocoTestCoverageVerification
```
