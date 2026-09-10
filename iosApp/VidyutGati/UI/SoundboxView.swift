import SwiftUI

public struct SoundboxView: View {
    @StateObject private var soundbox = IOSSoundboxEngine()
    @ObservedObject private var store = VidyutLocalStore.shared
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

                    Text("मुफ़्त आवाज़ बॉक्स (Digital Soundbox)")
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)

                    Text("बिना किसी डिवाइस किराये के 100% मुफ़्त")
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
                        Text("भुगतान आवाज़ ट्रिगर करें (Tap to Announce):")
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
                                    Text("₹\(Int(amt))")
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
                            Text("पिछली आवाज़ दुबारा सुनें (Repeat)")
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
                        Text("सारथी यूपीआई आईडी (Driver UPI)")
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
                    Text("हाल के भुगतान (Payment History)")
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)

                    if store.recentPayments.isEmpty {
                        Text("अभी कोई नया भुगतान नहीं है। ऊपर दिए गए बटनों से टेस्ट करें!")
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
                                Text("+₹\(Int(payment.amount))")
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
        .alert("पेमेंट रिकॉर्ड हटाएं? (Delete Entry)", isPresented: $showDeleteConfirmAlert, presenting: paymentToDelete) { payment in
            Button("रद्द करें (Cancel)", role: .cancel) {
                paymentToDelete = nil
            }
            Button("हटाएं (Delete)", role: .destructive) {
                store.deletePayment(id: payment.id)
                paymentToDelete = nil
            }
        } message: { payment in
            Text("क्या आप सच में \(payment.appSource.rawValue) से प्राप्त ₹\(Int(payment.amount)) का यह रिकॉर्ड हटाना चाहते हैं? यह क्रिया वापस नहीं ली जा सकती।")
        }
    }
}
