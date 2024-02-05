package com.talespalma.menssagebase.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.talespalma.menssagebase.databinding.ItemChatDestinaidBinding
import com.talespalma.menssagebase.databinding.ItemChatSendBinding
import com.talespalma.menssagebase.model.Menssage
import com.talespalma.menssagebase.utils.Constants

class AdapterChat : Adapter<ViewHolder>() {

    private var interListContacts: List<Menssage> = emptyList()
    fun updateList(externList: List<Menssage>) {
        interListContacts = externList
        notifyDataSetChanged()
    }

    //Classe 1  send
    class ItemSendViewHolder(
        val binding: ItemChatSendBinding
    ) : ViewHolder(binding.root) {
        fun setMenssage(message: Menssage) {
            binding.textViewSend.text = message.menssage
        }
        companion object {
            fun inflaterSendViewHolder(parent: ViewGroup): ItemSendViewHolder {
                val viewSend = ItemChatSendBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemSendViewHolder(viewSend)
            }
        }
    }

    //Classe 2 destination
    class ItemDestinationViewHolder(
        val binding: ItemChatDestinaidBinding
    ) : ViewHolder(binding.root) {
        fun setMenssage(message: Menssage) {
            binding.textViewDestination.text = message.menssage
        }
        companion object {
            fun inflaterSendViewHolder(parent: ViewGroup): ItemDestinationViewHolder {
                val viewSend = ItemChatDestinaidBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemDestinationViewHolder(viewSend)
            }
        }
    }

    //Esse método consegue pegar um item da lista e definir se ele é de um tipo.
    //Estou falando em português aqui porque isso é bem relevante :D
    override fun getItemViewType(position: Int): Int {
        val menssage = interListContacts[position]
        val idUser = FirebaseAuth.getInstance().currentUser?.uid.toString()

        //If id corrent == id menssage return tipo send else return tipo destination
        return if (idUser == menssage.idUser) {
            Constants.TIPO_SEND
        } else {
            Constants.TIPO_DESTINATION
        }
    }

    // To types left(blue) and right(green)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == Constants.TIPO_SEND) {
            ItemSendViewHolder.inflaterSendViewHolder(parent)
        } else {
            ItemDestinationViewHolder.inflaterSendViewHolder(parent)
        }
    }

    override fun getItemCount(): Int {
        return interListContacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //Set message corrent layout inflater
        val message = interListContacts[position]
        when (holder) {
            is ItemSendViewHolder -> holder.setMenssage(message)
            is ItemDestinationViewHolder -> holder.setMenssage(message)
        }

    }

}