package com.talespalma.menssagebase.activitys

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import com.talespalma.menssagebase.Adapters.AdapterChat
import com.talespalma.menssagebase.databinding.ActivityChatBinding
import com.talespalma.menssagebase.model.Conversation
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
    private val adapterViewChat = AdapterChat()

    private var destinationDate: UserModel? = null
    private var userDate: UserModel? = null


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
        startReyclerView()
        initListers()
    }

    private fun startReyclerView() {
        binding.activityChatReyclerView.adapter = adapterViewChat
        binding.activityChatReyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistrations.remove()
    }

    //Functions ------------------------------------------------------
    private fun initListers() {
        val userCorrent = firebaseAuth.currentUser?.uid!!
        val userDetination = destinationDate?.id!!
        val menssageSend = binding.activityChatEditMensage.text.toString()
        listenerRegistrations = firebaseFirestore
            .collection("messages")
            .document(userCorrent)
            .collection(userDetination)
            .orderBy("date")
            .addSnapshotListener { value, error ->
                val dates = value?.documents

                val listMenssages = mutableListOf<Menssage>()
                dates?.forEach {
                    val convertor = it.toObject(Menssage::class.java)
                    listMenssages.add(
                        Menssage(
                            convertor?.idUser!!,
                            convertor.menssage!!,
                            convertor.date
                        )
                    )
                }
                adapterViewChat.updateList(listMenssages)
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
        val userDetination = destinationDate?.id!!
        val menssageSend = binding.activityChatEditMensage.text.toString()

        if (menssageSend.isNotEmpty()) {
            binding.activityChatInputMensage.error = null
            val menssage = Menssage(userCorrent, menssageSend)
            //Save user
            saveMsgFirebase(userCorrent, userDetination, menssage)
            val conversationDestination = Conversation(
                userCorrent,
                userDetination,
                destinationDate!!.photos,
                destinationDate!!.name,
                menssageSend
            )
            salveConversationFireStore(conversationDestination)
            //Save destinations user
            saveMsgFirebase(userDetination, userCorrent, menssage)
            val conversationSend = Conversation(
                userDetination,
                userCorrent,
                userDate!!.photos,
                userDate!!.name,
                menssageSend
            )
            salveConversationFireStore(conversationSend)


        } else {
            binding.activityChatInputMensage.error = "Você não digitou nada"
        }
    }

    private fun salveConversationFireStore(conversationDestination: Conversation) {
        firebaseFirestore
            .collection("conversations")
            .document(conversationDestination.idUserCurernt)
            .collection("last_conversations")
            .document(conversationDestination.idDetinationsUser)
            .set(conversationDestination)
            .addOnFailureListener {
                toastMenssage("erro salvar menssagens")
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
            val userPhoto = destinationDate?.photos ?: ""
            if (userPhoto.isNotEmpty()) Picasso.get().load(destinationDate?.photos)
                .into(activityChatImageView)
            activityChatTextName.text = destinationDate?.name
        }
    }

    private fun recoverUserInfoFromDestinations() {
        //recover date user
//        userDate
        firebaseFirestore.collection("user")
            .document(firebaseAuth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val userConvetion = documentSnapshot.toObject(UserModel::class.java)
                if (userConvetion != null) {
                    userDate = userConvetion
                }
            }

        //Recupera idependente da origem um userModel
        val extras = intent.extras
        if (extras != null) {
            destinationDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable("userInfos", UserModel::class.java)
            } else {
                extras.getParcelable("userInfos")
            }
        }else{
            finish()
        }

    }
}