package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import com.nowiczenkoandrzej.imagecropper.databinding.FragmentPicturesListBinding
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.AnimationHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PicturesListFragment : Fragment(), PicturesAdapter.ItemClickListener {


    @Inject lateinit var anim: AnimationHandler

    //private val anim: AnimationHandler by lazy { AnimationHandler(requireContext()) }

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPicturesListBinding.inflate(inflater, container, false)
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
                Log.d("TAG", "subscribeCollector: $pictures")
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.position.collect { pos ->
                Log.d("Position", "subscribeCollector: $pos")
                Log.d("Position", "subscribeCollector: lm : ${binding.rvPictures.layoutManager}")
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


    override fun onItemClick(position: Int) {
        val picture = picturesAdapter.getItem(position)
        val action = PicturesListFragmentDirections.actionPicturesListFragmentToPictureDetailFragment(picture)
        viewModel.setPosition(position)
        Log.d("Position", "onItemClick: $position")
        navController.navigate(action)
    }

}