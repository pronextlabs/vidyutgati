import Foundation

public final class VidyutLocalStore: ObservableObject {
    public static let shared = VidyutLocalStore()

    @Published public var todayKhata: DailyKhata
    @Published public var recentPayments: [PaymentNotificationItem] = []
    @Published public var recentTrips: [TripItem] = []

    private let khataKey = "vidyutgati_daily_khata"
    private let paymentsKey = "vidyutgati_recent_payments"
    private let tripsKey = "vidyutgati_recent_trips"

    private init() {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        let todayIso = formatter.string(from: Date())

        if let data = UserDefaults.standard.data(forKey: khataKey),
           let decoded = try? JSONDecoder().decode(DailyKhata.self, from: data),
           decoded.dateIso == todayIso {
            self.todayKhata = decoded
        } else {
            self.todayKhata = DailyKhata(dateIso: todayIso)
        }

        if let payData = UserDefaults.standard.data(forKey: paymentsKey),
           let decodedPays = try? JSONDecoder().decode([PaymentNotificationItem].self, from: payData) {
            self.recentPayments = decodedPays
        }

        if let tripsData = UserDefaults.standard.data(forKey: tripsKey),
           let decodedTrips = try? JSONDecoder().decode([TripItem].self, from: tripsData) {
            self.recentTrips = decodedTrips
        }
    }

    public func saveKhata() {
        if let encoded = try? JSONEncoder().encode(todayKhata) {
            UserDefaults.standard.set(encoded, forKey: khataKey)
        }
    }

    public func saveTrips() {
        if let encoded = try? JSONEncoder().encode(recentTrips) {
            UserDefaults.standard.set(encoded, forKey: tripsKey)
        }
    }

    public func addTrip(routeName: String, passengers: Int, fare: Double, mode: PaymentMode) {
        let trip = TripItem(routeName: routeName, passengerCount: passengers, fareCollected: fare, paymentMode: mode)
        recentTrips.insert(trip, at: 0)
        if recentTrips.count > 50 {
            recentTrips = Array(recentTrips.prefix(50))
        }
        todayKhata.grossEarnings += fare
        todayKhata.totalPassengers += passengers
        todayKhata.totalTrips += 1
        saveKhata()
        saveTrips()
    }

    public func addTrip(passengers: Int, fare: Double) {
        addTrip(routeName: "रूट ट्रिप", passengers: passengers, fare: fare, mode: .cash)
    }

    public func deleteTrip(id: String) {
        if let idx = recentTrips.firstIndex(where: { $0.id == id }) {
            let trip = recentTrips[idx]
            todayKhata.grossEarnings = max(0.0, todayKhata.grossEarnings - trip.fareCollected)
            todayKhata.totalPassengers = max(0, todayKhata.totalPassengers - trip.passengerCount)
            todayKhata.totalTrips = max(0, todayKhata.totalTrips - 1)
            recentTrips.remove(at: idx)
            saveKhata()
            saveTrips()
        }
    }

    public func addPayment(amount: Double, appSource: PaymentApp) {
        let payment = PaymentNotificationItem(amount: amount, appSource: appSource, timestamp: Date())
        recentPayments.insert(payment, at: 0)
        if recentPayments.count > 30 {
            recentPayments = Array(recentPayments.prefix(30))
        }
        if let encoded = try? JSONEncoder().encode(recentPayments) {
            UserDefaults.standard.set(encoded, forKey: paymentsKey)
        }
    }

    public func deletePayment(id: String) {
        recentPayments.removeAll { $0.id == id }
        if let encoded = try? JSONEncoder().encode(recentPayments) {
            UserDefaults.standard.set(encoded, forKey: paymentsKey)
        }
    }

    public func resetKhata() {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        let todayIso = formatter.string(from: Date())
        todayKhata = DailyKhata(dateIso: todayIso)
        saveKhata()
    }
}
