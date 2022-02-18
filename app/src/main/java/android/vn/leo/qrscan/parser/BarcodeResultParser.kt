package android.vn.leo.qrscan.parser

import android.graphics.Color
import android.vn.leo.qrscan.model.BarcodeParsedResult
import com.google.zxing.client.result.*
import com.journeyapps.barcodescanner.BarcodeResult

class BarcodeResultParser {

    fun parse(result: BarcodeResult?): BarcodeParsedResult? {
        val nonNullResult = result ?: return null
        val targetResult = nonNullResult.result
        val parsedResult = ResultParser.parseResult(targetResult)
        val text = result.text
        val bitmap = result.getBitmapWithResultPoints(Color.YELLOW)
        val textResult = BarcodeParsedResult.TextResult(text, bitmap)
        return when (parsedResult.type) {
            ParsedResultType.TEXT -> textResult
            ParsedResultType.URI -> {
                val uriResult = URIResultParser().parse(targetResult)
                    ?: URLTOResultParser().parse(targetResult)
                    ?: BookmarkDoCoMoResultParser().parse(targetResult)
                    ?: return textResult
                uriResult.run {
                    BarcodeParsedResult.UrlResult(text, bitmap, title, uri)
                }
            }
            ParsedResultType.TEL -> {
                val telResult = TelResultParser().parse(targetResult) ?: return textResult
                telResult.run {
                    BarcodeParsedResult.TelResult(text, bitmap, title, number, telURI)
                }
            }
            ParsedResultType.SMS -> {
                val smsResult = SMSMMSResultParser().parse(targetResult)
                    ?: SMSTOMMSTOResultParser().parse(targetResult)
                    ?: return textResult
                smsResult.run {
                    BarcodeParsedResult.SmsResult(
                        text, bitmap,
                        subject,
                        body,
                        numbers.toList(),
                        smsuri
                    )
                }
            }
            ParsedResultType.EMAIL_ADDRESS -> {
                val emailResult = EmailAddressResultParser().parse(targetResult)
                    ?: EmailDoCoMoResultParser().parse(targetResult)
                    ?: SMTPResultParser().parse(targetResult)
                    ?: return textResult
                emailResult.run {
                    BarcodeParsedResult.EmailResult(
                        text, bitmap,
                        subject,
                        body,
                        tos.toList(),
                        cCs.toList(),
                        bcCs.toList()
                    )
                }
            }
            ParsedResultType.WIFI -> {
                val wifiResult = WifiResultParser().parse(targetResult) ?: return textResult
                wifiResult.run {
                    BarcodeParsedResult.WifiResult(
                        text, bitmap,
                        ssid,
                        password,
                        isHidden,
                        anonymousIdentity,
                        eapMethod,
                        identity,
                        networkEncryption,
                        phase2Method
                    )
                }
            }
            ParsedResultType.PRODUCT -> {
                val productResult = ProductResultParser().parse(targetResult)
                    ?: return textResult
                productResult.run {
                    BarcodeParsedResult.ProductResult(text, bitmap, normalizedProductID, productID)
                }
            }
            ParsedResultType.ADDRESSBOOK -> {
                val addressBookResult = AddressBookAUResultParser().parse(targetResult)
                    ?: AddressBookDoCoMoResultParser().parse(targetResult)
                    ?: VCardResultParser().parse(targetResult)
                    ?: BizcardResultParser().parse(targetResult)
                    ?: return textResult
                addressBookResult.run {
                    BarcodeParsedResult.ContactResult(
                        text,
                        bitmap,
                        names.toList(),
                        nicknames.toList(),
                        pronunciation,
                        phoneNumbers.toList(),
                        phoneTypes.toList(),
                        emails.toList(),
                        emailTypes.toList(),
                        instantMessenger,
                        note,
                        addresses.toList(),
                        addressTypes.toList(),
                        org,
                        birthday,
                        title,
                        urLs.toList(),
                        geo.toList()
                    )
                }
            }
            ParsedResultType.CALENDAR -> {
                val calendarResult = VEventResultParser().parse(targetResult) ?: return textResult
                calendarResult.run {
                    BarcodeParsedResult.CalendarResult(
                        text,
                        bitmap,
                        summary,
                        startTimestamp,
                        isStartAllDay,
                        endTimestamp,
                        isEndAllDay,
                        location,
                        organizer,
                        attendees.toList(),
                        description,
                        latitude,
                        longitude
                    )
                }
            }
            ParsedResultType.GEO -> {
                val geoResult = GeoResultParser().parse(targetResult) ?: return textResult
                geoResult.run {
                    BarcodeParsedResult.GeoResult(
                        text, bitmap,
                        altitude,
                        latitude,
                        longitude,
                        query,
                        geoURI
                    )
                }
            }
            ParsedResultType.VIN -> {
                val vinResult = VINResultParser().parse(targetResult) ?: return textResult
                vinResult.run {
                    BarcodeParsedResult.VINResult(
                        text,
                        bitmap,
                        vin,
                        worldManufacturerID,
                        vehicleDescriptorSection,
                        vehicleIdentifierSection,
                        countryCode,
                        vehicleAttributes,
                        modelYear,
                        plantCode,
                        sequentialNumber
                    )
                }
            }
            ParsedResultType.ISBN -> {
                val isbnResult = ISBNResultParser().parse(targetResult) ?: return textResult
                isbnResult.run { BarcodeParsedResult.ISBNResult(text, bitmap, isbn) }
            }
            else -> null
        }
    }
}