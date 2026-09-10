import Foundation
import AVFoundation

public final class IOSSoundboxEngine: NSObject, ObservableObject {
    private let synthesizer = AVSpeechSynthesizer()

    public override init() {
        super.init()
    }

    public func announcePayment(amount: Double, appSource: PaymentApp, explicitLanguage: AppLanguage? = nil) {
        let language = explicitLanguage ?? LocalizationManager.shared.currentLanguage
        let intAmount = Int(amount)
        let amountWords = IndianCurrencyFormatter.getSpokenAmountWords(intAmount, language: language)

        let text: String
        switch language {
        case .hindi:
            text = "\(appSource.hindiName) पर \(amountWords) रुपये प्राप्त हुए।"
        case .hinglish:
            text = "\(appSource.rawValue) par \(amountWords) rupaye receive hue."
        case .english:
            text = "Received \(amountWords) rupees on \(appSource.rawValue)."
        case .bengali:
            text = "\(appSource.rawValue)-এ \(amountWords) টাকা পাওয়া গেছে।"
        case .punjabi:
            text = "\(appSource.rawValue) ਤੇ \(amountWords) ਰੁਪਏ ਪ੍ਰਾਪਤ ਹੋਏ।"
        case .gujarati:
            text = "\(appSource.rawValue) પર \(amountWords) રૂપિયા મળ્યા."
        case .marathi:
            text = "\(appSource.rawValue) वर \(amountWords) रुपये मिळाले."
        case .tamil:
            text = "\(appSource.rawValue)-இல் \(amountWords) ரூபாய் பெறப்பட்டது."
        case .telugu:
            text = "\(appSource.rawValue)-లో \(amountWords) రూపాయలు వచ్చాయి."
        }

        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: language.ttsLocale) ?? AVSpeechSynthesisVoice(language: "en-IN")
        utterance.rate = 0.48 // Slightly deliberate for loud vehicle environments
        utterance.pitchMultiplier = 1.0
        utterance.volume = 1.0

        if synthesizer.isSpeaking {
            synthesizer.stopSpeaking(at: .immediate)
        }
        synthesizer.speak(utterance)
    }

    public func speakAlert(messageHindi: String, messageEnglish: String = "") {
        let currentLang = LocalizationManager.shared.currentLanguage
        let text = (currentLang == .english && !messageEnglish.isEmpty) ? messageEnglish : messageHindi
        let utterance = AVSpeechUtterance(string: text)
        utterance.voice = AVSpeechSynthesisVoice(language: currentLang.ttsLocale) ?? AVSpeechSynthesisVoice(language: "en-IN")
        utterance.rate = 0.48
        synthesizer.speak(utterance)
    }
}
