package com.unknowncompany.genshinimpactdatabase.favoritecharacter.di

import com.unknowncompany.genshinimpactdatabase.favoritecharacter.FavoriteCharacterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteCharacterModule = module {
    viewModel { FavoriteCharacterViewModel(get()) }
}