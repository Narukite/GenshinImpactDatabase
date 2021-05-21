package com.unknowncompany.genshinimpactdatabase.di

import com.unknowncompany.genshinimpactdatabase.MainActivityViewModel
import com.unknowncompany.genshinimpactdatabase.character.CharacterViewModel
import com.unknowncompany.genshinimpactdatabase.characterdetail.CharacterDetailViewModel
import com.unknowncompany.genshinimpactdatabase.core.domain.usecase.GenshinImpactInteractor
import com.unknowncompany.genshinimpactdatabase.core.domain.usecase.GenshinImpactUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<GenshinImpactUseCase> { GenshinImpactInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainActivityViewModel() }
    viewModel { CharacterViewModel(get(), get()) }
    viewModel { CharacterDetailViewModel(get()) }
}