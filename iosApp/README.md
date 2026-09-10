# VidyutGati (विद्युतगति) - iOS Application

<p align="center">
  <strong>विद्युतगति: ई-रिक्शा सारथी का डिजिटल साथी</strong><br>
  <em>100% Offline • High-Contrast Sunlight UI • Voice Soundbox • Driver Khata</em>
</p>

## Overview
The iOS application of **VidyutGati** is natively crafted in **Swift 5.9** and **SwiftUI**, maintaining 100% architectural and domain parity with the Android Jetpack Compose counterpart.

## Architecture
- **Framework**: SwiftUI (iOS 16.0+)
- **Storage**: Local persistent store (`UserDefaults` + Codable JSON snapshot) with zero cloud requirements
- **Voice Soundbox**: Native `AVSpeechSynthesizer` with Hindi (`hi-IN`) currency and alert synthesis
- **Battery Physics Model**: Open-circuit voltage discharge curve calculation for 48V Lead-Acid, 48V LFP, and 60V LFP chemistry

## Build Instructions
1. Open the project in **Xcode 15+**.
2. Target iOS 16.0+ simulator or connected iPhone.
3. Build and Run (**Cmd + R**).
