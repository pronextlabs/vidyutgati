package com.vidyutgati

import com.vidyutgati.core.battery.VoltageSagFilter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VoltageSagFilterTest {

    @Test
    fun testInitialSampleIsDirectlyAdopted() {
        val filter = VoltageSagFilter(smoothingFactor = 0.2)
        val initial = filter.filter(52.0)
        assertEquals(52.0, initial, 0.001)
    }

    @Test
    fun testTransientDipIsDampened() {
        // Smoothing factor 0.2 means a sudden drop contributes only 20%
        val filter = VoltageSagFilter(smoothingFactor = 0.2)
        filter.reset(52.0)

        // Driver stomps throttle, battery sags instantaneously to 44.0V
        val smoothed = filter.filter(44.0)

        // 0.2 * 44 + 0.8 * 52 = 8.8 + 41.6 = 50.4V
        assertEquals(50.4, smoothed, 0.01)

        // Verify smoothed voltage is significantly above the raw sagged voltage
        assertTrue(smoothed > 48.0)
    }

    @Test
    fun testConvergenceToStableVoltage() {
        val filter = VoltageSagFilter(smoothingFactor = 0.5)
        filter.reset(50.0)

        var voltage = 50.0
        repeat(10) {
            voltage = filter.filter(46.0)
        }

        // After repeated samples, it should converge to 46.0
        assertEquals(46.0, voltage, 0.05)
    }
}
