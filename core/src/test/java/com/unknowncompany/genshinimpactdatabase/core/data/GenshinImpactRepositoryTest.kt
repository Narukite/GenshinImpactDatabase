package com.unknowncompany.genshinimpactdatabase.core.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.LocalDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.local.entity.CharacterEntity
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.RemoteDataSource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiResponse
import com.unknowncompany.genshinimpactdatabase.core.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

class GenshinImpactRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)

    private val repository = GenshinImpactRepository(remote, local)

    private val dummyString = "dummy"
    private val dummyListString = listOf(dummyString)
    private val dummyBoolean = true
    private val dummyCharacterEntities: List<CharacterEntity> =
        DataDummy.getListCharacterEntityDummy()

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test
    fun getCharacterNames() {
        runBlockingTest {
            val flow = flow {
                emit(ApiResponse.Success(dummyListString))
            }

            `when`(remote.getCharacterNames()).thenReturn(flow)

            repository.getCharacterNames().test {
                verify(remote).getCharacterNames()
                val response = expectItem() as ApiResponse.Success
                assertNotNull(response)
                assertEquals(dummyListString.size, response.data.size)
                expectComplete()
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test
    fun getAllCharacter() {
        runBlockingTest {
            val flow = flow {
                emit(dummyCharacterEntities)
            }

            `when`(local.getAllCharacter()).thenReturn(flow)

            repository.getAllCharacter(ArrayList()).test {
                verify(local, times(2)).getAllCharacter()
                expectItem()
                val resource = expectItem()
                assertNotNull(resource)
                assertEquals(dummyCharacterEntities.size, resource.data?.size)
                expectComplete()
            }
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test
    fun getCharacterByNameQuery() {
        runBlockingTest {
            `when`(local.getCharacterByNameQuery(dummyString)).thenReturn(dummyCharacterEntities)

            val result = repository.getCharacterByNameQuery(dummyString)
            verify(local).getCharacterByNameQuery(dummyString)
            assertNotNull(result)
            assertEquals(dummyCharacterEntities.size, result.size)
        }
    }

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test
    fun getFavoriteCharacter() {
        runBlockingTest {
            val flow = flow {
                emit(dummyCharacterEntities)
            }

            `when`(local.getFavoriteCharacter()).thenReturn(flow)

            repository.getFavoriteCharacter().test {
                verify(local).getFavoriteCharacter()
                val result = expectItem()
                assertNotNull(result)
                assertEquals(dummyCharacterEntities.size, result.size)
                expectComplete()
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun updateFavoriteCharacterByCharacterId() {
        runBlockingTest {
            repository.updateFavoriteCharacterByCharacterId(dummyString, dummyBoolean)

            verify(local).updateFavoriteCharacterByCharacterId(dummyString, dummyBoolean)
        }
    }
}