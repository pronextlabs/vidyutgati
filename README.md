<p align="center">
  <img src="https://img.shields.io/badge/🛺%20E--Rickshaw%20Sarathi-ई--रिक्शा%20सारथी-F59E0B?style=for-the-badge&labelColor=0A0D14" alt="E-Rickshaw Sarathi Logo" />
</p>

<h1 align="center">E-Rickshaw Sarathi (ई-रिक्शा सारथी / টোটো সারথি)</h1>

<p align="center">
  <strong>The 100% Offline-First Smart Mobility Cockpit, Digital Soundbox & Daily Khata Companion for 2.4M+ E-Rickshaw & Toto Drivers across South Asia.</strong>
</p>

<p align="center">
  <a href="https://github.com/pronextlabs/vidyutgati/actions/workflows/ci.yml"><img src="https://img.shields.io/github/actions/workflow/status/pronextlabs/vidyutgati/ci.yml?branch=main&style=flat-square&logo=github-actions&label=CI%20Build" alt="CI Status" /></a>
  <a href="https://github.com/pronextlabs/vidyutgati/releases"><img src="https://img.shields.io/badge/Release-v1.1.1-F59E0B?style=flat-square&logo=github" alt="Release v1.1.1" /></a>
  <a href="https://developer.android.com/about/versions/15"><img src="https://img.shields.io/badge/Android-API%2035%20(Android%2015)-3DDC84?style=flat-square&logo=android" alt="Android 15" /></a>
  <a href="https://source.android.com/security/apksigning"><img src="https://img.shields.io/badge/Signed-v1%20%7C%20v2%20%7C%20v3-10B981?style=flat-square&logo=google-play" alt="Play Protect Signed" /></a>
  <a href="https://developer.apple.com/swift/"><img src="https://img.shields.io/badge/iOS-16.0%2B%20(Swift%205.9)-000000?style=flat-square&logo=apple" alt="iOS" /></a>
  <a href="#-zero-cloud-telemetry"><img src="https://img.shields.io/badge/Data%20Privacy-100%25%20Offline%20First-10B981?style=flat-square" alt="Offline First" /></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="License" /></a>
</p>

---

## 🌟 Researched & Built by ProNextLabs

**E-Rickshaw Sarathi** (codenamed *VidyutGati*) is an open-source initiative researched, designed, and developed by **[ProNextLabs](https://github.com/pronextlabs)**. 

Our mission with E-Rickshaw Sarathi is to deliver high-craft, hardware-eliminating software tailored directly to grassroots informal transit workers (*Sarathis*), eliminating monthly IoT rental fees, battery anxiety, and manual bookkeeping friction with zero telemetry or cloud lock-in.

---

## 🛺 The Problem It Solves

Electric Rickshaws form the backbone of last-mile feeder mobility across India and South Asia, ferrying over 60 million daily commuters. However, drivers face severe daily operational bottlenecks:
1. **Recurring IoT Soundbox Rents**: Drivers pay ₹120–₹150 every single month to payment companies just for a speaker box to confirm micro-payments in noisy markets.
2. **Daylight & Road Vibration Friction**: Standard apps have tiny 24dp buttons that cannot be used with one thumb while navigating traffic or wearing cotton gloves.
3. **Battery Voltage Sag Anxieties**: Under heavy throttle or bridge climbs with 4 passengers, raw battery voltage dips temporarily, triggering false battery cutoffs and driver panic.
4. **Disorganized Shift Accounting**: Daily calculations involving vehicle owner fixed rent (*thekedar bhatta*), battery swapping expenses, puncture repairs, and fares are calculated on scrap paper or mental memory.

**VidyutGati replaces all of this with a unified, dual-native, 100% offline-first digital cockpit.**

---

## ✨ Core Pillars & Features

```
┌────────────────────────────────────────────────────────────────────────┐
│                        VIDYUTGATI ARCHITECTURE                         │
├───────────────────┬────────────────────┬───────────────────────────────┤
│   SEAT COCKPIT    │   AWAZ SOUNDBOX    │          DAILY KHATA          │
│  72dp Touch Grid  │  Bilingual Hindi   │   Gross Earnings - Bhatta     │
│   Capacity State  │  Automatic UPI     │   - Charging = Net Profit     │
│  1-Tap Route Flip │  Soundbox Listener │   1-Tap WhatsApp Summary      │
├───────────────────┴────────────────────┴───────────────────────────────┤
│                     BATTERY & RANGE ESTIMATOR                          │
│     Physics OCV Curve  •  Voltage Sag EMA Filter  •  Swap Directory    │
├────────────────────────────────────────────────────────────────────────┤
│                       SAFE DELETE ARCHITECTURE                         │
│     Tactile Confirmations  •  Atomic Rollback  •  Zero Corruption     │
└────────────────────────────────────────────────────────────────────────┘
```

### 1. 🛺 Tactile Seat Cockpit (सवारी कॉकपिट)
* **72dp One-Thumb Touch Target Grid**: Designed specifically for quick, eyes-free thumb taps without distracting drivers from busy metro feeder stands.
* **3-Stage Visual State Machine**:
  * `0–2 Seats`: High-contrast Daylight Amber (`#F59E0B`) – *"🟢 सीटें खाली हैं"*
  * `3 Seats`: Electric Cyan (`#06B6D4`) – *"⚠️ केवल 1 सीट बाकी!"*
  * `4 Seats`: Emerald Green (`#10B981`) – *"⚡ पूरी गाड़ी फुल हो गई!"*
* **1-Tap Route Direction Flip (`⇄`)**: Inverts feeder routes instantly (e.g. *Metro Station ➔ Sector 62 Market* to *Sector 62 Market ➔ Metro Station*).
* **Rush-Hour 1-Tap Shortcut**: Fills all remaining capacity with a single tap during peak office rushes.
* **Recent Trip History with Rollback**: Displays recent trips with route, passenger count, fare, and payment badge.

### 2. 📢 Free Digital Voice Soundbox (मुफ़्त आवाज़ बॉक्स)
* **Zero Monthly Hardware Rentals**: Completely eliminates physical IoT soundbox rental charges.
* **Natural Hindi TTS Synthesis**: Native localized currency pronunciation via `TextToSpeech` (Android) and `AVSpeechSynthesizer` (iOS):
  * *"पेटीएम पर पंद्रह रुपये प्राप्त हुए"*
  * *"फोनपे पर बीस रुपये प्राप्त हुए"*
  * *"गूगल पे पर पचास रुपये प्राप्त हुए"*
* **Automated Hands-Free Notification Interceptor**: Uses Android `NotificationListenerService` to detect incoming UPI payment alerts from **Paytm**, **PhonePe**, **Google Pay**, and **BHIM**, announcing them automatically without touching the screen.
* **Smart Filter**: Safely rejects debit alerts, bank balances, bill payments, and promotional cashback messages.
* **Embedded Driver UPI QR**: Displays driver VPA and scannable QR card directly on device.

### 3. 📒 Roz Ka Bhatta & Khata (दैनिक हिसाब-किताब)
* **Zero Jargon Formula**: Focuses exclusively on driver net pocket savings:
  $$\text{Net Pocket Profit} = \text{Gross Fare Earnings} - \text{Thekedar Bhatta} - \text{Battery Charging} - \text{Expenses}$$
* **WhatsApp Share**: Generates authentic localized daily shift summaries for vehicle owners (*thekedars*) or family.
* **Weekly 7-Day Performance Analytics**: Aggregates total gross income, fleet owner bhatta paid, charging expenses, and net profit over active shifts.

### 4. ⚡ Battery Range Estimator with Voltage Sag Filter (बैटरी व रेंज गाइड)
* **Multi-Chemistry Physics Engine**:
  * 48V Lead-Acid (42.0V cutoff – 50.4V full)
  * 48V LFP Lithium (44.0V cutoff – 58.4V full)
  * 60V LFP Lithium (55.0V cutoff – 73.0V full)
* **Exponential Moving Average (EMA) Sag Filter**:
  $$V_t = 0.25 V_{\text{raw}} + 0.75 V_{t-1}$$
  Dampens temporary 3–5 second acceleration dips, preventing false battery cutoffs and alert spam.
* **Dynamic Passenger Payload Adjustment**: Recalculates estimated range (km) based on active passenger count (0 to 4 riders).
* **Nearby Battery Swapping Directory**: Quick-call contacts for Battery Smart, Sun Mobility, and local charging hubs.

### 5. 🛡️ Safe Delete with Explicit Confirmation & Atomic Rollback
* **Accident-Proof Operations**: Guarded dialogs with vibration alerts ensure bumpy roads or pothole jolts never accidentally erase transactions or trip records.
* **Atomic Earnings Rollback**: Deleting a completed trip automatically updates the daily ledger in SQLite/Room, preventing accounting discrepancies:
  ```sql
  UPDATE daily_khata 
  SET grossEarnings = MAX(0.0, grossEarnings - :amount),
      totalPassengers = MAX(0, totalPassengers - :passengers),
      totalTrips = MAX(0, totalTrips - 1) 
  WHERE dateIso = :dateIso
  ```

### 6. 🌐 9 Regional Languages & Indian Rupee (INR - ₹) Engine (100% Offline)
* **Zero Network Translation Overhead**: All 9 language catalogs are embedded directly on-device with type-safe string catalogs.
* **Languages Supported**:
  1. **Hinglish (`hi-Latn`)**: Conversational Latin-script Hindi ("Sawari Baithi", "Awaz Box", "Khata", "Gadi Full") matching everyday phone usage.
  2. **Hindi (`hi`)**: Devanagari script ("सवारी", "आवाज़", "खाता", "बैटरी").
  3. **English (`en`)**: Clean standard terminology.
  4. **Bengali (`bn`)**: West Bengal & Tripura Toto market (টোটো চালক, "যাত্রী", "সাউন্ডবক্স", "খাতা", "টাকা").
  5. **Punjabi (`pa`)**: Gurmukhi script for Punjab & Delhi fleets (ਰਿਕਸ਼ਾ ਸਾਰਥੀ, "ਸਵਾਰੀ", "ਖਾਤਾ", "ਰੁਪਏ").
  6. **Gujarati (`gu`)**: Gujarat urban fleets ("પેસેન્જર", "સાઉન્ડબોક્સ", "ખાતાવહી", "રૂપિયા").
  7. **Marathi (`mr`)**: Maharashtra fleets ("प्रवासी", "खातेवही", "फेऱ्या").
  8. **Tamil (`ta`)**: Tamil Nadu auto transit ("பயணிகள்", "கணக்கு", "ரூபாய்").
  9. **Telugu (`te`)**: Andhra Pradesh and Telangana ("ప్రయాణికులు", "ఖాతా", "రూపాయలు").
* **Indian Rupee (₹) Currency Standard**: All amounts formatted according to the Indian numbering system (`₹15`, `₹1,250`, `₹1,50,000` with lakh and crore grouping) and pronounced in natural words by the speech soundbox.
* **Dynamic In-App Switcher**: Switch languages instantly on the fly via the top bar pill (`🌐 [Current] ▾`) without restarting the app.

---

## 🏗️ Technical Architecture & Dual-Native Parity

| Dimension | Android (`/app`) | iOS (`/iosApp`) |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.0.0 / JVM 17 | Swift 5.9 |
| **UI Framework** | Jetpack Compose + Material 3 | SwiftUI |
| **Target SDK / OS** | **API 35 (Android 15)** / Min API 26 | iOS 16.0+ |
| **Persistence** | Room SQLite Database + Coroutines Flow | ObservableObject Store + Codable |
| **Voice Synthesis** | Android `TextToSpeech` (9 regional voices) | iOS `AVSpeechSynthesizer` (9 regional voices) |
| **Languages** | **9 Languages** (Hinglish, Hindi, English, Bengali, Punjabi, Gujarati, Marathi, Tamil, Telugu) | **9 Languages** (Full Parity) |
| **Currency** | **Indian Rupee (INR - ₹)** with Indian grouping | **Indian Rupee (INR - ₹)** with Indian grouping |
| **Haptic Feedback** | Tactile `LocalHapticFeedback` | CoreHaptics / `UIImpactFeedback` |
| **Internet Dependency** | **100% Offline-First (0 Network Calls)** | **100% Offline-First (0 Network Calls)** |
| **Permissions** | Minimal (`VIBRATE`, `RECEIVE_BOOT_COMPLETED`) | None (Zero permissions required) |

---

## 📁 Repository Structure

```
vidyutgati/
├── .github/
│   ├── workflows/
│   │   ├── ci.yml                       # GitHub Actions CI build & test
│   │   └── release.yml                  # Automated release & packaging
│   ├── ISSUE_TEMPLATE/                  # Bug reports & feature requests
│   └── PULL_REQUEST_TEMPLATE.md         # Pull request guidelines
│
├── app/                                 # Native Android Application (Kotlin / Compose)
│   ├── src/main/AndroidManifest.xml     # Minimal manifest & NotificationListener
│   ├── src/main/java/com/vidyutgati/
│   │   ├── VidyutGatiApp.kt             # Application class
│   │   ├── MainActivity.kt              # Bottom Navigation & 4-tab Scaffold
│   │   ├── core/
│   │   │   ├── designsystem/           # High-contrast daylight theme & SafeDeleteDialog
│   │   │   ├── database/               # Room SQLite (VidyutDatabase, DAOs, Entities)
│   │   │   ├── soundbox/               # SoundboxEngine & UpiNotificationParser
│   │   │   └── battery/                # BatteryRangeEstimator & VoltageSagFilter
│   │   ├── domain/model/Models.kt       # Domain models
│   │   └── ui/                          # Compose screens (SeatCockpit, Soundbox, Khata, Battery)
│   └── src/test/java/com/vidyutgati/   # Comprehensive unit test suite (100% pass)
│
├── iosApp/                              # Native iOS Application (Swift / SwiftUI)
│   ├── VidyutGati/
│   │   ├── App/VidyutGatiApp.swift      # @main SwiftUI entry point
│   │   ├── Domain/Models.swift          # Parity models & TripItem
│   │   ├── Core/                        # Theme, SoundboxEngine, SagFilter & Estimator
│   │   ├── Data/LocalStore.swift        # Parity local persistence store
│   │   └── UI/                          # SwiftUI views with safe delete alerts
│   └── README.md
│
├── CONTRIBUTING.md                      # Contribution guidelines
├── SECURITY.md                          # Vulnerability reporting
├── CHANGELOG.md                         # Release notes & version history
├── LICENSE                              # MIT License (ProNextLabs)
└── build.gradle.kts                     # Root build script
```

---

## 🛠️ Build & Development

### Prerequisites
- JDK 17 (Eclipse Temurin or OpenJDK)
- Android SDK with Platform 35 (`compileSdk = 35`, `targetSdk = 35`)
- Gradle 8.7+

### Run Unit Tests
```bash
./gradlew testDebugUnitTest
```

### Build Production Signed APK (Direct Device Installation)
```bash
./gradlew assembleRelease
# Generated at: app/build/outputs/apk/release/app-release.apk
# Signed with v1 (JAR), v2, and v3 schemes for seamless Google Play Protect compatibility
```

### Build Google Play Store Android App Bundle (AAB)
```bash
./gradlew bundleRelease
# Generated at: app/build/outputs/bundle/release/app-release.aab
```

---

## 📄 License & Credits

Released under the **[MIT License](LICENSE)**.  
Copyright © 2026 **[ProNextLabs](https://github.com/pronextlabs)**.

*VidyutGati is proudly built for the unsung green warriors of urban mobility.*
