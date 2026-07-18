# Core Guard Security Testing Guide

Professional mobile security testing includes both static and dynamic analysis.

## Static Analysis (SAST)

### 1. SonarQube

Configuration in CI/CD automatically scans for:
- OWASP Mobile Top 10 vulnerabilities
- CWE/SANS Top 25 weaknesses
- Android-specific security issues
- Code quality metrics

**Access Results**: GitHub Security tab → Code scanning

### 2. Semgrep Pattern-Based Analysis

Detects:
- Hardcoded secrets
- Insecure API usage
- Vulnerable dependencies
- Android security misconfigurations

**Local Run**:
```bash
semgrep --config=p/android \
        --config=p/security-audit \
        --config=p/owasp-top-ten \
        app/src \
        --json -o semgrep-results.json
```

### 3. Android Lint

Strict lint rules enabled in `build.gradle`:

```bash
./gradlew lint
# Reports at: app/build/reports/lint-results.html
```

**Security Checks**:
- HardcodedDebugMode
- InsecureProtocol
- ExportedActivity/Service
- MissingPermission
- AllowBackup

### 4. Dependency Scanning

#### OWASP Dependency Check
```bash
./gradlew dependencyCheckAnalyze
# Reports at: app/build/reports/dependency-check-report.html
```

#### Gradle Dependency Graph
```bash
./gradlew dependencies --configuration releaseRuntimeClasspath
```

## Dynamic Analysis (DAST)

### 1. Mobile Security Framework (MobSF)

**Setup**:
```bash
# Start MobSF in Docker
docker run -it -p 8000:8000 \
  opensecurity/mobile-security-framework-mobsf:latest

# Visit: http://localhost:8000
```

**Analysis**:
1. Upload APK
2. MobSF performs:
   - Binary analysis
   - Permission scanning
   - Certificate verification
   - Network configuration analysis
   - Android manifest review
   - APK comparison

**Security Checks Performed**:
- Debuggable app flag
- Signature verification
- Certificate analysis
- Exported components
- Cleartext traffic
- Insecure TLS configurations

### 2. Frida Runtime Instrumentation

**Setup**:
```bash
# On device (rooted)
frida-server &

# On host
frida-ps -U  # List processes
```

**Monitor Root Detection**:
```javascript
// root-detection-monitor.js
Java.perform(function () {
    var SecurityValidator = Java.use("com.coldboar.coreguard.security.SecurityValidator");
    
    SecurityValidator.isDeviceRooted.implementation = function () {
        console.log("[+] isDeviceRooted() called");
        var result = this.isDeviceRooted();
        console.log("[+] Result: " + result);
        return result;
    };
});
```

**Run**:
```bash
frida -U -l root-detection-monitor.js -f com.coldboar.coreguard
```

**Other Monitoring Scripts**:
```bash
# Monitor all method calls
frida-trace -U -i "*SecurityValidator*" com.coldboar.coreguard

# Monitor network access
frida-trace -U -i "*okhttp*" com.coldboar.coreguard

# Monitor file access
frida-trace -U -i "*SharedPreferences*" com.coldboar.coreguard
```

### 3. drozer Intent Analysis

**Setup**:
```bash
# Install drozer on device
adb install drozer-agent.apk

# Connect from host
adb forward tcp:31415 tcp:31415
drozer console connect
```

**Security Assessment**:
```bash
# List exported activities
app.activity.exported

# List exported services
app.service.exported

# List exported broadcast receivers
app.broadcast.receivers

# Analyze content providers
app.provider.exported

# Attempt to interact
app.activity.start --action android.intent.action.MAIN \
                   --component com.coldboar.coreguard/.MainActivity
```

### 4. Manual Device Testing

#### Certificate Pinning Verification

```bash
# Attempt MitM with proxy
# Set HTTP proxy on device

# Install Burp Suite certificate
# Monitor for certificate pinning errors

# Expected: Connection should fail or warn
# Not Expected: Plain data transmitted through proxy
```

#### Permission Testing

```bash
# Try to access restricted resources
adb shell pm grant com.coldboar.coreguard android.permission.READ_CONTACTS
adb shell pm revoke com.coldboar.coreguard android.permission.READ_CONTACTS

# Monitor with logcat
adb logcat | grep -i "permission"
```

#### Debugger Detection

```bash
# Attach debugger
adb forward tcp:5005 tcp:5005
jdb -attach localhost:5005

# App should:
# - Detect debugger
# - Disable sensitive features
# - Log security event
# - Possibly exit
```

#### Root/Jailbreak Detection

```bash
# On rooted device:
adb shell
su

# App should:
# - Detect root
# - Warn user
# - Disable features
# - Log security event
```

### 5. Burp Suite Mobile Testing

**Setup**:
1. Start Burp Suite Professional
2. Configure device HTTP proxy
3. Install Burp certificate
4. Use Intercept and Repeater

**Tests**:
- Request tampering
- Response modification
- Network traffic inspection
- API endpoint security
- Authentication bypass attempts

**Expected Results**:
- Certificate pinning prevents traffic interception
- API validates requests server-side
- No sensitive data in transit

## Automated Security Testing

### CI/CD Automation

The GitHub Actions workflow automatically runs:

```yaml
- Lint Security Checks
- SonarQube Analysis
- Semgrep SAST
- Dependency Check
- Android Lint
- Build Verification
- APK Signature Verification
```

**View Results**:
- GitHub Security tab (Code scanning alerts)
- Actions tab (Workflow logs)
- Artifacts (Reports)

## Security Metrics

Track these metrics over time:

| Metric | Target | Current |
|--------|--------|---------|
| CVSS Score | < 5.0 | - |
| High Vulnerabilities | 0 | - |
| Critical Vulnerabilities | 0 | - |
| Code Coverage | > 80% | - |
| Test Pass Rate | 100% | - |
| Dependency Age | < 6 months | - |

## Compliance Testing

### OWASP Top 10 Mobile Risks

1. **Improper Platform Usage** → ✅ Lint checks
2. **Insecure Data Storage** → ✅ Encryption checks
3. **Insecure Communication** → ✅ TLS pinning
4. **Insecure Authentication** → ✅ Backend validation
5. **Insufficient Cryptography** → ✅ Semgrep checks
6. **Insecure Authorization** → ✅ Permission checks
7. **Client Code Quality** → ✅ SonarQube
8. **Code Tampering** → ✅ ProGuard + Signature
9. **Reverse Engineering** → ✅ Obfuscation
10. **Extraneous Functionality** → ✅ Manifest audit

## Tools & Resources

### Installed Tools (CI/CD)
- SonarQube
- Semgrep
- OWASP Dependency Check
- Android Lint
- Gradle Build Tools

### Local Tools
- MobSF (Docker)
- Frida (Python)
- drozer (Python/Java)
- Burp Suite (Commercial)
- Android Studio
- adb / apkanalyzer

### References
- [OWASP Mobile Testing Guide](https://owasp.org/www-project-mobile-testing-guide/)
- [Android Security & Privacy Year in Review](https://security.googleblog.com/)
- [CWE/SANS Top 25](https://cwe.mitre.org/top25/)

## Test Report Template

```markdown
# Security Test Report - Core Guard v1.0.0

## Executive Summary
- **Test Date**: 2026-07-18
- **Tested Version**: 1.0.0
- **Test Type**: Pre-Release Security Assessment
- **Overall Risk**: LOW

## Findings

### Critical Issues
- None identified

### High Issues
- None identified

### Medium Issues
- 1 item: [description]
  - Status: Not exploitable in current context
  - Mitigation: [description]

### Low Issues
- None identified

## Recommendations

1. Continue quarterly security assessments
2. Monitor dependency updates
3. Implement runtime monitoring
4. Conduct annual penetration test

## Artifacts

- Static Analysis Report
- Dynamic Analysis Report
- APK Analysis Report
- Certificate Verification
- Dependency Report

---
Tested by: Security Team
Verified by: Release Manager
```

---

**Last Updated**: 2026-07-18  
**Version**: 1.0
