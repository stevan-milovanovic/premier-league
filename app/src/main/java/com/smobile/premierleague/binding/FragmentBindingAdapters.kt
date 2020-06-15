package com.smobile.premierleague.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }

    @BindingAdapter("drawableTopUrl")
    fun bindDrawableTop(textView: TextView, url: String?) {
        Glide.with(fragment).load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        resource,
                        null,
                        null
                    )
                }
            })
    }
}