package com.talespalma.menssagebase.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.talespalma.menssagebase.databinding.FragmentsConversationsBinding

class ConversationsFragments : Fragment() {
    private var _binding:FragmentsConversationsBinding? = null
    private val binding : FragmentsConversationsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentsConversationsBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}