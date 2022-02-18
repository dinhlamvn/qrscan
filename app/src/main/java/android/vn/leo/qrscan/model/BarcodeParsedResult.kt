package android.vn.leo.qrscan.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.zxing.client.result.AddressBookParsedResult
import com.google.zxing.client.result.CalendarParsedResult
import com.google.zxing.client.result.ISBNParsedResult
import com.google.zxing.client.result.VINParsedResult
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
        val anonymousIdentity: String,
        val eapMethod: String,
        val identity: String,
        val networkEncryption: String,
        val phase2Method: String
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
        val addressBookParsedResult: AddressBookParsedResult
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class CalendarResult(
        override val text: String,
        override val bitmap: Bitmap,
        val calendarParsedResult: CalendarParsedResult
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
        val vinParsedResult: VINParsedResult
    ) : BarcodeParsedResult(text, bitmap)

    @Parcelize
    data class ISBNResult(
        override val text: String,
        override val bitmap: Bitmap,
        val isbnParsedResult: ISBNParsedResult
    ) : BarcodeParsedResult(text, bitmap)
}
