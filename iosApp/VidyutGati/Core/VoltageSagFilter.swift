import Foundation

/**
 * Exponential Moving Average (EMA) filter to dampen sudden voltage sag
 * during high throttle acceleration or climbing gradients on iOS.
 */
public final class IOSVoltageSagFilter {
    private let smoothingFactor: Double
    private var smoothedVoltage: Double?

    public init(smoothingFactor: Double = 0.25) {
        self.smoothingFactor = smoothingFactor
    }

    public func filter(rawVoltage: Double) -> Double {
        if let current = smoothedVoltage {
            let updated = (smoothingFactor * rawVoltage) + ((1.0 - smoothingFactor) * current)
            smoothedVoltage = updated
            return updated
        } else {
            smoothedVoltage = rawVoltage
            return rawVoltage
        }
    }

    public func reset(initialVoltage: Double? = nil) {
        smoothedVoltage = initialVoltage
    }

    public func getCurrentSmoothedVoltage() -> Double? {
        return smoothedVoltage
    }
}
