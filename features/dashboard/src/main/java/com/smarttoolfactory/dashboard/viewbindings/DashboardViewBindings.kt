package com.smarttoolfactory.dashboard.viewbindings

import android.graphics.Color
import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.smarttoolfactory.dashboard.R

/**
 * Binding adapter used with this class android:src used with binding of this object
 * loads image from url into specified view
 *
 * @param view image to be loaded into
 * @param path of the image to be fetched
 */
@BindingAdapter("imageSrc")
fun setImageUrl(view: ImageView, path: String?) {

    try {

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.placeholder)

        Glide
            .with(view.context)
            .setDefaultRequestOptions(requestOptions)
            .load(path)
            .into(view)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("favoriteImageSrc")
fun ImageButton.setFavoriteImageSrc(favorite: Boolean) {

    if (favorite) {
        setColorFilter(Color.rgb(244, 81, 30))
    } else {
        setColorFilter(Color.rgb(41, 182, 246))
    }

    val imageResource = if (favorite) R.drawable.ic_baseline_favorite_30
    else R.drawable.ic_baseline_favorite_border_30

    setImageResource(imageResource)
}
