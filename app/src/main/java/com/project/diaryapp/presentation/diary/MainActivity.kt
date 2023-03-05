package com.project.diaryapp.presentation.diary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.project.diaryapp.Const
import com.project.diaryapp.R
import com.project.diaryapp.databinding.ActivityMainBinding
import com.project.diaryapp.presentation.BaseActivity
import com.project.diaryapp.presentation.auth.LoginActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding by lazy { _binding!! }

    private val sharedPreferences: SharedPreferences by inject()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject()

    private val diaryViewModel: DiaryViewModel by viewModel()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    private val navController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.dashboardFragment || destination.id == R.id.archivedFragment) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }

        binding.btnLogout.setOnClickListener {
            sharedPreferencesEditor.clear().apply()
            diaryViewModel.clearDiaryDatabase()
            finish()
            LoginActivity.start(this)
        }

        binding.tvUsername.text = getString(R.string.format_username, sharedPreferences.getString(Const.USER_NAME, ""))
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}