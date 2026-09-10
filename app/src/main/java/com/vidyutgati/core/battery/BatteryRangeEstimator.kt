package com.vidyutgati.core.battery

import com.vidyutgati.domain.model.BatteryChemistry
import com.vidyutgati.domain.model.BatteryHealthStatus

object BatteryRangeEstimator {

    /**
     * Calculates the State-of-Charge (SoC) percentage based on measured open-circuit voltage
     * and battery chemistry.
     */
    fun calculateSoC(voltage: Double, chemistry: BatteryChemistry): Int {
        if (voltage <= chemistry.cutoffVoltage) return 0
        if (voltage >= chemistry.fullVoltage) return 100

        val voltageRange = chemistry.fullVoltage - chemistry.cutoffVoltage
        val currentOffset = voltage - chemistry.cutoffVoltage
        val rawPercentage = (currentOffset / voltageRange) * 100.0

        return rawPercentage.toInt().coerceIn(0, 100)
    }

    /**
     * Estimates remaining driving range in kilometers based on battery SoC and current passenger load.
     * Higher passenger count increases energy draw per km, reducing total range.
     */
    fun estimateRangeKm(
        socPercentage: Int,
        chemistry: BatteryChemistry,
        passengerCount: Int = 0
    ): Int {
        // Base nominal range at 100% SoC with single driver
        val baseMaxRangeKm = when (chemistry) {
            BatteryChemistry.LEAD_ACID_48V -> 65.0
            BatteryChemistry.LFP_48V -> 90.0
            BatteryChemistry.LFP_60V -> 115.0
        }

        // Each passenger adds ~65kg payload, introducing a ~6.5% increased rolling/grade resistance
        val loadFactor = 1.0 - (passengerCount.coerceIn(0, 6) * 0.065)

        val remainingRange = (socPercentage / 100.0) * baseMaxRangeKm * loadFactor
        return remainingRange.toInt().coerceAtLeast(0)
    }

    /**
     * Full health status inspection with safety warnings in Hindi & English
     */
    fun evaluateHealth(
        voltage: Double,
        chemistry: BatteryChemistry,
        passengerCount: Int = 0
    ): BatteryHealthStatus {
        val soc = calculateSoC(voltage, chemistry)
        val range = estimateRangeKm(soc, chemistry, passengerCount)
        val isLow = soc <= 20 || range <= 10

        val warning = when {
            soc <= 10 -> "⚠️ अति गंभीर! तुरंत चार्जिंग या स्वैप स्टेशन जाएं (Battery Critical)"
            soc <= 20 -> "⚠️ बैटरी कम है (20% से कम)! अगली सवारी से पहले चार्ज करें"
            soc <= 40 -> "⚡ सामान्य स्तर (40% से नीचे) - शाम की शिफ्ट के लिए ध्यान दें"
            else -> "✅ बैटरी स्वस्थ है (पर्याप्त चार्ज)"
        }

        return BatteryHealthStatus(
            inputVoltage = voltage,
            chemistry = chemistry,
            socPercentage = soc,
            estimatedRangeKm = range,
            isCriticalLow = isLow,
            warningMessage = warning
        )
    }
}
