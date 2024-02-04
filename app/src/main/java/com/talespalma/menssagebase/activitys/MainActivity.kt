package com.talespalma.menssagebase.activitys

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.talespalma.menssagebase.Adapters.ViewPageAdapter
import com.talespalma.menssagebase.R
import com.talespalma.menssagebase.databinding.ActivityMainBinding
import com.talespalma.menssagebase.utils.toastMenssage

//App pode ser algo com tema meio militar para dar uma aspect exotivo
class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //Firebase auth
    val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolBar()
        startNavegationTabs()

    }

    //Start navegation tabs ins view pages
    private fun startNavegationTabs() {
        val tabLayout = binding.tabLayout
        val viewPage = binding.mainViewPage
        //tabs
        val tabs = listOf("COVERSAS", "CONTATOS")
        //AdapterViewPage
        viewPage.adapter = ViewPageAdapter(
            list = tabs,
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle
        )
            //Completar toda largura da tab
        tabLayout.isTabIndicatorFullWidth = true
        //Esse método junto o tab layout com o view page
        TabLayoutMediator(tabLayout, viewPage) { tab, positon ->
            tab.text = tabs[positon]
        }.attach()

    }

    //Set material toolbar
    @SuppressLint("SuspiciousIndentation")
    private fun setToolBar() {
        val myToolbar = binding.includeToolbar.materialToolbar
        setSupportActionBar(myToolbar)
        supportActionBar.apply {
            title = "Menssage Base"
            addMenuProvider(
                menuProvider()
            )
        }
    }


    //Config menu
    private fun menuProvider() = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_main, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.item_perfil -> {
                    startActivity(Intent(applicationContext, PerfilActivity::class.java))
                }

                R.id.item_SingOut -> {
                    singOutUser()
                }
            }
            return true
        }
    }

    //Logout user and AlertDialog
    private fun singOutUser() {
        AlertDialog.Builder(this)
            .setTitle("Sair da conta ?")
            .setMessage("Deseja mesmo sair da sua conta?")
            .setPositiveButton("Sim") { dialog, position ->
                firebaseAuth.signOut()
                toastMenssage("Deslogado com sucesso")
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
            .setNegativeButton("Não", null)
            .create()
            .show()
    }

}