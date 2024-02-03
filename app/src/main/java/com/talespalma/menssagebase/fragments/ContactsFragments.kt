package com.talespalma.menssagebase.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.auth.User
import com.talespalma.menssagebase.databinding.FragmentContactsBinding
import com.talespalma.menssagebase.databinding.FragmentsConversationsBinding
import com.talespalma.menssagebase.model.UserModel

class ContactsFragments : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding get() = _binding!!
    private lateinit var eventListerRegistration:ListenerRegistration

    //Firebase services
    val firebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        addListContacs()
    }

    private fun addListContacs() {
        eventListerRegistration = firebaseFirestore
            .collection("user")
            .addSnapshotListener { querySnapshot, error ->
                val listUsers = mutableListOf<UserModel>()
                val documents = querySnapshot?.documents
               //Add list users
                documents?.forEach { DocumentSnapshot ->
                   val users = DocumentSnapshot.toObject(UserModel::class.java)
                   val userCurrent = firebaseAuth.currentUser?.uid.toString()
                    //Validade users is not himself
                   if(users != null) {
                       if(users.id != userCurrent){
                           listUsers.add(users)
                       }
                   }
                }
                //Att recycler view

            }

    }


    override fun onDestroy() {
        super.onDestroy()
        eventListerRegistration.remove()
        _binding = null
    }
}