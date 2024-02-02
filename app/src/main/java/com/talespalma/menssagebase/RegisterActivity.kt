package com.talespalma.menssagebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.talespalma.menssagebase.databinding.ActivityRegisterBinding
import com.talespalma.menssagebase.model.UserModel
import com.talespalma.menssagebase.utils.toastMenssage
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    //Firebase services
    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val firebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    //User infos
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    //On Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startToolbar()
        startEventClick()


    }

    //Start all events of clicks
    private fun startEventClick() {
        binding.btnRegister.setOnClickListener {
            startFields()
            if (validateFields()) {
                registerUser()

            }
        }
    }
    //Start infos user
    private fun startFields() {
        name = binding.edtName.text.toString()
        email = binding.edtEmail.text.toString()
        password = binding.edtPassword.text.toString()
    }

    //Validade if Fields is not empty
    private fun validateFields(): Boolean {
        return if (name.isNotEmpty()) {
            binding.textInputLayoutName.error = null
            if (email.isNotEmpty()) {
                binding.textInputLayoutEmail.error = null
                if (password.isNotEmpty()) {
                    binding.textInputLayoutPassword.error = null
                    true
                } else {
                    binding.textInputLayoutPassword.error = "Fill in your password please"
                    false
                }
            } else {
                binding.textInputLayoutEmail.error = "Fill in your email please"
                false
            }
        } else {
            binding.textInputLayoutName.error = "Fill in your name please"
            false
        }
    }

    //Register user in FirebaseAuth
    private fun registerUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->

                //Register user firebase
                val userID = result.result.user?.uid
                if (userID != null) {
                    val user = UserModel(
                        userID, name, email,""
                    )
                    inserUserFireStore(user)
                }
            }
            .addOnFailureListener { error ->
                errorHandling(error)
            }
    }

    //Erros handling in register
    private fun errorHandling(error: Exception) {
        try {
            throw error

        } catch (passwordWeak: FirebaseAuthWeakPasswordException) {
            toastMenssage("Senha muito fraca , digite outra com letras,numero e caractres especiais")
            passwordWeak.printStackTrace()
            binding.textInputLayoutPassword.error = passwordWeak.message
        } catch (emailInUsing: FirebaseAuthUserCollisionException) {
            toastMenssage("Email já pertence a uma conta")
            emailInUsing.printStackTrace()
            binding.textInputLayoutEmail.error = emailInUsing.message
        } catch (credentialsInvalido: FirebaseAuthInvalidCredentialsException) {
            toastMenssage("Email invalido , digite outro email")
            credentialsInvalido.printStackTrace()
            binding.textInputLayoutEmail.error = credentialsInvalido.message
        }
    }

    //Insert into firestore
    private fun inserUserFireStore(user: UserModel) {
        firebaseFirestore.collection("user")
            .document(user.id)
            .set(user)
            .addOnSuccessListener {
                toastMenssage("Sucesso ao fazer cadastro")
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            .addOnFailureListener {
                toastMenssage("Erro ao fazer cadastro")
            }
    }
    //Start material toolbar
    private fun startToolbar() {
        val toolbar = binding.includeToolBar.materialToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Register"
            setDisplayHomeAsUpEnabled(true)
        }
    }

}