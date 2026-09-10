package com.vidyutgati.ui.khata

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.vidyutgati.core.designsystem.SafeDeleteConfirmationDialog
import com.vidyutgati.core.i18n.IndianCurrencyFormatter
import com.vidyutgati.core.i18n.LanguageManager

@Composable
fun DailyKhataScreen(
    viewModel: DailyKhataViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val languageManager = remember { LanguageManager.getInstance(context) }
    val strings by languageManager.strings.collectAsState()

    val khata by viewModel.todayKhata.collectAsState()
    val weekly by viewModel.weeklySummary.collectAsState()
    val haptic = LocalHapticFeedback.current
    var showResetDialog by remember { mutableStateOf(false) }

    val netProfitColor = if (khata.isProfitable) EmeraldProfit else CoralAlert

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Date Header & WhatsApp Share
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = strings.dailyKhataTitle,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = viewModel.formattedDisplayDate,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, viewModel.generateWhatsAppSummary())
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, strings.shareWhatsAppButton))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldProfit),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = strings.shareWhatsAppButton,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 2. Net Pocket Profit Hero Card
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
                    Text(
                        text = strings.netProfitTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = IndianCurrencyFormatter.formatInr(khata.netProfit),
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Black,
                        color = netProfitColor
                    )

                    Surface(
                        color = netProfitColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (khata.isProfitable) strings.profitBadge else strings.lossBadge,
                            color = netProfitColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = strings.passengersLabel, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "${khata.totalPassengers}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = strings.tripsLabel, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "${khata.totalTrips}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Breakdown Cards

        // A. Gross Earnings Card
        item {
            KhataBreakdownCard(
                title = strings.grossEarningsLabel,
                amount = khata.grossEarnings,
                icon = Icons.Default.CurrencyRupee,
                iconColor = EmeraldProfit,
                onAdd20 = { viewModel.updateGrossEarnings(20.0) },
                onAdd50 = { viewModel.updateGrossEarnings(50.0) },
                onAdd100 = { viewModel.updateGrossEarnings(100.0) }
            )
        }

        // B. Thekedar Bhatta Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Thekedar",
                            tint = CoralAlert,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = strings.thekedarRentLabel,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "दैनिक फिक्स किराया",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = "-${IndianCurrencyFormatter.formatInr(khata.thekedarRent)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralAlert
                    )
                }
            }
        }

        // C. Battery Charging Expense Card
        item {
            KhataBreakdownCard(
                title = strings.chargingExpenseLabel,
                amount = khata.chargingExpense,
                icon = Icons.Default.Bolt,
                iconColor = ElectricAmber,
                isExpense = true,
                onAdd20 = { viewModel.addChargingExpense(50.0) },
                onAdd50 = { viewModel.addChargingExpense(100.0) },
                onAdd100 = { viewModel.addChargingExpense(150.0) },
                labels = listOf("+₹50", "+₹100", "+₹150")
            )
        }

        // D. Other Expenses Card
        item {
            KhataBreakdownCard(
                title = strings.otherExpensesLabel,
                amount = khata.otherExpenses,
                icon = Icons.Default.Handyman,
                iconColor = CyanRoute,
                isExpense = true,
                onAdd20 = { viewModel.addOtherExpense(20.0) },
                onAdd50 = { viewModel.addOtherExpense(50.0) },
                onAdd100 = { viewModel.addOtherExpense(100.0) }
            )
        }

        // E. 7-Day Performance & Fleet Rental Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = strings.weeklyOverviewTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "कुल ${weekly.activeDays} कार्य दिवसों का हिसाब",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "7-दिन कुल कमाई", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = IndianCurrencyFormatter.formatInr(weekly.totalGross), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EmeraldProfit)
                        }
                        Column {
                            Text(text = "कुल दिया भत्ता", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "-${IndianCurrencyFormatter.formatInr(weekly.totalBhattaPaid)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CoralAlert)
                        }
                        Column {
                            Text(text = "शुद्ध साप्ताहिक बचत", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = IndianCurrencyFormatter.formatInr(weekly.totalNetProfit), fontSize = 16.sp, fontWeight = FontWeight.Black, color = if (weekly.totalNetProfit >= 0) EmeraldProfit else CoralAlert)
                        }
                    }
                }
            }
        }

        // F. Safe Reset Today's Khata Option
        item {
            OutlinedButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showResetDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralAlert)
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "Reset Khata",
                    tint = CoralAlert,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = strings.resetKhataButton,
                    color = CoralAlert,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // G. ProNextLabs Open-Source Credits
        item {
            Text(
                text = "⚡ विद्युतगति • Open-Source by ProNextLabs\n100% Offline • Made with ❤️ for Sarathis",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
            )
        }
    }

    // Safe Delete Confirmation Dialog for Khata Reset
    if (showResetDialog) {
        SafeDeleteConfirmationDialog(
            title = strings.resetKhataPromptTitle,
            message = strings.resetKhataPromptMessage,
            confirmButtonText = strings.deleteAction,
            dismissButtonText = strings.cancelAction,
            onConfirm = {
                viewModel.resetTodayKhata()
                showResetDialog = false
            },
            onDismiss = {
                showResetDialog = false
            }
        )
    }
}

@Composable
fun KhataBreakdownCard(
    title: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    isExpense: Boolean = false,
    onAdd20: () -> Unit,
    onAdd50: () -> Unit,
    onAdd100: () -> Unit,
    labels: List<String> = listOf("+₹20", "+₹50", "+₹100")
) {
    val haptic = LocalHapticFeedback.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "${if (isExpense) "-" else ""}${IndianCurrencyFormatter.formatInr(amount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isExpense) CoralAlert else EmeraldProfit
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Add Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onAdd20()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = labels[0], fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onAdd50()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = labels[1], fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onAdd100()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = labels[2], fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
