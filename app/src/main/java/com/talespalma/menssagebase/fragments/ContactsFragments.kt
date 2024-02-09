package com.talespalma.menssagebase.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.talespalma.menssagebase.Adapters.AdapterContacts
import com.talespalma.menssagebase.activitys.ChatActivity
import com.talespalma.menssagebase.databinding.FragmentContactsBinding
import com.talespalma.menssagebase.model.UserModel
import com.talespalma.menssagebase.utils.Constants

class ContactsFragments : Fragment() {

    // Globals Vars -----------------------------------------
    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding get() = _binding!!
    private lateinit var eventListerRegistration: ListenerRegistration
    private val adapterContacts = AdapterContacts{user ->
        val intent = Intent(context,ChatActivity::class.java)
        intent.putExtra("userInfos",user)
        intent.putExtra("origem",Constants.CONTACTS_ORIGEM)

        startActivity(intent)
    }

    //Firebase services
    val firebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    //Life cycles  -----------------------------------------
    override fun onStart() {
        super.onStart()
        addListContacs()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        startListView()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        eventListerRegistration.remove()
        _binding = null
    }


    //Functions ----------------------------------------------------

    //Start List view layout
    private fun startListView() {
        binding.ContactsReyclerView.adapter = adapterContacts
        binding.ContactsReyclerView.layoutManager = LinearLayoutManager(context)
        binding.ContactsReyclerView.addItemDecoration(
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        )
    }

    //Add infos in list view from firebase
    private fun addListContacs() {
        binding.apply {
            swapiRefresh.setOnRefreshListener {
                ContactsReyclerView.adapter?.notifyDataSetChanged()
                swapiRefresh.isRefreshing = false
            }
        }
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
                    if (users != null && users.id != userCurrent) {
                        listUsers.add(users)
                    }
                }
                //Att recycler view
                if(listUsers.isNotEmpty()){
                    adapterContacts.updateList(listUsers)
                }else{
                    binding.fragmentContacstsTextResultList.text = "Você não têm  contatos"
                }

            }

    }



}