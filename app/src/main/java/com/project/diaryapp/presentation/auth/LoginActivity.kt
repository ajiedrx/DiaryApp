package com.project.diaryapp.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.project.diaryapp.R
import com.project.diaryapp.base.Result
import com.project.diaryapp.databinding.ActivityLoginBinding
import com.project.diaryapp.showSnackbar
import com.project.diaryapp.validateAlphanumeric
import com.project.diaryapp.validateEmail
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding by lazy { _binding!! }

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()
        initAction()
    }

    private fun initAction(){
        with(binding){
            edtEmail.addTextChangedListener {
                tilEmail.error = if(it?.toString().isNullOrEmpty())
                    getString(R.string.empty_attribute_message, "Email")
                else null
            }

            edtPassword.addTextChangedListener {
                tilPassword.error = if(it?.toString().isNullOrEmpty())
                    getString(R.string.empty_attribute_message, "Password")
                else null
            }
            btnRegister.setOnClickListener {
                RegisterActivity.start(this@LoginActivity)
            }
            btnLogin.setOnClickListener {
                val validationResult = validateFields()
                if(validationResult.isEmpty()){
                    authViewModel.login(
                        email = edtEmail.text.toString(),
                        password = edtPassword.text.toString()
                    )
                } else {
                    binding.root.showSnackbar(validationResult)
                }
            }
        }
    }

    private fun initObservers(){
        authViewModel.login.observe(this){
            when(it){
                is Result.Loading -> {
                    setIsLoadingState(true)
                }
                is Result.Success -> {
                    setIsLoadingState(false)
                    binding.root.showSnackbar(getString(R.string.message_register_success))
                    //Redirect to dashboard page TODO
                }
                is Result.Error -> {
                    setIsLoadingState(false)
                    binding.root.showSnackbar(it.errorMessage.ifEmpty { getString(R.string.message_unknown_error) })
                }
            }
        }
    }

    private fun validateFields(): String{
        val stringBuilder = StringBuilder()
        with(binding){
            if(!edtEmail.text.toString().validateEmail()){
                stringBuilder.append(getString(R.string.invalid_attribute, "email") + "\n")
            }
            if(!edtPassword.text.toString().validateAlphanumeric()){
                stringBuilder.append(getString(R.string.invalid_attribute, "password") + "\n")
            }
            return stringBuilder.removeSuffix("\n").toString()
        }
    }

    private fun setIsLoadingState(isLoading: Boolean){
        binding.btnLogin.visibility = if(isLoading) View.INVISIBLE else View.VISIBLE
        binding.progressCircular.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}