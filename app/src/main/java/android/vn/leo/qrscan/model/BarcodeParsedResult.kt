package android.vn.leo.qrscan.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class BarcodeParsedResult(open val text: String, open val bitmap: Bitmap) : Parcelable {

    @Parcelize
    data class TextResult(override val text: String, override val bitmap: Bitmap) :
        BarcodeParsedResult(text, bitmap), Parcelable

    @Parcelize
    data class UrlResult(
        override val text: String,
        override val bitmap: Bitmap,
        val title: String?,
        val url: String
    ) : BarcodeParsedResult(text, bitmap), Parcelable

    @Parcelize
    data class TelResult(
        override val text: String,
        override val bitmap: Bitmap,
        val title: String,
        val phoneNumber: String,
        val uri: String
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class SmsResult(
        override val text: String,
        override val bitmap: Bitmap,
        val subject: String,
        val body: String,
        val receivers: List<String>,
        val smsUri: String
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class EmailResult(
        override val text: String,
        override val bitmap: Bitmap,
        val subject: String,
        val body: String,
        val receivers: List<String>,
        val ccs: List<String>,
        val bcCs: List<String>
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class WifiResult(
        override val text: String,
        override val bitmap: Bitmap,
        val ssid: String,
        val password: String,
        val isHidden: Boolean,
        val anonymousIdentity: String?,
        val eapMethod: String?,
        val identity: String?,
        val networkEncryption: String?,
        val phase2Method: String?
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class ProductResult(
        override val text: String,
        override val bitmap: Bitmap,
        val normalizedProductId: String,
        val productId: String
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class ContactResult(
        override val text: String,
        override val bitmap: Bitmap,
        val names: List<String?> = emptyList(),
        val nicknames: List<String?> = emptyList(),
        val pronunciation: String? = null,
        val phoneNumbers: List<String?> = emptyList(),
        val phoneTypes: List<String?> = emptyList(),
        val emails: List<String?> = emptyList(),
        val emailTypes: List<String?> = emptyList(),
        val instantMessenger: String? = null,
        val note: String? = null,
        val addresses: List<String?> = emptyList(),
        val addressTypes: List<String?> = emptyList(),
        val org: String? = null,
        val birthday: String? = null,
        val title: String? = null,
        val urls: List<String?> = emptyList(),
        val geo: List<String?> = emptyList(),
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class CalendarResult(
        override val text: String,
        override val bitmap: Bitmap,
        private val summary: String? = null,
        val start: Long = 0,
        val startAllDay: Boolean = false,
        val end: Long = 0,
        val endAllDay: Boolean = false,
        val location: String? = null,
        val organizer: String? = null,
        val attendees: List<String> = emptyList(),
        val description: String? = null,
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class GeoResult(
        override val text: String,
        override val bitmap: Bitmap,
        val altitude: Double,
        val latitude: Double,
        val longitude: Double,
        val query: String,
        val uri: String
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class VINResult(
        override val text: String,
        override val bitmap: Bitmap,
        private val vin: String? = null,
        val worldManufacturerID: String? = null,
        val vehicleDescriptorSection: String? = null,
        val vehicleIdentifierSection: String? = null,
        val countryCode: String? = null,
        val vehicleAttributes: String? = null,
        val modelYear: Int = 0,
        val plantCode: Char = 0.toChar(),
        val sequentialNumber: String? = null
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class ISBNResult(
        override val text: String,
        override val bitmap: Bitmap,
        val isbn: String? = null
    ) : BarcodeParsedResult(text, bitmap)
}
