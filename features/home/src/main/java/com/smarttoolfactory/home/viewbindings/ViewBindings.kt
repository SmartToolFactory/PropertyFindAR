package com.smarttoolfactory.home.viewbindings

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.home.R

/*
    *** Bindings for RecyclerView ***
 */

/**
 * [BindingAdapter]s for the [PropertyItem]s to ListAdapter.
 */
@BindingAdapter("app:items")
fun RecyclerView.setItems(items: List<PropertyItem>?) {

    items?.let {
        (adapter as ListAdapter<PropertyItem, *>)?.submitList(items)
    }
}

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

/**
 * Display or hide a view based on a condition
 *
 * @param condition if it's true this View's visibility is set to [View.VISIBLE]
 */
@BindingAdapter("visibilityBasedOn")
fun View.visibilityBasedOn(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

@BindingAdapter("favoriteImageSrc")
fun ImageButton.setFavoriteImageSrc(favorite: Boolean) {

    val imageResource = if (favorite) R.drawable.ic_baseline_favorite_30
    else R.drawable.ic_baseline_favorite_border_30

    setImageResource(imageResource)
}
