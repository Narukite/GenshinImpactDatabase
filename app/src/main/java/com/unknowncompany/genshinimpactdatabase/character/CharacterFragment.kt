package com.unknowncompany.genshinimpactdatabase.character

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    @ObsoleteCoroutinesApi
    @FlowPreview
    @ExperimentalCoroutinesApi
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
            viewModel.characterNames
                .observe(viewLifecycleOwner, getCharacterNamesObserver)

            viewModel.searchResult.observe(viewLifecycleOwner, { characters ->
                CoroutineScope(Dispatchers.Default).launch {
                    Log.d("Cek", "onViewCreated: searchResult onChanged")

                    val data = DataMapper.mapDomainModelsToPresentationModels(characters,
                        requireActivity().resources)
                    (Dispatchers.Main){
                        characterAdapter.setData(data)
                    }
                }
            })

            val parentViewModel: MainActivityViewModel by sharedViewModel()
            parentViewModel.nameQuery.observe(viewLifecycleOwner, { name ->
                lifecycleScope.launch {
                    viewModel.nameQueryChannel.send(name)
                }
            })
            val parentActivity = activity as MainActivity
            parentActivity.onViewAttachedToWindowCallback = {
                viewModel.getAllCharacter(ArrayList())
                    .removeObserver(getAllCharacterObserver)
            }
            parentActivity.onViewDetachedFromWindowCallback = {
                viewModel.getAllCharacter(ArrayList())
                    .observe(viewLifecycleOwner, getAllCharacterObserver)
                parentViewModel.nameQuery.value = ""
            }

        }
    }

    private val getCharacterNamesObserver =
        Observer<ApiResponse<List<String>>> { characterNames ->
            if (characterNames != null) {
                when (characterNames) {
                    is ApiResponse.Success -> {
                        viewModel.getAllCharacter(characterNames.data).observe(
                            viewLifecycleOwner,
                            getAllCharacterObserver)
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

                        viewModel.getAllCharacter(ArrayList())
                            .observe(viewLifecycleOwner, getAllCharacterObserver)
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

                        Log.d("Cek", "getAllCharacterObserver success")

                        binding.progressBar.visibility = View.GONE
                        characterAdapter.setData(DataMapper.mapDomainModelsToPresentationModels(
                            characters.data as List<Character>,
                            requireActivity().resources))
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text =
                            characters.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}