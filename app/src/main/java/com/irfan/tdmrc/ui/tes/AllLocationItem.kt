package com.irfan.tdmrc.ui.tes

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.irfan.tdmrc.BuildConfig
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.remote.PetaResponseItem
import com.irfan.tdmrc.databinding.ItemAllBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

class AllLocationItem(private val peta: PetaResponseItem) : BindableItem<ItemAllBinding>(){
    override fun bind(viewBinding: ItemAllBinding, position: Int) {
        viewBinding.apply {
            lokasi.text = peta.lokasi
            tvKabupaten.text = "Kabupaten :" + peta.kabupaten
            tvKecamatan.text = "Lokasi : " + peta.desa + ", " + peta.kecamatan
            tvElevasi.text = "Elevasi : " + peta.elev
            tvKondisi.text = "Kondisi : " + peta.kondisi
            tvWaktu.text = "Waktu : " + peta.waktu
            tvKeterangan.text = "Keterangan : " + peta.keterangan
        }

        val gambar = "https://maps.googleapis.com/maps/api/staticmap?center=${peta.latitude},${peta.longitude}&zoom=18&markers=${peta.latitude},${peta.longitude}&size=540x480&key=${BuildConfig.GMK_KEY}"
        Picasso.get().load(gambar).into(viewBinding.imageView2)

        viewBinding.card.setOnClickListener {
            val url = "https://maps.google.com/?q=${peta.latitude},${peta.longitude}"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(viewBinding.root.context, intent, null)
        }
    }

    override fun getLayout(): Int = R.layout.item_all

    override fun initializeViewBinding(view: View): ItemAllBinding {
        return ItemAllBinding.bind(view)
    }
}