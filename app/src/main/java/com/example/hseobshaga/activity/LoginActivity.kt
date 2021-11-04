package com.example.hseobshaga.activity

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.hseobshaga.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        mAuth = FirebaseAuth.getInstance()
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()
        //val currentUser = mAuth.currentUser
        //updateUI(currentUser)
        binding.loginBtn.setOnClickListener {
            checkLoginInfo()
        }

        binding.registritationBtn.setOnClickListener {
            startRegistrationActivity()
        }

    }

    private fun startRegistrationActivity(){
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    private fun loginWithEmailAndPassword(login : String, password : String){
        mAuth.signInWithEmailAndPassword(login,password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                try {
                    throw it.exception!!
                }
                catch (wrongPassword : FirebaseAuthInvalidCredentialsException){
                    binding.passwordET.error = "Неверный пароль"
                    binding.passwordET.requestFocus()
                }
                catch (wrongEmail : FirebaseAuthInvalidUserException){
                    binding.mailET.error = "Нет пользователя с таким адресом"
                    binding.mailET.requestFocus()
                }
                Log.d("authError", "! logged", it.exception)
            }
        }
    }

    private fun checkLoginInfo(){
        val login = binding.mailET.text.toString()
        val password = binding.passwordET.text.toString()

        if (login.isEmpty()){
            binding.mailET.error = "Введите email"
            binding.mailET.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(login).matches()){
            binding.mailET.error = "Невверный email"
            binding.mailET.requestFocus()
            return
        }
        if (password.isEmpty()){
            binding.passwordET.error = "Введите пароль"
            binding.passwordET.requestFocus()
            return
        }

        if (password.length < 6){
            binding.passwordET.error = "Слишком короткий пароль"
            binding.passwordET.requestFocus()
            return
        }
        loginWithEmailAndPassword(login, password)
    }


}

