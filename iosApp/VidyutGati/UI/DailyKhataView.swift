import SwiftUI

public struct DailyKhataView: View {
    @ObservedObject private var store = VidyutLocalStore.shared
    @ObservedObject private var localization = LocalizationManager.shared
    @State private var showResetConfirmAlert: Bool = false

    private var netProfitColor: Color {
        store.todayKhata.isProfitable ? VidyutTheme.emeraldProfit : VidyutTheme.coralAlert
    }

    public var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                // Header
                HStack {
                    VStack(alignment: .leading, spacing: 2) {
                        Text(localization.strings.dailyKhataTitle)
                            .font(.title3)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                        Text(Date().formatted(date: .complete, time: .omitted))
                            .font(.caption)
                            .foregroundColor(VidyutTheme.textMuted)
                    }
                    Spacer()
                    ShareLink(item: generateShareSummary()) {
                        HStack(spacing: 4) {
                            Image(systemName: "square.and.arrow.up")
                            Text(localization.strings.shareWhatsAppButton)
                                .fontWeight(.bold)
                        }
                        .font(.subheadline)
                        .foregroundColor(.black)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 8)
                        .background(VidyutTheme.emeraldProfit)
                        .cornerRadius(10)
                    }
                }

                // Net Profit Hero Card
                VStack(spacing: 12) {
                    Text(localization.strings.netProfitTitle)
                        .font(.subheadline)
                        .foregroundColor(VidyutTheme.textMuted)

                    Text(IndianCurrencyFormatter.formatInr(store.todayKhata.netProfit))
                        .font(.system(size: 52, weight: .black))
                        .foregroundColor(netProfitColor)

                    Text(store.todayKhata.isProfitable ? localization.strings.profitBadge : localization.strings.lossBadge)
                        .font(.caption)
                        .fontWeight(.bold)
                        .foregroundColor(netProfitColor)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 4)
                        .background(netProfitColor.opacity(0.15))
                        .cornerRadius(10)

                    Divider().background(VidyutTheme.borderSlate)

                    HStack {
                        Spacer()
                        VStack {
                            Text(localization.strings.passengersLabel)
                                .font(.caption2)
                                .foregroundColor(VidyutTheme.textMuted)
                            Text("\(store.todayKhata.totalPassengers)")
                                .font(.headline)
                                .foregroundColor(.white)
                        }
                        Spacer()
                        VStack {
                            Text(localization.strings.tripsLabel)
                                .font(.caption2)
                                .foregroundColor(VidyutTheme.textMuted)
                            Text("\(store.todayKhata.totalTrips)")
                                .font(.headline)
                                .foregroundColor(.white)
                        }
                        Spacer()
                    }
                }
                .padding()
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(20)

                // Itemized Breakdown Cards
                // Gross Earnings
                KhataItemCard(
                    title: localization.strings.grossEarningsLabel,
                    amount: store.todayKhata.grossEarnings,
                    icon: "indianrupeesign.circle.fill",
                    iconColor: VidyutTheme.emeraldProfit,
                    isExpense: false,
                    onAdd20: { store.todayKhata.grossEarnings += 20; store.saveKhata() },
                    onAdd50: { store.todayKhata.grossEarnings += 50; store.saveKhata() },
                    onAdd100: { store.todayKhata.grossEarnings += 100; store.saveKhata() }
                )

                // Thekedar Bhatta
                HStack {
                    Image(systemName: "building.2.fill")
                        .font(.title2)
                        .foregroundColor(VidyutTheme.coralAlert)
                    VStack(alignment: .leading, spacing: 2) {
                        Text(localization.strings.thekedarRentLabel)
                            .font(.subheadline)
                            .fontWeight(.semibold)
                            .foregroundColor(.white)
                        Text("दैनिक फिक्स किराया")
                            .font(.caption2)
                            .foregroundColor(VidyutTheme.textMuted)
                    }
                    Spacer()
                    Text("-\(IndianCurrencyFormatter.formatInr(store.todayKhata.thekedarRent))")
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(VidyutTheme.coralAlert)
                }
                .padding()
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(14)

                // Battery Charging Expense
                KhataItemCard(
                    title: localization.strings.chargingExpenseLabel,
                    amount: store.todayKhata.chargingExpense,
                    icon: "bolt.fill",
                    iconColor: VidyutTheme.electricAmber,
                    isExpense: true,
                    labels: ["+₹50", "+₹100", "+₹150"],
                    onAdd20: { store.todayKhata.chargingExpense += 50; store.saveKhata() },
                    onAdd50: { store.todayKhata.chargingExpense += 100; store.saveKhata() },
                    onAdd100: { store.todayKhata.chargingExpense += 150; store.saveKhata() }
                )

                // Other Expenses
                KhataItemCard(
                    title: localization.strings.otherExpensesLabel,
                    amount: store.todayKhata.otherExpenses,
                    icon: "wrench.and.screwdriver.fill",
                    iconColor: VidyutTheme.cyanRoute,
                    isExpense: true,
                    onAdd20: { store.todayKhata.otherExpenses += 20; store.saveKhata() },
                    onAdd50: { store.todayKhata.otherExpenses += 50; store.saveKhata() },
                    onAdd100: { store.todayKhata.otherExpenses += 100; store.saveKhata() }
                )

                // Safe Reset Today's Khata
                Button(action: {
                    showResetConfirmAlert = true
                }) {
                    HStack {
                        Image(systemName: "arrow.counterclockwise.circle")
                        Text(localization.strings.resetKhataButton)
                            .fontWeight(.bold)
                    }
                    .font(.subheadline)
                    .foregroundColor(VidyutTheme.coralAlert)
                    .frame(maxWidth: .infinity)
                    .frame(height: 44)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(VidyutTheme.coralAlert.opacity(0.6), lineWidth: 1)
                    )
                }
                .padding(.top, 8)

                // ProNextLabs Credits
                Text("⚡ विद्युतगति • Open-Source by ProNextLabs\n100% Offline • Made with ❤️ for Sarathis")
                    .font(.caption2)
                    .multilineTextAlignment(.center)
                    .foregroundColor(VidyutTheme.textMuted)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 8)
            }
            .padding()
        }
        .background(VidyutTheme.deepObsidian.ignoresSafeArea())
        .alert(localization.strings.resetKhataPromptTitle, isPresented: $showResetConfirmAlert) {
            Button(localization.strings.cancelAction, role: .cancel) {}
            Button(localization.strings.deleteAction, role: .destructive) {
                store.resetKhata()
            }
        } message: {
            Text(localization.strings.resetKhataPromptMessage)
        }
    }

    private func generateShareSummary() -> String {
        let k = store.todayKhata
        return """
🛺 *\(localization.strings.appTitle) - \(localization.strings.dailyKhataTitle)*
━━━━━━━━━━━━━━━━━━
💰 \(localization.strings.grossEarningsLabel): \(IndianCurrencyFormatter.formatInr(k.grossEarnings)) (\(k.totalPassengers) \(localization.strings.passengersLabel) / \(k.totalTrips) \(localization.strings.tripsLabel))
🏢 \(localization.strings.thekedarRentLabel): -\(IndianCurrencyFormatter.formatInr(k.thekedarRent))
⚡ \(localization.strings.chargingExpenseLabel): -\(IndianCurrencyFormatter.formatInr(k.chargingExpense))
🔧 \(localization.strings.otherExpensesLabel): -\(IndianCurrencyFormatter.formatInr(k.otherExpenses))
━━━━━━━━━━━━━━━━━━
✅ *\(localization.strings.netProfitTitle): \(IndianCurrencyFormatter.formatInr(k.netProfit))*
━━━━━━━━━━━━━━━━━━
_ProNextLabs VidyutGati_
        """
    }
}

struct KhataItemCard: View {
    let title: String
    let amount: Double
    let icon: String
    let iconColor: Color
    let isExpense: Bool
    var labels: [String] = ["+₹20", "+₹50", "+₹100"]
    let onAdd20: () -> Void
    let onAdd50: () -> Void
    let onAdd100: () -> Void

    var body: some View {
        VStack(spacing: 10) {
            HStack {
                Image(systemName: icon)
                    .font(.title3)
                    .foregroundColor(iconColor)
                Text(title)
                    .font(.subheadline)
                    .fontWeight(.semibold)
                    .foregroundColor(.white)
                Spacer()
                Text("\(isExpense ? "-" : "")\(IndianCurrencyFormatter.formatInr(amount))")
                    .font(.headline)
                    .fontWeight(.black)
                    .foregroundColor(isExpense ? VidyutTheme.coralAlert : VidyutTheme.emeraldProfit)
            }

            HStack(spacing: 8) {
                Button(action: onAdd20) {
                    Text(labels[0])
                        .font(.caption)
                        .fontWeight(.bold)
                        .frame(maxWidth: .infinity)
                        .frame(height: 34)
                        .background(VidyutTheme.borderSlate)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
                Button(action: onAdd50) {
                    Text(labels[1])
                        .font(.caption)
                        .fontWeight(.bold)
                        .frame(maxWidth: .infinity)
                        .frame(height: 34)
                        .background(VidyutTheme.borderSlate)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
                Button(action: onAdd100) {
                    Text(labels[2])
                        .font(.caption)
                        .fontWeight(.bold)
                        .frame(maxWidth: .infinity)
                        .frame(height: 34)
                        .background(VidyutTheme.borderSlate)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
            }
        }
        .padding()
        .background(VidyutTheme.surfaceSlate)
        .cornerRadius(14)
    }
}
