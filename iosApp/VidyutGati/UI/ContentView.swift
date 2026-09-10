import SwiftUI

public struct ContentView: View {
    @State private var selectedTab = 0

    public init() {}

    public var body: some View {
        TabView(selection: $selectedTab) {
            SeatCockpitView()
                .tabItem {
                    Label("सवारी", systemImage: "car.side.fill")
                }
                .tag(0)

            SoundboxView()
                .tabItem {
                    Label("आवाज़", systemImage: "speaker.wave.3.fill")
                }
                .tag(1)

            DailyKhataView()
                .tabItem {
                    Label("खाता", systemImage: "indianrupeesign.circle.fill")
                }
                .tag(2)

            BatteryView()
                .tabItem {
                    Label("बैटरी", systemImage: "bolt.batteryblock.fill")
                }
                .tag(3)
        }
        .accentColor(VidyutTheme.electricAmber)
    }
}
