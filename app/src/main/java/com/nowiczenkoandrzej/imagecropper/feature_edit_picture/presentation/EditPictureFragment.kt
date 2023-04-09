package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.presentation

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.nowiczenkoandrzej.imagecropper.R
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.databinding.FragmentEditPictureBinding
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.FilterType
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.PictureFilter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditPictureFragment : Fragment() {

    private val viewModel: EditPictureViewModel by viewModels()

    private var _binding: FragmentEditPictureBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val args by navArgs<EditPictureFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onEvent(EditPictureEvent.SetPictureToEdit(picture = args.picture))
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
        viewModel.editedPicture.onEach { picture ->
            binding.cropImageView.setImageBitmap(picture.editedBitmap)
        }.launchIn(lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect{ isLoading ->
                    when(isLoading) {
                        true -> {
                            binding.container.visibility = View.GONE
                            binding.pgEdit.visibility = View.VISIBLE
                        }
                        false -> {
                            binding.container.visibility = View.VISIBLE
                            binding.pgEdit.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setListeners(){

        binding.btnSave.setOnClickListener {
            savePicture()
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
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.Normal))
        }

        binding.filterInvert.setOnClickListener {
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.Invert))
        }

        binding.filterBlackWhite.setOnClickListener {
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.BlackAndWhite))
        }

        binding.filterSepia.setOnClickListener {
            viewModel.onEvent(EditPictureEvent.AddFilter(FilterType.Sepia))
        }
    }

    private fun savePicture() {

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.enter_title_dialog, null)
        val edTitle = dialogLayout.findViewById<EditText>(R.id.ed_title)


        val displayMetrics = DisplayMetrics()
        val size = displayMetrics.widthPixels
        val cropped: Bitmap? = binding.cropImageView.getCroppedImage(
            reqWidth = size - 32,
            reqHeight = size - 32
        )


        viewModel.onEvent(EditPictureEvent.SavePicture(
            PictureModel(
                editedBitmap = cropped,
              //  title = edTitle.text.toString()
            )))

        binding.container.isVisible = true
        binding.pgEdit.isVisible = false

        navController.popBackStack()
    }

    private fun setFiltersToButton(){
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.mountains_icon)
        val pf1 = PictureFilter(bitmap = bitmap)
        val pf2 = PictureFilter(bitmap = bitmap)
        val pf3 = PictureFilter(bitmap = bitmap)

        binding.filterInvert.setImageBitmap(pf1.invertPicture())
        binding.filterBlackWhite.setImageBitmap(pf2.blackAndWhitePicture())
        binding.filterSepia.setImageBitmap(pf3.sepiaPicture())
    }

}