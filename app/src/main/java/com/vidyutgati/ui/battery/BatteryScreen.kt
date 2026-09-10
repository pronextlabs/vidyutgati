package com.vidyutgati.ui.battery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidyutgati.core.designsystem.CoralAlert
import com.vidyutgati.core.designsystem.CyanRoute
import com.vidyutgati.core.designsystem.ElectricAmber
import com.vidyutgati.core.designsystem.EmeraldProfit
import com.vidyutgati.core.i18n.LanguageManager
import com.vidyutgati.domain.model.BatteryChemistry

@Composable
fun BatteryScreen(
    viewModel: BatteryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val languageManager = remember { LanguageManager.getInstance(context) }
    val strings by languageManager.strings.collectAsState()

    val chemistry by viewModel.selectedChemistry.collectAsState()
    val voltage by viewModel.currentVoltage.collectAsState()
    val smoothedVoltage by viewModel.smoothedVoltage.collectAsState()
    val load by viewModel.passengerLoad.collectAsState()
    val status by viewModel.healthStatus.collectAsState()
    val haptic = LocalHapticFeedback.current

    val batteryColor = when {
        status.socPercentage <= 15 -> CoralAlert
        status.socPercentage <= 35 -> ElectricAmber
        else -> EmeraldProfit
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Battery Gauge Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .background(batteryColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (status.isCriticalLow) Icons.Default.BatteryAlert else Icons.Default.BatteryChargingFull,
                            contentDescription = "Battery Status",
                            tint = batteryColor,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${status.estimatedRangeKm}",
                            fontSize = 62.sp,
                            fontWeight = FontWeight.Black,
                            color = batteryColor
                        )
                        Text(
                            text = " KM",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Text(
                        text = strings.estRangeLabel,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = batteryColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${status.socPercentage}% चार्ज | ${String.format("%.1f", smoothedVoltage)}V (${strings.sagFilterBadge})",
                            color = batteryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = status.warningMessage,
                        color = if (status.isCriticalLow) CoralAlert else MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Voice Alert Button
                    OutlinedButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.speakBatteryStatus()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Voice",
                            tint = CyanRoute,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "आवाज़ में स्थिति सुनें (Speak Status)",
                            color = CyanRoute,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. Battery Chemistry Selector
        item {
            Text(
                text = "बैटरी प्रकार (Battery Chemistry):",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    BatteryChemistry.LFP_48V to "48V LFP लिथियम",
                    BatteryChemistry.LEAD_ACID_48V to "48V लेड-एसिड",
                    BatteryChemistry.LFP_60V to "60V LFP"
                ).forEach { (chem, label) ->
                    val isSelected = chemistry == chem
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.setChemistry(chem)
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricAmber,
                            selectedLabelColor = Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Voltage Adjustment Slider
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "वर्तमान वोल्टेज (Current Voltage):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${String.format("%.1f", voltage)}V",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricAmber
                        )
                    }

                    Slider(
                        value = voltage.toFloat(),
                        onValueChange = {
                            viewModel.setVoltage(it.toDouble())
                        },
                        valueRange = chemistry.cutoffVoltage.toFloat()..chemistry.fullVoltage.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = ElectricAmber,
                            activeTrackColor = ElectricAmber
                        )
                    )
                }
            }
        }

        // 4. Passenger Load Adjustment
        item {
            Text(
                text = "वर्तमान सवारी लोड (Passenger Weight):",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    0 to "खाली (0)",
                    2 to "2 सवारियाँ",
                    4 to "फुल (4 सवारियाँ)"
                ).forEach { (count, label) ->
                    val isSelected = load == count
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.setPassengerLoad(count)
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanRoute,
                            selectedLabelColor = Color.Black
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 5. Emergency Battery Swap Station Directory Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EvStation,
                            contentDescription = "Swap Station",
                            tint = EmeraldProfit,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "निकटतम बैटरी स्वैप केंद्र (Nearby Swap Stalls)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    listOf(
                        "बैटरी स्मार्ट हब - मेट्रो पिलर 142" to "खुला है • ₹160/स्वैप",
                        "सन मोबिलिटी स्टेशन - सेक्टर 62 बस स्टॉप" to "खुला है • ₹180/स्वैप",
                        "राजू ई-रिक्शा चार्जिंग पॉइंट - अट्टा मार्केट" to "खुला है • ₹40/घंटा"
                    ).forEach { (name, timing) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = timing, fontSize = 11.sp, color = EmeraldProfit)
                            }
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call",
                                tint = CyanRoute,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
