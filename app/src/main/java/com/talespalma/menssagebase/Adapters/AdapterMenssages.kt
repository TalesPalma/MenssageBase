package com.talespalma.menssagebase.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.talespalma.menssagebase.databinding.ItemMenssageBinding
import com.talespalma.menssagebase.model.Conversation
import com.talespalma.menssagebase.model.Menssage

class AdapterMenssages(
    val onClick:(Conversation)-> Unit
): Adapter<AdapterMenssages.ConversasViewHolder>() {

    //Global vars Adapter
    private var listAdapter = emptyList<Conversation>()

    inner class ConversasViewHolder(
        val binding: ItemMenssageBinding
    ) : ViewHolder(binding.root) {

         fun setInfoUser(menssage: Conversation) {
            val imageView = binding.itemMenssageImageView
            if(menssage.photo.isNotEmpty()) Picasso.get().load(menssage.photo).into(imageView)
            binding.itemMenssageTextName.text = menssage.name
            binding.itemMenssageLastMenssage.text = menssage.menssage
            binding.contraintLayout.setOnClickListener {
                onClick(menssage)
            }
        }



    }

    //Adapter functions
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversasViewHolder {
        val view = ItemMenssageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConversasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAdapter.size
    }

    override fun onBindViewHolder(holder: ConversasViewHolder, position: Int) {
       holder.setInfoUser(listAdapter[position])
    }



    fun addList(listExtern: List<Conversation>) {
        listAdapter = listExtern
        notifyDataSetChanged()
    }


}