package com.talespalma.menssagebase.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import com.talespalma.menssagebase.databinding.FragmentContactsBinding
import com.talespalma.menssagebase.databinding.ItemContactsBinding
import com.talespalma.menssagebase.model.UserModel
//
class AdapterContacts(
    val onClick: (UserModel) -> Unit
) : Adapter<AdapterContacts.ContactsViewHolder>() {

    private var interListContacts: List<UserModel> = emptyList()

    inner class ContactsViewHolder(
        val binding: ItemContactsBinding
    ) : ViewHolder(binding.root) {

    }

    fun updateList(externList: List<UserModel>) {
        interListContacts = externList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = ItemContactsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return interListContacts.size
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val urlImage = interListContacts[position].photos
        val nameUser = interListContacts[position].name

        if (urlImage.isNotEmpty()) Picasso.get().load(urlImage)
            .into(holder.binding.fragmentsConstacsImage)
        holder.binding.fragmentsConstacsTextName.text = nameUser

        holder.binding.layoutConversation.setOnClickListener {
            onClick(interListContacts[position])
        }

    }
}

