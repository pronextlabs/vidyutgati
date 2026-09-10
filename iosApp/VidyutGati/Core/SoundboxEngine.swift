import Foundation
import AVFoundation

public final class IOSSoundboxEngine: NSObject, ObservableObject {
    private let synthesizer = AVSpeechSynthesizer()

    public override init() {
        super.init()
    }

    public func convertAmountToHindiWords(_ amount: Int) -> String {
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
        case 250: return "ढाई सौ"
        case 300: return "तीन सौ"
        case 500: return "पांच सौ"
        default: return "\(amount)"
        }
    }

    public func announcePayment(amount: Double, appSource: PaymentApp) {
        let intAmount = Int(amount)
        let hindiAmount = convertAmountToHindiWords(intAmount)
        let text = "\(appSource.hindiName) पर \(hindiAmount) रुपये प्राप्त हुए।"

        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: "hi-IN") ?? AVSpeechSynthesisVoice(language: "en-IN")
        utterance.rate = 0.48 // Slightly deliberate for loud vehicle environments
        utterance.pitchMultiplier = 1.0
        utterance.volume = 1.0

        if synthesizer.isSpeaking {
            synthesizer.stopSpeaking(at: .immediate)
        }
        synthesizer.speak(utterance)
    }

    public func speakAlert(messageHindi: String) {
        let utterance = AVSpeechUtterance(string: messageHindi)
        utterance.voice = AVSpeechSynthesisVoice(language: "hi-IN") ?? AVSpeechSynthesisVoice(language: "en-IN")
        utterance.rate = 0.48
        synthesizer.speak(utterance)
    }
}
