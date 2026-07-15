# ColdBoar CoreGuard

ColdBoar CoreGuard is an Android prototype for displaying device memory usage and a simulated CPU utilization indicator. The application includes a basic premium-upgrade flow and an accompanying software requirements draft.

## Repository contents

| Path | Description |
|---|---|
| `app/src/main/` | Android application source, layouts, and manifest |
| `app/build.gradle` | Original Android module build configuration supplied with the project |
| `docs/CoreGuard_Elite_SRD_Draft-1.pdf` | Software requirements draft |
| `LICENSE` | Apache License 2.0 |

## Current implementation

The main screen periodically reads Android memory statistics and refreshes the displayed RAM usage. CPU utilization is currently simulated with a random value rather than measured from the device. When the simulated value exceeds the configured threshold, non-premium users are directed to the upgrade screen.

> **Prototype status:** Subscription handling is represented by a local placeholder and is not connected to a production billing provider. The source snapshot also does not include a Gradle wrapper or complete root-level Android build configuration.

## Package

The Android source package is `com.coldboar.coreguard`.

## License

This project is distributed under the Apache License 2.0. See [`LICENSE`](LICENSE).
