package com.ghostreborn.testtv

import android.R.attr.height
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
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
import androidx.core.graphics.toColorInt

class MainFragment: BrowseSupportFragment() {

    private lateinit var mMetrics: DisplayMetrics
    private lateinit var mManager: BackgroundManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mManager = BackgroundManager.getInstance(activity)
        mMetrics = DisplayMetrics()
        setupUIElements()
        prepareBackgroundManager()
        updateBackground(
            Anime(
                "One Piece",
                "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"
            )
        )
        loadRows()
        view?.findViewById<ViewGroup>(androidx.leanback.R.id.browse_container_dock)?.apply {
            setPadding(0, 200, 0, 0)
        }
    }

    private fun loadRows() {
        val animeList = getAnimeList()
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        for (anime in animeList) {
            listRowAdapter.add(anime)
        }
        rowsAdapter.add(ListRow(HeaderItem(0, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(1, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(2, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(3, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(4, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(5, "Anime"), listRowAdapter))
        rowsAdapter.add(ListRow(HeaderItem(6, "Anime"), listRowAdapter))
        adapter = rowsAdapter
    }

    fun getAnimeList(): List<Anime> {
        return listOf(
            Anime(
                "Anime Title 1",
                "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"
            ),
            Anime(
                "Anime Title 2",
                "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"
            ),
            Anime(
                "Anime Title 3",
                "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"
            ),
            Anime(
                "Anime Title 4",
                "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"
            ),
            Anime(
                "Anime Title 5",
                "https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg"
            )
        )
    }

    private fun setupUIElements() {
        headersState = HEADERS_HIDDEN
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireActivity(), R.color.brand_color)
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_affordance)
    }

    private fun updateBackground(anime: Anime) {
        val textDrawable = object : Drawable() {
            override fun draw(canvas: Canvas) {
                Log.e("TAG", bounds.width().toString())
                val paint = Paint()
                paint.color = Color.WHITE
                paint.style = Paint.Style.FILL
                canvas.drawRect(0f, 0f, bounds.width().toFloat(), bounds.height().toFloat(), paint)
            }

            override fun setAlpha(alpha: Int) {}

            override fun setColorFilter(colorFilter: ColorFilter?) {}

            override fun getOpacity(): Int = android.graphics.PixelFormat.TRANSLUCENT

        }
        mManager.attach(requireActivity().window)
        mManager.drawable = textDrawable
    }

    private fun prepareBackgroundManager() {
        mMetrics = DisplayMetrics()
//        mManager.attach(requireActivity().window)
        val defaultBackground =
            ContextCompat.getDrawable(requireActivity(), R.drawable.default_background)
        mManager.drawable = defaultBackground
        CoroutineScope(Dispatchers.IO).launch {
            val request = ImageRequest.Builder(requireActivity())
                .data("https://s4.anilist.co/file/anilistcdn/media/anime/banner/21-wf37VakJmZqs.jpg")
                .scale(Scale.FILL)
                .build()

            val result = activity?.imageLoader?.execute(request)
            withContext(Dispatchers.Main) {
                if (result?.drawable != null) {
                    val layers = arrayOf(result.drawable, defaultBackground)
                    val layersDrawable = LayerDrawable(layers)
                    mManager.drawable = layersDrawable
                }
            }
        }
    }
}