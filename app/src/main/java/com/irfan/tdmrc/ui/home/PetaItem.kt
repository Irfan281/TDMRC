package com.irfan.tdmrc.ui.home

import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.Toast
import com.irfan.tdmrc.BuildConfig
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.remote.PetaResponseItem
import com.irfan.tdmrc.databinding.ItemLokasiBinding
import com.irfan.tdmrc.ui.peta.DetailPetaActivity
import com.squareup.picasso.Picasso

class PetaItem(private val peta: PetaResponseItem, private val myLat: Double, private val myLong: Double) : com.xwray.groupie.viewbinding.BindableItem<ItemLokasiBinding>(){
    override fun bind(viewBinding: ItemLokasiBinding, position: Int) {
        val gambar = "https://maps.googleapis.com/maps/api/staticmap?center=${peta.latitude},${peta.longitude}&zoom=18&markers=${peta.latitude},${peta.longitude}&size=272x258&key=${BuildConfig.GMK_KEY}"

        Picasso.get().load(gambar).into(viewBinding.imageView)

        viewBinding.apply {
            textView.text = peta.lokasi
            tvJarak.text = String.format("%.2f", peta.jarak) + " km"
        }

        viewBinding.cardPeta.setOnClickListener {
            it.context.startActivity(Intent(it.context, DetailPetaActivity::class.java).apply { putExtra("peta", peta)
               putExtra("myLat", myLat)
               putExtra("myLong", myLong)

           })
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_lokasi
    }

    override fun initializeViewBinding(view: View): ItemLokasiBinding {
        return ItemLokasiBinding.bind(view)
    }
}