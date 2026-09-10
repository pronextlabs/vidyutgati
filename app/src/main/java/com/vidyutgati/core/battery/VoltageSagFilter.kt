package com.vidyutgati.core.battery

/**
 * Exponential Moving Average (EMA) filter to dampen sudden voltage sag
 * during high throttle acceleration or climbing gradients.
 *
 * Prevents false "Battery Critical" alerts when an e-rickshaw pulls a heavy 4-passenger load from a stop.
 */
class VoltageSagFilter(
    private val smoothingFactor: Double = 0.2 // Weight given to new sample (0.0 to 1.0)
) {
    private var smoothedVoltage: Double? = null

    /**
     * Filters an incoming raw voltage sample and returns the stable smoothed voltage.
     */
    fun filter(rawVoltage: Double): Double {
        val current = smoothedVoltage
        return if (current == null) {
            smoothedVoltage = rawVoltage
            rawVoltage
        } else {
            val updated = (smoothingFactor * rawVoltage) + ((1.0 - smoothingFactor) * current)
            smoothedVoltage = updated
            updated
        }
    }

    /**
     * Resets the filter, useful when switching battery chemistry or starting a new shift.
     */
    fun reset(initialVoltage: Double? = null) {
        smoothedVoltage = initialVoltage
    }

    fun getCurrentSmoothedVoltage(): Double? = smoothedVoltage
}
