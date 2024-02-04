package com.talespalma.menssagebase.activitys

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import com.talespalma.menssagebase.databinding.ActivityChatBinding
import com.talespalma.menssagebase.model.Menssage
import com.talespalma.menssagebase.model.UserModel
import com.talespalma.menssagebase.utils.Constants
import com.talespalma.menssagebase.utils.toastMenssage
import java.util.Date


class ChatActivity : AppCompatActivity() {

    //Global vars
    val binding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }
    val firebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var listenerRegistrations: ListenerRegistration


    private var userDates: UserModel? = null

    //Life cycler
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //User infos from ConstactsFraments envent of click
        recoverUserInfoFromDestinations()
        setActionBar()
        setInfos()
        startClickEventes()
        initListers()
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistrations.remove()
    }

    //Functions ------------------------------------------------------
    private fun initListers() {
        val userCorrent = firebaseAuth.currentUser?.uid!!
        val userDetination = userDates?.id!!
        val menssageSend = binding.activityChatEditMensage.text.toString()
        listenerRegistrations = firebaseFirestore
            .collection("messages")
            .document(userCorrent)
            .collection(userDetination)
            .addSnapshotListener { value, error ->
                val dates = value?.documents

                val listMenssages = mutableListOf<Menssage>()
                dates?.forEach {
                    val convertor = it.toObject(Menssage::class.java)
                    listMenssages.add(Menssage(convertor?.menssage!!, convertor.date))
                }
                Log.i("teste_list", listMenssages.toString())
            }
    }

    private fun startClickEventes() {
        with(binding) {
            activityChatFabSend.setOnClickListener {
                salveMensagens()
            }
        }
    }

    private fun salveMensagens() {
        val userCorrent = firebaseAuth.currentUser?.uid!!
        val userDetination = userDates?.id!!
        val menssageSend = binding.activityChatEditMensage.text.toString()

        if (menssageSend.isNotEmpty()) {
            binding.activityChatInputMensage.error = null
            val menssage = Menssage(menssageSend)
            saveMsgFirebase(userCorrent, userDetination, menssage)
            //Invert funtions
            saveMsgFirebase(userDetination, userCorrent, menssage)

        } else {
            binding.activityChatInputMensage.error = "Você não digitou nada"
        }
    }


    private fun saveMsgFirebase(
        userCorrent: String,
        userDetination: String,
        menssage: Menssage
    ) {
        firebaseFirestore
            .collection("messages")
            .document(userCorrent)
            .collection(userDetination)
            .add(menssage)
            .addOnSuccessListener {
                binding.activityChatEditMensage.setText("")
            }
            .addOnFailureListener {
                toastMenssage("Erro em enviar mensagem ")
            }
    }

    private fun setActionBar() {
        val toolbar = binding.includeActionbar.materialToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setInfos() {
        with(binding) {
            val userPhoto = userDates?.photos ?: ""
            if (userPhoto.isNotEmpty()) Picasso.get().load(userDates?.photos)
                .into(activityChatImageView)
            activityChatTextName.text = userDates?.name
        }
    }

    private fun recoverUserInfoFromDestinations() {
        val extras = intent.extras

        //Verify origem infos
        if (extras != null) {
            val origem = extras.getString("origem")
            if (origem == Constants.CONTACTS_ORIGEM) {

                userDates = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    extras.getParcelable("userInfos", UserModel::class.java)
                } else {
                    extras.getParcelable("userInfos")
                }

            } else if (origem == Constants.CONVERSATIONS_ORIGEM) {
                //dados conversations
            } else {
                finish()
            }
        }

    }
}