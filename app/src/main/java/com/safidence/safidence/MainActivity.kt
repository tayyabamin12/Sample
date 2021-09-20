package com.safidence.safidence

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.ActivityMainBinding
import com.safidence.safidence.ui.base.ViewModelFactory
import com.safidence.safidence.ui.login.LoginActivity
import com.safidence.safidence.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainViewModel: MainViewModel
    private var _binding: ActivityMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val root: View = binding.root
        setContentView(root)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            if (it.itemId == R.id.nav_logout) {
                binding.drawerLayout.close()
                mainViewModel.logout(SavePref(this).getAccessToken())
                showProgressDialog()
            }
            true
        }

        setupLogoutObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(MainViewModel::class.java)
    }

    private fun setupLogoutObserver() {
        mainViewModel.getLogoutResponse().observe(this, Observer{
            dismissDialog()
            if (it.status == "success") {
                logout()
            }
        })

        mainViewModel.getExceptionResponse().observe(this, Observer {
            dismissDialog()
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun logout() {
        SavePref(this).setAccessToken()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (this::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}