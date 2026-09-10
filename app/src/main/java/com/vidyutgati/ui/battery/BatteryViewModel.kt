package com.vidyutgati.ui.battery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vidyutgati.core.battery.BatteryRangeEstimator
import com.vidyutgati.core.battery.VoltageSagFilter
import com.vidyutgati.core.soundbox.SoundboxEngine
import com.vidyutgati.domain.model.BatteryChemistry
import com.vidyutgati.domain.model.BatteryHealthStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BatteryViewModel(application: Application) : AndroidViewModel(application) {

    val soundboxEngine = SoundboxEngine(application)
    val voltageFilter = VoltageSagFilter(smoothingFactor = 0.25)

    private val _selectedChemistry = MutableStateFlow(BatteryChemistry.LFP_48V)
    val selectedChemistry: StateFlow<BatteryChemistry> = _selectedChemistry.asStateFlow()

    private val _currentVoltage = MutableStateFlow(52.5)
    val currentVoltage: StateFlow<Double> = _currentVoltage.asStateFlow()

    private val _smoothedVoltage = MutableStateFlow(52.5)
    val smoothedVoltage: StateFlow<Double> = _smoothedVoltage.asStateFlow()

    private val _passengerLoad = MutableStateFlow(2)
    val passengerLoad: StateFlow<Int> = _passengerLoad.asStateFlow()

    private val _healthStatus = MutableStateFlow(
        BatteryRangeEstimator.evaluateHealth(52.5, BatteryChemistry.LFP_48V, 2)
    )
    val healthStatus: StateFlow<BatteryHealthStatus> = _healthStatus.asStateFlow()

    init {
        voltageFilter.reset(52.5)
    }

    fun setChemistry(chem: BatteryChemistry) {
        _selectedChemistry.value = chem
        val defaultV = when (chem) {
            BatteryChemistry.LEAD_ACID_48V -> 48.0
            BatteryChemistry.LFP_48V -> 52.5
            BatteryChemistry.LFP_60V -> 65.0
        }
        _currentVoltage.value = defaultV
        voltageFilter.reset(defaultV)
        _smoothedVoltage.value = defaultV
        recalculate()
    }

    fun setVoltage(v: Double) {
        _currentVoltage.value = v
        val filtered = voltageFilter.filter(v)
        _smoothedVoltage.value = filtered
        recalculate()
    }

    fun setPassengerLoad(count: Int) {
        _passengerLoad.value = count
        recalculate()
    }

    private fun recalculate() {
        // Evaluate health based on the smoothed voltage to prevent false sag warnings
        _healthStatus.value = BatteryRangeEstimator.evaluateHealth(
            _smoothedVoltage.value,
            _selectedChemistry.value,
            _passengerLoad.value
        )
    }

    fun speakBatteryStatus() {
        val status = _healthStatus.value
        val hindiMsg = "बैटरी ${status.socPercentage} प्रतिशत है। अनुमानित रेंज ${status.estimatedRangeKm} किलोमीटर बची है।"
        val engMsg = "Battery is ${status.socPercentage} percent. Estimated range is ${status.estimatedRangeKm} kilometers."
        soundboxEngine.speakAlert(hindiMsg, engMsg)
    }

    override fun onCleared() {
        super.onCleared()
        soundboxEngine.shutdown()
    }
}

