package com.talespalma.menssagebase.Adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.talespalma.menssagebase.databinding.ItemChatDestinaidBinding
import com.talespalma.menssagebase.databinding.ItemChatSendBinding

class AdapterChat :Adapter<ViewHolder>(){

    //Classe 1  send
    class ItemSendViewHolder(
      val  binding: ItemChatSendBinding
    ) : ViewHolder(binding.root) {

    }
    //Classe 2 destination
    class ItemDestinationViewHolder(
        val binding:ItemChatDestinaidBinding
    ): ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}