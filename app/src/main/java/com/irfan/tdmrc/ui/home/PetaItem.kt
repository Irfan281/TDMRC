package com.irfan.tdmrc.ui.home

import android.location.Location
import android.view.View
import com.irfan.tdmrc.BuildConfig
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.remote.PetaResponseItem
import com.irfan.tdmrc.databinding.ItemLokasiBinding
import com.squareup.picasso.Picasso

class PetaItem(private val peta: PetaResponseItem) : com.xwray.groupie.viewbinding.BindableItem<ItemLokasiBinding>(){
    override fun bind(viewBinding: ItemLokasiBinding, position: Int) {
        val gambar = "https://maps.googleapis.com/maps/api/staticmap?center=${peta.latitude},${peta.longitude}&zoom=18&markers=${peta.latitude},${peta.longitude}&size=272x258&key=${BuildConfig.GMK_KEY}"

        Picasso.get().load(gambar).into(viewBinding.imageView)

//        val results = FloatArray(1)
//        Location.distanceBetween(lat, long, peta.latitude.toDouble(), peta.longitude.toDouble(), results)

        viewBinding.apply {
            textView.text = peta.lokasi
            tvJarak.text = String.format("%.2f", peta.jarak) + " km"
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_lokasi
    }

    override fun initializeViewBinding(view: View): ItemLokasiBinding {
        return ItemLokasiBinding.bind(view)
    }
}