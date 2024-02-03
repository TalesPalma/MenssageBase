package com.talespalma.menssagebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.talespalma.menssagebase.databinding.ActivityLoginBinding
import com.talespalma.menssagebase.utils.toastMenssage

class LoginActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    //Firebase services
    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    //User infos
    lateinit var email:String
    lateinit var password:String
    lateinit var name:String


    // On start
    override fun onStart() {
        super.onStart()
      verifyUserIslogin()
    }

    //On create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        eventOnCliks()

    }
    // All events of clicks
    private fun eventOnCliks() {
       with(binding){

           //Text view register
           textCadastro.setOnClickListener {
               startActivity(
                   Intent(applicationContext, RegisterActivity::class.java)
               )
           }

           //Button login
           btnLogin.setOnClickListener {
               if(verifyFieldNotEmpty()){
                   firebaseAuth.signInWithEmailAndPassword(email,password)
                       .addOnSuccessListener {
                           toastMenssage("Logado com sucesso!!")
                           startActivity(Intent(applicationContext,MainActivity::class.java))
                       }
                       .addOnFailureListener {error ->
                           exceptionsErros(error)
                       }
               }
           }

       }


    }

    //Verify user login
    private fun verifyUserIslogin(){
        val userUid = firebaseAuth.currentUser
        if(userUid != null){
            startActivity(Intent(this,MainActivity::class.java))
            toastMenssage("Usuario está logado")
        }
    }


    //Erros with verify login
    private fun exceptionsErros(error: Exception) {
        try {
            throw error
        }catch (user:FirebaseAuthInvalidUserException){
            toastMenssage("Usuario invalido")
        }catch (credentials:FirebaseAuthInvalidCredentialsException){
            toastMenssage("Usuario não existente")
        }
    }

    //Verify is fields is not empty
    private fun verifyFieldNotEmpty():Boolean {
        email = binding.editEmail.text.toString()
        password = binding.editPassword.text.toString()
       return if(email.isNotEmpty()){
            binding.inputEmail.error = null
            if(password.isNotEmpty()){
                binding.inputPassword.error = null
                true
            }else{
                binding.inputPassword.error = "Fill in the password field..."
                false
            }
        }else{
            binding.inputEmail.error = "Fill in the email field..."
           false
        }
    }

}