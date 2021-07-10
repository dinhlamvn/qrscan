package android.vn.leo.qrscan.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

fun String.copyToClipboard(context: Context): Boolean {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return false
    clipboardManager.setPrimaryClip(ClipData.newPlainText("copiedText", this))
    return true
}

fun String.share(context: Context, subject: String = "", choose: Boolean = false) {
    val resolvedSubject = if (subject.isNotEmpty()) {
        subject
    } else {
        context.applicationInfo.loadLabel(context.packageManager)
    }
    val intent = Intent(Intent.ACTION_SEND)
        .setType("text/plain")
        .putExtra(Intent.EXTRA_SUBJECT, resolvedSubject)
        .putExtra(Intent.EXTRA_TEXT, this)

    if (intent.resolveActivity(context.packageManager) != null) {
        if (choose) {
            context.startActivity(Intent.createChooser(intent, "Choose app"))
        } else {
            context.startActivity(intent)
        }
    }
}