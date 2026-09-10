import Foundation

public struct IOSBatteryRangeEstimator {

    public static func calculateSoC(voltage: Double, chemistry: BatteryChemistry) -> Int {
        if voltage <= chemistry.cutoffVoltage { return 0 }
        if voltage >= chemistry.fullVoltage { return 100 }

        let voltageRange = chemistry.fullVoltage - chemistry.cutoffVoltage
        let currentOffset = voltage - chemistry.cutoffVoltage
        let rawPercentage = (currentOffset / voltageRange) * 100.0

        return min(100, max(0, Int(rawPercentage)))
    }

    public static func estimateRangeKm(socPercentage: Int, chemistry: BatteryChemistry, passengerCount: Int = 0) -> Int {
        let baseMaxRangeKm: Double
        switch chemistry {
        case .leadAcid48V: baseMaxRangeKm = 65.0
        case .lfp48V: baseMaxRangeKm = 90.0
        case .lfp60V: baseMaxRangeKm = 115.0
        }

        let clampedPassengers = min(6, max(0, passengerCount))
        let loadFactor = 1.0 - (Double(clampedPassengers) * 0.065)

        let remainingRange = (Double(socPercentage) / 100.0) * baseMaxRangeKm * loadFactor
        return max(0, Int(remainingRange))
    }

    public static func evaluateHealth(voltage: Double, chemistry: BatteryChemistry, passengerCount: Int = 0) -> BatteryHealthStatus {
        let soc = calculateSoC(voltage: voltage, chemistry: chemistry)
        let range = estimateRangeKm(socPercentage: soc, chemistry: chemistry, passengerCount: passengerCount)
        let isLow = soc <= 20 || range <= 10

        let warning: String
        if soc <= 10 {
            warning = "⚠️ अति गंभीर! तुरंत चार्जिंग या स्वैप स्टेशन जाएं"
        } else if soc <= 20 {
            warning = "⚠️ बैटरी कम है (20% से कम)! अगली सवारी से पहले चार्ज करें"
        } else if soc <= 40 {
            warning = "⚡ सामान्य स्तर (40% से नीचे) - शाम की शिफ्ट के लिए ध्यान दें"
        } else {
            warning = "✅ बैटरी स्वस्थ है (पर्याप्त चार्ज)"
        }

        return BatteryHealthStatus(
            inputVoltage: voltage,
            chemistry: chemistry,
            socPercentage: soc,
            estimatedRangeKm: range,
            isCriticalLow: isLow,
            warningMessage = warning
        )
    }
}
