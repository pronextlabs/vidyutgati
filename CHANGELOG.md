# Changelog

All notable changes to **VidyutGati (विद्युतगति)** will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.1.1] - 2026-09-11

### Fixed & Hardened (Play Protect & Sideload Installation)
- **Resolved Google Play Protect Security Warnings & Installation Failures**:
  - **Production Signing with Multi-Scheme Validation (v1 + v2 + v3)**: Replaced debug keystore (`CN=Android Debug`) with dedicated production release keystore (`CN=E-Rickshaw Sarathi, OU=Green Transit Mobility, O=ProNextLabs, L=New Delhi, ST=Delhi, C=IN`). Simultaneous v1 (JAR), v2, and v3 digital signatures resolve package parsing failures on MIUI, ColorOS, FuntouchOS, and older Android versions.
  - **Disabled Debuggable Flag**: Non-debuggable release build (`isDebuggable = false`) removes the `application-debuggable` flag flagged by Android 14+ / Google Play Protect.
  - **Custom High-Craft Adaptive Launcher Icons**: Replaced empty icon stub (`icon=''`) with vector adaptive launcher icons (`mipmap-anydpi-v26/ic_launcher.xml` & `ic_launcher_round.xml`) and full raster density fallbacks (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`), resolving Play Protect heuristic red flags.
  - **Manifest Hardening**: Set `android:allowBackup="false"` to prevent unauthorized local SQLite khata extraction, declared `android:usesCleartextTraffic="false"`, and added clear user/system descriptions (`android:description`) to `UpiSoundboxNotificationListener`.

### Changed & Rebranded
- **Purpose-Built App Renaming to "E-Rickshaw Sarathi"**:
  - Renamed the app to **E-Rickshaw Sarathi (ई-रिक्शा सारथी)** to make its utility for informal transit operators immediately recognizable.
  - Region-tailored branding across all 9 languages:
    - Hindi: **ई-रिक्शा सारथी (E-Rickshaw Sarathi)**
    - Bengali / Toto: **টোটো সারথি (Toto Sarathi)**
    - Punjabi: **ਈ-ਰਿਕਸ਼ਾ ਸਾਰਥੀ (E-Rickshaw Sarathi)**
    - Gujarati: **ઈ-રિક્ષા સારથી (E-Rickshaw Sarathi)**
    - Marathi: **ई-रिक्षा सारथी (E-Rickshaw Sarathi)**
    - Tamil: **இ-ரிக்ஷா சாரதி (E-Rickshaw Sarathi)**
    - Telugu: **ఇ-రిక్షా సారథి (E-Rickshaw Sarathi)**
    - Hinglish / English: **E-Rickshaw Sarathi**

---

## [1.1.0] - 2026-09-10

### Added
- **Multi-Language Support Across 9 Languages (100% Offline)**:
  - **Hinglish (`hi-Latn`)**: Conversational Latin-script Hindi colloquial phrasing ("Sawari Baithi", "Awaz Box", "Khata", "Gadi Full", "Per Seat Kiraya") tailored for modern smartphone habits.
  - **Hindi (`hi`)**: Complete Devanagari translation ("सवारी", "आवाज़ बॉक्स", "दैनिक हिसाब", "बैटरी").
  - **English (`en`)**: Clean standard terminology for pan-India and international usage.
  - **Bengali (`bn`)**: Full support for West Bengal & Tripura Toto drivers (টোটো চালক, "যাত্রী", "সাউন্ডবক্স", "খাতা", "টাকা").
  - **Punjabi (`pa`)**: Gurmukhi script support for Punjab & Delhi fleets (ਰਿਕਸ਼ਾ ਸਾਰਥੀ, "ਸਵਾਰੀ", "ਖਾਤਾ", "ਰੁਪਏ").
  - **Gujarati (`gu`)**: Dedicated localization for Gujarat urban fleets ("પેસેન્જર", "સાઉન્ડબોક્સ", "ખાતાવહી", "રૂપિયા").
  - **Marathi (`mr`)**: Dedicated localization for Maharashtra auto/e-rickshaw operators ("प्रवासी", "खातेवही", "फेऱ्या").
  - **Tamil (`ta`)**: Full localization for Tamil Nadu last-mile transit ("பயணிகள்", "கணக்கு", "ரூபாய்").
  - **Telugu (`te`)**: Full localization for Andhra Pradesh and Telangana ("ప్రయాణికులు", "ఖాతా", "రూపాయలు").
- **Indian Rupee (INR - ₹) Currency Engine**:
  - `IndianCurrencyFormatter`: Implements the standard Indian numbering system (`₹15`, `₹1,250`, `₹1,50,000` with lakh/crore commas).
  - Multi-lingual spoken currency units (`রুপয়ে`, `rupaye`, `rupees`, `টাকা`, `ਰੁਪਏ`, `રૂપિયા`, `ரூபாய்`, `రూపాయలు`).
- **Dynamic In-App Language Selector Dialog**:
  - High-contrast modal dialog accessible directly from the Top App Bar (`🌐 [भाषा] ▾`).
  - Instant live UI re-composition without restarting the application.
  - Persistent preference backed by Android `SharedPreferences` and iOS `UserDefaults`.
- **Dual-Native Voice Soundbox Regional Synthesis**:
  - Speech synthesis engine adapts to active language on Android (`TextToSpeech`) and iOS (`AVSpeechSynthesizer`) with native voices (`hi-IN`, `bn-IN`, `pa-IN`, `gu-IN`, `mr-IN`, `ta-IN`, `te-IN`, `en-IN`).
- **Unit Testing**:
  - Added `LanguageAndCurrencyTest.kt` verifying currency grouping, spoken words, and complete string catalogs across all 9 languages.

---

## [1.0.1] - 2026-09-10

### Added
- **Safe Delete Architecture with Explicit Confirmation**:
  - Reusable `SafeDeleteConfirmationDialog` modal with tactile long-press vibration alerts and high-contrast red styling (`CoralAlert = #EF4444`).
  - **Seat Cockpit Recent Trip History**: Scrollable view of completed shifts with individual safe delete triggers.
  - **Atomic Ledger Rollback**: Deleting any trip automatically deducts earnings, passenger count, and trip totals from the daily SQLite database (`rollbackTripEarnings`), preventing balance inflation.
  - **Soundbox Transaction Deletion**: Guarded deletion for manual test payments and received notifications.
  - **Guarded Khata Reset**: Multi-step confirmation before zeroing today's shift figures to safeguard drivers on bumpy roads.
  - **iOS SwiftUI Parity**: Matching `.alert` confirmation dialogs across `SeatCockpitView`, `SoundboxView`, and `DailyKhataView`.
- **Hands-Free Automated UPI Voice Soundbox**:
  - `UpiSoundboxNotificationListener` service to auto-detect incoming payments from Paytm, PhonePe, Google Pay, and BHIM.
  - `UpiNotificationParser` regex engine rejecting debits/marketing ads while extracting exact micro-fare amounts.
  - In-app notification access activation card on `SoundboxScreen`.
- **Voltage Sag EMA Smoothing Filter**:
  - `VoltageSagFilter` ($V_t = \alpha V_{\text{raw}} + (1-\alpha) V_{t-1}$) to eliminate false low-battery alerts during acceleration.
  - Real-time sag protection badge on `BatteryScreen`.
- **Seat Cockpit Rush-Hour Quick Fill**:
  - 1-Tap "⚡ पूरी गाड़ी फुल (Fill 4/4 Seats)" shortcut.
- **Weekly Khata 7-Day Performance Card**:
  - Rolling 7-day overview of gross earnings, total thekedar rent paid, and net pocket savings.
- **Google Play Compliance & GitHub CI/CD**:
  - Targets Android 15 (API 35), `versionCode = 2`, Android App Bundle (`.aab`) packaging verified.
  - GitHub Actions CI workflow (`.github/workflows/ci.yml`) and automated release pipeline.
  - Open-source credits to ProNextLabs (https://github.com/pronextlabs).

---

## [1.0.0] - 2026-09-10

### Added
- **Seat Cockpit (सवारी कॉकपिट)**:
  - 72dp high-contrast touch targets for rapid single-thumb seat occupancy tracking (`+` / `-`).
  - Dynamic 3-stage status badge (`खाली`, `1 बाकी`, `गाड़ी फुल`) with synchronized color progression.
  - 1-Tap route reversal button (`⇄`) and per-seat fare quick selector (₹10, ₹15, ₹20, ₹25).
  - Cash and UPI trip completion with automatic gross earnings deposit into the local ledger.
- **Awaz Box (मुफ़्त आवाज़ बॉक्स)**:
  - 100% free software soundbox engine utilizing Android `TextToSpeech` and iOS `AVSpeechSynthesizer`.
  - Natural Hindi currency number synthesis (e.g. *"पेटीएम पर पंद्रह रुपये प्राप्त हुए"*).
  - Quick-tap payment simulation (₹10, ₹15, ₹20, ₹30, ₹50, ₹100) and 1-tap announcement repeat.
  - Embedded driver UPI QR card and persistent payment transaction history.
- **Roz Ka Bhatta & Khata (दैनिक हिसाब-किताब)**:
  - Offline-first daily driver ledger tracking gross earnings, owner rent (*thekedar bhatta*), battery charging costs, and miscellaneous expenses.
  - Prominent **आज की शुद्ध जेब कमाई (Net Profit)** calculation with profit/loss indicators.
  - 1-Tap WhatsApp share summary formatter for fleet owners and family members.
- **Battery & Range Estimator (बैटरी व रेंज गाइड)**:
  - Physics-based open-circuit voltage discharge curve model for 48V Lead-Acid, 48V LFP, and 60V LFP batteries.
  - Dynamic payload weight adjustment (0 to 4 passengers) for realistic remaining range calculation in kilometers.
  - Loud voice status announcer and critical low-battery alerts (<20% or <10 km).
  - Emergency roadside swap station directory with 1-tap phone dials.
- **Dual-Native Architecture**:
  - Jetpack Compose + Material 3 for Android (`/app`).
  - SwiftUI for iOS (`/iosApp`).
  - 100% offline-first local SQLite (Room) & Codable persistence.
