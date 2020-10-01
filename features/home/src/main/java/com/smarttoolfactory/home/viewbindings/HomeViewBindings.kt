package com.smarttoolfactory.home.viewbindings

import android.graphics.drawable.AnimatedVectorDrawable
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.smarttoolfactory.home.R

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

    val animatedVectorDrawable = if (favorite) {
        AppCompatResources.getDrawable(
            context,
            R.drawable.avd_heart_favorite
        ) as? AnimatedVectorDrawable
    } else {
        AppCompatResources.getDrawable(
            context,
            R.drawable.avd_heart_empty
        ) as? AnimatedVectorDrawable
    }
    setImageDrawable(animatedVectorDrawable)
}
