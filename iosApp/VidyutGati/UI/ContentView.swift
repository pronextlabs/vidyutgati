import SwiftUI

public struct ContentView: View {
    @State private var selectedTab = 0
    @ObservedObject private var localization = LocalizationManager.shared
    @State private var showLanguageSheet = false

    public init() {}

    public var body: some View {
        NavigationView {
            VStack(spacing: 0) {
                // Top Action Bar with INR Currency Badge and Language Selector
                HStack {
                    VStack(alignment: .leading, spacing: 2) {
                        Text(localization.strings.appTitle)
                            .font(.system(size: 18, weight: .black))
                            .foregroundColor(VidyutTheme.electricAmber)
                        Text("100% Offline • ProNextLabs")
                            .font(.system(size: 10, weight: .medium))
                            .foregroundColor(VidyutTheme.textMuted)
                    }

                    Spacer()

                    // INR Currency Badge
                    Text("₹ INR")
                        .font(.system(size: 11, weight: .bold))
                        .foregroundColor(VidyutTheme.emeraldProfit)
                        .padding(.horizontal, 8)
                        .padding(.vertical, 4)
                        .background(VidyutTheme.emeraldProfit.opacity(0.15))
                        .cornerRadius(8)

                    // Language Selector Button
                    Button(action: {
                        showLanguageSheet = true
                    }) {
                        HStack(spacing: 4) {
                            Text(localization.currentLanguage.flag)
                                .font(.system(size: 12))
                            Text(localization.currentLanguage.nativeName)
                                .font(.system(size: 12, weight: .bold))
                                .foregroundColor(.white)
                            Image(systemName: "chevron.down")
                                .font(.system(size: 10, weight: .bold))
                                .foregroundColor(VidyutTheme.electricAmber)
                        }
                        .padding(.horizontal, 10)
                        .padding(.vertical, 6)
                        .background(VidyutTheme.borderSlate)
                        .cornerRadius(16)
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .background(VidyutTheme.backgroundDark)

                Divider()
                    .background(VidyutTheme.borderSlate)

                // Tab Content
                TabView(selection: $selectedTab) {
                    SeatCockpitView()
                        .tabItem {
                            Label(localization.strings.tabSeats, systemImage: "car.side.fill")
                        }
                        .tag(0)

                    SoundboxView()
                        .tabItem {
                            Label(localization.strings.tabSoundbox, systemImage: "speaker.wave.3.fill")
                        }
                        .tag(1)

                    DailyKhataView()
                        .tabItem {
                            Label(localization.strings.tabKhata, systemImage: "indianrupeesign.circle.fill")
                        }
                        .tag(2)

                    BatteryView()
                        .tabItem {
                            Label(localization.strings.tabBattery, systemImage: "bolt.batteryblock.fill")
                        }
                        .tag(3)
                }
                .accentColor(VidyutTheme.electricAmber)
            }
            .navigationBarHidden(true)
            .background(VidyutTheme.backgroundDark.edgesIgnoringSafeArea(.all))
            .sheet(isPresented: $showLanguageSheet) {
                LanguageSelectionSheet(selectedLanguage: $localization.currentLanguage) { lang in
                    localization.setLanguage(lang)
                    showLanguageSheet = false
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

public struct LanguageSelectionSheet: View {
    @Binding var selectedLanguage: AppLanguage
    var onSelect: (AppLanguage) -> Void
    @Environment(\.presentationMode) var presentationMode

    public var body: some View {
        NavigationView {
            List {
                Section(header: Text("Select App Language / भाषा चुनें").font(.caption).foregroundColor(VidyutTheme.textMuted)) {
                    ForEach(AppLanguage.allCases) { lang in
                        Button(action: {
                            onSelect(lang)
                        }) {
                            HStack {
                                Text(lang.flag)
                                    .font(.title3)
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(lang.nativeName)
                                        .font(.headline)
                                        .foregroundColor(selectedLanguage == lang ? VidyutTheme.electricAmber : .white)
                                    Text(lang.englishName)
                                        .font(.caption)
                                        .foregroundColor(VidyutTheme.textMuted)
                                }
                                Spacer()
                                if selectedLanguage == lang {
                                    Image(systemName: "checkmark.circle.fill")
                                        .foregroundColor(VidyutTheme.electricAmber)
                                }
                            }
                            .padding(.vertical, 4)
                        }
                    }
                }
            }
            .listStyle(InsetGroupedListStyle())
            .navigationTitle("🌐 भाषा (Languages)")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Done") {
                        presentationMode.wrappedValue.dismiss()
                    }
                    .foregroundColor(VidyutTheme.electricAmber)
                }
            }
        }
        .preferredColorScheme(.dark)
    }
}
