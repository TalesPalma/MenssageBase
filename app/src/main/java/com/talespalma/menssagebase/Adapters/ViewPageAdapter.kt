package com.talespalma.menssagebase.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.talespalma.menssagebase.fragments.ContactsFragments
import com.talespalma.menssagebase.fragments.MenssageFragments

class ViewPageAdapter(
     val list:List<String>,
     fragmentManager: FragmentManager,
     lifecycle:Lifecycle
):FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
       return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> ContactsFragments()
            else->  MenssageFragments()
        }
    }


}