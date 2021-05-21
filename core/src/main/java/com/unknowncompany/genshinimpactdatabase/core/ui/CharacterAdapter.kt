package com.unknowncompany.genshinimpactdatabase.core.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.unknowncompany.genshinimpactdatabase.core.R
import com.unknowncompany.genshinimpactdatabase.core.databinding.ItemListCharacterBinding
import com.unknowncompany.genshinimpactdatabase.core.utils.ImageViewHelper

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    companion object {
        const val ITEM_NAME_EXCEPTION = "Traveler"
    }

    private var listData = ArrayList<CharacterModel>()
    var onItemClick: ((CharacterModel) -> Unit)? = null

    fun setData(newListData: List<CharacterModel>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CharacterAdapter.CharacterViewHolder =
        CharacterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_character, parent, false))

    override fun onBindViewHolder(holder: CharacterAdapter.CharacterViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListCharacterBinding.bind(itemView)

        fun bind(data: CharacterModel) {
            with(binding) {
                with(ImageViewHelper) {
                    setImageViewDrawableForPlaceholderOrError(
                        ICON_LOADING, itemView.context, ivItemImagePlaceholder)
                    setImageViewVisibilityOnResourceReady(
                        false, ivItemImagePlaceholder, ivItemImage
                    )
                }

                Glide.with(itemView.context)
                    .load(data.image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            with(ImageViewHelper) {
                                setImageViewDrawableForPlaceholderOrError(
                                    ICON_ERROR, itemView.context, ivItemImagePlaceholder)
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            with(ImageViewHelper) {
                                setImageViewVisibilityOnResourceReady(
                                    true, ivItemImagePlaceholder, ivItemImage
                                )
                            }
                            return false
                        }

                    })
                    .into(ivItemImage)

                tvItemName.text = data.name
                tvItemConstellation.text = data.constellation
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listData[adapterPosition])
            }
        }
    }

}