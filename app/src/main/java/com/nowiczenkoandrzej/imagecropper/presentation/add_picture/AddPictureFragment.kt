package com.nowiczenkoandrzej.imagecropper.presentation.add_picture

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.nowiczenkoandrzej.imagecropper.databinding.FragmentAddPictureBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel

@AndroidEntryPoint
class AddPictureFragment : Fragment() {

    private val viewModel: AddPictureViewModel by viewModels()

    private var _binding: FragmentAddPictureBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.onEvent(AddPictureEvent.EnterActivity)
        _binding = FragmentAddPictureBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        subscribeCollector()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onEvent(AddPictureEvent.EnterActivity)
    }

    override fun onStop() {
        viewModel.onEvent(AddPictureEvent.EnterActivity)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners(){
        binding.btnChooseImage.setOnClickListener {
            viewModel.onEvent(AddPictureEvent.ChoosePicture)
        }

        binding.btnCrop.setOnClickListener {
            val displayMetrics = DisplayMetrics()

            val width = displayMetrics.widthPixels
            val height = width * 3 / 4

            val cropped: Bitmap? = binding.cropImageView.getCroppedImage(
                reqWidth = width - 32,
                reqHeight = height - 32
            )
            viewModel.onEvent(AddPictureEvent.PictureCropped(
                PictureModel(
                    title = "",
                    bitmap = cropped!!
                )
            ))

            binding.containerEdit.isVisible = true
            binding.containerCrop.isVisible = false
        }

        binding.btnRotateLeft.setOnClickListener {
            binding.cropImageView.rotateImage(-90)
        }

        binding.btnRotateRight.setOnClickListener {
            binding.cropImageView.rotateImage(90)
        }

        binding.btnFlipImageHorizontally.setOnClickListener {
            binding.cropImageView.flipImageHorizontally()
        }

    }

    private fun subscribeCollector() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureState.collect { state ->
                    state.bitmap?.let { bitmap ->
                        Glide
                            .with(requireContext())
                            .load(bitmap)
                            .into(binding.ivCroppedImage)
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pickPictureToCrop.collect {
                    when(it) {
                        true -> getImageFromGallery.launch("image/*")
                        false -> Unit
                    }
                }
            }
        }
    }

    private val getImageFromGallery = registerForActivityResult(GetContent()) { uri: Uri? ->
        viewModel.onEvent(AddPictureEvent.EnterActivity)
        if(uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
            binding.cropImageView.setImageBitmap(bitmap)
            binding.containerEdit.isVisible = false
            binding.containerCrop.isVisible = true
        }
    }

}