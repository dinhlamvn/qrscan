package android.vn.leo.qrscan.extensions

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun Bitmap.loadAsScanResult(context: Context, imageView: ImageView) = Glide.with(context)
    .load(this)
    .transform(RoundedCorners(20.toDp(context.resources.displayMetrics)))
    .skipMemoryCache(true)
    .into(imageView)

fun Bitmap.loadAsScanResult(fragment: Fragment, imageView: ImageView) = Glide.with(fragment)
    .load(this)
    .transform(RoundedCorners(20.toDp(fragment.resources.displayMetrics)))
    .skipMemoryCache(true)
    .into(imageView)