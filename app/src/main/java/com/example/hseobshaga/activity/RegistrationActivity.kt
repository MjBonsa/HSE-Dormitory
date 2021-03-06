package com.example.hseobshaga.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import com.example.hseobshaga.R
import com.example.hseobshaga.adapters.CustomSpinnerAdapter
import com.example.hseobshaga.data.User
import com.example.hseobshaga.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import java.lang.Exception


class RegistrationActivity : AppCompatActivity() {

    private var data = ArrayList<String>()
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setRoomsData()
        auth = FirebaseAuth.getInstance()
    }


    override fun onStart() {
        super.onStart()
        binding.registritationBtn.setOnClickListener {
            Log.d("lol","HelloFromOnCreate")
            checkLoginInfoAndRegister()
        }
    }

    private fun setRoomsData(){
        Firebase.database.getReference("").child("rooms").get()
        .addOnCompleteListener{ task ->
            data.add("Комната")
            task.result.children.forEach{
                data.add("Room " + it.key.toString())
            }
            runSpinner()

        }
    }

    private fun runSpinner(){
        val adapter = CustomSpinnerAdapter(this, R.layout.spinner_item,R.id.roomId,data)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // Покраска спинерра обратно при ошибке
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position > 0 )
                binding.spinnerLayout.setBackgroundResource(R.drawable.edit_text)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO()
            }

        }


    }

    private fun createUser(mail : String, password : String){
        auth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userAuth = auth.currentUser
                    updateUserInfo(userAuth!!)
                    updateUI(userAuth)
                } else {
                    try {
                        throw task.exception!!
                    }
                    catch (existEmail: FirebaseAuthUserCollisionException) {
                        binding.mail.error = "Этот электронный адрес уже используется"
                        binding.mail.requestFocus()
                    }
                    catch (e :Exception){
                        Toast.makeText(this,"Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }

                }
            }

    }

    private fun updateUserInfo(userAuth: FirebaseUser){
        FirebaseDatabase.getInstance().getReference("users")
            .child(userAuth.uid).setValue(User(
                binding.firstName.text.toString(),
                binding.secondName.text.toString(),
                binding.mail.text.toString(),
                binding.spinner.selectedItem.toString().substring(5),
                "",
                ""
            ))
        FirebaseDatabase.getInstance().getReference("rooms")
            .child(binding.spinner.selectedItem.toString().substring(5))
            .child(userAuth.uid)
            .setValue(
                binding.firstName.text.toString() + " " + binding.secondName.text.toString()
            )
    }

    private fun updateUI(user: FirebaseUser?){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun checkLoginInfoAndRegister(){
        val firstName = binding.firstName.text.toString()
        val secondName = binding.secondName.text.toString()
        val mail = binding.mail.text.toString()
        val password = binding.password.text.toString()

        if (firstName.isEmpty()){
            binding.firstName.error = "Введите имя"
            binding.firstName.requestFocus()
            return
        }
        if (secondName.isEmpty()){
            binding.firstName.error = "Введите фамилию"
            binding.firstName.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            binding.mail.error = "Невверный email"
            binding.mail.requestFocus()
            return
        }
        if (password.isEmpty()){
            binding.password.error = "Введите пароль"
            binding.password.requestFocus()
            return
        }

        if (password.length < 6){
            binding.password.error = "Слишком короткий пароль"
            binding.password.requestFocus()
            return
        }

        if (binding.spinner.selectedItem.toString() == "Комната"){
            binding.spinnerLayout.setBackgroundResource(R.drawable.edit_text_error)
            binding.spinnerLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            return
        }
        createUser(mail,password)
    }
}