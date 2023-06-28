package com.crystallake.scan.ocr

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.crystallake.scan.databinding.ItemScanResultBinding

class TextAdapter : SingleDataBindingAdapter<String, ItemScanResultBinding>() {
    override fun generateDataBinding(inflater: LayoutInflater, parent: ViewGroup): ItemScanResultBinding {
        return ItemScanResultBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(binding: ItemScanResultBinding, position: Int) {
        binding.result.text = dataList[position]
        binding.root.setOnClickListener {
            Toast.makeText(binding.result.context, dataList[position], Toast.LENGTH_SHORT).show()
        }
    }

}