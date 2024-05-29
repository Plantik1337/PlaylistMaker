package com.example.playlistmaker.mediateka.ui.fragment


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Outline
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.CreatePlaylistFragmentBinding
import com.example.playlistmaker.mediateka.data.Playlist
import com.example.playlistmaker.mediateka.ui.viewmodel.CreatePlaylistViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class CreatePlaylistFragment : Fragment() {
    companion object {
        var hasImage = false
        var hasName = false
        var hasDescription = false

        var editMode = false

        const val PLAYLIST = "PLAYLIST"

        fun createArg(playlist: Playlist?): Bundle = bundleOf(
            PLAYLIST to playlist
        )
    }

    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private lateinit var binding: CreatePlaylistFragmentBinding
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = CreatePlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editPlaylist: Playlist? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(PLAYLIST, Playlist::class.java)
        } else {
            @Suppress("DEPRECATION") requireArguments().getParcelable<Playlist>(PLAYLIST)
        }


        fun canCreate(): Boolean {
            when (hasName) {
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


        editMode = when (editPlaylist) {
            null -> {
                false
            }

            else -> {
                true
            }
        }

        if (editMode) {
            binding.createButton.text = "Сохранить"

            if (editPlaylist?.imageURI != null && editPlaylist.imageURI.isNotBlank()) {
                Glide.with(this).load(editPlaylist.imageURI).into(binding.imageButton)
                hasImage = true
            }
            binding.PlaylistName.setText(editPlaylist?.playlistName)
            hasPlaylistName(true)
            if (editPlaylist?.description.isNullOrEmpty()) {
                hasPlaylistDescription(false)
            } else {
                binding.PlaylistDescription.setText(editPlaylist?.description)
                hasPlaylistDescription(true)
            }
            canCreate()
        }
        Log.i("Is EditMode", "${editMode}, ${editPlaylist}")


        binding.PlaylistName.addTextChangedListener(onTextChanged = { s: CharSequence?, _: Int, _: Int, _: Int ->
            if (s!!.isNotBlank()) {
                hasPlaylistName(true)
                canCreate()
            } else {
                hasPlaylistName(false)
                canCreate()
            }
        })

        binding.PlaylistDescription.addTextChangedListener(onTextChanged = { s: CharSequence?, _: Int, _: Int, _: Int ->
            if (s!!.isNotBlank()) {
                hasPlaylistDescription(true)
            } else {
                hasPlaylistDescription(false)
            }

        })

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    selectedImageUri = uri
                    binding.imageButton.setImageURI(uri)

                    binding.imageButton.clipToOutline = true
                    binding.imageButton.outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View, outline: Outline) {
                            val cornerRadius = 30
                            outline.setRoundRect(
                                0, 0, view.width, view.height, cornerRadius.toFloat()
                            )
                        }
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }


        binding.imageButton.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
            hasImage = true
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })

        fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
            return try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                val fileName = "${UUID.randomUUID()}.jpg"
                val file = File(context.filesDir, fileName)
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        binding.createButton.setOnClickListener {
            if (!editMode) {


                when (canCreate()) {
                    true -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val imagePath = selectedImageUri?.let { uri ->
                                saveImageToInternalStorage(requireContext(), uri)
                            }
                            viewModel.createPlaylist(
                                playlist = Playlist(
                                    playlistName = binding.PlaylistName.text.toString(),
                                    description = if (hasDescription) {
                                        binding.PlaylistDescription.text.toString()
                                    } else {
                                        null
                                    },
                                    imageURI = imagePath ?: "",
                                    trackIdList = emptyList(),
                                    numberOfTracks = 0,
                                    key = 0
                                )
                            )
                            Toast.makeText(
                                requireContext(),
                                "Плейлист ${binding.PlaylistName.text} создан",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigateUp()
                        }

                    }

                    false -> {
                        Toast.makeText(
                            requireContext(), "Не заполнены обязательные поля", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                lifecycleScope.launch {

                    val image = if (selectedImageUri != null) {
                        selectedImageUri?.let { uri ->
                            //deleteImageFromInternalStorage(editPlaylist?.imageURI.toString())
                            saveImageToInternalStorage(requireContext(), uri)
                        }
                    } else {
                        editPlaylist?.imageURI
                    }
                    viewModel.updatePlaylist(
                        Playlist(
                            key = editPlaylist!!.key,
                            playlistName = binding.PlaylistName.text.toString(),
                            description = if (hasDescription) {
                                binding.PlaylistDescription.text.toString()
                            } else {
                                null
                            },
                            imageURI = image,
                            trackIdList = editPlaylist.trackIdList,
                            numberOfTracks = 0
                        )
                    )
                    Toast.makeText(
                        requireContext(),
                        "Плейлист ${binding.PlaylistName.text} создан",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    fun hasPlaylistName(hasPlaylistName: Boolean) {
        hasName = hasPlaylistName
        binding.PlaylistName.isSelected = hasPlaylistName
        binding.PlaylistNameLittleText.isVisible = hasPlaylistName
    }

    fun hasPlaylistDescription(hasDescriptionText: Boolean) {
        hasDescription = hasDescriptionText
        binding.PlaylistDescription.isSelected = hasDescriptionText
        binding.descriptionLittleText.isVisible = hasDescriptionText
    }


    private fun onBackPressed() {
        if (hasImage || hasName || hasDescription) {
            AlertDialog.Builder(requireContext()).setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны!")
                .setPositiveButton("Завершить") { _, _ ->
                    // Завершаем Activity при нажатии на "Да"
                    findNavController().navigateUp()
                }.setNegativeButton("Отмена", null).show()
        } else {
            findNavController().navigateUp()
        }
    }

}