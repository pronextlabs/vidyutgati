# Contributing to VidyutGati (विद्युतगति)

Thank you for your interest in contributing to **VidyutGati**! This project is maintained by **[ProNextLabs](https://github.com/pronextlabs)** as an open-source initiative to empower 2.4M+ E-Rickshaw drivers across India and South Asia with free, offline-first digital mobility tools.

---

## 🧭 Principles to Respect

1. **100% Offline-First**: Every core feature (Seat Cockpit, Soundbox, Khata, Battery Range) must function without active mobile internet connectivity. No cloud lock-in.
2. **Driver Safety & Ergonomics**: Minimum 48dp (ideally 64–72dp) touch targets. Never introduce distracting micro-interactions while the vehicle is in motion.
3. **Safe Deletion & Atomic Rollback**: Any record deletion must require explicit confirmation and maintain zero ledger corruption.
4. **Daylight Contrast**: Use our curated color tokens (Deep Obsidian `#0A0D14`, Electric Amber `#F59E0B`, Cyan Route `#06B6D4`, Emerald Profit `#10B981`, Coral Alert `#EF4444`).

---

## 🛠️ Development Workflow

### Prerequisites
- JDK 17
- Android SDK 35 (Android 15)
- Gradle 8.7+
- (For iOS) Xcode 15+ & Swift 5.9

### Local Build & Test
```bash
# Clone the repository
git clone https://github.com/pronextlabs/vidyutgati.git
cd vidyutgati

# Run unit tests
./gradlew testDebugUnitTest

# Assemble Debug APK
./gradlew assembleDebug

# Build Play Store App Bundle (AAB)
./gradlew bundleDebug
```

---

## 🔀 Submitting Changes

1. Fork the repo and create your feature branch: `git checkout -b feature/amazing-feature`
2. Commit your changes following Conventional Commits: `git commit -m 'feat: add regional voice language support'`
3. Ensure all tests pass: `./gradlew testDebugUnitTest`
4. Push to your branch: `git push origin feature/amazing-feature`
5. Open a Pull Request on GitHub.

---

## 🤝 Community & Support

Built with pride by **ProNextLabs** ([@pronextlabs](https://github.com/pronextlabs)).
