package com.ghostreborn.testtv

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Scale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment: BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        prepareBackgroundManager()
        loadRows()
    }

    private fun loadRows() {
        val animeList = getAnimeList()
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        for(anime in animeList) {
            listRowAdapter.add(anime)
        }
        val header = HeaderItem(0, "Anime")
        rowsAdapter.add(ListRow(header, listRowAdapter))
        adapter = rowsAdapter
    }

    fun getAnimeList(): List<Anime> {
        // Replace this with your actual data loading logic
        return listOf(
            Anime("Anime Title 1", "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg")
        )
    }

    private fun setupUIElements() {
        title = "App Name"
        headersState = HEADERS_HIDDEN
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
                .data("https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg")
                .scale(Scale.FILL)
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