package com.smarttoolfactory.home.viewbindings

import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

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

    val stateSet =
        intArrayOf(android.R.attr.state_checked * if (favorite) 1 else -1)
    setImageState(stateSet, true)

//    val animatedVectorDrawable = if (favorite) {
//        AppCompatResources.getDrawable(
//            context,
//            R.drawable.avd_heart_favorite
//        ) as? AnimatedVectorDrawable
//    } else {
//        AppCompatResources.getDrawable(
//            context,
//            R.drawable.avd_heart_empty
//        ) as? AnimatedVectorDrawable
//    }
//    setImageDrawable(animatedVectorDrawable)
}
