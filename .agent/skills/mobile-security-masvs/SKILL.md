---
name: mobile-security-masvs
description: Mobile Application Security Verification Standard (OWASP MASVS). Advanced audit for Android/iOS security.
allowed-tools: Read, Glob, Grep, Bash
---

# Mobile Security MASVS

> Security verification standard for mobile applications, focusing on architectural and implementation security.

## 📱 Verification Categories

| Category | Focus | Key Checks |
|----------|-------|------------|
| **V1: Architecture** | Security by Design | Proper threat modeling, secure components, verified third-parties |
| **V2: Data Storage** | Protection of PII | No sensitive data in logs, cache, or external storage. Use Keystore. |
| **V3: Crypto** | Cryptographic implementation | Strong algos (AES-GCM), key management, no hardcoded keys |
| **V4: Auth/Authz** | Identity management | Secure session handling, MFA, biometric security |
| **V5: Network** | Communication security | TLS/SSL pinning, no cleartext traffic, certificate validation |
| **V6: Platform** | OS Interaction | Secure Intents, Permissions, Broadcast Receivers, WebViews |
| **V7: Code Quality** | Source code integrity | No debug code, proper obfuscation (ProGuard/R8), memory safety |
| **V8: Resilience** | Anti-reversing | Root detection, integrity checks, anti-debugging |

---

## 🔍 Android Source Code Checklist (Kotlin/Java)

### 1. Data Storage (V2)
- [ ] Check `SharedPrefs` for `MODE_PRIVATE`.
- [ ] Ensure `encrypted-sharedpreferences` is used for sensitive data.
- [ ] Verify `allowBackup="false"` in `AndroidManifest.xml`.
- [ ] Check for hardcoded file paths that might be public.

### 2. Cryptography (V3)
- [ ] Search for `SecretKeySpec`.
- [ ] Verify `KeyStore` usage for key storage.
- [ ] Check for weak algos: `DES`, `3DES`, `RC4`, `SHA-1`, `MD5`.
- [ ] No hardcoded salts or IVs.

### 3. Network (V5)
- [ ] Check `network_security_config.xml`.
- [ ] Verify `cleartextTrafficPermitted="false"`.
- [ ] Confirm Certificate Pinning implementation (OkHttp `CertificatePinner`).

### 4. Platform Interaction (V6)
- [ ] Check `exported="true"` in components (Activities, Services, Providers).
- [ ] Verify WebView settings: `setJavaScriptEnabled(true)` only if necessary.
- [ ] Audit `addJavascriptInterface` usage (RCE risk).

---

## 🛠️ Audit Commands

| Command | Purpose |
|---------|---------|
| `grep -rI "MODE_WORLD_READABLE" .` | Find insecure file permissions |
| `grep -rI "setJavaScriptEnabled(true)" .` | Find potentially vulnerable WebViews |
| `grep -rI "allowBackup=\"true\"" .` | Find insecure backup configurations |
| `grep -rI "http://" .` | Find cleartext communication |

---

> **Remember:** MASVS L1 is standard security, L2 is for high-stakes apps (banking/etc), and R is for resilience against reverse engineering.
