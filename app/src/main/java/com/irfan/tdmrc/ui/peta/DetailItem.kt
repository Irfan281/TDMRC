package com.irfan.tdmrc.ui.peta

import android.content.Intent
import android.view.View
import com.irfan.tdmrc.BuildConfig
import com.irfan.tdmrc.R
import com.irfan.tdmrc.data.remote.PetaResponseItem
import com.irfan.tdmrc.databinding.ItemDetailPetaBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

class DetailItem(private val compose: String) : BindableItem<ItemDetailPetaBinding>(){
    override fun bind(viewBinding: ItemDetailPetaBinding, position: Int) {
        val hasil = compose.split(":")

        viewBinding.apply {
            textView2.text = hasil[0]
            tvIsi.text = hasil[1]
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_detail_peta
    }

    override fun initializeViewBinding(view: View): ItemDetailPetaBinding {
        return ItemDetailPetaBinding.bind(view)
    }
}