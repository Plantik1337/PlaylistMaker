package com.example.playlistmaker.mediateka.ui.fragment


import android.graphics.Outline
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.CreatePlaylistFragmentBinding
import com.example.playlistmaker.mediateka.viewmodel.PlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CreatePlaylistFragment : Fragment() {
    companion object {
        var hasImage = false
        var hasName = false
        var hasDescription = false
    }

//    private val viewModel by viewModel<PlaylistViewModel> {
//        parametersOf("Строка")
//    }


    private lateinit var binding: CreatePlaylistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = CreatePlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BottomNavigationView.GONE

        fun canCreate(): Boolean {
            when (hasImage || hasName) {
                true -> {
                    binding.createButton.background.setTint(requireContext().getColor(R.color.blue))
                    return true
                }

                false -> {
                    binding.createButton.background.setTint(requireContext().getColor(R.color.grey))
                    return false
                }
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    canCreate()
                    binding.imageButton.setImageURI(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.PlaylistName.addTextChangedListener(onTextChanged = { s: CharSequence?, _: Int, _: Int, _: Int ->
            if (binding.PlaylistName.hasFocus() && s?.isNotEmpty() == true) {
                hasName = true
                canCreate()
                binding.PlaylistName.isSelected = true
                binding.PlaylistNameLittleText.isVisible = true
            } else {
                hasName = false
                canCreate()
                binding.PlaylistName.isSelected = false
                binding.PlaylistNameLittleText.isVisible = false
            }
        })

        binding.PlaylistDescription.addTextChangedListener(onTextChanged = { s: CharSequence?, _: Int, _: Int, _: Int ->
            if (binding.PlaylistDescription.hasFocus() && s?.isNotEmpty() == true) {
                hasDescription = true
                binding.PlaylistDescription.isSelected = true
                binding.descriptionLittleText.isVisible = true
            } else {
                hasDescription = false
                binding.PlaylistDescription.isSelected = false
                binding.descriptionLittleText.isVisible = false
            }

        })

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            hasImage = true
            binding.imageButton.clipToOutline = true
            binding.imageButton.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    val cornerRadius = 30
                    outline.setRoundRect(0, 0, view.width, view.height, cornerRadius.toFloat())
                }
            }
        }

        binding.createButton.setOnClickListener {
            when (canCreate()) {
                true -> {}
                false -> {
                    Toast.makeText(requireContext(), "Не заполнены обязательные поля",Toast.LENGTH_SHORT).show()
                }
            }
        }



        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (hasImage || hasName || hasDescription) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Завершить создание плейлиста?")
                            .setMessage("Все несохраненные данные будут потеряны!")
                            .setPositiveButton("Заввершить") { _, _ ->
                                // Завершаем Activity при нажатии на "Да"
                                findNavController().navigateUp()
                            }.setNegativeButton("Отмена", null).show()
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })
    }

}