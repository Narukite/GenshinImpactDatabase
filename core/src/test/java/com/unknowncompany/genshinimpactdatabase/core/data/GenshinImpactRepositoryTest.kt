package com.unknowncompany.genshinimpactdatabase.core.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.LocalDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.RemoteDataSource
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import com.unknowncompany.genshinimpactdatabase.core.utils.DataDummy
import com.unknowncompany.genshinimpactdatabase.core.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.time.ExperimentalTime

class GenshinImpactRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)

    private val repository = GenshinImpactRepository(remote, local)

    private val dummyCharacterEntities: List<CharacterEntity> =
        DataDummy.getListCharacterEntityDummy()
    private val dummyCharacters: List<Character> = DataDummy.getListCharacterDummy()

    @Test
    fun getCharacterNames() {
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun getAllCharacter() {
        runBlocking {
            val flow = flow {
                emit(ArrayList())
                delay(10)
                emit(dummyCharacterEntities)
            }

//            `when`(scopes.default().async()).thenReturn(flow)

            repository.getAllCharacter(ArrayList()).test {
                expectItem()
                coroutineScope.advanceTimeBy(10)
                assertEquals(dummyCharacterEntities.size, expectItem().data?.size)
            }
        }
    }

    @Test
    fun getCharacterByNameQuery() {
    }

    @Test
    fun getFavoriteCharacter() {
    }

    @Test
    fun updateFavoriteCharacterByCharacterId() {
    }
}