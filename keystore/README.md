# Release Keystore & Signing Configuration

This directory contains the production release keystore for **E-Rickshaw Sarathi (ई-रिक्शा सारथी)**.

### Keystore Specifications:
- **Keystore File**: `erickshaw-sarathi-release.jks`
- **Alias**: `erickshawsarathi`
- **Distinguished Name (DN)**: `CN=E-Rickshaw Sarathi, OU=Green Transit Mobility, O=ProNextLabs, L=New Delhi, ST=Delhi, C=IN`
- **Algorithm**: RSA 2048-bit with SHA256withRSA
- **Signatures Enabled**:
  - v1 Scheme (JAR signing) for universal Android installer compatibility
  - v2 Scheme (Full APK Signature)
  - v3 Scheme (APK Signature Scheme v3)
- **Validity**: 10,000 days (~27 years)

This configuration eliminates debug-mode warnings, package parsing errors, and Google Play Protect untrusted developer blocks.
