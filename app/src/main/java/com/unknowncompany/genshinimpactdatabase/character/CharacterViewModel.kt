package com.unknowncompany.genshinimpactdatabase.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.unknowncompany.genshinimpactdatabase.core.data.Resource
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import com.unknowncompany.genshinimpactdatabase.core.domain.usecase.GenshinImpactUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class CharacterViewModel(
    private val genshinImpactUseCase: GenshinImpactUseCase,
    application: Application,
) : AndroidViewModel(application) {

    val characterNames = genshinImpactUseCase.getCharacterNames().asLiveData()

    fun getAllCharacter(characterNames: List<String>): LiveData<Resource<List<Character>>> =
        genshinImpactUseCase.getAllCharacter(characterNames).asLiveData()

    @ObsoleteCoroutinesApi
    val nameQueryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @ObsoleteCoroutinesApi
    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResult = nameQueryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            genshinImpactUseCase.getCharacterByNameQuery(it)
        }
        .asLiveData()

}