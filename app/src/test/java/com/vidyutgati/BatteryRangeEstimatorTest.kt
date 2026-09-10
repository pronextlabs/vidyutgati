package com.vidyutgati

import com.vidyutgati.core.battery.BatteryRangeEstimator
import com.vidyutgati.domain.model.BatteryChemistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BatteryRangeEstimatorTest {

    @Test
    fun testLeadAcidSoCCalculation() {
        // Full voltage 50.4V -> 100%
        val socFull = BatteryRangeEstimator.calculateSoC(50.4, BatteryChemistry.LEAD_ACID_48V)
        assertEquals(100, socFull)

        // Cutoff voltage 42.0V -> 0%
        val socEmpty = BatteryRangeEstimator.calculateSoC(42.0, BatteryChemistry.LEAD_ACID_48V)
        assertEquals(0, socEmpty)

        // Mid-point ~46.2V -> ~50%
        val socMid = BatteryRangeEstimator.calculateSoC(46.2, BatteryChemistry.LEAD_ACID_48V)
        assertEquals(50, socMid)
    }

    @Test
    fun testLFP48VSoCCalculation() {
        val socFull = BatteryRangeEstimator.calculateSoC(58.4, BatteryChemistry.LFP_48V)
        assertEquals(100, socFull)

        val socCutoff = BatteryRangeEstimator.calculateSoC(43.0, BatteryChemistry.LFP_48V)
        assertEquals(0, socCutoff)
    }

    @Test
    fun testRangeEstimationWithPassengerLoad() {
        // At 100% SoC with 0 passengers, base range for LFP 48V is 90km
        val rangeEmpty = BatteryRangeEstimator.estimateRangeKm(100, BatteryChemistry.LFP_48V, passengerCount = 0)
        assertEquals(90, rangeEmpty)

        // With 4 passengers, loadFactor is 1.0 - (4 * 0.065) = 0.74 => 90 * 0.74 = 66km
        val rangeFull = BatteryRangeEstimator.estimateRangeKm(100, BatteryChemistry.LFP_48V, passengerCount = 4)
        assertTrue(rangeFull < rangeEmpty)
        assertEquals(66, rangeFull)
    }

    @Test
    fun testLowBatteryWarningTrigger() {
        // Voltage close to cutoff => low SoC (<= 20% or <= 10 km)
        val statusLow = BatteryRangeEstimator.evaluateHealth(43.5, BatteryChemistry.LEAD_ACID_48V, 2)
        assertTrue(statusLow.isCriticalLow)
        assertTrue(statusLow.socPercentage <= 20)

        // Healthy battery
        val statusHealthy = BatteryRangeEstimator.evaluateHealth(50.0, BatteryChemistry.LEAD_ACID_48V, 2)
        assertFalse(statusHealthy.isCriticalLow)
    }
}
