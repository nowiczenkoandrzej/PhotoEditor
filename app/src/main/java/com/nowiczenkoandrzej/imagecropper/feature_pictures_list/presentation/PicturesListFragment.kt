package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import com.nowiczenkoandrzej.imagecropper.BuildConfig
import com.nowiczenkoandrzej.imagecropper.R
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import com.nowiczenkoandrzej.imagecropper.databinding.FragmentPicturesListBinding
import com.nowiczenkoandrzej.imagecropper.feature_picture_details.util.DetailsElements
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.AnimationHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_picture_detail.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PicturesListFragment : Fragment(), PicturesAdapter.ItemClickListener {


    @Inject lateinit var anim: AnimationHandler

    private var clicked = false

    private val viewModel: PicturesListViewModel by viewModels()

    private var _binding: FragmentPicturesListBinding? = null
    private val binding get() = _binding!!


    private lateinit var navController: NavController

    private lateinit var picturesAdapter: PicturesAdapter

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri == null) return@registerForActivityResult

        val picture = PictureItem(
            picture = uri.toString(),
            originalPicture = uri.toString()
        )

        val action = PicturesListFragmentDirections.actionPicturesListFragmentToEditPictureFragment(picture)
        navController.navigate(action)
    }


    private var mImageUri: Uri? = null
    private val getImageFromCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { isTaken ->
        if(isTaken) {
            mImageUri.let { uri ->
                val picture = PictureItem(
                    picture = uri.toString(),
                    originalPicture = uri.toString()
                )
                val action = PicturesListFragmentDirections.actionPicturesListFragmentToEditPictureFragment(picture)
                navController.navigate(action)
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPicturesListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setRecycleView()
        subscribeCollector()
        setListeners()
        clicked = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search by title"
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null)
                    viewModel.getPicturesByName(newText)
                else
                    viewModel.getPicturesByName("")
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun setRecycleView() {
        picturesAdapter = PicturesAdapter(requireContext())
        picturesAdapter.setClickListener(this)
        binding.rvPictures.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = picturesAdapter
        }
    }

    private fun subscribeCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pictureList.collect { pictures ->
                picturesAdapter.setList(pictures)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.position.collect { pos ->
                binding.rvPictures.post {
                    binding.rvPictures.layoutManager?.scrollToPosition(pos)
                }
            }
        }

    }

    private fun setListeners(){
        binding.btnMain.setOnClickListener {
            onAddButtonClicked()
        }

        binding.btnGallery.setOnClickListener {
            getImageFromGallery.launch("image/*")
        }

        binding.btnCamera.setOnClickListener {
            takePicture()
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        binding.btnGallery.isVisible = clicked
        binding.btnCamera.isVisible = clicked
        binding.btnCamera.isClickable = !clicked
        binding.btnGallery.isClickable = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            binding.btnCamera.startAnimation(anim.slideUp)
            binding.btnGallery.startAnimation(anim.slideUp)
            binding.btnMain.startAnimation(anim.rotateOpen)
        } else {
            binding.btnCamera.startAnimation(anim.slideDown)
            binding.btnGallery.startAnimation(anim.slideDown)
            binding.btnMain.startAnimation(anim.rotateClose)
        }
    }



    override fun onItemClick(position: Int, items: DetailsElements) {
        val picture = picturesAdapter.getItem(position)
        val action = PicturesListFragmentDirections.actionPicturesListFragmentToPictureDetailFragment(picture)
        viewModel.setPosition(position)

        val extras = FragmentNavigator.Extras.Builder()
            .addSharedElement(items.picture, "transition_details_image")
            .addSharedElement(items.title, "transition_details_title")
            .addSharedElement(items.date, "transition_details_date")
            .build()

        navController.navigate(action, extras)
    }

    private fun takePicture() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                mImageUri = uri
                getImageFromCamera.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

}