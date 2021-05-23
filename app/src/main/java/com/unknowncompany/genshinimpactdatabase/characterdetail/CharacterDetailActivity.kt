package com.unknowncompany.genshinimpactdatabase.characterdetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.unknowncompany.genshinimpactdatabase.R
import com.unknowncompany.genshinimpactdatabase.core.ui.CharacterModel
import com.unknowncompany.genshinimpactdatabase.core.utils.ImageViewHelper
import com.unknowncompany.genshinimpactdatabase.databinding.ActivityCharacterDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class CharacterDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val viewModel: CharacterDetailViewModel by viewModel()
    private lateinit var binding: ActivityCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val characterData = intent.getParcelableExtra<CharacterModel>(EXTRA_DATA)
        bindCharacterData(characterData)
    }

    private fun bindCharacterData(characterData: CharacterModel?) {
        characterData?.let {
            supportActionBar?.title = ""
            loadImage(it.image)

            var favoriteState = characterData.isFavorite
            setStatusFavorite(favoriteState)
            binding.fab.setOnClickListener {
                favoriteState = !favoriteState
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.updateFavoriteCharacterByCharacterId(
                        characterData.characterId,
                        characterData.isFavorite)
                    setStatusFavorite(favoriteState)
                }
            }

            val contentBinding = binding.content
            with(contentBinding) {
                tvName.text = characterData.name
                tvVisionWeapon.text = characterData.visionWeapon
                tvNationValue.text = characterData.nation
                tvAffiliationValue.text = characterData.affiliation
                tvConstellationValue.text = characterData.constellation
                tvBirthdayValue.text = characterData.birthday
                tvRarityValue.text = characterData.rarity
                tvDescription.text = characterData.description
            }
        }
    }

    private fun loadImage(image: String) {
        with(ImageViewHelper) {
            setImageViewDrawableForPlaceholderOrError(
                ICON_LOADING, this@CharacterDetailActivity, binding.ivDetailImagePlaceholder)
            setImageViewVisibilityOnResourceReady(
                false, binding.ivDetailImagePlaceholder, binding.ivDetailImage
            )
        }

        Glide.with(this)
            .load(image)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    with(ImageViewHelper) {
                        setImageViewDrawableForPlaceholderOrError(
                            ICON_ERROR,
                            this@CharacterDetailActivity,
                            binding.ivDetailImagePlaceholder)
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
                            true,
                            binding.ivDetailImagePlaceholder,
                            binding.ivDetailImage
                        )
                    }
                    return false
                }
            })
            .into(binding.ivDetailImage)

    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_favorite_green))
        } else {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_favorite_border_green))
        }
    }

}