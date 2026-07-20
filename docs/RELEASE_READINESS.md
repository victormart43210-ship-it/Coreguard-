# CoreGuard release checklist

This repository contains a native Kotlin Android prototype. It is not automatically ready for publication merely because it builds.

## Local build

1. Install Android Studio, JDK 17, Android SDK 35, and the Android SDK build tools.
2. Open the repository in Android Studio and sync Gradle.
3. Run unit tests:

```bash
./gradlew test
```

4. Build a release bundle:

```bash
./gradlew bundleRelease
```

The output is under `app/build/outputs/bundle/release/`.

This repository does not currently include a Gradle wrapper. Use Android Studio's configured Gradle during development, then generate and commit a wrapper with a compatible Gradle version before relying on command-line or CI builds.

## Signing

The release build is intentionally unsigned/configured without a production key. Create an upload keystore outside the repository, configure signing through local `key.properties` or CI secrets, and never commit keys, passwords, or certificates that contain private material.

## Billing

The current lifetime button is explicitly demo-only and does not process a payment. Before charging users, integrate Google Play Billing, create real products in Play Console, verify purchases, restore entitlements, handle pending/cancelled/refunded purchases, and test with license testers. Do not publish the demo entitlement as a paid feature.

## Play Console preparation

Before submission, provide:

- Application name, icon, screenshots, feature graphic, and store listing copy.
- A privacy policy URL that accurately describes data handling.
- Data Safety answers based on the final shipped behavior.
- Content rating and target audience declarations.
- App access instructions if any login or restricted feature is added.
- A signed Android App Bundle (`.aab`) with a unique package ID and incremented version code.
- Closed-test/internal-test validation on representative devices.

## Current limitations

- CPU is simulated and labeled as simulated; it is not a device CPU benchmark.
- RAM usage is a best-effort system memory estimate.
- No network access or production billing is implemented in this prototype.
- A production privacy policy, signing configuration, icon artwork, and Play Console declarations must be supplied by the publisher.
