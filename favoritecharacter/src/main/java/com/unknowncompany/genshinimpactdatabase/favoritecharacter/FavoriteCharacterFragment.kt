package com.unknowncompany.genshinimpactdatabase.favoritecharacter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.unknowncompany.genshinimpactdatabase.characterdetail.CharacterDetailActivity
import com.unknowncompany.genshinimpactdatabase.core.ui.CharacterAdapter
import com.unknowncompany.genshinimpactdatabase.core.utils.DataMapper
import com.unknowncompany.genshinimpactdatabase.favoritecharacter.databinding.FragmentFavoriteCharacterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteCharacterFragment : Fragment() {

    private val viewModel: FavoriteCharacterViewModel by viewModel()
    private lateinit var characterAdapter: CharacterAdapter

    private var _binding: FragmentFavoriteCharacterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            characterAdapter = CharacterAdapter()
            characterAdapter.onItemClick = { selectedData ->
                val intent = Intent(activity, CharacterDetailActivity::class.java)
                intent.putExtra(CharacterDetailActivity.EXTRA_DATA, selectedData)
                startActivity(intent)
            }

            CoroutineScope(Dispatchers.Main).launch {
                viewModel.favoriteCharacter.await().observe(viewLifecycleOwner, { data ->
                    characterAdapter.setData(DataMapper
                        .mapDomainModelsToPresentationModels(data,
                            requireActivity().resources))
                    binding.viewEmpty.root.visibility =
                        if (data.isNotEmpty()) View.GONE else View.VISIBLE
                })
            }

            with(binding.rvFavoriteCharacter) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = characterAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}