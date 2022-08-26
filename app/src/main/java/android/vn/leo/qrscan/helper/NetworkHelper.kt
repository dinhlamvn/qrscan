package android.vn.leo.qrscan.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.vn.leo.qrscan.R
import android.vn.leo.qrscan.extensions.copyToClipboard
import android.widget.Toast

object NetworkHelper {

    @JvmStatic
    fun accessToWifiNetwork(
        context: Context,
        ssid: String,
        password: String,
        isHidden: Boolean = false,
        block: (Network) -> Unit = { }
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val wifiSpecifierBuilder = WifiNetworkSpecifier.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(password)
                .setIsHiddenSsid(isHidden)
            val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .setNetworkSpecifier(wifiSpecifierBuilder.build())
                .build()
            val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    block.invoke(network)
                }
            }
            connectivityManager.requestNetwork(request, networkCallback)
        } else {
            password.copyToClipboard(context)
            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
        }
    }
}