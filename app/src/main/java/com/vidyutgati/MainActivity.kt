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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.vidyutgati.core.designsystem.LanguageSelectorDialog
import com.vidyutgati.core.i18n.AppLanguage
import com.vidyutgati.core.i18n.LanguageManager

enum class NavigationItem(val icon: ImageVector) {
    SEATS(Icons.Default.DirectionsCar),
    SOUNDBOX(Icons.Default.VolumeUp),
    KHATA(Icons.Default.AccountBalanceWallet),
    BATTERY(Icons.Default.BatteryChargingFull)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    seatCockpitViewModel: SeatCockpitViewModel,
    soundboxViewModel: SoundboxViewModel,
    dailyKhataViewModel: DailyKhataViewModel,
    batteryViewModel: BatteryViewModel
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val languageManager = remember { LanguageManager.getInstance(context) }
    val currentLanguage by languageManager.currentLanguage.collectAsState()
    val strings by languageManager.strings.collectAsState()

    var selectedTab by remember { mutableStateOf(NavigationItem.SEATS) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = strings.appTitle,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "100% Offline • INR (₹)",
                            fontSize = 11.sp,
                            color = ElectricAmber,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                actions = {
                    Surface(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                showLanguageDialog = true
                            },
                        color = ElectricAmber.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricAmber.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = currentLanguage.flag, fontSize = 14.sp)
                            Text(
                                text = currentLanguage.nativeName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricAmber
                            )
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Switch Language",
                                tint = ElectricAmber,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationItem.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    val tabTitle = when (tab) {
                        NavigationItem.SEATS -> strings.tabSeats
                        NavigationItem.SOUNDBOX -> strings.tabSoundbox
                        NavigationItem.KHATA -> strings.tabKhata
                        NavigationItem.BATTERY -> strings.tabBattery
                    }
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tabTitle,
                                tint = if (isSelected) ElectricAmber else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = tabTitle,
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

    if (showLanguageDialog) {
        LanguageSelectorDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { newLang ->
                languageManager.setLanguage(newLang)
            },
            onDismiss = {
                showLanguageDialog = false
            }
        )
    }
}
