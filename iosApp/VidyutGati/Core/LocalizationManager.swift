import Foundation
import SwiftUI
import Combine

public enum AppLanguage: String, CaseIterable, Identifiable, Codable {
    case hindi = "hi"
    case hinglish = "hi-Latn"
    case english = "en"
    case bengali = "bn"
    case punjabi = "pa"
    case gujarati = "gu"
    case marathi = "mr"
    case tamil = "ta"
    case telugu = "te"

    public var id: String { rawValue }

    public var nativeName: String {
        switch self {
        case .hindi: return "हिन्दी"
        case .hinglish: return "Hinglish (हिंग्लिश)"
        case .english: return "English"
        case .bengali: return "বাংলা (Toto)"
        case .punjabi: return "ਪੰਜਾਬੀ"
        case .gujarati: return "ગુજરાતી"
        case .marathi: return "मराठी"
        case .tamil: return "தமிழ்"
        case .telugu: return "తెలుగు"
        }
    }

    public var englishName: String {
        switch self {
        case .hindi: return "Hindi"
        case .hinglish: return "Hinglish"
        case .english: return "English"
        case .bengali: return "Bengali"
        case .punjabi: return "Punjabi"
        case .gujarati: return "Gujarati"
        case .marathi: return "Marathi"
        case .tamil: return "Tamil"
        case .telugu: return "Telugu"
        }
    }

    public var ttsLocale: String {
        switch self {
        case .hindi, .hinglish: return "hi-IN"
        case .english: return "en-IN"
        case .bengali: return "bn-IN"
        case .punjabi: return "pa-IN"
        case .gujarati: return "gu-IN"
        case .marathi: return "mr-IN"
        case .tamil: return "ta-IN"
        case .telugu: return "te-IN"
        }
    }

    public var flag: String {
        switch self {
        case .english: return "🌐"
        default: return "🇮🇳"
        }
    }
}

public struct IndianCurrencyFormatter {
    public static func formatInr(_ amount: Double, showDecimals: Bool = false) -> String {
        let intPart = Int64(amount)
        let formatted = formatIndianNumber(intPart)
        if showDecimals && amount.truncatingRemainder(dividingBy: 1.0) != 0.0 {
            let decimals = String(format: ".%02d", Int((amount - Double(intPart)) * 100))
            return "₹\(formatted)\(decimals)"
        }
        return "₹\(formatted)"
    }

    public static func formatInr(_ amount: Int) -> String {
        return formatInr(Double(amount))
    }

    private static func formatIndianNumber(_ number: Int64) -> String {
        if number < 0 { return "-\(formatIndianNumber(-number))" }
        let s = String(number)
        if s.count <= 3 { return s }

        let last3 = String(s.suffix(3))
        let rest = String(s.dropLast(3))

        var result = ""
        var count = 0
        for char in rest.reversed() {
            result.append(char)
            count += 1
            if count == 2 && result.count < rest.count + (rest.count / 2) {
                result.append(",")
                count = 0
            }
        }
        if result.hasSuffix(",") {
            result.removeLast()
        }
        return String(result.reversed()) + "," + last3
    }

    public static func getSpokenAmountWords(_ amount: Int, language: AppLanguage) -> String {
        switch language {
        case .hindi:
            switch amount {
            case 5: return "पांच"
            case 10: return "दस"
            case 15: return "पंद्रह"
            case 20: return "बीस"
            case 25: return "पच्चीस"
            case 30: return "तीस"
            case 35: return "पैंतीस"
            case 40: return "चालीस"
            case 50: return "पचास"
            case 60: return "साठ"
            case 70: return "सत्तर"
            case 80: return "अस्सी"
            case 90: return "नब्बे"
            case 100: return "एक सौ"
            case 150: return "एक सौ पचास"
            case 200: return "दो सौ"
            default: return "\(amount)"
            }
        case .hinglish:
            switch amount {
            case 5: return "paanch"
            case 10: return "das"
            case 15: return "pandrah"
            case 20: return "bees"
            case 25: return "pachhis"
            case 30: return "tees"
            case 35: return "paintis"
            case 40: return "chalis"
            case 50: return "pachas"
            case 60: return "saath"
            case 70: return "sattar"
            case 80: return "assi"
            case 90: return "nabbe"
            case 100: return "ek sau"
            case 150: return "dedh sau"
            case 200: return "do sau"
            default: return "\(amount)"
            }
        case .english:
            switch amount {
            case 5: return "five"
            case 10: return "ten"
            case 15: return "fifteen"
            case 20: return "twenty"
            case 25: return "twenty-five"
            case 30: return "thirty"
            case 35: return "thirty-five"
            case 40: return "forty"
            case 50: return "fifty"
            case 60: return "sixty"
            case 70: return "seventy"
            case 80: return "eighty"
            case 90: return "ninety"
            case 100: return "one hundred"
            case 150: return "one hundred fifty"
            case 200: return "two hundred"
            default: return "\(amount)"
            }
        case .bengali:
            switch amount {
            case 5: return "পাঁচ"
            case 10: return "দশ"
            case 15: return "পনেরো"
            case 20: return "কুড়ি"
            case 25: return "পঁচিশ"
            case 30: return "ত্রিশ"
            case 40: return "চল্লিশ"
            case 50: return "পঞ্চাশ"
            case 100: return "একশো"
            default: return "\(amount)"
            }
        case .punjabi:
            switch amount {
            case 5: return "ਪੰਜ"
            case 10: return "ਦਸ"
            case 15: return "ਪੰਦਰਾਂ"
            case 20: return "ਵੀਹ"
            case 25: return "ਪੱਚੀ"
            case 30: return "ਤੀਹ"
            case 40: return "ਚਾਲੀ"
            case 50: return "ਪੰਜਾਹ"
            case 100: return "ਇੱਕ ਸੌ"
            default: return "\(amount)"
            }
        case .gujarati:
            switch amount {
            case 5: return "પાંચ"
            case 10: return "દસ"
            case 15: return "પંદર"
            case 20: return "વીસ"
            case 25: return "પચ્ચીસ"
            case 30: return "ત્રીસ"
            case 40: return "ચાલીસ"
            case 50: return "પચાસ"
            case 100: return "એક સો"
            default: return "\(amount)"
            }
        case .marathi:
            switch amount {
            case 5: return "पाच"
            case 10: return "दहा"
            case 15: return "पंधरा"
            case 20: return "वीस"
            case 25: return "पंचवीस"
            case 30: return "तीस"
            case 40: return "चाळीस"
            case 50: return "पन्नास"
            case 100: return "शंभर"
            default: return "\(amount)"
            }
        case .tamil:
            switch amount {
            case 5: return "ஐந்து"
            case 10: return "பத்து"
            case 15: return "பதினைந்து"
            case 20: return "இருபது"
            case 25: return "இருபத்தைந்து"
            case 30: return "முப்பது"
            case 40: return "நாற்பது"
            case 50: return "ஐம்பது"
            case 100: return "நூறு"
            default: return "\(amount)"
            }
        case .telugu:
            switch amount {
            case 5: return "ఐదు"
            case 10: return "పది"
            case 15: return "పదిహేను"
            case 20: return "ఇరవై"
            case 25: return "ఇరవై ఐదు"
            case 30: return "ముప్పై"
            case 40: return "నలభై"
            case 50: return "యాభై"
            case 100: return "వంద"
            default: return "\(amount)"
            }
        }
    }

    public static func getCurrencyUnitWord(_ language: AppLanguage) -> String {
        switch language {
        case .hindi, .marathi, .gujarati, .punjabi: return "रुपये"
        case .hinglish: return "rupaye"
        case .english: return "rupees"
        case .bengali: return "টাকা"
        case .tamil: return "ரூபாய்"
        case .telugu: return "రూపాయలు"
        }
    }
}

public struct UiStrings {
    public let appTitle: String
    public let tabSeats: String
    public let tabSoundbox: String
    public let tabKhata: String
    public let tabBattery: String
    public let activeRoute: String
    public let seatsFullBadge: String
    public let oneSeatLeftBadge: String
    public let seatsRemainingBadge: (Int) -> String
    public let passengersOnboard: String
    public let boardPassenger: String
    public let deboardPassenger: String
    public let rushHourQuickFill: String
    public let farePerSeat: String
    public let tripCompleteTitle: (Int) -> String
    public let cashPayment: String
    public let upiPayment: String
    public let recentTrips: String
    public let noTripsYet: String
    public let deleteTripPromptTitle: String
    public let deleteTripPromptMessage: (String, Int, Int) -> String
    public let soundboxTitle: String
    public let soundboxSubtitle: String
    public let tapToAnnounce: String
    public let repeatAnnouncement: String
    public let autoSoundboxTitle: String
    public let autoSoundboxSubtitle: String
    public let enableAutoSoundbox: String
    public let driverUpiTitle: String
    public let recentPayments: String
    public let noPaymentsYet: String
    public let deletePaymentPromptTitle: String
    public let deletePaymentPromptMessage: (String, Int) -> String
    public let dailyKhataTitle: String
    public let netProfitTitle: String
    public let profitBadge: String
    public let lossBadge: String
    public let passengersLabel: String
    public let tripsLabel: String
    public let grossEarningsLabel: String
    public let thekedarRentLabel: String
    public let chargingExpenseLabel: String
    public let otherExpensesLabel: String
    public let resetKhataButton: String
    public let resetKhataPromptTitle: String
    public let resetKhataPromptMessage: String
    public let shareWhatsAppButton: String
    public let weeklyOverviewTitle: String
    public let batteryGuideTitle: String
    public let sagFilterBadge: String
    public let estRangeLabel: String
    public let batteryHealthy: String
    public let batteryLowWarning: String
    public let selectLanguageTitle: String
    public let confirmDelete: String
    public let cancelAction: String
    public let deleteAction: String
}

public struct LocalizedStringsCatalog {
    public static func get(_ language: AppLanguage) -> UiStrings {
        switch language {
        case .hindi: return hindiStrings
        case .hinglish: return hinglishStrings
        case .english: return englishStrings
        case .bengali: return bengaliStrings
        case .punjabi: return punjabiStrings
        case .gujarati: return gujaratiStrings
        case .marathi: return marathiStrings
        case .tamil: return tamilStrings
        case .telugu: return teluguStrings
        }
    }

    private static let hindiStrings = UiStrings(
        appTitle: "विद्युतगति (VidyutGati)",
        tabSeats: "सवारी",
        tabSoundbox: "आवाज़",
        tabKhata: "खाता",
        tabBattery: "बैटरी",
        activeRoute: "सक्रिय रूट (Active Route)",
        seatsFullBadge: "⚡ गाड़ी फुल है (FULL)",
        oneSeatLeftBadge: "⚠️ केवल 1 सीट बाकी!",
        seatsRemainingBadge: { "\($0) सीटें खाली हैं" },
        passengersOnboard: "सवारियां बैठी हैं (Passengers)",
        boardPassenger: "+ सवारी बैठी",
        deboardPassenger: "सवारी उतरी",
        rushHourQuickFill: "⚡ भीड़ समय: 1-टैप पूरी गाड़ी फुल",
        farePerSeat: "प्रति सवारी किराया:",
        tripCompleteTitle: { "ट्रिप समाप्त व किराया संग्रह (₹\($0))" },
        cashPayment: "💵 नकद (Cash)",
        upiPayment: "📱 यूपीआई (UPI)",
        recentTrips: "हाल की ट्रिप (Recent Trips)",
        noTripsYet: "आज अभी कोई ट्रिप दर्ज नहीं हुई है।",
        deleteTripPromptTitle: "ट्रिप हटाएं? (Delete Trip)",
        deleteTripPromptMessage: { route, fare, count in "क्या आप सच में \(route) (₹\(fare), \(count) सवारियां) की ट्रिप हटाना चाहते हैं? आज के खाते से इसकी कमाई घट जाएगी।" },
        soundboxTitle: "मुफ़्त आवाज़ बॉक्स (Digital Soundbox)",
        soundboxSubtitle: "बिना किसी डिवाइस किराये के 100% मुफ़्त",
        tapToAnnounce: "भुगतान आवाज़ ट्रिगर करें (Tap to Announce):",
        repeatAnnouncement: "पिछली आवाज़ दुबारा सुनें (Repeat)",
        autoSoundboxTitle: "स्वचालित हैंड्स-फ्री आवाज़ (Auto UPI)",
        autoSoundboxSubtitle: "पेटीएम, फोनपे या गूगल पे पेमेंट आने पर ऐप खुद बोलकर बताएगा।",
        enableAutoSoundbox: "हैंड्स-फ्री ऑटो आवाज़ चालू करें",
        driverUpiTitle: "सारथी यूपीआई आईडी (Driver UPI)",
        recentPayments: "हाल के भुगतान (Payment History)",
        noPaymentsYet: "अभी कोई नया भुगतान नहीं है।",
        deletePaymentPromptTitle: "पेमेंट रिकॉर्ड हटाएं? (Delete Entry)",
        deletePaymentPromptMessage: { app, fare in "क्या आप \(app) से प्राप्त ₹\(fare) का रिकॉर्ड हटाना चाहते हैं?" },
        dailyKhataTitle: "दैनिक हिसाब (Daily Khata)",
        netProfitTitle: "आज की शुद्ध जेब कमाई (Net Profit)",
        profitBadge: "✅ बचत खाते में लाभ",
        lossBadge: "⚠️ अभी खर्चा बाकी है",
        passengersLabel: "सवारियाँ",
        tripsLabel: "चक्कर (Trips)",
        grossEarningsLabel: "कुल सवारी कमाई (Gross)",
        thekedarRentLabel: "मालिक का किराया (ठेकेदार भत्ता)",
        chargingExpenseLabel: "बैटरी चार्जिंग / स्वैप खर्च",
        otherExpensesLabel: "अन्य खर्च (पंक्चर, चाय)",
        resetKhataButton: "आज का हिसाब रीसेट करें (Reset Khata)",
        resetKhataPromptTitle: "आज का खाता रीसेट करें?",
        resetKhataPromptMessage: "क्या आप आज का पूरा हिसाब शून्य (₹0) करना चाहते हैं? यह क्रिया वापस नहीं ली जा सकती।",
        shareWhatsAppButton: "शेयर करें",
        weeklyOverviewTitle: "साप्ताहिक रिपोर्ट (7-Day Overview)",
        batteryGuideTitle: "बैटरी व रेंज गाइड",
        sagFilterBadge: "⚡ वोल्टेज सैग फिल्टर सक्रिय",
        estRangeLabel: "अनुमानित बची हुई दूरी",
        batteryHealthy: "✅ बैटरी सामान्य स्थिति में है",
        batteryLowWarning: "⚠️ बैटरी कम है! पास के चार्जिंग स्टेशन जाएं।",
        selectLanguageTitle: "भाषा चुनें (Select Language)",
        confirmDelete: "हटाने की पुष्टि करें",
        cancelAction: "रद्द करें (Cancel)",
        deleteAction: "हटाएं (Delete)"
    )

    private static let hinglishStrings = UiStrings(
        appTitle: "VidyutGati",
        tabSeats: "Sawari",
        tabSoundbox: "Awaz Box",
        tabKhata: "Khata",
        tabBattery: "Battery",
        activeRoute: "Active Route",
        seatsFullBadge: "⚡ Gadi Full Hai (FULL)",
        oneSeatLeftBadge: "⚠️ Sirf 1 Seat Baki!",
        seatsRemainingBadge: { "\($0) Seats Khali Hain" },
        passengersOnboard: "Sawariyan Baithi Hain",
        boardPassenger: "+ Sawari Baithi",
        deboardPassenger: "Sawari Utri",
        rushHourQuickFill: "⚡ Rush Hour: 1-Tap Gadi Full",
        farePerSeat: "Per Seat Kiraya:",
        tripCompleteTitle: { "Trip Complete & Kiraya Collect (₹\($0))" },
        cashPayment: "💵 Cash",
        upiPayment: "📱 UPI",
        recentTrips: "Recent Trips History",
        noTripsYet: "Aaj abhi koi trip record nahi hui.",
        deleteTripPromptTitle: "Trip Delete Karein?",
        deleteTripPromptMessage: { route, fare, count in "Kya aap \(route) (₹\(fare), \(count) sawariyan) ki trip delete karna chahte hain? Khate se rollback ho jayega." },
        soundboxTitle: "Free Awaz Box (Digital Soundbox)",
        soundboxSubtitle: "Bina kisi machine rent ke 100% Free",
        tapToAnnounce: "Payment Sound Trigger Karein:",
        repeatAnnouncement: "Pichli Awaz Dobara Sunein (Repeat)",
        autoSoundboxTitle: "Automatic Hands-Free Awaz (Auto UPI)",
        autoSoundboxSubtitle: "Paytm, PhonePe ya GPay aane par app khud bolkar batayega.",
        enableAutoSoundbox: "Hands-Free Auto Awaz Enable Karein",
        driverUpiTitle: "Driver UPI ID",
        recentPayments: "Recent Payments History",
        noPaymentsYet: "Abhi koi naya payment nahi aaya.",
        deletePaymentPromptTitle: "Payment Record Delete Karein?",
        deletePaymentPromptMessage: { app, fare in "Kya aap \(app) se received ₹\(fare) ka record delete karna chahte hain?" },
        dailyKhataTitle: "Daily Khata (Hisab-Kitab)",
        netProfitTitle: "Aaj Ki Shuddh Jeb Kamai (Net Profit)",
        profitBadge: "✅ Jeb Mein Profit",
        lossBadge: "⚠️ Abhi Kharcha Baki Hai",
        passengersLabel: "Sawariyan",
        tripsLabel: "Chakkar (Trips)",
        grossEarningsLabel: "Kul Sawari Kamai (Gross)",
        thekedarRentLabel: "Malik Ka Kiraya (Thekedar Bhatta)",
        chargingExpenseLabel: "Battery Charging / Swap Kharcha",
        otherExpensesLabel: "Other Kharche (Puncture, Chai)",
        resetKhataButton: "Aaj Ka Khata Reset Karein",
        resetKhataPromptTitle: "Aaj Ka Khata Reset Karein?",
        resetKhataPromptMessage: "Kya aap aaj ka hisab ₹0 karna chahte hain? Yeh wapas nahi aayega.",
        shareWhatsAppButton: "Share Karein",
        weeklyOverviewTitle: "Weekly Report (7-Day Overview)",
        batteryGuideTitle: "Battery & Range Guide",
        sagFilterBadge: "⚡ Voltage Sag Filter Active",
        estRangeLabel: "Bachi Hui Range (Km)",
        batteryHealthy: "✅ Battery Normal Condition Mein Hai",
        batteryLowWarning: "⚠️ Battery Low Hai! Charging Station Jayein.",
        selectLanguageTitle: "Language Chunein (Select Language)",
        confirmDelete: "Delete Confirmation",
        cancelAction: "Cancel",
        deleteAction: "Delete"
    )

    private static let englishStrings = UiStrings(
        appTitle: "VidyutGati",
        tabSeats: "Seats",
        tabSoundbox: "Soundbox",
        tabKhata: "Ledger",
        tabBattery: "Battery",
        activeRoute: "Active Route",
        seatsFullBadge: "⚡ Vehicle Full (FULL)",
        oneSeatLeftBadge: "⚠️ Only 1 Seat Left!",
        seatsRemainingBadge: { "\($0) Seats Available" },
        passengersOnboard: "Passengers Onboard",
        boardPassenger: "+ Board",
        deboardPassenger: "Deboard",
        rushHourQuickFill: "⚡ Rush Hour: 1-Tap Fill All Seats",
        farePerSeat: "Fare Per Seat:",
        tripCompleteTitle: { "Trip Complete & Collect Fare (₹\($0))" },
        cashPayment: "💵 Cash",
        upiPayment: "📱 UPI",
        recentTrips: "Recent Trips History",
        noTripsYet: "No trips completed yet today.",
        deleteTripPromptTitle: "Delete Trip?",
        deleteTripPromptMessage: { route, fare, count in "Are you sure you want to delete trip \(route) (₹\(fare), \(count) riders)? Earnings will be rolled back from today's ledger." },
        soundboxTitle: "Free Digital Voice Soundbox",
        soundboxSubtitle: "100% Free with zero device rental fees",
        tapToAnnounce: "Trigger Payment Voice Announcement:",
        repeatAnnouncement: "Replay Last Announcement (Repeat)",
        autoSoundboxTitle: "Automatic Hands-Free Soundbox (Auto UPI)",
        autoSoundboxSubtitle: "Speaks payments automatically from Paytm, PhonePe, and Google Pay.",
        enableAutoSoundbox: "Enable Hands-Free Auto Voice Access",
        driverUpiTitle: "Driver UPI ID",
        recentPayments: "Recent Payments History",
        noPaymentsYet: "No payments logged yet. Test with buttons above!",
        deletePaymentPromptTitle: "Delete Payment Log?",
        deletePaymentPromptMessage: { app, fare in "Delete payment log of ₹\(fare) from \(app)? This cannot be undone." },
        dailyKhataTitle: "Daily Shift Ledger (Khata)",
        netProfitTitle: "Today's Net Pocket Profit",
        profitBadge: "✅ Profitable Shift",
        lossBadge: "⚠️ Operational Overhead Pending",
        passengersLabel: "Passengers",
        tripsLabel: "Trips (Rounds)",
        grossEarningsLabel: "Total Gross Fare Earnings",
        thekedarRentLabel: "Vehicle Owner Rent (Thekedar Bhatta)",
        chargingExpenseLabel: "Battery Charging / Swap Cost",
        otherExpensesLabel: "Miscellaneous (Tea, Puncture)",
        resetKhataButton: "Reset Today's Ledger (Reset Khata)",
        resetKhataPromptTitle: "Reset Today's Ledger?",
        resetKhataPromptMessage: "Are you sure you want to reset today's ledger to ₹0? This cannot be undone.",
        shareWhatsAppButton: "Share Summary",
        weeklyOverviewTitle: "Weekly 7-Day Performance Overview",
        batteryGuideTitle: "Battery Health & Range Radar",
        sagFilterBadge: "⚡ Voltage Sag EMA Filter Active",
        estRangeLabel: "Estimated Remaining Range (Km)",
        batteryHealthy: "✅ Battery health is optimal",
        batteryLowWarning: "⚠️ Battery critically low! Head to nearest swap stall.",
        selectLanguageTitle: "Select Language",
        confirmDelete: "Confirm Delete",
        cancelAction: "Cancel",
        deleteAction: "Delete"
    )

    private static let bengaliStrings = UiStrings(
        appTitle: "বিদ্যুৎগতি (টোটো চালক)",
        tabSeats: "যাত্রী",
        tabSoundbox: "সাউন্ডবক্স",
        tabKhata: "খাতা",
        tabBattery: "ব্যাটারি",
        activeRoute: "সক্রিয় রুট (Active Route)",
        seatsFullBadge: "⚡ টোটো ফুল (FULL)",
        oneSeatLeftBadge: "⚠️ আর ১টি সিট বাকি!",
        seatsRemainingBadge: { "\($0)টি সিট খালি আছে" },
        passengersOnboard: "যাত্রী বসে আছেন (Passengers)",
        boardPassenger: "+ যাত্রী উঠল",
        deboardPassenger: "যাত্রী নামল",
        rushHourQuickFill: "⚡ ভিড়ের সময়: ১-ট্যাপ ফুল গাড়ি",
        farePerSeat: "প্রতি যাত্রী ভাড়া:",
        tripCompleteTitle: { "ট্রিপ শেষ ও ভাড়া আদায় (₹\($0))" },
        cashPayment: "💵 নগদ (Cash)",
        upiPayment: "📱 ইউপিআই (UPI)",
        recentTrips: "সাম্প্রতিক ট্রিপ তালিকা",
        noTripsYet: "আজকের কোনো ট্রিপ এখনও শুরু হয়নি।",
        deleteTripPromptTitle: "ট্রিপ ডিলিট করবেন?",
        deleteTripPromptMessage: { route, fare, count in "আপনি কি সত্যই \(route) (₹\(fare), \(count) জন যাত্রী) ট্রিপ ডিলিট করতে চান? খাতা থেকে টাকা কমে যাবে।" },
        soundboxTitle: "ডিজিটাল সাউন্ডবক্স (ফ্রি)",
        soundboxSubtitle: "কোনো মেশিন ভাড়া ছাড়া ১০০% ফ্রি",
        tapToAnnounce: "পেমেন্ট আওয়াজ শুনুন:",
        repeatAnnouncement: "পুনরায় শুনুন (Repeat)",
        autoSoundboxTitle: "অটো ইউপিআই সাউন্ডবক্স",
        autoSoundboxSubtitle: "Paytm, PhonePe, GPay পেমেন্ট এলে স্বয়ংক্রিয়ভাবে বাংলায় বলবে।",
        enableAutoSoundbox: "অটো ভয়েস চালু করুন",
        driverUpiTitle: "চালকের ইউপিআই আইডি",
        recentPayments: "সাম্প্রতিক পেমেন্ট তালিকা",
        noPaymentsYet: "এখনও কোনো পেমেন্ট রেকর্ড হয়নি।",
        deletePaymentPromptTitle: "পেমেন্ট রেকর্ড ডিলিট করবেন?",
        deletePaymentPromptMessage: { app, fare in "আপনি কি \(app) থেকে প্রাপ্ত ₹\(fare) রেকর্ড ডিলিট করতে চান?" },
        dailyKhataTitle: "দৈনিক খাতা হিসাব",
        netProfitTitle: "আজকের আসল লাভ (পকেটের টাকা)",
        profitBadge: "✅ আজকের লাভ সুরক্ষিত",
        lossBadge: "⚠️ খরচ এখনও ওঠেনি",
        passengersLabel: "মোট যাত্রী",
        tripsLabel: "মোট ট্রিপ",
        grossEarningsLabel: "মোট যাত্রী ভাড়া আয়",
        thekedarRentLabel: "মালিকের ভাড়া (ঠেকাদার ভাতা)",
        chargingExpenseLabel: "ব্যাটারি চার্জিং / সোয়াপ খরচ",
        otherExpensesLabel: "অন্যান্য খরচ (চা, পাংচার)",
        resetKhataButton: "আজকের খাতা শূন্য (Reset) করুন",
        resetKhataPromptTitle: "আজকের খাতা রিসেট করবেন?",
        resetKhataPromptMessage: "আজকের সমস্ত আয় ও ব্যয় শূন্য (₹০) হয়ে যাবে। এটি বাতিল করা যাবে না।",
        shareWhatsAppButton: "শেয়ার করুন",
        weeklyOverviewTitle: "সাপ্তাহিক রিপোর্ট (৭ দিন)",
        batteryGuideTitle: "ব্যাটারি ও মাইলেজ গাইড",
        sagFilterBadge: "⚡ ভোল্টেজ স্যাগ ফিল্টার সক্রিয়",
        estRangeLabel: "বাকি পথ চলবে (কিলোমিটার)",
        batteryHealthy: "✅ ব্যাটারির অবস্থা ভালো",
        batteryLowWarning: "⚠️ ব্যাটারি শেষ হতে চলেছে! চার্জিং পয়েন্টে যান।",
        selectLanguageTitle: "ভাষা নির্বাচন করুন (Language)",
        confirmDelete: "ডিলিট নিশ্চিত করুন",
        cancelAction: "বাতিল করুন (Cancel)",
        deleteAction: "ডিলিট করুন (Delete)"
    )

    private static let punjabiStrings = UiStrings(
        appTitle: "ਵਿਦਯੁਤਗਤੀ (ਰਿਕਸ਼ਾ ਸਾਰਥੀ)",
        tabSeats: "ਸਵਾਰੀ",
        tabSoundbox: "ਆਵਾਜ਼",
        tabKhata: "ਖਾਤਾ",
        tabBattery: "ਬੈਟਰੀ",
        activeRoute: "ਚਾਲੂ ਰੂਟ (Active Route)",
        seatsFullBadge: "⚡ ਗੱਡੀ ਫੁੱਲ ਹੈ (FULL)",
        oneSeatLeftBadge: "⚠️ ਸਿਰਫ਼ 1 ਸੀਟ ਬਾਕੀ!",
        seatsRemainingBadge: { "\($0) ਸੀਟਾਂ ਖਾਲੀ ਹਨ" },
        passengersOnboard: "ਸਵਾਰੀਆਂ ਬੈਠੀਆਂ ਹਨ",
        boardPassenger: "+ ਸਵਾਰੀ ਚੜ੍ਹੀ",
        deboardPassenger: "ਸਵਾਰੀ ਉੱਤਰੀ",
        rushHourQuickFill: "⚡ ਰਸ਼ ਟਾਈਮ: 1-ਟੈਪ ਗੱਡੀ ਫੁੱਲ",
        farePerSeat: "ਪ੍ਰਤੀ ਸਵਾਰੀ ਕਿਰਾਇਆ:",
        tripCompleteTitle: { "ਟ੍ਰਿਪ ਸਮਾਪਤ ਤੇ ਕਿਰਾਇਆ (₹\($0))" },
        cashPayment: "💵 ਨਕਦ (Cash)",
        upiPayment: "📱 ਯੂਪੀਆਈ (UPI)",
        recentTrips: "ਹਾਲ ਦੀਆਂ ਟ੍ਰਿਪਾਂ",
        noTripsYet: "ਅੱਜ ਅਜੇ ਕੋਈ ਟ੍ਰਿਪ ਦਰਜ ਨਹੀਂ ਹੋਈ।",
        deleteTripPromptTitle: "ਟ੍ਰਿਪ ਹਟਾਓ? (Delete Trip)",
        deleteTripPromptMessage: { route, fare, count in "ਕੀ ਤੁਸੀਂ \(route) (₹\(fare), \(count) ਸਵਾਰੀਆਂ) ਟ੍ਰਿਪ ਹਟਾਉਣਾ ਚਾਹੁੰਦੇ ਹੋ?" },
        soundboxTitle: "ਮੁਫ਼ਤ ਆਵਾਜ਼ ਬਾਕਸ (Digital Soundbox)",
        soundboxSubtitle: "ਬਿਨਾਂ ਕਿਸੇ ਮਸ਼ੀਨ ਕਿਰਾਏ ਦੇ 100% ਮੁਫ਼ਤ",
        tapToAnnounce: "ਪੇਮੈਂਟ ਆਵਾਜ਼ ਸੁਣੋ:",
        repeatAnnouncement: "ਦੁਬਾਰਾ ਸੁਣੋ (Repeat)",
        autoSoundboxTitle: "ਆਟੋਮੈਟਿਕ ਆਵਾਜ਼ (Auto UPI)",
        autoSoundboxSubtitle: "Paytm, PhonePe ਜਾਂ GPay ਤੋਂ ਪੈਸੇ ਆਉਣ 'ਤੇ ਐਪ ਖੁਦ ਬੋਲੇਗੀ।",
        enableAutoSoundbox: "ਹੈਂਡਸ-ਫ੍ਰੀ ਆਵਾਜ਼ ਚਾਲੂ ਕਰੋ",
        driverUpiTitle: "ਚਾਲਕ UPI ID",
        recentPayments: "ਹਾਲ ਦੇ ਭੁਗਤਾਨ",
        noPaymentsYet: "ਅਜੇ ਕੋਈ ਨਵਾਂ ਭੁਗਤਾਨ ਨਹੀਂ ਹੈ।",
        deletePaymentPromptTitle: "ਭੁਗਤਾਨ ਰਿਕਾਰਡ ਹਟਾਓ?",
        deletePaymentPromptMessage: { app, fare in "ਕੀ ਤੁਸੀਂ \(app) ਤੋਂ ਪ੍ਰਾਪਤ ₹\(fare) ਦਾ ਰਿਕਾਰਡ ਹਟਾਉਣਾ ਚਾਹੁੰਦੇ ਹੋ?" },
        dailyKhataTitle: "ਰੋਜ਼ਾਨਾ ਖਾਤਾ (Daily Khata)",
        netProfitTitle: "ਅੱਜ ਦੀ ਸ਼ੁੱਧ ਬਚਤ (Net Profit)",
        profitBadge: "✅ ਜੇਬ ਵਿਚ ਮੁਨਾਫ਼ਾ",
        lossBadge: "⚠️ ਅਜੇ ਖ਼ਰਚਾ ਬਾਕੀ ਹੈ",
        passengersLabel: "ਸਵਾਰੀਆਂ",
        tripsLabel: "ਚੱਕਰ (Trips)",
        grossEarningsLabel: "ਕੁੱਲ ਕਿਰਾਇਆ ਕਮਾਈ",
        thekedarRentLabel: "ਮਾਲਕ ਦਾ ਕਿਰਾਇਆ (ਭੱਤਾ)",
        chargingExpenseLabel: "ਬੈਟਰੀ ਚਾਰਜਿੰਗ / ਸਵੈਪ ਖ਼ਰਚ",
        otherExpensesLabel: "ਹੋਰ ਖ਼ਰਚੇ (ਪੰਕਚਰ, ਚਾਹ)",
        resetKhataButton: "ਅੱਜ ਦਾ ਖਾਤਾ ਰੀਸੈਟ ਕਰੋ",
        resetKhataPromptTitle: "ਅੱਜ ਦਾ ਖਾਤਾ ਰੀਸੈਟ ਕਰਨਾ ਹੈ?",
        resetKhataPromptMessage: "ਕੀ ਤੁਸੀਂ ਅੱਜ ਦਾ ਹਿਸਾਬ ₹0 ਕਰਨਾ ਚਾਹੁੰਦੇ ਹੋ?",
        shareWhatsAppButton: "ਸ਼ੇਅਰ ਕਰੋ",
        weeklyOverviewTitle: "ਹਫ਼ਤਾਵਾਰੀ ਰਿਪੋਰਟ (7 ਦਿਨ)",
        batteryGuideTitle: "ਬੈਟਰੀ ਤੇ ਰੇਂਜ ਗਾਈਡ",
        sagFilterBadge: "⚡ ਵੋਲਟੇਜ ਸੈਗ ਫਿਲਟਰ ਚਾਲੂ",
        estRangeLabel: "ਅੰਦਾਜ਼ਨ ਬਾਕੀ ਦੂਰੀ (Km)",
        batteryHealthy: "✅ ਬੈਟਰੀ ਠੀਕ ਹੈ",
        batteryLowWarning: "⚠️ ਬੈਟਰੀ ਘੱਟ ਹੈ! ਚਾਰਜਿੰਗ ਸਟੇਸ਼ਨ ਜਾਓ।",
        selectLanguageTitle: "ਭਾਸ਼ਾ ਚੁਣੋ (Select Language)",
        confirmDelete: "ਪੁਸ਼ਟੀ ਕਰੋ",
        cancelAction: "ਰੱਦ ਕਰੋ (Cancel)",
        deleteAction: "ਹਟਾਓ (Delete)"
    )

    private static let gujaratiStrings = UiStrings(
        appTitle: "વિદ્યુતગતિ (રિક્ષા સારથી)",
        tabSeats: "પેસેન્જર",
        tabSoundbox: "સાઉન્ડબોક્સ",
        tabKhata: "ખાતાવહી",
        tabBattery: "બેટરી",
        activeRoute: "સક્રિય રૂટ (Active Route)",
        seatsFullBadge: "⚡ ગાડી ફુલ છે (FULL)",
        oneSeatLeftBadge: "⚠️ માત્ર 1 સીટ બાકી!",
        seatsRemainingBadge: { "\($0) સીટો ખાલી છે" },
        passengersOnboard: "મુસાફરો બેઠા છે",
        boardPassenger: "+ પેસેન્જર બેઠા",
        deboardPassenger: "પેસેન્જર ઉતર્યા",
        rushHourQuickFill: "⚡ રશ કલાક: 1-ટેપ ગાડી ફુલ",
        farePerSeat: "સીટ દીઠ ભાડું:",
        tripCompleteTitle: { "ટ્રિપ પૂર્ણ અને ભાડું (₹\($0))" },
        cashPayment: "💵 રોકડ (Cash)",
        upiPayment: "📱 યુપીઆઈ (UPI)",
        recentTrips: "તાજેતરની સવારીઓ",
        noTripsYet: "આજે હજુ કોઈ સવારી નોંધાઈ નથી.",
        deleteTripPromptTitle: "ટ્રિપ ડિલીટ કરો?",
        deleteTripPromptMessage: { route, fare, count in "શું તમે \(route) (₹\(fare), \(count) સવારીઓ) ટ્રિપ ડિલીટ કરવા માંગો છો?" },
        soundboxTitle: "મફત વોઇસ સાઉન્ડબોક્સ",
        soundboxSubtitle: "કોઈપણ મશીન ભાડા વગર ૧૦૦% મફત",
        tapToAnnounce: "પેમેન્ટ અવાજ સાંભળો:",
        repeatAnnouncement: "ફરીથી સાંભળો (Repeat)",
        autoSoundboxTitle: "ઓટોમેટિક સાઉન્ડબોક્સ (Auto UPI)",
        autoSoundboxSubtitle: "Paytm, PhonePe અથવા GPay માંથી નાણાં આવે ત્યારે આપમેળે બોલશે.",
        enableAutoSoundbox: "ઓટો વોઇસ ચાલુ કરો",
        driverUpiTitle: "ડ્રાઈવર UPI ID",
        recentPayments: "તાજેતરની ચૂકવણીઓ",
        noPaymentsYet: "હજુ કોઈ નવી ચૂકવણી આવી નથી.",
        deletePaymentPromptTitle: "પેમેન્ટ રેકોર્ડ ડિલીટ કરો?",
        deletePaymentPromptMessage: { app, fare in "શું તમે \(app) તરફથી મળેલ ₹\(fare) રેકોર્ડ ડિલીટ કરવા માંગો છો?" },
        dailyKhataTitle: "દૈનિક ખાતાવહી (Daily Khata)",
        netProfitTitle: "આજની ચોખ્ખી બચત (Net Profit)",
        profitBadge: "✅ નફાકારક દિવસ",
        lossBadge: "⚠️ ખર્ચ હજુ નીકળ્યો નથી",
        passengersLabel: "કુલ પેસેન્જર",
        tripsLabel: "ફેરા (Trips)",
        grossEarningsLabel: "કુલ ભાડાની આવક",
        thekedarRentLabel: "માલિકનું ભાડું (ભથ્થું)",
        chargingExpenseLabel: "બેટરી ચાર્જિંગ / સ્વેપ ખર્ચ",
        otherExpensesLabel: "અન્ય ખર્ચ (પંચર, ચા)",
        resetKhataButton: "આજનું ખાતું રીસેટ કરો",
        resetKhataPromptTitle: "ખાતું રીસેટ કરવું છે?",
        resetKhataPromptMessage: "આજનો બધો હિસાબ શૂન્ય (₹0) થઈ જશે.",
        shareWhatsAppButton: "શેર કરો",
        weeklyOverviewTitle: "સાપ્તાહિક અહેવાલ (૭ દિવસ)",
        batteryGuideTitle: "બેટરી અને રેન્જ ગાઈડ",
        sagFilterBadge: "⚡ વોલ્ટેજ સેગ ફિલ્ટર સક્રિય",
        estRangeLabel: "અંદાજિત બાકી કિમી",
        batteryHealthy: "✅ બેટરી સ્થિતિ સામાન્ય છે",
        batteryLowWarning: "⚠️ બેટરી ઓછી છે! સ્ટેશન પર જાઓ.",
        selectLanguageTitle: "ભાષા પસંદ કરો (Language)",
        confirmDelete: "ડિલીટ પુષ્ટિ",
        cancelAction: "રદ કરો (Cancel)",
        deleteAction: "ડિલીટ કરો (Delete)"
    )

    private static let marathiStrings = UiStrings(
        appTitle: "विद्युतगती (रिक्षा चालक)",
        tabSeats: "प्रवासी",
        tabSoundbox: "आवाज बॉक्स",
        tabKhata: "खातेवही",
        tabBattery: "बॅटरी",
        activeRoute: "सक्रिय मार्ग (Active Route)",
        seatsFullBadge: "⚡ गाडी फुल आहे (FULL)",
        oneSeatLeftBadge: "⚠️ फक्त १ जागा शिल्लक!",
        seatsRemainingBadge: { "\($0) जागा रिकाम्या आहेत" },
        passengersOnboard: "प्रवासी बसले आहेत",
        boardPassenger: "+ प्रवासी बसले",
        deboardPassenger: "प्रवासी उतरले",
        rushHourQuickFill: "⚡ गर्दीच्या वेळी: १-टॅप गाडी फुल",
        farePerSeat: "प्रति प्रवासी भाडे:",
        tripCompleteTitle: { "फेरी पूर्ण व भाडे जमा (₹\($0))" },
        cashPayment: "💵 रोख (Cash)",
        upiPayment: "📱 यूपीआय (UPI)",
        recentTrips: "अलीकडील फेऱ्या",
        noTripsYet: "आज अद्याप कोणतीही फेरी नोंदवलेली नाही.",
        deleteTripPromptTitle: "फेरी हटवावी का? (Delete)",
        deleteTripPromptMessage: { route, fare, count in "तुम्हाला \(route) (₹\(fare), \(count) प्रवासी) फेरी नक्की हटवायची आहे का?" },
        soundboxTitle: "मोफत आवाज बॉक्स (Digital Soundbox)",
        soundboxSubtitle: "कोणत्याही मशीन भाड्याशिवाय १००% मोफत",
        tapToAnnounce: "पेमेंट आवाज ऐका:",
        repeatAnnouncement: "पुन्हा ऐका (Repeat)",
        autoSoundboxTitle: "स्वयंचलित आवाज (Auto UPI)",
        autoSoundboxSubtitle: "Paytm, PhonePe किंवा GPay वरून पैसे आल्यास ॲप स्वतः बोलेल.",
        enableAutoSoundbox: "हँड्स-फ्री आवाज सुरू करा",
        driverUpiTitle: "सारथी UPI ID",
        recentPayments: "अलीकडील पेमेंट इतिहास",
        noPaymentsYet: "अद्याप नवीन पेमेंट आलेले नाही.",
        deletePaymentPromptTitle: "पेमेंट रेकॉर्ड हटवावा का?",
        deletePaymentPromptMessage: { app, fare in "तुम्हाला \(app) कडून आलेले ₹\(fare) रेकॉर्ड हटवायचे आहे का?" },
        dailyKhataTitle: "दैनिक खातेवही (Daily Khata)",
        netProfitTitle: "आजचा निव्वळ नफा (Net Profit)",
        profitBadge: "✅ खिशात नफा शिल्लक",
        lossBadge: "⚠️ खर्च अद्याप भरून आलेला नाही",
        passengersLabel: "प्रवासी",
        tripsLabel: "फेऱ्या (Trips)",
        grossEarningsLabel: "एकूण प्रवासी भाडे",
        thekedarRentLabel: "मालकाचे भाडे (ठेकेदार भत्ता)",
        chargingExpenseLabel: "बॅटरी चार्जिंग / स्वॅप खर्च",
        otherExpensesLabel: "इतर खर्च (पंचर, चहा)",
        resetKhataButton: "आजचे खाते रीसेट करा",
        resetKhataPromptTitle: "आजचे खाते रीसेट करायचे का?",
        resetKhataPromptMessage: "आजचा सर्व हिशोब शून्य (₹०) होईल. हे परत करता येणार नाही.",
        shareWhatsAppButton: "शेअर करा",
        weeklyOverviewTitle: "साप्ताहिक अहवाल (७ दिवस)",
        batteryGuideTitle: "बॅटरी व अंतर मार्गदर्शक",
        sagFilterBadge: "⚡ व्होल्टेज सॅग फिल्टर सक्रिय",
        estRangeLabel: "अंदाजे शिल्लक अंतर (Km)",
        batteryHealthy: "✅ बॅटरी उत्तम स्थितीत आहे",
        batteryLowWarning: "⚠️ बॅटरी कमी आहे! चार्जिंग केंद्रावर जा.",
        selectLanguageTitle: "भाषा निवडा (Select Language)",
        confirmDelete: "हटवण्याची पुष्टी करा",
        cancelAction: "रद्द करा (Cancel)",
        deleteAction: "हटवा (Delete)"
    )

    private static let tamilStrings = UiStrings(
        appTitle: "வித்யுத் கதி (ஆட்டோ)",
        tabSeats: "பயணிகள்",
        tabSoundbox: "சவுண்ட்பாக்ஸ்",
        tabKhata: "கணக்கு",
        tabBattery: "பேட்டரி",
        activeRoute: "செயலில் உள்ள வழித்தடம்",
        seatsFullBadge: "⚡ வண்டி ஃபுல் (FULL)",
        oneSeatLeftBadge: "⚠️ 1 சீட் மட்டுமே உள்ளது!",
        seatsRemainingBadge: { "\($0) இருக்கைகள் காலியாக உள்ளன" },
        passengersOnboard: "பயணிகள் அமர்ந்துள்ளனர்",
        boardPassenger: "+ பயணி ஏறினார்",
        deboardPassenger: "பயணி இறங்கினார்",
        rushHourQuickFill: "⚡ கூட்ட நெரிசல்: 1-தட்டு வண்டி ஃபுல்",
        farePerSeat: "ஒரு இருக்கை கட்டணம்:",
        tripCompleteTitle: { "பயணம் முடிந்தது & கட்டணம் (₹\($0))" },
        cashPayment: "💵 ரொக்கம் (Cash)",
        upiPayment: "📱 யுபிஐ (UPI)",
        recentTrips: "சமீபத்திய பயணங்கள்",
        noTripsYet: "இன்று எந்த பயணமும் இன்னும் பதிவு செய்யப்படவில்லை.",
        deleteTripPromptTitle: "பயணத்தை நீக்கவா?",
        deleteTripPromptMessage: { route, fare, count in "\(route) (₹\(fare), \(count) பயணிகள்) பயணத்தை நீக்க விரும்புகிறீர்களா?" },
        soundboxTitle: "இலவச டிஜிட்டல் சவுண்ட்பாக்ஸ்",
        soundboxSubtitle: "சாதன வாடகை இல்லாமல் 100% இலவசம்",
        tapToAnnounce: "பணம் பெற்ற குரல் ஒலியை இயக்க:",
        repeatAnnouncement: "மீண்டும் கேட்க (Repeat)",
        autoSoundboxTitle: "தானியங்கி சவுண்ட்பாக்ஸ் (Auto UPI)",
        autoSoundboxSubtitle: "Paytm, PhonePe அல்லது GPay இல் பணம் வந்தால் தானாகவே அறிவிக்கும்.",
        enableAutoSoundbox: "ஆட்டோ குரலை இயக்கு",
        driverUpiTitle: "ஓட்டுநர் UPI ID",
        recentPayments: "சமீபத்திய கட்டணங்கள்",
        noPaymentsYet: "புதிய கட்டணம் எதுவும் இல்லை.",
        deletePaymentPromptTitle: "கட்டண பதிவை நீக்கவா?",
        deletePaymentPromptMessage: { app, fare in "\(app) இலிருந்து பெறப்பட்ட ₹\(fare) பதிவை நீக்க விரும்புகிறீர்களா?" },
        dailyKhataTitle: "தினசரி கணக்கு (Daily Khata)",
        netProfitTitle: "இன்றைய நிகர லாபம் (Net Profit)",
        profitBadge: "✅ லாபகரமான நாள்",
        lossBadge: "⚠️ செலவுகள் இன்னும் ஈடுசெய்யப்படவில்லை",
        passengersLabel: "பயணிகள்",
        tripsLabel: "சுற்றுகள் (Trips)",
        grossEarningsLabel: "மொத்த கட்டண வருமானம்",
        thekedarRentLabel: "உரிமையாளர் வாடகை (படி)",
        chargingExpenseLabel: "பேட்டரி சார்ஜிங் / ஸ்வாப் செலவு",
        otherExpensesLabel: "இதர செலவுகள் (டீ, பஞ்சர்)",
        resetKhataButton: "இன்றைய கணக்கை மீட்டமைக்க",
        resetKhataPromptTitle: "கணக்கை மீட்டமைக்கவா?",
        resetKhataPromptMessage: "இன்றைய கணக்கு பூஜ்ஜியமாக (₹0) மாறும். இதை மாற்ற முடியாது.",
        shareWhatsAppButton: "பகிர்",
        weeklyOverviewTitle: "வாராந்திர கண்ணோட்டம் (7 நாட்கள்)",
        batteryGuideTitle: "பேட்டரி & தூர வழிகாட்டி",
        sagFilterBadge: "⚡ வோல்டேஜ் சாக் வடிகட்டி இயங்குகிறது",
        estRangeLabel: "மதிப்பிடப்பட்ட மீதமுள்ள தூரம் (Km)",
        batteryHealthy: "✅ பேட்டரி நல்ல நிலையில் உள்ளது",
        batteryLowWarning: "⚠️ பேட்டரி குறைவு! சார்ஜிங் நிலையத்திற்கு செல்லவும்.",
        selectLanguageTitle: "மொழியைத் தேர்ந்தெடுக்கவும்",
        confirmDelete: "நீக்குதலை உறுதிசெய்",
        cancelAction: "ரத்து செய் (Cancel)",
        deleteAction: "நீக்கு (Delete)"
    )

    private static let teluguStrings = UiStrings(
        appTitle: "విద్యుత్‌గతి (రిక్షా సారథి)",
        tabSeats: "ప్రయాణికులు",
        tabSoundbox: "సౌండ్‌బాక్స్",
        tabKhata: "ఖాతా",
        tabBattery: "బ్యాటరీ",
        activeRoute: "ప్రస్తుత రూట్ (Active Route)",
        seatsFullBadge: "⚡ బండి ఫుల్ (FULL)",
        oneSeatLeftBadge: "⚠️ కేవలం 1 సీటు మిగిలింది!",
        seatsRemainingBadge: { "\($0) సీట్లు ఖాళీగా ఉన్నాయి" },
        passengersOnboard: "ప్రయాణికులు ఉన్నారు",
        boardPassenger: "+ ప్రయాణికుడు ఎక్కాడు",
        deboardPassenger: "దిగాడు",
        rushHourQuickFill: "⚡ రద్దీ సమయం: 1-ట్యాప్ బండి ఫుల్",
        farePerSeat: "ఒక్కో సీటు ఛార్జీ:",
        tripCompleteTitle: { "ట్రిప్ పూర్తి & ఛార్జీ వసూలు (₹\($0))" },
        cashPayment: "💵 నగదు (Cash)",
        upiPayment: "📱 యూపీఐ (UPI)",
        recentTrips: "ఇటీవలి ట్రిప్పులు",
        noTripsYet: "నేడు ఇంకా ఏ ట్రిప్ నమోదు కాలేదు.",
        deleteTripPromptTitle: "ట్రిప్ తొలగించాలా?",
        deleteTripPromptMessage: { route, fare, count in "\(route) (₹\(fare), \(count) ప్రయాణికులు) ట్రిప్ తొలగించాలనుకుంటున్నారా?" },
        soundboxTitle: "ఉచిత డిజిటల్ సౌండ్‌బాక్స్",
        soundboxSubtitle: "పరికర అద్దె లేకుండా 100% ఉచితం",
        tapToAnnounce: "చెల్లింపు వాయిస్ వినండి:",
        repeatAnnouncement: "మళ్లీ వినండి (Repeat)",
        autoSoundboxTitle: "ఆటోమేటిక్ సౌండ్‌బాక్స్ (Auto UPI)",
        autoSoundboxSubtitle: "Paytm, PhonePe లేదా GPay ద్వారా డబ్బులు రాగానే యాప్ స్వయంగా చెబుతుంది.",
        enableAutoSoundbox: "ఆటో వాయిస్ ఆన్ చేయండి",
        driverUpiTitle: "డ్రైవర్ UPI ID",
        recentPayments: "ఇటీవలి చెల్లింపులు",
        noPaymentsYet: "ఇంకా కొత్త చెల్లింపు రాలేదు.",
        deletePaymentPromptTitle: "చెల్లింపు రికార్డ్ తొలగించాలా?",
        deletePaymentPromptMessage: { app, fare in "\(app) నుండి వచ్చిన ₹\(fare) రికార్డ్ తొలగించాలనుకుంటున్నారా?" },
        dailyKhataTitle: "రోజువారీ లెక్క (Daily Khata)",
        netProfitTitle: "ఈరోజు నికర లాభం (Net Profit)",
        profitBadge: "✅ లాభసాటి షిఫ్ట్",
        lossBadge: "⚠️ ఖర్చులు ఇంకా భర్తీ కాలేదు",
        passengersLabel: "ప్రయాణికులు",
        tripsLabel: "రౌండ్లు (Trips)",
        grossEarningsLabel: "మొత్తం ఛార్జీల ఆదాయం",
        thekedarRentLabel: "వాహన యజమాని అద్దె (భత్యం)",
        chargingExpenseLabel: "బ్యాటరీ ఛార్జింగ్ / స్వాప్ ఖర్చు",
        otherExpensesLabel: "ఇతర ఖర్చులు (టీ, పంచర్)",
        resetKhataButton: "ఈరోజు ఖాతా రీసెట్ చేయండి",
        resetKhataPromptTitle: "ఈరోజు ఖాతా రీసెట్ చేయాలా?",
        resetKhataPromptMessage: "ఈరోజు మొత్తం లెక్క శూన్యం (₹0) అవుతుంది.",
        shareWhatsAppButton: "షేర్ చేయండి",
        weeklyOverviewTitle: "వారపు నివేదిక (7 రోజులు)",
        batteryGuideTitle: "బ్యాటరీ & రేంజ్ గైడ్",
        sagFilterBadge: "⚡ వోల్టేజ్ సాగ్ ఫిల్టర్ పనిచేస్తోంది",
        estRangeLabel: "అంచనా మిగిలిన దూరం (Km)",
        batteryHealthy: "✅ బ్యాటరీ సాధారణ స్థితిలో ఉంది",
        batteryLowWarning: "⚠️ బ్యాటరీ తక్కువగా ఉంది! ఛార్జింగ్ కేంద్రానికి వెళ్లండి.",
        selectLanguageTitle: "భాషను ఎంచుకోండి",
        confirmDelete: "నిర్ధారించండి",
        cancelAction: "రద్దు చేయి (Cancel)",
        deleteAction: "తొలగించు (Delete)"
    )
}

public final class LocalizationManager: ObservableObject {
    public static let shared = LocalizationManager()

    private let languageKey = "selected_language_code"

    @Published public var currentLanguage: AppLanguage {
        didSet {
            UserDefaults.standard.set(currentLanguage.rawValue, forKey: languageKey)
            strings = LocalizedStringsCatalog.get(currentLanguage)
        }
    }

    @Published public var strings: UiStrings

    private init() {
        let savedCode = UserDefaults.standard.string(forKey: languageKey) ?? AppLanguage.hindi.rawValue
        let initialLang = AppLanguage(rawValue: savedCode) ?? .hindi
        self.currentLanguage = initialLang
        self.strings = LocalizedStringsCatalog.get(initialLang)
    }

    public func setLanguage(_ language: AppLanguage) {
        self.currentLanguage = language
    }
}
