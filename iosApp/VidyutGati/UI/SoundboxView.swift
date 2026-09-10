import SwiftUI

public struct SoundboxView: View {
    @StateObject private var soundbox = IOSSoundboxEngine()
    @ObservedObject private var store = VidyutLocalStore.shared
    @ObservedObject private var localization = LocalizationManager.shared
    @State private var selectedApp: PaymentApp = .paytm
    @State private var lastAmount: Double? = nil

    @State private var paymentToDelete: PaymentNotificationItem? = nil
    @State private var showDeleteConfirmAlert: Bool = false

    private let quickAmounts: [Double] = [10, 15, 20, 30, 50, 100]

    public var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                // 1. Digital Soundbox Hero Card
                VStack(spacing: 14) {
                    ZStack {
                        Circle()
                            .fill(VidyutTheme.electricAmber.opacity(0.2))
                            .frame(width: 64, height: 64)
                        Image(systemName: "speaker.wave.3.fill")
                            .font(.system(size: 30))
                            .foregroundColor(VidyutTheme.electricAmber)
                    }

                    Text(localization.strings.soundboxTitle)
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)

                    Text(localization.strings.soundboxSubtitle)
                        .font(.caption)
                        .foregroundColor(VidyutTheme.textMuted)

                    // App Selector
                    HStack(spacing: 6) {
                        ForEach(PaymentApp.allCases, id: \.self) { app in
                            let isSelected = selectedApp == app
                            Button(action: {
                                selectedApp = app
                            }) {
                                Text(app.rawValue)
                                    .font(.caption)
                                    .fontWeight(isSelected ? .bold : .regular)
                                    .padding(.horizontal, 10)
                                    .padding(.vertical, 6)
                                    .background(isSelected ? VidyutTheme.electricAmber : VidyutTheme.borderSlate)
                                    .foregroundColor(isSelected ? .black : .white)
                                    .cornerRadius(8)
                            }
                        }
                    }

                    // Quick Tap Amounts
                    VStack(alignment: .leading, spacing: 8) {
                        Text(localization.strings.tapToAnnounce)
                            .font(.caption)
                            .fontWeight(.semibold)
                            .foregroundColor(VidyutTheme.textMuted)

                        LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())], spacing: 10) {
                            ForEach(quickAmounts, id: \.self) { amt in
                                Button(action: {
                                    lastAmount = amt
                                    soundbox.announcePayment(amount: amt, appSource: selectedApp)
                                    store.addPayment(amount: amt, appSource: selectedApp)
                                }) {
                                    Text(IndianCurrencyFormatter.formatInr(amt))
                                        .font(.title3)
                                        .fontWeight(.black)
                                        .foregroundColor(.black)
                                        .frame(maxWidth: .infinity)
                                        .frame(height: 50)
                                        .background(amt <= 20 ? VidyutTheme.emeraldProfit : VidyutTheme.cyanRoute)
                                        .cornerRadius(12)
                                }
                            }
                        }
                    }

                    // Repeat Button
                    Button(action: {
                        let amt = lastAmount ?? 15.0
                        soundbox.announcePayment(amount: amt, appSource: selectedApp)
                    }) {
                        HStack {
                            Image(systemName: "arrow.counterclockwise")
                            Text(localization.strings.repeatAnnouncement)
                        }
                        .font(.subheadline)
                        .fontWeight(.bold)
                        .foregroundColor(VidyutTheme.electricAmber)
                        .frame(maxWidth: .infinity)
                        .frame(height: 44)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(VidyutTheme.electricAmber, lineWidth: 1.5)
                        )
                    }
                }
                .padding()
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(20)

                // 2. Driver UPI Card
                HStack(spacing: 14) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 8)
                            .fill(Color.white)
                            .frame(width: 52, height: 52)
                        Image(systemName: "qrcode")
                            .font(.system(size: 36))
                            .foregroundColor(.black)
                    }

                    VStack(alignment: .leading, spacing: 2) {
                        Text(localization.strings.driverUpiTitle)
                            .font(.caption)
                            .foregroundColor(VidyutTheme.textMuted)
                        Text("driver.vidyutgati@upi")
                            .font(.subheadline)
                            .fontWeight(.bold)
                            .foregroundColor(VidyutTheme.electricAmber)
                        Text("सवारी से सीधे अपने खाते में पैसे लें")
                            .font(.caption2)
                            .foregroundColor(VidyutTheme.emeraldProfit)
                    }
                    Spacer()
                }
                .padding()
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(16)

                // 3. Payment History
                VStack(alignment: .leading, spacing: 10) {
                    Text(localization.strings.recentPayments)
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)

                    if store.recentPayments.isEmpty {
                        Text(localization.strings.noPaymentsYet)
                            .font(.footnote)
                            .foregroundColor(VidyutTheme.textMuted)
                            .padding()
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(VidyutTheme.surfaceSlate.opacity(0.5))
                            .cornerRadius(12)
                    } else {
                        ForEach(store.recentPayments) { payment in
                            HStack {
                                VStack(alignment: .leading, spacing: 2) {
                                    Text("\(payment.appSource.rawValue) से प्राप्त")
                                        .font(.subheadline)
                                        .fontWeight(.semibold)
                                        .foregroundColor(.white)
                                    Text(payment.timestamp.formatted(date: .omitted, time: .shortened))
                                        .font(.caption)
                                        .foregroundColor(VidyutTheme.textMuted)
                                }
                                Spacer()
                                Text("+\(IndianCurrencyFormatter.formatInr(payment.amount))")
                                    .font(.title3)
                                    .fontWeight(.black)
                                    .foregroundColor(VidyutTheme.emeraldProfit)

                                Button(action: {
                                    paymentToDelete = payment
                                    showDeleteConfirmAlert = true
                                }) {
                                    Image(systemName: "trash")
                                        .font(.subheadline)
                                        .foregroundColor(VidyutTheme.coralAlert.opacity(0.8))
                                        .padding(.leading, 8)
                                }
                            }
                            .padding()
                            .background(VidyutTheme.surfaceSlate)
                            .cornerRadius(12)
                        }
                    }
                }
            }
            .padding()
        }
        .background(VidyutTheme.deepObsidian.ignoresSafeArea())
        .alert(localization.strings.deletePaymentPromptTitle, isPresented: $showDeleteConfirmAlert, presenting: paymentToDelete) { payment in
            Button(localization.strings.cancelAction, role: .cancel) {
                paymentToDelete = nil
            }
            Button(localization.strings.deleteAction, role: .destructive) {
                store.deletePayment(id: payment.id)
                paymentToDelete = nil
            }
        } message: { payment in
            Text(localization.strings.deletePaymentPromptMessage(payment.appSource.rawValue, Int(payment.amount)))
        }
    }
}
