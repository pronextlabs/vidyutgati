package com.vidyutgati

import com.vidyutgati.core.soundbox.UpiNotificationParser
import com.vidyutgati.domain.model.PaymentApp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class UpiNotificationParserTest {

    @Test
    fun testPaytmReceivedPaymentExtraction() {
        val result = UpiNotificationParser.parse(
            packageName = "net.one97.paytm",
            title = "Payment Received",
            text = "Received Rs. 15 from Priya Sharma"
        )
        assertNotNull(result)
        assertEquals(15.0, result!!.amount, 0.001)
        assertEquals(PaymentApp.PAYTM, result.app)
    }

    @Test
    fun testPhonePeReceivedRupeeSymbolExtraction() {
        val result = UpiNotificationParser.parse(
            packageName = "com.phonepe.app",
            title = "Money Received",
            text = "You have received ₹20 from Rahul Kumar"
        )
        assertNotNull(result)
        assertEquals(20.0, result!!.amount, 0.001)
        assertEquals(PaymentApp.PHONEPE, result.app)
    }

    @Test
    fun testGooglePayPaisaExtraction() {
        val result = UpiNotificationParser.parse(
            packageName = "com.google.android.apps.nbu.paisa.user",
            title = "Google Pay",
            text = "Paid you ₹10.00 for Toto Ride"
        )
        assertNotNull(result)
        assertEquals(10.0, result!!.amount, 0.001)
        assertEquals(PaymentApp.GPAY, result.app)
    }

    @Test
    fun testDebitNotificationIgnored() {
        val result = UpiNotificationParser.parse(
            packageName = "com.phonepe.app",
            title = "Paid successfully",
            text = "You paid ₹50 to Petrol Pump"
        )
        assertNull(result)
    }

    @Test
    fun testMarketingOffersIgnored() {
        val result = UpiNotificationParser.parse(
            packageName = "net.one97.paytm",
            title = "Special Offer",
            text = "Get ₹100 cashback offer on your next electricity bill"
        )
        assertNull(result)
    }
}
