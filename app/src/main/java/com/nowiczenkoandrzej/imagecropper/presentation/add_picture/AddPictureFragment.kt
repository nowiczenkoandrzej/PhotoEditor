package com.nowiczenkoandrzej.imagecropper.presentation.add_picture

import android.graphics.*
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
import com.nowiczenkoandrzej.imagecropper.databinding.FragmentPictureBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.presentation.util.PictureFilter

@AndroidEntryPoint
class AddPictureFragment : Fragment() {

    private val viewModel: AddPictureViewModel by viewModels()

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!

    private var picture: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        subscribeCollector()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners(){
        binding.btnNewImage.setOnClickListener {
            viewModel.onEvent(AddPictureEvent.ChoosePicture)
        }

        binding.btnSave.setOnClickListener {
            val displayMetrics = DisplayMetrics()

            val size = displayMetrics.widthPixels

            val cropped: Bitmap? = binding.cropImageView.getCroppedImage(
                reqWidth = size - 32,
                reqHeight = size - 32
            )
            viewModel.onEvent(AddPictureEvent.PictureCropped(
                PictureModel(
                    title = "",
                    bitmap = cropped!!
                )
            ))

            binding.containerEdit.isVisible = false
            binding.containerPicture.isVisible = true
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

        binding.btn11.setOnClickListener {
            binding.cropImageView.setAspectRatio(1,1)
        }

        binding.btn34.setOnClickListener {
            binding.cropImageView.setAspectRatio(3,4)
        }

        binding.btn916.setOnClickListener {
            binding.cropImageView.setAspectRatio(9,19)
        }

        binding.btnFreeAspectRatio.setOnClickListener {
            binding.cropImageView.clearAspectRatio()
        }

        binding.filterNormal.setOnClickListener {
            binding.cropImageView.setImageBitmap(picture)
        }

        binding.filterNegative.setOnClickListener {
            val pf = PictureFilter(picture!!)
            binding.cropImageView.setImageBitmap(pf.negativePicture())
        }

        binding.filterBlackWhite.setOnClickListener {
            val pf = PictureFilter(picture!!)
            binding.cropImageView.setImageBitmap(pf.blackAndWhitePicture())
        }

        binding.filterSepia.setOnClickListener {
            val pf = PictureFilter(picture!!)
            binding.cropImageView.setImageBitmap(pf.sepiaPicture())
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
            picture = bitmap
            binding.cropImageView.setImageBitmap(bitmap)
            binding.containerEdit.isVisible = true
            binding.containerPicture.isVisible = false
        }
    }

}