package com.unknowncompany.genshinimpactdatabase.favoritecharacter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unknowncompany.genshinimpactdatabase.favoritecharacter.databinding.ActivityFavoriteCharacterBinding
import com.unknowncompany.genshinimpactdatabase.favoritecharacter.di.favoriteCharacterModule
import org.koin.core.context.loadKoinModules

class FavoriteCharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadKoinModules(favoriteCharacterModule)

        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, FavoriteCharacterFragment())
                .commit()
            supportActionBar?.title = getString(R.string.favorite_characters)
        }
    }
}