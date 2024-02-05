package com.talespalma.menssagebase.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.talespalma.menssagebase.Adapters.AdapterMenssages
import com.talespalma.menssagebase.activitys.ChatActivity
import com.talespalma.menssagebase.databinding.FragmentMenssageBinding
import com.talespalma.menssagebase.model.Conversation
import com.talespalma.menssagebase.model.UserModel
import com.talespalma.menssagebase.utils.Constants

class MenssageFragments : Fragment() {

    //Global vars
    val firebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var listenerRegistrations: ListenerRegistration

    // Avoid leak memory
    private var _binding: FragmentMenssageBinding? = null
    private val binding: FragmentMenssageBinding get() = _binding!!

    private val adapterMenssages = AdapterMenssages(){conversation ->
        val intent = Intent(context,ChatActivity::class.java)
        val usuario = UserModel(
            id = conversation.idDetinationsUser,
            name = conversation.name,
            photos = conversation.photo
        )
        intent.putExtra("userInfos",usuario)
        startActivity(intent)
    }


    //Life cycler

    override fun onStart() {
        super.onStart()
        addListMenssage()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenssageBinding.inflate(inflater, container, false)
        configReyclerView()


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        listenerRegistrations.remove()
    }


    //Functions -----------------------------------------------------------------
    private fun addListMenssage() {
        val idUSer = firebaseAuth.currentUser?.uid.toString()
        if (idUSer != null) {
            listenerRegistrations = firebaseFirestore
                .collection("conversations")
                .document(idUSer)
                .collection("last_conversations")
                .addSnapshotListener { value, error ->
                    val documents = value?.documents
                    val listDatesConversations = mutableListOf<Conversation>()

                    documents?.forEach {documentSnapshot ->
                        val date = documentSnapshot.toObject(Conversation::class.java)
                        listDatesConversations.add(date!!)
                    }
                    adapterMenssages.addList(listDatesConversations)
                }

        }

    }
    private fun configReyclerView(){
        binding.fragmentMenssageRecyclerView.adapter = adapterMenssages
        binding.fragmentMenssageRecyclerView.layoutManager = LinearLayoutManager(context)
    }



}