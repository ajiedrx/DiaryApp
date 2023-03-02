package com.project.diaryapp.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.project.diaryapp.R
import com.project.diaryapp.base.Result
import com.project.diaryapp.databinding.ActivityRegisterBinding
import com.project.diaryapp.showSnackbar
import com.project.diaryapp.validateAlphanumeric
import com.project.diaryapp.validateEmail
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding by lazy { _binding!! }

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
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
            edtUsername.addTextChangedListener {
                tilUsername.error = if(it?.toString().isNullOrEmpty())
                    getString(R.string.empty_attribute_message, "Username")
                else null
            }
            edtPassword.addTextChangedListener {
                tilPassword.error = if(it?.toString().isNullOrEmpty())
                    getString(R.string.empty_attribute_message, "Password")
                else null
            }
            edtConfirmPassword.addTextChangedListener {
                tilConfirmPassword.error = if(it?.toString().isNullOrEmpty())
                    getString(R.string.empty_attribute_message, "Confirm password")
                else null
            }

            btnRegister.setOnClickListener {
                val validationResult = validateFields()
                if(validationResult.isEmpty()){
                    authViewModel.register(
                        email = edtEmail.text.toString(),
                        password = edtPassword.text.toString(),
                        username = edtUsername.text.toString()
                    )
                } else {
                    binding.root.showSnackbar(validationResult)
                }
            }
        }
    }

    private fun initObservers(){
        authViewModel.register.observe(this){
            when(it){
                is Result.Loading -> {
                    setIsLoadingState(true)
                }
                is Result.Success -> {
                    setIsLoadingState(false)
                    Toast.makeText(this, getString(R.string.message_register_success), Toast.LENGTH_LONG).show()
                    LoginActivity.start(this)
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
            if(!edtUsername.text.toString().validateAlphanumeric()){
                stringBuilder.append(getString(R.string.invalid_attribute, "username") + "\n")
            }
            if(!edtPassword.text.toString().validateAlphanumeric()){
                stringBuilder.append(getString(R.string.invalid_attribute, "password") + "\n")
            }
            if(edtConfirmPassword.text.toString() != edtPassword.text.toString()){
                stringBuilder.append(getString(R.string.invalid_confirmation_password) + "\n")
            }
            return stringBuilder.removeSuffix("\n").toString()
        }
    }

    private fun setIsLoadingState(isLoading: Boolean){
        binding.btnRegister.visibility = if(isLoading) View.INVISIBLE else View.VISIBLE
        binding.progressCircular.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}