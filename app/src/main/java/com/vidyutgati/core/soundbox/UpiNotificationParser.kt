package com.vidyutgati.core.soundbox

import com.vidyutgati.domain.model.PaymentApp
import java.util.regex.Pattern

data class ParsedUpiPayment(
    val amount: Double,
    val app: PaymentApp,
    val senderName: String? = null
)

object UpiNotificationParser {

    private val AMOUNT_PATTERN = Pattern.compile("(?:₹|rs\\.?|inr)\\s*([0-9]+(?:\\.[0-9]{1,2})?)", Pattern.CASE_INSENSITIVE)

    private val POSITIVE_KEYWORDS = listOf(
        "received", "credited", "paid you", "sent you", "received payment",
        "prapt", "prapt hue", "jama hue", "has sent", "transferred"
    )

    private val NEGATIVE_KEYWORDS = listOf(
        "paid to", "sent to", "debited", "withdrawn", "recharge successful",
        "order placed", "bill paid", "cashback offer", "loan", "offer"
    )

    fun identifyApp(packageName: String): PaymentApp? {
        return when {
            packageName.contains("paytm", ignoreCase = true) -> PaymentApp.PAYTM
            packageName.contains("phonepe", ignoreCase = true) -> PaymentApp.PHONEPE
            packageName.contains("paisa", ignoreCase = true) || packageName.contains("google", ignoreCase = true) -> PaymentApp.GPAY
            packageName.contains("npci", ignoreCase = true) || packageName.contains("bhim", ignoreCase = true) -> PaymentApp.BHIM
            else -> null
        }
    }

    /**
     * Parses notification title and text to confirm an incoming payment and extract amount.
     */
    fun parse(packageName: String, title: String?, text: String?): ParsedUpiPayment? {
        val app = identifyApp(packageName) ?: return null
        val fullContent = "${title.orEmpty()} ${text.orEmpty()}".lowercase()

        // 1. Must NOT contain negative debit/marketing keywords
        if (NEGATIVE_KEYWORDS.any { fullContent.contains(it) }) {
            return null
        }

        // 2. Must contain at least one positive receiving keyword
        if (!POSITIVE_KEYWORDS.any { fullContent.contains(it) }) {
            return null
        }

        // 3. Extract amount
        val matcher = AMOUNT_PATTERN.matcher(fullContent)
        if (matcher.find()) {
            val amountStr = matcher.group(1) ?: return null
            val amount = amountStr.toDoubleOrNull() ?: return null
            if (amount > 0 && amount <= 5000) { // E-rickshaw micro-fares are typically <= ₹500
                return ParsedUpiPayment(amount = amount, app = app)
            }
        }

        return null
    }
}
