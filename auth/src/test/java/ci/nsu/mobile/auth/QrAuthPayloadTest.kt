package ci.nsu.mobile.auth

import ci.nsu.mobile.auth.qr.QrAuthPayload
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QrAuthPayloadTest {
    @Test
    fun payloadRoundTripUsesJsonFormat() {
        val payload = QrAuthPayload(login = "student", password = "secret")

        val parsed = QrAuthPayload.fromRaw(payload.toJson())

        assertEquals(payload, parsed)
    }

    @Test
    fun separatedStringFormatIsSupported() {
        val parsed = QrAuthPayload.fromRaw("student:secret")

        assertEquals(QrAuthPayload("student", "secret"), parsed)
    }

    @Test
    fun invalidPayloadReturnsNull() {
        assertNull(QrAuthPayload.fromRaw(""))
        assertNull(QrAuthPayload.fromRaw("""{"login":"","password":"secret"}"""))
        assertNull(QrAuthPayload.fromRaw("student"))
    }
}
