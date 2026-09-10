import Foundation

public enum PaymentMode: String, Codable, CaseIterable {
    case cash = "CASH"
    case upi = "UPI"
}

public enum PaymentApp: String, Codable, CaseIterable {
    case paytm = "Paytm"
    case phonepe = "PhonePe"
    case gpay = "Google Pay"
    case bhim = "BHIM UPI"

    public var hindiName: String {
        switch self {
        case .paytm: return "पेटीएम"
        case .phonepe: return "फोनपे"
        case .gpay: return "गूगल पे"
        case .bhim: return "भीम यूपीआई"
        }
    }
}

public struct SeatOccupancyState: Codable {
    public var currentOccupancy: Int
    public var maxCapacity: Int
    public var farePerSeat: Double
    public var routeOrigin: String
    public var routeDestination: String
    public var isForwardRoute: BooleanLiteralType

    public init(
        currentOccupancy: Int = 0,
        maxCapacity: Int = 4,
        farePerSeat: Double = 15.0,
        routeOrigin: String = "मेट्रो स्टेशन गेट 2",
        routeDestination: String = "सेक्टर 62 मार्केट",
        isForwardRoute: Bool = true
    ) {
        self.currentOccupancy = currentOccupancy
        self.maxCapacity = maxCapacity
        self.farePerSeat = farePerSeat
        self.routeOrigin = routeOrigin
        self.routeDestination = routeDestination
        self.isForwardRoute = isForwardRoute
    }

    public var isFull: Bool {
        return currentOccupancy >= maxCapacity
    }

    public var seatsRemaining: Int {
        return max(0, maxCapacity - currentOccupancy)
    }

    public var currentRouteDisplay: String {
        return isForwardRoute ? "\(routeOrigin) ➔ \(routeDestination)" : "\(routeDestination) ➔ \(routeOrigin)"
    }
}

public struct DailyKhata: Codable, Identifiable {
    public var id: String { dateIso }
    public var dateIso: String
    public var grossEarnings: Double
    public var thekedarRent: Double
    public var chargingExpense: Double
    public var otherExpenses: Double
    public var totalTrips: Int
    public var totalPassengers: Int

    public init(
        dateIso: String,
        grossEarnings: Double = 0.0,
        thekedarRent: Double = 350.0,
        chargingExpense: Double = 0.0,
        otherExpenses: Double = 0.0,
        totalTrips: Int = 0,
        totalPassengers: Int = 0
    ) {
        self.dateIso = dateIso
        self.grossEarnings = grossEarnings
        self.thekedarRent = thekedarRent
        self.chargingExpense = chargingExpense
        self.otherExpenses = otherExpenses
        self.totalTrips = totalTrips
        self.totalPassengers = totalPassengers
    }

    public var netProfit: Double {
        return grossEarnings - thekedarRent - chargingExpense - otherExpenses
    }

    public var isProfitable: Bool {
        return netProfit >= 0
    }
}

public enum BatteryChemistry: String, Codable, CaseIterable {
    case leadAcid48V = "48V लेड-एसिड"
    case lfp48V = "48V LFP लिथियम"
    case lfp60V = "60V LFP"

    public var cutoffVoltage: Double {
        switch self {
        case .leadAcid48V: return 42.0
        case .lfp48V: return 44.0
        case .lfp60V: return 55.0
        }
    }

    public var fullVoltage: Double {
        switch self {
        case .leadAcid48V: return 50.4
        case .lfp48V: return 58.4
        case .lfp60V: return 73.0
        }
    }
}

public struct BatteryHealthStatus {
    public let inputVoltage: Double
    public let chemistry: BatteryChemistry
    public let socPercentage: Int
    public let estimatedRangeKm: Int
    public let isCriticalLow: Bool
    public let warningMessage: String
}

public struct PaymentNotificationItem: Identifiable, Codable {
    public let id: String
    public let amount: Double
    public let appSource: PaymentApp
    public let timestamp: Date

    public init(id: String = UUID().uuidString, amount: Double, appSource: PaymentApp, timestamp: Date = Date()) {
        self.id = id
        self.amount = amount
        self.appSource = appSource
        self.timestamp = timestamp
    }
}

public struct TripItem: Identifiable, Codable {
    public let id: String
    public let routeName: String
    public let passengerCount: Int
    public let fareCollected: Double
    public let paymentMode: PaymentMode
    public let timestamp: Date

    public init(
        id: String = UUID().uuidString,
        routeName: String,
        passengerCount: Int,
        fareCollected: Double,
        paymentMode: PaymentMode,
        timestamp: Date = Date()
    ) {
        self.id = id
        self.routeName = routeName
        self.passengerCount = passengerCount
        self.fareCollected = fareCollected
        self.paymentMode = paymentMode
        self.timestamp = timestamp
    }
}
