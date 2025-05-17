package com.ghostreborn.testtv

import android.content.Context
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide

class CardPresenter: Presenter() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        mContext = parent.context
        val cardView = ImageCardView(mContext)
        cardView.cardType = ImageCardView.CARD_TYPE_MAIN_ONLY
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        item: Any?
    ) {
        val anime = item as Anime
        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = anime.title
        cardView.setMainImageDimensions(150, 200)
        Glide.with(mContext)
            .load(anime.thumbnail)
            .into(cardView.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        Glide.with(mContext)
            .clear(cardView.mainImageView)
        cardView.badgeImage = null
    }
}