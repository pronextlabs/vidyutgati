import SwiftUI

public struct SeatCockpitView: View {
    @State private var state = SeatOccupancyState()
    @State private var lastMessage: String? = nil
    @State private var tripToDelete: TripItem? = nil
    @State private var showDeleteTripAlert: Bool = false
    @ObservedObject private var store = VidyutLocalStore.shared
    @ObservedObject private var localization = LocalizationManager.shared
    @StateObject private var soundbox = IOSSoundboxEngine()

    private var statusColor: Color {
        if state.isFull {
            return VidyutTheme.emeraldProfit
        } else if state.seatsRemaining == 1 {
            return VidyutTheme.cyanRoute
        } else {
            return VidyutTheme.electricAmber
        }
    }

    public var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                // 1. Route Direction Banner
                HStack {
                    VStack(alignment: .leading, spacing: 4) {
                        Text(localization.strings.activeRoute)
                            .font(.caption)
                            .foregroundColor(VidyutTheme.textMuted)
                        Text(state.currentRouteDisplay)
                            .font(.headline)
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                    }
                    Spacer()
                    Button(action: {
                        state.isForwardRoute.toggle()
                        lastMessage = "\(localization.strings.activeRoute): \(state.currentRouteDisplay)"
                    }) {
                        Image(systemName: "arrow.left.arrow.right")
                            .font(.system(size: 20, weight: .bold))
                            .foregroundColor(VidyutTheme.cyanRoute)
                            .frame(width: 44, height: 44)
                            .background(VidyutTheme.cyanRoute.opacity(0.15))
                            .clipShape(Circle())
                    }
                }
                .padding()
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(16)

                // 2. Central Seat Gauge Card
                VStack(spacing: 14) {
                    Text(state.isFull ? localization.strings.seatsFullBadge : (state.seatsRemaining == 1 ? localization.strings.oneSeatLeftBadge : localization.strings.seatsRemainingBadge(state.seatsRemaining)))
                        .font(.subheadline)
                        .fontWeight(.heavy)
                        .foregroundColor(statusColor)
                        .padding(.horizontal, 14)
                        .padding(.vertical, 6)
                        .background(statusColor.opacity(0.2))
                        .cornerRadius(20)

                    HStack(alignment: .bottom, spacing: 4) {
                        Text("\(state.currentOccupancy)")
                            .font(.system(size: 64, weight: .black))
                            .foregroundColor(statusColor)
                        Text("/ \(state.maxCapacity)")
                            .font(.system(size: 28, weight: .bold))
                            .foregroundColor(VidyutTheme.textMuted)
                            .padding(.bottom, 8)
                    }

                    Text(localization.strings.passengersOnboard)
                        .font(.footnote)
                        .foregroundColor(VidyutTheme.textMuted)

                    // 4 Seat Boxes
                    HStack(spacing: 12) {
                        ForEach(1...state.maxCapacity, id: \.self) { i in
                            let isOccupied = i <= state.currentOccupancy
                            ZStack {
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(isOccupied ? statusColor : VidyutTheme.borderSlate)
                                    .frame(width: 58, height: 58)
                                Image(systemName: "person.fill")
                                    .font(.system(size: 28))
                                    .foregroundColor(isOccupied ? .black : VidyutTheme.textMuted)
                            }
                        }
                    }

                    // Rush Hour Quick-Fill Button
                    Button(action: {
                        state.currentOccupancy = state.maxCapacity
                        lastMessage = "\(localization.strings.rushHourQuickFill) (\(state.maxCapacity))"
                    }) {
                        Text("\(localization.strings.rushHourQuickFill) (\(state.maxCapacity))")
                            .font(.footnote)
                            .fontWeight(.bold)
                            .foregroundColor(state.isFull ? VidyutTheme.textMuted : VidyutTheme.emeraldProfit)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 8)
                            .overlay(
                                RoundedRectangle(cornerRadius: 10)
                                    .stroke(state.isFull ? VidyutTheme.borderSlate : VidyutTheme.emeraldProfit.opacity(0.6), lineWidth: 1)
                            )
                    }
                    .disabled(state.isFull)
                    .padding(.horizontal)
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 20)
                .background(VidyutTheme.surfaceSlate)
                .cornerRadius(20)

                // 3. Huge 72pt Tactile Buttons
                HStack(spacing: 16) {
                    Button(action: {
                        if state.currentOccupancy > 0 {
                            state.currentOccupancy -= 1
                            lastMessage = "\(localization.strings.deboardPassenger) (\(state.currentOccupancy)/\(state.maxCapacity))"
                        }
                    }) {
                        HStack {
                            Image(systemName: "minus.circle.fill")
                                .font(.title2)
                            Text(localization.strings.deboardPassenger)
                                .font(.headline)
                                .fontWeight(.bold)
                        }
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .frame(height: 68)
                        .background(VidyutTheme.borderSlate)
                        .cornerRadius(16)
                    }
                    .disabled(state.currentOccupancy == 0)

                    Button(action: {
                        if state.currentOccupancy < state.maxCapacity {
                            state.currentOccupancy += 1
                            lastMessage = "\(localization.strings.boardPassenger) (\(state.currentOccupancy)/\(state.maxCapacity))"
                        }
                    }) {
                        HStack {
                            Image(systemName: "plus.circle.fill")
                                .font(.title2)
                            Text(localization.strings.boardPassenger)
                                .font(.headline)
                                .fontWeight(.heavy)
                        }
                        .foregroundColor(.black)
                        .frame(maxWidth: .infinity)
                        .frame(height: 68)
                        .background(VidyutTheme.emeraldProfit)
                        .cornerRadius(16)
                    }
                    .disabled(state.isFull)
                }

                // 4. Fare Per Seat Selector
                HStack {
                    Text(localization.strings.farePerSeat)
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                    Spacer()
                    HStack(spacing: 8) {
                        ForEach([10.0, 15.0, 20.0, 25.0], id: \.self) { fare in
                            let isSelected = state.farePerSeat == fare
                            Button(action: {
                                state.farePerSeat = fare
                                lastMessage = "\(localization.strings.farePerSeat) \(IndianCurrencyFormatter.formatInr(fare))"
                            }) {
                                Text(IndianCurrencyFormatter.formatInr(fare))
                                    .font(.subheadline)
                                    .fontWeight(isSelected ? .bold : .regular)
                                    .padding(.horizontal, 10)
                                    .padding(.vertical, 6)
                                    .background(isSelected ? VidyutTheme.electricAmber : VidyutTheme.borderSlate)
                                    .foregroundColor(isSelected ? .black : .white)
                                    .cornerRadius(8)
                            }
                        }
                    }
                }

                // 5. Trip Completed & Collect Fare
                VStack(alignment: .leading, spacing: 10) {
                    let passengers = state.currentOccupancy > 0 ? state.currentOccupancy : state.maxCapacity
                    let totalFare = Double(passengers) * state.farePerSeat
                    Text(localization.strings.tripCompleteTitle(Int(totalFare)))
                        .font(.subheadline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)

                    HStack(spacing: 12) {
                        Button(action: {
                            store.addTrip(routeName: state.currentRouteDisplay, passengers: passengers, fare: totalFare, mode: .cash)
                            state.currentOccupancy = 0
                            state.isForwardRoute.toggle()
                            lastMessage = "✅ \(localization.strings.cashPayment): \(IndianCurrencyFormatter.formatInr(totalFare))"
                        }) {
                            Text(localization.strings.cashPayment)
                                .fontWeight(.bold)
                                .foregroundColor(.black)
                                .frame(maxWidth: .infinity)
                                .frame(height: 46)
                                .background(VidyutTheme.electricAmber)
                                .cornerRadius(12)
                        }

                        Button(action: {
                            store.addTrip(routeName: state.currentRouteDisplay, passengers: passengers, fare: totalFare, mode: .upi)
                            store.addPayment(amount: totalFare, appSource: .paytm)
                            soundbox.announcePayment(amount: totalFare, appSource: .paytm)
                            state.currentOccupancy = 0
                            state.isForwardRoute.toggle()
                            lastMessage = "✅ \(localization.strings.upiPayment): \(IndianCurrencyFormatter.formatInr(totalFare))"
                        }) {
                            Text(localization.strings.upiPayment)
                                .fontWeight(.bold)
                                .foregroundColor(.black)
                                .frame(maxWidth: .infinity)
                                .frame(height: 46)
                                .background(VidyutTheme.cyanRoute)
                                .cornerRadius(12)
                        }
                    }
                }
                .padding()
                .background(VidyutTheme.surfaceSlate.opacity(0.8))
                .cornerRadius(16)

                // 6. Recent Trips History with Safe Delete
                VStack(alignment: .leading, spacing: 10) {
                    Text(localization.strings.recentTrips)
                        .font(.headline)
                        .fontWeight(.bold)
                        .foregroundColor(.white)

                    if store.recentTrips.isEmpty {
                        Text(localization.strings.noTripsYet)
                            .font(.footnote)
                            .foregroundColor(VidyutTheme.textMuted)
                            .padding()
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .background(VidyutTheme.surfaceSlate.opacity(0.5))
                            .cornerRadius(12)
                    } else {
                        ForEach(store.recentTrips) { trip in
                            HStack {
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(trip.routeName)
                                        .font(.subheadline)
                                        .fontWeight(.semibold)
                                        .foregroundColor(.white)
                                    Text("\(trip.timestamp.formatted(date: .omitted, time: .shortened)) • \(trip.passengerCount) \(localization.strings.passengersLabel) • \(trip.paymentMode == .upi ? localization.strings.upiPayment : localization.strings.cashPayment)")
                                        .font(.caption)
                                        .foregroundColor(VidyutTheme.textMuted)
                                }
                                Spacer()
                                Text("+\(IndianCurrencyFormatter.formatInr(trip.fareCollected))")
                                    .font(.subheadline)
                                    .fontWeight(.heavy)
                                    .foregroundColor(VidyutTheme.emeraldProfit)

                                Button(action: {
                                    tripToDelete = trip
                                    showDeleteTripAlert = true
                                }) {
                                    Image(systemName: "trash")
                                        .font(.subheadline)
                                        .foregroundColor(VidyutTheme.coralAlert.opacity(0.8))
                                        .padding(.leading, 8)
                                }
                            }
                            .padding()
                            .background(VidyutTheme.surfaceSlate)
                            .cornerRadius(12)
                        }
                    }
                }

                if let msg = lastMessage {
                    Text(msg)
                        .font(.footnote)
                        .foregroundColor(VidyutTheme.electricAmber)
                }
            }
            .padding()
        }
        .background(VidyutTheme.deepObsidian.ignoresSafeArea())
        .alert(localization.strings.deleteTripPromptTitle, isPresented: $showDeleteTripAlert, presenting: tripToDelete) { trip in
            Button(localization.strings.cancelAction, role: .cancel) {
                tripToDelete = nil
            }
            Button(localization.strings.deleteAction, role: .destructive) {
                store.deleteTrip(id: trip.id)
                tripToDelete = nil
            }
        } message: { trip in
            Text(localization.strings.deleteTripPromptMessage(trip.routeName, Int(trip.fareCollected), trip.passengerCount))
        }
    }
}
