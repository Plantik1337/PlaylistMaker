package com.example.playlistmaker.settings

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras


class SettingsViewModel(
    private val courseLink: String,
    private val myEmail: String,
    private val messageSbjectViaEmail: String,
    private val messageViaEmail: String,
    private val offerLink: String,
) : ViewModel() {

    var intentLiveData = MutableLiveData<Intent>()

    companion object {
        fun getViewModelFactory(
            courseLink: String,
            myEmail: String,
            messageSbjectViaEmail: String,
            messageViaEmail: String,
            offerLink: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return SettingsViewModel(
                        courseLink,
                        myEmail,
                        messageSbjectViaEmail,
                        messageViaEmail,
                        offerLink,
                    ) as T
                }
            }
    }


    //fun themeSwitcher()
    fun onShareClick() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseLink)
            type = "text/plain"
        }
        intentLiveData.value = sendIntent
    }

    fun onContactUsClick() {
        val sendIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // тип данных для отправки
            putExtra(Intent.EXTRA_EMAIL, myEmail)// почта
            putExtra(
                Intent.EXTRA_SUBJECT,
                messageSbjectViaEmail
            ) // Тема сообщения

            putExtra(
                Intent.EXTRA_TEXT,
                messageViaEmail
            ) // Текст сообщения
        }
        intentLiveData.value = sendIntent
    }

    fun onTermsOfUseClick() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data =
            Uri.parse(offerLink) // Ссылка на пользовательское соглашение
        intentLiveData.value = sendIntent
    }

    override fun onCleared() {
        super.onCleared()
    }


}