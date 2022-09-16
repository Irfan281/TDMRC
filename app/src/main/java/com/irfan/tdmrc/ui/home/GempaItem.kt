package com.irfan.tdmrc.ui.home


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.remote.GempaResponseItem
import com.irfan.tdmrc.databinding.ItemGempaBinding
import com.irfan.tdmrc.ui.gempa.DetailGempaActivity
import com.xwray.groupie.viewbinding.BindableItem
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class GempaItem(private val gempa: GempaResponseItem) : BindableItem<ItemGempaBinding>() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun bind(binding: ItemGempaBinding, position: Int) {
        binding.apply {
            tvSkala.text = gempa.magnitude
            tvTempatGempa.text = gempa.wilayah
            tvDalam.text = "Kedalaman : ${gempa.kedalaman}"
            tvRasa.text = "Dirasakan : ${gempa.dirasakan}"
            tvWaktu.text = formatDate(gempa.dateTime)
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(currentDateString: String): String {
        val instant = Instant.parse(currentDateString.replace("+00:00", "Z"))
        val formatter = DateTimeFormatter.ofPattern("EEEE | dd MMM yyyy | HH:mm")
            .withZone(ZoneId.of(TimeZone.getDefault().id))
        return formatter.format(instant)
    }

    override fun getLayout(): Int {
        return R.layout.item_gempa
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initializeViewBinding(view: View): ItemGempaBinding {
        view.setOnClickListener {
            view.context.startActivity(Intent(view.context, DetailGempaActivity::class.java).apply {
                putExtra("koordinat", gempa.point.coordinates)
                putExtra("wilayah", gempa.wilayah)
                putExtra("skala", gempa.magnitude)
                putExtra("dalam", gempa.kedalaman)
                putExtra("rasa", gempa.dirasakan)
                putExtra("waktu", formatDate(gempa.dateTime))
            })
        }
        return ItemGempaBinding.bind(view)
    }
}