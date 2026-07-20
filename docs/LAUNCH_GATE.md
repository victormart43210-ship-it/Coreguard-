# CoreGuard Launch Gate (v1)

**Repository:** `victormart43210-ship-it/Coreguard-`  
**Package:** `com.coldboar.coreguard`  
**Date:** ____________________  
**Release Version (name/code):** ____________________  

---

## Decision

- [ ] **GO** (all required checks pass)
- [ ] **CONDITIONAL GO** (minor non-security/non-policy items only)
- [ ] **NO-GO** (any required check fails)

> Rule: Any failure in Sections 1–6 is **automatic NO-GO**.

---

## 1) Build & Release Integrity (Required)

- [ ] `./gradlew test` passes
- [ ] `./gradlew assembleDebug` passes
- [ ] `./gradlew bundleRelease` passes
- [ ] Release build has minification enabled (`minifyEnabled=true`)
- [ ] Release build has resource shrinking enabled (`shrinkResources=true`)
- [ ] Production artifact is not debug-signed

**Evidence / links:**  
- CI run: ____________________  
- Build logs/artifacts: ____________________  

---

## 2) Runtime Security Checks Are Live (Required)

- [ ] Debugger detection implemented and visible in Security Status
- [ ] Emulator detection implemented and visible
- [ ] Root indicator detection implemented and visible
- [ ] Runtime signature verification implemented
- [ ] Build type check (debug/release) visible
- [ ] Check explanations are human-readable

**Evidence / links (screenshots/test notes):**  
- ____________________  

---

## 3) Restricted Mode Behavior (Required)

- [ ] High-risk state disables sensitive actions
- [ ] User receives clear warning and reason
- [ ] No crash-loop when risk is detected
- [ ] Tested on at least one physical device
- [ ] Tested on at least one emulator

**Evidence / test matrix:**  
- Device(s): ____________________  
- Emulator(s): ____________________  
- Notes: ____________________  

---

## 4) Secrets & Storage (Required)

- [ ] No hardcoded API keys/secrets/tokens in source
- [ ] Sensitive local state uses encrypted/Keystore-backed storage
- [ ] Secret scan completed (manual and/or tool)
- [ ] No keystores/private credentials committed to git

**Evidence / tools used:**  
- ____________________  

---

## 5) Billing Truthfulness & Safety (Required)

- [ ] If demo-only, UI explicitly says demo-only
- [ ] If charging users, Play Billing integration is complete
- [ ] Purchase verification path documented (prefer server-side)
- [ ] Refund/cancel/pending purchase states handled/documented
- [ ] No claims of “verified payment” without actual verification

**Evidence / links:**  
- ____________________  

---

## 6) Policy / Legal / Play Compliance (Required)

- [ ] Privacy Policy URL is live and accurate
- [ ] Data Safety form matches actual app behavior
- [ ] Content rating completed
- [ ] Target audience declarations completed
- [ ] App access instructions provided (if any restricted/login features)

**Evidence / Play Console screenshots:**  
- ____________________  

---

## 7) Quality Evidence (Required for GO)

- [ ] Unit tests cover core security evaluators/utilities
- [ ] CI is green on target commit/branch
- [ ] No unresolved high-severity crash in test track
- [ ] Known limitations documented clearly (no exaggerated claims)

**Evidence / links:**  
- ____________________  

---

## 8) Store Listing & Assets (Required for Submission)

- [ ] App icon ready
- [ ] Screenshots ready
- [ ] Feature graphic ready (if required)
- [ ] Store listing text accurate and non-misleading
- [ ] Version name/code updated correctly
- [ ] Signed `.aab` ready and archived

**Evidence / asset location:**  
- ____________________  

---

## Known Limitations (Must be explicit)

- [ ] CPU metric is labeled simulated unless true implementation exists
- [ ] Security checks are risk signals, not absolute guarantees
- [ ] Any unimplemented billing verification is disclosed
- [ ] Residual risks are documented in `docs/THREAT_MODEL.md`

---

## Final Sign-off

**Engineering Lead**  
Name: ____________________  
Signature: ____________________  
Date: ____________________

**Security Reviewer**  
Name: ____________________  
Signature: ____________________  
Date: ____________________

**Product/Release Owner**  
Name: ____________________  
Signature: ____________________  
Date: ____________________

---

## Final Notes / Exceptions

Document any exception approved for release and why it is acceptable:

- Exception: ____________________  
- Risk accepted by: ____________________  
- Mitigation plan/date: ____________________
