package com.vidyutgati.domain.model

data class SeatOccupancyState(
    val currentOccupancy: Int = 0,
    val maxCapacity: Int = 4,
    val farePerSeat: Double = 15.0,
    val routeOrigin: String = "मेट्रो स्टेशन गेट 2",
    val routeDestination: String = "सेक्टर 62 मार्केट",
    val isForwardRoute: Boolean = true
) {
    val isFull: Boolean get() = currentOccupancy >= maxCapacity
    val seatsRemaining: Int get() = (maxCapacity - currentOccupancy).coerceAtLeast(0)
    
    val currentRouteDisplay: String get() = if (isForwardRoute) {
        "$routeOrigin ➔ $routeDestination"
    } else {
        "$routeDestination ➔ $routeOrigin"
    }
}

data class DailyKhata(
    val dateIso: String,
    val grossEarnings: Double = 0.0,
    val thekedarRent: Double = 350.0,
    val chargingExpense: Double = 0.0,
    val otherExpenses: Double = 0.0,
    val totalTrips: Int = 0,
    val totalPassengers: Int = 0
) {
    val netProfit: Double get() = grossEarnings - thekedarRent - chargingExpense - otherExpenses
    val isProfitable: Boolean get() = netProfit >= 0
}

data class Trip(
    val id: Long = 0,
    val routeName: String,
    val timestampEpoch: Long = System.currentTimeMillis(),
    val passengerCount: Int,
    val fareCollected: Double,
    val paymentMode: PaymentMode = PaymentMode.CASH
)

enum class PaymentMode {
    CASH,
    UPI
}

data class PaymentEvent(
    val id: String = java.util.UUID.randomUUID().toString(),
    val amount: Double,
    val appSource: PaymentApp = PaymentApp.PAYTM,
    val timestampEpoch: Long = System.currentTimeMillis(),
    val isSpoken: Boolean = false
)

enum class PaymentApp(val displayName: String, val hindiName: String) {
    PAYTM("Paytm", "पेटीएम"),
    PHONEPE("PhonePe", "फोनपे"),
    GPAY("Google Pay", "गूगल पे"),
    BHIM("BHIM UPI", "भीम यूपीआई")
}

enum class BatteryChemistry(val nominalVoltage: Double, val cutoffVoltage: Double, val fullVoltage: Double) {
    LEAD_ACID_48V(nominalVoltage = 48.0, cutoffVoltage = 42.0, fullVoltage = 50.4),
    LFP_48V(nominalVoltage = 51.2, cutoffVoltage = 44.0, fullVoltage = 58.4),
    LFP_60V(nominalVoltage = 64.0, cutoffVoltage = 55.0, fullVoltage = 73.0)
}

data class BatteryHealthStatus(
    val inputVoltage: Double,
    val chemistry: BatteryChemistry,
    val socPercentage: Int,
    val estimatedRangeKm: Int,
    val isCriticalLow: Boolean,
    val warningMessage: String
)
