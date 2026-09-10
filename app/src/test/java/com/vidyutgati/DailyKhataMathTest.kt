package com.vidyutgati

import com.vidyutgati.domain.model.DailyKhata
import com.vidyutgati.domain.model.SeatOccupancyState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyKhataMathTest {

    @Test
    fun testNetProfitCalculation() {
        val khata = DailyKhata(
            dateIso = "2026-09-10",
            grossEarnings = 1200.0,
            thekedarRent = 350.0,
            chargingExpense = 150.0,
            otherExpenses = 50.0
        )
        // 1200 - 350 - 150 - 50 = 650
        assertEquals(650.0, khata.netProfit, 0.001)
        assertTrue(khata.isProfitable)
    }

    @Test
    fun testLossCalculationWhenExpensesExceedEarnings() {
        val khata = DailyKhata(
            dateIso = "2026-09-10",
            grossEarnings = 400.0,
            thekedarRent = 350.0,
            chargingExpense = 150.0,
            otherExpenses = 0.0
        )
        // 400 - 350 - 150 = -100
        assertEquals(-100.0, khata.netProfit, 0.001)
        assertFalse(khata.isProfitable)
    }

    @Test
    fun testTripRollbackMath() {
        val initialGross = 500.0
        val initialPassengers = 25
        val initialTrips = 6

        // Rollback a trip of ₹60 with 4 passengers
        val rolledGross = maxOf(0.0, initialGross - 60.0)
        val rolledPassengers = maxOf(0, initialPassengers - 4)
        val rolledTrips = maxOf(0, initialTrips - 1)

        assertEquals(440.0, rolledGross, 0.001)
        assertEquals(21, rolledPassengers)
        assertEquals(5, rolledTrips)
    }

    @Test
    fun testTripRollbackLowerBoundFloorZero() {
        val initialGross = 20.0
        val initialPassengers = 1
        val initialTrips = 1

        // Attempting to rollback more than recorded should floor at 0
        val rolledGross = maxOf(0.0, initialGross - 50.0)
        val rolledPassengers = maxOf(0, initialPassengers - 4)
        val rolledTrips = maxOf(0, initialTrips - 2)

        assertEquals(0.0, rolledGross, 0.0)
        assertEquals(0, rolledPassengers)
        assertEquals(0, rolledTrips)
    }
}

class SeatCockpitStateTest {

    @Test
    fun testOccupancyBounds() {
        val state = SeatOccupancyState(currentOccupancy = 0, maxCapacity = 4)
        assertFalse(state.isFull)
        assertEquals(4, state.seatsRemaining)

        val fullState = state.copy(currentOccupancy = 4)
        assertTrue(fullState.isFull)
        assertEquals(0, fullState.seatsRemaining)
    }

    @Test
    fun testRouteReversal() {
        val state = SeatOccupancyState(
            routeOrigin = "मेट्रो",
            routeDestination = "मार्केट",
            isForwardRoute = true
        )
        assertEquals("मेट्रो ➔ मार्केट", state.currentRouteDisplay)

        val reversed = state.copy(isForwardRoute = false)
        assertEquals("मार्केट ➔ मेट्रो", reversed.currentRouteDisplay)
    }
}
