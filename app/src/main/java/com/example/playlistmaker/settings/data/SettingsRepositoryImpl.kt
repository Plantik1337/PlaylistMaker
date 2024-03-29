package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    override fun shareLink() {
        val courseLinkUrl = context.getString(R.string.practicum)
        val sendIntent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, courseLinkUrl)
                type = "text/plain"
            }
        val intent = Intent.createChooser(sendIntent, context.getString(R.string.Share_App))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun supportContact() {
        val sendIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // тип данных для отправки
            putExtra(Intent.EXTRA_EMAIL, context.getString(R.string.MyEmail))// почта
            putExtra(
                Intent.EXTRA_SUBJECT,
                context.getString(R.string.MessageSbjectViaEmail)
            ) // Тема сообщения

            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.MessageViaEmail)
            ) // Текст сообщения
        }
        val intent = Intent.createChooser(sendIntent, context.getString(R.string.Contact_Us))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun termOfUse() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data =
            Uri.parse(context.getString(R.string.OfferLink)) // Ссылка на пользовательское соглашение
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(sendIntent)
    }

}