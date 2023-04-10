package com.nowiczenkoandrzej.imagecropper.feature_picture_details.presentation

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.nowiczenkoandrzej.imagecropper.databinding.FragmentPictureDetailBinding
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.presentation.EditPictureFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PictureDetailFragment : Fragment() {

    private val viewModel: PictureDetailViewModel by viewModels()

    private var _binding: FragmentPictureDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val args by navArgs<EditPictureFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onEvent(PictureDetailEvent.EnterScreen(picture = args.picture))
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navController.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureDetailBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        subscribeCollector()
        setListeners()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeCollector() {
        viewModel.state.onEach { picture ->
            binding.ivPicture.setImageURI(Uri.parse(picture.picture))
            binding.tvTitle.text = picture.title
            binding.tvLastEdit.text = picture.lastEdit.toString()
        }.launchIn(lifecycleScope)
    }

    private fun setListeners() {
        binding.ibEdit.setOnClickListener {
            val action = PictureDetailFragmentDirections.actionPictureDetailFragmentToEditPictureFragment(viewModel.state.value)
            navController.navigate(action)
        }
    }

}