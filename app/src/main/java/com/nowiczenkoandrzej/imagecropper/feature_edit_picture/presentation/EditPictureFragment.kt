package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.presentation

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.nowiczenkoandrzej.imagecropper.R
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import com.nowiczenkoandrzej.imagecropper.databinding.FragmentEditPictureBinding
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.DialogClickListener
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.FilterType
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.PictureFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPictureFragment : Fragment(), DialogClickListener {

    private val viewModel: EditPictureViewModel by viewModels()

    private var _binding: FragmentEditPictureBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val args by navArgs<EditPictureFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onEvent(EditPictureEvent.SetPictureToEdit(picture = args.picture, context = requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPictureBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        subscribeCollector()
        setListeners()
        setFiltersToButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeCollector(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.editedBitmap.collect { bitmap ->
                    binding.cropImageView.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun setListeners(){

        binding.btnSave.setOnClickListener {
            showDialogToSetTitle()
        }

        // Rotation and flipping

        binding.btnRotateLeft.setOnClickListener {
            binding.cropImageView.rotateImage(-90)

        }

        binding.btnRotateRight.setOnClickListener {
            binding.cropImageView.rotateImage(90)
        }


        binding.btnFlipImageHorizontally.setOnClickListener {
            binding.cropImageView.flipImageHorizontally()
        }

        // Aspect ratio

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

        // Filters

        binding.filterNormal.setOnClickListener {
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.Normal, requireContext()))
        }

        binding.filterInvert.setOnClickListener {
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.Invert, requireContext()))
        }

        binding.filterBlackWhite.setOnClickListener {
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.BlackAndWhite, requireContext()))
        }

        binding.filterSepia.setOnClickListener {
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.Sepia, requireContext()))
        }
    }

    private fun savePicture(title: String) {

        val displayMetrics = DisplayMetrics()
        val size = displayMetrics.widthPixels
        val cropped: Bitmap? = binding.cropImageView.getCroppedImage(
            reqWidth = size - 32,
            reqHeight = size - 32
        )

        val picture = getPicture(cropped!!, title) ?: return

        viewModel.onEvent(EditPictureEvent.SavePicture(picture))
        navController.popBackStack(R.id.picturesListFragment, false)
    }

    private fun getPicture(bitmap: Bitmap, title: String): PictureItem?{
        val uri: Uri = try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/PhotoEditor")
            }
            val contentResolver = requireContext().contentResolver
            val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            val outputStream = contentResolver.openOutputStream(imageUri!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.flush()
            outputStream?.close()
            imageUri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return null

        return PictureItem(
            picture = uri.toString(),
            originalPicture = viewModel.editedPicture.value.originalPicture,
            title = title
        )

    }

    private fun showDialogToSetTitle() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Set Title")
        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, _ ->
            onDialogPositiveClick(input.text.toString())
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            onDialogNegativeClick()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setFiltersToButton(){
        val resourceId = R.drawable.mountains_icon
        val packageName = requireContext().packageName
        val uri = Uri.parse("android.resource://$packageName/$resourceId")

        val pf1 = PictureFilter(uri = uri, requireContext())
        val pf2 = PictureFilter(uri = uri, requireContext())
        val pf3 = PictureFilter(uri = uri, requireContext())

        binding.filterInvert.setImageBitmap(pf1.invertPicture())
        binding.filterBlackWhite.setImageBitmap(pf2.blackAndWhitePicture())
        binding.filterSepia.setImageBitmap(pf3.sepiaPicture())
    }

    override fun onDialogPositiveClick(title: String) {
        savePicture(title)
    }

    override fun onDialogNegativeClick() {
        return
    }

}