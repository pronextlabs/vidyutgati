# Changelog

All notable changes to **VidyutGati (विद्युतगति)** will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
