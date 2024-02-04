package com.talespalma.menssagebase.activitys

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.talespalma.menssagebase.databinding.ActivityPerfilBinding
import com.talespalma.menssagebase.utils.toastMenssage

class PerfilActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityPerfilBinding.inflate(layoutInflater)
    }

    //Firebase services
    val firebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    //Permissions
    private var permissionCamera = false
    private var permissionGallery = false
    private var uriPhoto: Uri? = null

    //Manager Galerry
    private val managerGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            binding.perfilImageView.setImageURI(uri)
            uriPhoto = uri
        } else {
            toastMenssage("Nenhuma imagem foi selecionada")
        }
    }

    //On start
    override fun onStart() {
        super.onStart()
        requesUserInfos()
    }

    //Oncreate
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requestedPermissions()
        startToolbar()
        startClickEvents()
    }

    //Start events of clicks
    private fun startClickEvents() {
        binding.perfilFloatAddPicture.setOnClickListener {
            if (permissionGallery) {
                managerGallery.launch("image/*")
            } else {
                requestedPermissions()
            }
        }
        binding.perfilBtnUpdate.setOnClickListener {
            // if uriPhoto is not null att two infos else att name info
            if (uriPhoto != null) {
                updateImageAndNameUser()
            } else {
                updateNameUser()
            }

        }

    }

    //Update  name user in firebase
    private fun updateNameUser() {
        val nameUser = binding.perfilEditNome.text.toString()
        val infoAtt = mapOf(
            "name" to nameUser
        )
        firebaseFirestore.collection("user")
            .document(
                firebaseAuth.currentUser
                    ?.uid.toString()
            )
            .update(infoAtt)
    }

    //Request infos from firebase
    private fun requesUserInfos() {
        val reference =
            firebaseFirestore.collection("user")
                .document(firebaseAuth.currentUser?.uid.toString())
        reference.get().addOnCompleteListener { taks ->
            val url = taks.result.get("photos").toString()
            val nameUser = taks.result.get("name").toString()
            setInfosUser(nameUser, url)
        }.addOnFailureListener {
            Log.i("info_firebase", " ERROR IN SNAPS DOWLOAD FIREBASE : ${it.message}")
        }
    }

    //Set user infos
    private fun setInfosUser(nameUser: String, url: String) {
        if (url.isNotEmpty()) {
            binding.perfilEditNome.setText(nameUser)
            Picasso.get().load(url).into(binding.perfilImageView);
        } else {
            binding.perfilEditNome.setText(nameUser)
        }
    }

    //Update image and name user
    private fun updateImageAndNameUser() {
        val uri = uriPhoto
        firebaseStorage.getReference("Users").child("Images")
            .child(firebaseAuth.currentUser?.uid.toString()).child("perfil.jpg").putFile(uri!!)
            .addOnSuccessListener { Taks ->
                toastMenssage("User update success!!")
                Taks.metadata?.reference?.downloadUrl?.addOnSuccessListener { url ->
                    val name = binding.perfilEditNome.text.toString()
                    val infoAtt = mapOf("photos" to url, "name" to name)
                    firebaseFirestore.collection("user")
                        .document(firebaseAuth.currentUser?.uid.toString()).update(infoAtt)
                }
            }.addOnFailureListener {
                toastMenssage("Erro user photo update")
                Log.i("info_imageUpload", it.message.toString())
            }
    }


    //Request permissions for user
    private fun requestedPermissions() {
        //Verify if user already has permissions

        //Camera permission
        permissionCamera = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        //Files read permission
        permissionGallery = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

        //List Permissions denied
        val permissionsDenied = mutableListOf<String>()
        //Add permissions list
        when {
            !permissionCamera -> permissionsDenied.add(Manifest.permission.CAMERA)
            !permissionGallery -> permissionsDenied.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        // Permissions Denied
        if (permissionsDenied.isNotEmpty()) {
            //Request multi permissions
            val managerPermissions = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                permissionCamera = permissions[Manifest.permission.CAMERA] ?: permissionCamera
                permissionGallery =
                    permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: permissionGallery
            }
            managerPermissions.launch(permissionsDenied.toTypedArray())
        }

    }

    //Start material tool bar
    private fun startToolbar() {
        val materialToolBar = binding.includeToolBar.materialToolbar
        setSupportActionBar(materialToolBar)
        supportActionBar?.apply {
            title = "Perfil"
            setDisplayHomeAsUpEnabled(true)
        }

    }


}