package igor.petrov.final_android_lvl1.presentation.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.DBUpdateState
import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionForRecyclerView
import igor.petrov.final_android_lvl1.databinding.DialogError401WrongTokenBinding
import igor.petrov.final_android_lvl1.databinding.DialogError402LimitReachedBinding
import igor.petrov.final_android_lvl1.databinding.DialogError404Binding
import igor.petrov.final_android_lvl1.databinding.DialogInputTextBinding
import igor.petrov.final_android_lvl1.databinding.FragmentUserBinding
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import igor.petrov.final_android_lvl1.presentation.adapters.CollectionAdapter
import igor.petrov.final_android_lvl1.presentation.adapters.InterestedAdapter
import kotlinx.coroutines.launch

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private val collectionAdapter = CollectionAdapter({ collection: CollectionsDB -> onCollectionClicked(collection) }, { collection: CollectionsDB -> onDeleteCollectionClicked(collection) })
    private val interestedAdapter = InterestedAdapter { item: ItemCollectionForRecyclerView -> onItemClicked(item) }
    private val viewedAdapter = InterestedAdapter { item: ItemCollectionForRecyclerView -> onItemClicked(item) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // ------------------------------------------------------------------------------------------collections-------------------------------------------------------------------------------------

        binding.recyclerViewCollections.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerViewCollections.adapter = collectionAdapter
        userViewModel.getCollections()
        userViewModel.getInterested(false)
        userViewModel.getViewed(false)

        binding.btnCreateCollection.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.text_create_collection))
                .setMessage(resources.getString(R.string.text_create_collection_input_name))
                .setView(DialogInputTextBinding.inflate(layoutInflater, null, false).root)
                .setNegativeButton(resources.getString(R.string.text_cancel)) { dialog, which -> }
                .setPositiveButton(resources.getString(R.string.text_create_collection_accept)) { dialog, which ->
                    userViewModel.newCollection((dialog as AppCompatDialog).findViewById<EditText>(R.id.editTextCollectionName)?.text.toString(), collectionAdapter)
                }.show()
        }

        lifecycleScope.launch {
            userViewModel.collectionList.collect {
                collectionAdapter.setData(it.toMutableList())
            }
        }

        lifecycleScope.launch {
            userViewModel.dBUpdateState.collect {
                binding.progressBarDBUpdaiting.visibility = if (it == DBUpdateState.Updating) View.VISIBLE else View.GONE
            }
        }

        // ------------------------------------------------------------------------------------------interested-------------------------------------------------------------------------------------

        binding.recyclerViewInterested.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewInterested.adapter = interestedAdapter

        lifecycleScope.launch {
            userViewModel.interestedList.collect {
                binding.btnShowAllInterested.text = "${userViewModel.interestedListSize}  >"
                interestedAdapter.setData(it.toMutableList())
            }
        }

        binding.btnShowAllInterested.setOnClickListener {
            if (interestedAdapter.itemCount == 1) {
                Toast.makeText(requireContext(), requireContext().getText(R.string.text_no_films_in_collection), Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle().apply {
                    putString("showType", "roomCollection")
                    putString("collection", "interested")
                    putInt("collectionId", 4)
                    putString("label", "All interested")
                }
                findNavController().navigate(R.id.action_global_fragmentShowAll, bundle)
            }
        }

        // ------------------------------------------------------------------------------------------viewed-------------------------------------------------------------------------------------

        binding.recyclerViewFilmViewed.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFilmViewed.adapter = viewedAdapter

        lifecycleScope.launch {
            userViewModel.viewedList.collect {
                binding.btnShowAllViewed.text = "${userViewModel.viewedListSize}  >"
                viewedAdapter.setData(it.toMutableList())
            }
        }

        binding.btnShowAllViewed.setOnClickListener {
            if (viewedAdapter.itemCount == 1) {
                Toast.makeText(requireContext(), requireContext().getText(R.string.text_no_films_in_collection), Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle().apply {
                    putString("showType", "roomCollection")
                    putString("collection", "viewed")
                    putInt("collectionId", 3)
                    putString("label", "All viewed")
                }
                findNavController().navigate(R.id.action_global_fragmentShowAll, bundle)
            }
        }
    }


    private fun onDeleteCollectionClicked(collection: CollectionsDB) {
        userViewModel.deleteCollection(collection)
        collectionAdapter.deleteData(collection)
    }

    private fun onCollectionClicked(collection: CollectionsDB) {
        if (collection.collectionSize == 0) {
            Toast.makeText(requireContext(), requireContext().getString(R.string.text_no_films_in_collection), Toast.LENGTH_SHORT).show()
        } else {
            val bundle = Bundle().apply {
                putString("showType", "roomCollection")
                putString("collection", collection.collectionName)
                putInt("collectionId", collection.collectionId)
                putString("label", "Collection ${collection.collectionName}")
            }
            findNavController().navigate(R.id.action_global_fragmentShowAll, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClicked(item: ItemCollectionForRecyclerView) {
        if (item.itemName == requireContext().getString(R.string.text_clear_history)) {
            userViewModel.clearCollection(item.collectionId)
        }else if (item.itemName == requireContext().getString(R.string.text_no_films_in_collection)){
            Toast.makeText(requireContext(), "Collection is empty.", Toast.LENGTH_SHORT).show()
        } else {
            when (item.itemType) {
                ItemCollectionType.FILM -> {
                    val bundle = Bundle().apply {
                        putInt("kinopoiskId", item.itemId)
                        putString("label", item.itemName)
                    }
                    findNavController().navigate(R.id.action_global_fragmentFilm, bundle)
                }

                ItemCollectionType.PERSON -> {
                    val bundle = Bundle().apply {
                        putInt("personId", item.itemId)
                        putString("label", item.itemName)
                    }
                    findNavController().navigate(R.id.action_global_fragmentPerson, bundle)
                }
            }
        }
    }

    private fun handleError(error: Throwable) {
        if (error is KinopoiskException) {
            val materialDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.text_error))
                .setCancelable(false)

            when ((error as KinopoiskException).code) {
                401 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_401))
                    materialDialog.setView(DialogError401WrongTokenBinding.inflate(layoutInflater, null, false).root) //401 view
                }
                402 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_402))
                    materialDialog.setView(DialogError402LimitReachedBinding.inflate(layoutInflater, null, false).root)
                }
                404 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_404))
                    //.setView(DialogInputTextBinding.inflate(layoutInflater, null, false).root) //404 view
                }
                429 -> {
                    materialDialog.setMessage(resources.getString(R.string.text_error_429))
                    //.setView(DialogInputTextBinding.inflate(layoutInflater, null, false).root) //404 view
                }
                else -> {
                    materialDialog.setMessage("${resources.getString(R.string.text_error_unknown)} \ncode: ${error.code} \n\nmessage: ${error.message}")
                    materialDialog.setView(DialogError404Binding.inflate(layoutInflater, null, false).root)
                }
            }
            materialDialog.setNegativeButton(resources.getString(R.string.text_exit)) { dialog, which ->
                activity?.finish()
            }
                .setPositiveButton(resources.getString(R.string.text_retry)) { dialog, which ->
                    userViewModel.getCollections()
                    userViewModel.getInterested(false)
                    userViewModel.getViewed(false)
                }.show()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.text_error))
                .setCancelable(false)
                .setMessage(error.message)
                .setView(DialogError404Binding.inflate(layoutInflater, null, false).root)
                .setNegativeButton(resources.getString(R.string.text_exit)) { dialog, which ->
                    activity?.finish()
                }
                .setPositiveButton(resources.getString(R.string.text_retry)) { dialog, which ->
                    userViewModel.getCollections()
                    userViewModel.getInterested(false)
                    userViewModel.getViewed(false)
                }.show()
        }
    }
}