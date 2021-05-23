package com.unknowncompany.genshinimpactdatabase.character

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.unknowncompany.genshinimpactdatabase.MainActivity
import com.unknowncompany.genshinimpactdatabase.MainActivityViewModel
import com.unknowncompany.genshinimpactdatabase.R
import com.unknowncompany.genshinimpactdatabase.characterdetail.CharacterDetailActivity
import com.unknowncompany.genshinimpactdatabase.core.data.Resource
import com.unknowncompany.genshinimpactdatabase.core.data.source.remote.network.ApiResponse
import com.unknowncompany.genshinimpactdatabase.core.domain.model.Character
import com.unknowncompany.genshinimpactdatabase.core.ui.CharacterAdapter
import com.unknowncompany.genshinimpactdatabase.core.utils.DataMapper
import com.unknowncompany.genshinimpactdatabase.databinding.FragmentCharacterBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterFragment : Fragment() {

    private val parentViewModel: MainActivityViewModel by sharedViewModel()
    private val viewModel: CharacterViewModel by viewModel()
    private lateinit var characterAdapter: CharacterAdapter

    private var _binding: FragmentCharacterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @ObsoleteCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            characterAdapter = CharacterAdapter()
            characterAdapter.onItemClick = { selectedData ->
                val intent = Intent(activity, CharacterDetailActivity::class.java)
                intent.putExtra(CharacterDetailActivity.EXTRA_DATA, selectedData)
                startActivity(intent)
            }
            with(binding.rvCharacter) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = characterAdapter
            }

            binding.progressBar.visibility = View.VISIBLE
            viewModel.resources = requireActivity().resources
            observeCharacterNames()
            observeSearchResult()
            observeNameQuery()

            val parentActivity = activity as MainActivity
            parentActivity.onViewAttachedToWindowCallback = {
                removeAllCharacterObserver()
            }
            parentActivity.onViewDetachedFromWindowCallback = {
                observeAllCharacter()
                parentViewModel.nameQuery.value = ""
            }

        }
    }

    private fun observeCharacterNames() {
        lifecycleScope.launch {
            viewModel.getCharacterNames()
                .observe(viewLifecycleOwner, getCharacterNamesObserver)
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @ObsoleteCoroutinesApi
    private fun observeSearchResult() {
        viewModel.searchResult.observe(viewLifecycleOwner, { characters ->
            characterAdapter.setData(characters)
        })
    }

    @ObsoleteCoroutinesApi
    private fun observeNameQuery() {
        parentViewModel.nameQuery.observe(viewLifecycleOwner, { name ->
            lifecycleScope.launch {
                viewModel.nameQueryChannel.send(name)
            }
        })
    }

    private fun removeAllCharacterObserver() {
        lifecycleScope.launch {
            viewModel.getAllCharacter(ArrayList())
                .removeObserver(getAllCharacterObserver)
        }
    }

    private fun observeAllCharacter(list: List<String> = ArrayList()) {
        lifecycleScope.launchWhenCreated {
            viewModel.getAllCharacter(list)
                .observe(viewLifecycleOwner, getAllCharacterObserver)
        }
    }

    private val getCharacterNamesObserver =
        Observer<ApiResponse<List<String>>> { characterNames ->
            if (characterNames != null) {
                when (characterNames) {
                    is ApiResponse.Success -> {
                        observeAllCharacter(characterNames.data)
                    }
                    is ApiResponse.Empty -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text =
                            getString(R.string.data_on_the_server_is_empty)
                    }
                    is ApiResponse.Error -> {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.failed_fetching_the_data),
                            Toast.LENGTH_SHORT
                        ).show()

                        observeAllCharacter()
                    }
                }
            }
        }

    private val getAllCharacterObserver =
        Observer<Resource<List<Character>>> { characters ->
            if (characters != null) {
                when (characters) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if (characters.data.isNullOrEmpty()) {
                            binding.viewError.root.visibility = View.VISIBLE
                            binding.viewError.tvError.text =
                                characters.message ?: getString(R.string.no_internet)
                        } else {
                            lifecycleScope.launch {
                                val data = async(Dispatchers.Default) {
                                    DataMapper.mapDomainModelsToPresentationModels(
                                        characters.data,
                                        resources)
                                }
                                characterAdapter.setData(data.await())
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text =
                            characters.message ?: getString(R.string.no_internet)
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}