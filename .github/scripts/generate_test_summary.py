import glob
import os
import sys
import xml.etree.ElementTree as ET


def main():
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

    failures_str = f"❌ {failures}" if failures > 0 else "0"
    errors_str = f"💥 {errors}" if errors > 0 else "0"

    summary_lines = [
        "### 🧪 Test & JaCoCo Coverage Summary",
        "",
        "| Metric | Result |",
        "| :--- | :--- |",
        f"| **Total Tests** | `{total_tests}` |",
        f"| **Passed** | `✅ {passed}` |",
        f"| **Failures** | `{failures_str}` |",
        f"| **Errors** | `{errors_str}` |",
        f"| **Skipped** | `{skipped}` |",
        f"| **Duration** | `{duration:.2f}s` |",
        f"| **Instruction Coverage** | `{cov_instructions}` |",
        f"| **Branch Coverage** | `{cov_branches}` |",
        f"| **Line Coverage** | `{cov_lines}` |",
        "",
    ]
    summary = "\n".join(summary_lines)

    summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
    if summary_path:
        with open(summary_path, "a", encoding="utf-8") as f:
            f.write(summary)
    else:
        if hasattr(sys.stdout, "reconfigure"):
            sys.stdout.reconfigure(encoding="utf-8")
        print(summary)


if __name__ == "__main__":
    main()
