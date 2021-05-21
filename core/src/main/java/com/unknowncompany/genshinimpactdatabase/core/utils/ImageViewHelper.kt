package com.unknowncompany.genshinimpactdatabase.core.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.unknowncompany.genshinimpactdatabase.core.R

object ImageViewHelper {

    const val ICON_LOADING = "icon_loading"
    const val ICON_ERROR = "icon_error"

    fun setImageViewDrawableForPlaceholderOrError(
        iconType: String,
        context: Context,
        placeholder: ImageView,
    ) {
        val icLoading = ContextCompat.getDrawable(context, R.drawable.ic_loading)
        val icError = ContextCompat.getDrawable(context, R.drawable.ic_error)

        val drawable =
            when (iconType) {
                ICON_LOADING -> icLoading
                ICON_ERROR -> icError
                else -> return
            }

        placeholder.setImageDrawable(drawable)
    }

    fun setImageViewVisibilityOnResourceReady(
        isLoaded: Boolean,
        placeholder: ImageView,
        itemImage: ImageView,
    ) {
        if (isLoaded) {
            placeholder.visibility = View.GONE
            itemImage.visibility = View.VISIBLE
        } else {
            placeholder.visibility = View.VISIBLE
            itemImage.visibility = View.INVISIBLE
        }
    }

}