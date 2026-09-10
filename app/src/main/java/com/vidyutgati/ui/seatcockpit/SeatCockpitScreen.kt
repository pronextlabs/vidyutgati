package com.vidyutgati.ui.seatcockpit

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidyutgati.core.database.TripEntity
import com.vidyutgati.core.designsystem.CoralAlert
import com.vidyutgati.core.designsystem.CyanRoute
import com.vidyutgati.core.designsystem.ElectricAmber
import com.vidyutgati.core.designsystem.EmeraldProfit
import com.vidyutgati.core.designsystem.SafeDeleteConfirmationDialog
import com.vidyutgati.domain.model.PaymentMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SeatCockpitScreen(
    viewModel: SeatCockpitViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.seatState.collectAsState()
    val recentTrips by viewModel.recentTrips.collectAsState()
    val lastMessage by viewModel.lastActionMessage.collectAsState()
    val haptic = LocalHapticFeedback.current
    var tripToDelete by remember { mutableStateOf<TripEntity?>(null) }

    val statusColor by animateColorAsState(
        targetValue = when {
            state.isFull -> EmeraldProfit
            state.currentOccupancy == state.maxCapacity - 1 -> CyanRoute
            else -> ElectricAmber
        },
        label = "statusColor"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Route Direction Banner with 1-Tap Flip
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "सक्रिय रूट (Active Route)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = state.currentRouteDisplay,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.toggleRouteDirection()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(CyanRoute.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Reverse Route",
                        tint = CyanRoute
                    )
                }
            }
        }

        // 2. Central Live Seat Gauge & Visual Occupancy Graphic
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status Badge
                Surface(
                    color = statusColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, statusColor)
                ) {
                    Text(
                        text = when {
                            state.isFull -> "⚡ गाड़ी फुल है (FULL)"
                            state.seatsRemaining == 1 -> "⚠️ केवल 1 सीट बाकी!"
                            else -> "🟢 ${state.seatsRemaining} सीटें खाली हैं"
                        },
                        color = statusColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Big Occupancy Number
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${state.currentOccupancy}",
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor
                    )
                    Text(
                        text = " / ${state.maxCapacity}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Text(
                    text = "सवारियां बैठी हैं (Passengers Onboard)",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4 Physical Seats Layout Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 1..state.maxCapacity) {
                        val isOccupied = i <= state.currentOccupancy
                        val seatColor = if (isOccupied) statusColor else MaterialTheme.colorScheme.surfaceVariant
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(seatColor)
                                .border(
                                    width = 2.dp,
                                    color = if (isOccupied) statusColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Seat $i",
                                tint = if (isOccupied) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rush Hour Quick-Fill Button
                OutlinedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.fillAllSeats()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isFull
                ) {
                    Text(
                        text = "⚡ भीड़ समय: 1-टैप पूरी गाड़ी फुल (Fill All ${state.maxCapacity} Seats)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.isFull) MaterialTheme.colorScheme.onSurfaceVariant else EmeraldProfit
                    )
                }
            }
        }

        // 3. Huge 72dp Tactile Buttons for Rapid One-Thumb Operation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Decrement Button (Deboard)
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.decrementSeat()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                enabled = state.currentOccupancy > 0
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Remove",
                        tint = CoralAlert,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "सवारी उतरी",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Increment Button (Board)
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.incrementSeat()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldProfit),
                enabled = state.currentOccupancy < state.maxCapacity
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+ सवारी बैठी",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }
            }
        }

        // 4. Fare Quick Setting
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "प्रति सवारी किराया:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(10.0, 15.0, 20.0, 25.0).forEach { fare ->
                    val isSelected = state.farePerSeat == fare
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            viewModel.setFarePerSeat(fare)
                        },
                        label = {
                            Text(
                                text = "₹${fare.toInt()}",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricAmber,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }
        }

        // 5. Trip Complete & Record Fare Buttons
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                val totalExpected = (if (state.currentOccupancy > 0) state.currentOccupancy else state.maxCapacity) * state.farePerSeat
                Text(
                    text = "ट्रिप समाप्त व किराया संग्रह (₹${totalExpected.toInt()})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.completeTripAndCollect(PaymentMode.CASH)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricAmber),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "💵 नकद (Cash)",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.completeTripAndCollect(PaymentMode.UPI)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanRoute),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "📱 यूपीआई (UPI)",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 6. Recent Trips History with Safe Delete
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "हाल की ट्रिप (Recent Trips)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (recentTrips.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "आज अभी कोई ट्रिप दर्ज नहीं हुई है। ट्रिप पूरी होने पर यहाँ दिखेगी।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            } else {
                recentTrips.forEach { trip ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = trip.routeName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(trip.timestampEpoch))
                                Text(
                                    text = "$timeStr • ${trip.passengerCount} सवारियाँ • ${if (trip.paymentMode == "UPI") "📱 UPI" else "💵 नकद"}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "+₹${trip.fareCollected.toInt()}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldProfit
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        tripToDelete = trip
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete Trip",
                                        tint = CoralAlert.copy(alpha = 0.8f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Last Action Feedback Toast/Snackbar Area
        lastMessage?.let { msg ->
            Text(
                text = msg,
                color = ElectricAmber,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }

    // Safe Delete Confirmation Dialog for Trips (with automatic Khata rollback)
    tripToDelete?.let { trip ->
        SafeDeleteConfirmationDialog(
            title = "ट्रिप हटाएं? (Delete Trip)",
            message = "क्या आप सच में यह ट्रिप (${trip.routeName} - ₹${trip.fareCollected.toInt()}, ${trip.passengerCount} सवारियां) हटाना चाहते हैं?\n\nआज के खाते से इसकी कमाई अपने आप घटा दी जाएगी (Rollback)। यह क्रिया वापस नहीं ली जा सकती।",
            confirmButtonText = "हटाएं (Delete)",
            dismissButtonText = "रद्द करें (Cancel)",
            onConfirm = {
                viewModel.deleteTrip(trip.id)
                tripToDelete = null
            },
            onDismiss = {
                tripToDelete = null
            }
        )
    }
}
