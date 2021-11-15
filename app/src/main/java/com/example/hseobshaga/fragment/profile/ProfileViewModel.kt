package com.example.hseobshaga.fragment.profile

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ShareActionProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hseobshaga.data.User
import com.example.hseobshaga.data.UserRequestUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ProfileViewModel: ViewModel() {

    private var currentState: ProfileUIState = ProfileUIState()

    private val mutableFlowState: MutableStateFlow<ProfileUIState> = MutableStateFlow(currentState)
    val flowState: StateFlow<ProfileUIState> = mutableFlowState

    private val mutableFlowAction: MutableSharedFlow<ProfileAction> = MutableSharedFlow(replay = 0)
    val flowAction: SharedFlow<ProfileAction> = mutableFlowAction

    private val authFirebase = FirebaseAuth.getInstance()
    private var currentUser: User? = null

    private val descriptionRef = FirebaseDatabase.getInstance()
        .reference
        .child("users")
        .child(authFirebase.currentUser!!.uid)
        .child("description")

    private val storage = Firebase.storage
    private val userImageReference: StorageReference =
        storage.getReference("users/${authFirebase.currentUser!!.uid}.jpg")

    init {
        loadUser()
        loadAvatar()
        loadUserRequests()
    }

    fun uploadNewImage(uri: Uri) {
        currentState.userAvatar = uri.toString()
        submitState()
        val uploadTask = userImageReference.putFile(uri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    viewModelScope.launch(Dispatchers.Main) {
                        submitAction(ProfileAction.ShowSnackBar(it.stackTraceToString()))
                    }
                }
            }
            userImageReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                currentState.userAvatar = downloadUri.toString()
                submitState()
            } else {
                // Handle failures
                // ...
            }
        }
    }

    fun userEnterNewDescription(description: String) {
        descriptionRef.setValue(description)
            .addOnFailureListener {
                viewModelScope.launch(Dispatchers.Main) {
                    submitAction(ProfileAction.ShowSnackBar(it.stackTraceToString()))
                }
            }
            .addOnSuccessListener { currentState.profile?.description  = description }
    }

    private fun loadAvatar() {
        userImageReference.downloadUrl
            .addOnSuccessListener {
                currentState.userAvatar = it.toString()
                submitState()
            }
    }


    private fun loadUser() {
        currentState.initLoadingState = LoadingState.LOADING
        submitState()
        FirebaseDatabase.getInstance()
            .getReference()
            .child("users")
            .child(authFirebase.currentUser!!.uid)
            .get()
            .addOnFailureListener {
                viewModelScope.launch(Dispatchers.Main) {
                    submitAction(ProfileAction.ShowSnackBar(it.stackTraceToString()))
                }
            }
            .addOnCompleteListener { task ->
                currentUser = task.result.getValue(User::class.java)
                currentState.profile = currentUser
                currentState.initLoadingState = LoadingState.SUCCESS
                submitState()
            }
    }

    private fun loadUserRequests() {
        currentState.userRequestsLoading = LoadingState.LOADING
        submitState()
        val requestsList = mutableListOf<UserRequestUI>()
        FirebaseDatabase.getInstance()
            .getReference()
            .child("request")
            .child(authFirebase.currentUser!!.uid)
            .get()
            .addOnFailureListener {
                viewModelScope.launch(Dispatchers.Main) {
                    submitAction(ProfileAction.ShowSnackBar(it.stackTraceToString()))
                }
            }
            .addOnSuccessListener { result ->
                result.children.forEach { data ->
                    data.getValue(UserRequestUI::class.java)?.let {
                        requestsList.add(
                            it
                        )
                    }
                }
                currentState.userRequestsLoading = LoadingState.SUCCESS
                currentState.userRequests = requestsList
                submitState()
            }
    }

    private fun submitState() {
        val newState = currentState.copy()
        mutableFlowState.value = newState
        currentState = newState
    }

    private suspend fun submitAction(action: ProfileAction) {
        mutableFlowAction.emit(action)
    }
}