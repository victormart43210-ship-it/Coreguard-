# Third-Party Developer Tools

## uv for Windows ARM64

The CoreGuard project may provide `uv-aarch64-pc-windows-msvc.zip` as an **optional developer download** through a GitHub Release. It is not an Android application dependency, is not included in the CoreGuard APK, and is not required to build or run the current Android prototype.

| Field | Value |
|---|---|
| Upstream project | [Astral uv](https://github.com/astral-sh/uv) |
| Platform | Windows on ARM64 (`aarch64-pc-windows-msvc`) |
| Upstream license | Apache License 2.0 or MIT License, at the recipient's option |
| Official releases | [github.com/astral-sh/uv/releases](https://github.com/astral-sh/uv/releases) |
| CoreGuard status | User-provided archive; version and checksum must be verified before publication |

> **Supply-chain notice:** Do not execute or redistribute a user-provided binary until its ZIP integrity, SHA-256 checksum, contents, upstream version, and GitHub artifact attestation have been verified. The official uv project documents verification with `gh attestation verify <file> --repo astral-sh/uv`.

The archive should be published as a GitHub Release asset rather than committed to source history. Release notes must record the exact version, byte size, SHA-256 checksum, upstream source URL, and attestation result. If provenance cannot be established, the asset must remain explicitly labeled **unverified user-provided binary**.

## Excluded material

A separately supplied text excerpt from the React Compiler playground is unrelated to CoreGuard and is not included in this project.
