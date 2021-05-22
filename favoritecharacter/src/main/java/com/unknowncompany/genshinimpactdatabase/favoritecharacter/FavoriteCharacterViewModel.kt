package com.unknowncompany.genshinimpactdatabase.favoritecharacter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.unknowncompany.genshinimpactdatabase.core.domain.usecase.GenshinImpactUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class FavoriteCharacterViewModel(genshinImpactUseCase: GenshinImpactUseCase) : ViewModel() {

    val favoriteCharacter = viewModelScope.async(Dispatchers.Default) {
        genshinImpactUseCase.getFavoriteCharacter().asLiveData()
    }

}