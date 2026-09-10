package com.vidyutgati

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vidyutgati.core.designsystem.ElectricAmber
import com.vidyutgati.core.designsystem.VidyutGatiTheme
import com.vidyutgati.ui.battery.BatteryScreen
import com.vidyutgati.ui.battery.BatteryViewModel
import com.vidyutgati.ui.khata.DailyKhataScreen
import com.vidyutgati.ui.khata.DailyKhataViewModel
import com.vidyutgati.ui.seatcockpit.SeatCockpitScreen
import com.vidyutgati.ui.seatcockpit.SeatCockpitViewModel
import com.vidyutgati.ui.soundbox.SoundboxScreen
import com.vidyutgati.ui.soundbox.SoundboxViewModel

enum class NavigationItem(val title: String, val icon: ImageVector) {
    SEATS("सवारी", Icons.Default.DirectionsCar),
    SOUNDBOX("आवाज़", Icons.Default.VolumeUp),
    KHATA("खाता", Icons.Default.AccountBalanceWallet),
    BATTERY("बैटरी", Icons.Default.BatteryChargingFull)
}

class MainActivity : ComponentActivity() {

    private val seatCockpitViewModel: SeatCockpitViewModel by viewModels()
    private val soundboxViewModel: SoundboxViewModel by viewModels()
    private val dailyKhataViewModel: DailyKhataViewModel by viewModels()
    private val batteryViewModel: BatteryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VidyutGatiTheme {
                MainScreen(
                    seatCockpitViewModel = seatCockpitViewModel,
                    soundboxViewModel = soundboxViewModel,
                    dailyKhataViewModel = dailyKhataViewModel,
                    batteryViewModel = batteryViewModel
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    seatCockpitViewModel: SeatCockpitViewModel,
    soundboxViewModel: SoundboxViewModel,
    dailyKhataViewModel: DailyKhataViewModel,
    batteryViewModel: BatteryViewModel
) {
    var selectedTab by remember { mutableStateOf(NavigationItem.SEATS) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationItem.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) ElectricAmber else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ElectricAmber else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = ElectricAmber.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (selectedTab) {
            NavigationItem.SEATS -> SeatCockpitScreen(
                viewModel = seatCockpitViewModel,
                modifier = modifier
            )
            NavigationItem.SOUNDBOX -> SoundboxScreen(
                viewModel = soundboxViewModel,
                modifier = modifier
            )
            NavigationItem.KHATA -> DailyKhataScreen(
                viewModel = dailyKhataViewModel,
                modifier = modifier
            )
            NavigationItem.BATTERY -> BatteryScreen(
                viewModel = batteryViewModel,
                modifier = modifier
            )
        }
    }
}
