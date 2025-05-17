package com.ghostreborn.testtv

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment: BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        prepareBackgroundManager()
    }

    private fun setupUIElements() {
        title = "App Name"
        headersState = HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireActivity(), R.color.brand_color)
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_affordance)
    }

    private fun prepareBackgroundManager() {
        val mManager = BackgroundManager.getInstance(activity)
        mManager.attach(requireActivity().window)
        val defaultBackground = ContextCompat.getDrawable(requireActivity(), R.drawable.default_background)
        mManager.drawable = defaultBackground
        CoroutineScope(Dispatchers.IO).launch {
            val request = ImageRequest.Builder(requireActivity())
                .data("https://wp.youtube-anime.com/s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx21-YCDoj1EkAxFn.jpg?w=250")
                .build()

            val result = activity?.imageLoader?.execute(request)
            val drawable = result?.drawable
            withContext(Dispatchers.Main) {
                if (drawable != null) {
                    val layers = arrayOf(drawable, defaultBackground)
                    val layersDrawable = LayerDrawable(layers)
                    mManager.drawable = layersDrawable
                }
            }
        }
    }

}