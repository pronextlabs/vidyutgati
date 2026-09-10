import SwiftUI

public struct BatteryView: View {
    @State private var selectedChemistry: BatteryChemistry = .lfp48V
    @State private var voltage: Double = 52.5
    @State private var passengerLoad: Int = 2
    @StateObject private var soundbox = IOSSoundboxEngine()

    private var status: BatteryHealthStatus {
        IOSBatteryRangeEstimator.evaluateHealth(voltage: voltage, chemistry: selectedChemistry, passengerCount: passengerLoad)
    }

    private var batteryColor: Color {
        if status.socPercentage <= 15 {
            return VidyutTheme.coralAlert
        } else if status.socPercentage <= 35 {
            return VidyutTheme.electricAmber
        } else {
            return VidyutTheme.emeraldProfit
        }
    }

    public var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                // 1. Battery Gauge Hero Card
                VStack(spacing: 12) {
                    ZStack {
                        Circle()
                            .fill(batteryColor.opacity(0.2))
                            .frame(width: 68, height: 68)
                        Image(systemName: status.isCriticalLow ? "battery.0percent" : "battery.100percent.bolt")
                            .font(.system(size: 34))
                            .foregroundColor(batteryColor)
                    }

                    HStack(alignment: .bottom, spacing: 4) {
                        Text("\(status.estimatedRangeKm)")
                            .font(.system(size: 60, weight: .black))
                            .foregroundColor(batteryColor)
                        Text("KM")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(VidyutTheme.textMuted)
                            .padding(.bottom, 8)
                    }

                    Text("अनुमानित बची हुई दूरी (Estimated Range)")
                        .font(.footnote)
                        .foregroundColor(VidyutTheme.textMuted)

                    Text("\(status.socPercentage)% चार्ज | \(String(format: "%.1f", voltage))V")
                        .font(.subheadline)
                        .fontWeight(.bold)
                        .foregroundColor(batteryColor)
                        .padding(.horizontal, 14)
                        .padding(.vertical, 6)
                        .background(batteryColor.opacity(0.15))
                        .cornerRadius(10)

                    Text(status.warningMessage)
                        .font(.footnote)
                        .fontWeight(.semibold)
                        .foregroundColor(status.isCriticalLow ? VidyutTheme.coralAlert : .white)
                        .multilineTextAlignment(.center)
                        .padding(.horizontal)

                    // Voice Alert Button
                    Button(action: {
                        let msg = "बैटरी \(status.socPercentage) प्रतिशत है। अनुमानित रेंज \(status.estimatedRangeKm) किलोमीटर बची है।"
                        soundbox.speakAlert(messageHindi: msg)
                    }) {
                        HStack {
                            Image(systemName: "speaker.wave.2.fill")
                            Text("आवाज़ में स्थिति सुनें (Speak Status)")
                        }
                        .font(.subheadline)
                        .fontWeight(.bold)
                        .foregroundColor(VidyutTheme.cyanRoute)
                        .frame(maxWidth: .infinity)
                        .frame(height: 44)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(VidyutTheme.cyanRoute, lineWidth: 1.5)
                        )
                    }
                }
                .padding()
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(20)

                // 2. Battery Chemistry Selector
                VStack(alignment: .leading, spacing: 8) {
                    Text("बैटरी प्रकार (Battery Chemistry):")
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)

                    HStack(spacing: 8) {
                        ForEach(BatteryChemistry.allCases, id: \.self) { chem in
                            let isSelected = selectedChemistry == chem
                            Button(action: {
                                selectedChemistry = chem
                                switch chem {
                                case .leadAcid48V: voltage = 48.0
                                case .lfp48V: voltage = 52.5
                                case .lfp60V: voltage = 65.0
                                }
                            }) {
                                Text(chem.rawValue)
                                    .font(.caption)
                                    .fontWeight(isSelected ? .bold : .regular)
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 38)
                                    .background(isSelected ? VidyutTheme.electricAmber : VidyutTheme.borderSlate)
                                    .foregroundColor(isSelected ? .black : .white)
                                    .cornerRadius(8)
                            }
                        }
                    }
                }

                // 3. Voltage Stepper
                VStack(alignment: .leading, spacing: 8) {
                    HStack {
                        Text("वर्तमान वोल्टेज:")
                            .font(.subheadline)
                            .fontWeight(.semibold)
                            .foregroundColor(.white)
                        Spacer()
                        Text("\(String(format: "%.1f", voltage))V")
                            .font(.headline)
                            .fontWeight(.bold)
                            .foregroundColor(VidyutTheme.electricAmber)
                    }

                    Slider(value: $voltage, in: selectedChemistry.cutoffVoltage...selectedChemistry.fullVoltage, step: 0.2)
                        .accentColor(VidyutTheme.electricAmber)
                }
                .padding()
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(16)

                // 4. Passenger Load Adjustment
                VStack(alignment: .leading, spacing: 8) {
                    Text("वर्तमान सवारी लोड (Passenger Weight):")
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)

                    HStack(spacing: 8) {
                        ForEach([(0, "खाली (0)"), (2, "2 सवारियाँ"), (4, "फुल (4)")], id: \.0) { item in
                            let isSelected = passengerLoad == item.0
                            Button(action: { passengerLoad = item.0 }) {
                                Text(item.1)
                                    .font(.caption)
                                    .fontWeight(isSelected ? .bold : .regular)
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 38)
                                    .background(isSelected ? VidyutTheme.cyanRoute : VidyutTheme.borderSlate)
                                    .foregroundColor(isSelected ? .black : .white)
                                    .cornerRadius(8)
                            }
                        }
                    }
                }

                // 5. Swap Stations Directory
                VStack(alignment: .leading, spacing: 10) {
                    HStack {
                        Image(systemName: "ev.charger.fill")
                            .foregroundColor(VidyutTheme.emeraldProfit)
                        Text("निकटतम बैटरी स्वैप केंद्र (Nearby Swap Stalls)")
                            .font(.subheadline)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                    }

                    ForEach([
                        ("बैटरी स्मार्ट हब - मेट्रो पिलर 142", "खुला है • ₹160/स्वैप"),
                        ("सन मोबिलिटी स्टेशन - सेक्टर 62", "खुला है • ₹180/स्वैप"),
                        ("राजू ई-रिक्शा पॉइंट - अट्टा मार्केट", "खुला है • ₹40/घंटा")
                    ], id: \.0) { item in
                        HStack {
                            VStack(alignment: .leading, spacing: 2) {
                                Text(item.0)
                                    .font(.caption)
                                    .fontWeight(.semibold)
                                    .foregroundColor(.white)
                                Text(item.1)
                                    .font(.caption2)
                                    .foregroundColor(VidyutTheme.emeraldProfit)
                            }
                            Spacer()
                            Image(systemName: "phone.circle.fill")
                                .font(.title3)
                                .foregroundColor(VidyutTheme.cyanRoute)
                        }
                        .padding(.vertical, 4)
                    }
                }
                .padding()
                .background(VidyutTheme.surfaceSlate.opacity(0.7))
                .cornerRadius(16)
            }
            .padding()
        }
        .background(VidyutTheme.deepObsidian.ignoresSafeArea())
    }
}
