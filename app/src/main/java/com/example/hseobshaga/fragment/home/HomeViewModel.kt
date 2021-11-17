package com.example.hseobshaga.fragment.home

import androidx.lifecycle.ViewModel
import com.example.hseobshaga.data.User
import com.example.hseobshaga.fragment.profile.HomeUiState
import com.example.hseobshaga.fragment.profile.LoadingState
import com.example.hseobshaga.fragment.profile.ProfileAction
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.*

class HomeViewModel: ViewModel() {


    private var currentState: HomeUiState = HomeUiState()
    
    private val authFirebase = FirebaseAuth.getInstance()
    private var currentUser: User? = null

    private val mutableFlowState: MutableStateFlow<HomeUiState> = MutableStateFlow(currentState)
    val flowState: StateFlow<HomeUiState> = mutableFlowState
    private val mutableFlowAction: MutableSharedFlow<ProfileAction> = MutableSharedFlow(replay = 0)
    val flowAction: SharedFlow<ProfileAction> = mutableFlowAction


    private val storage = Firebase.storage
    private val userImageReference: StorageReference =
        storage.getReference("users/${authFirebase.currentUser!!.uid}.jpg")

    init {
        loadUser()
    }

    private fun loadNeighboursArray(roomNumber : String) {
        Firebase.database.reference.child("rooms").child(roomNumber).get()
                .addOnCompleteListener{ task ->
                    currentState.userNeighboursState = LoadingState.SUCCESS
                    submitState()
                    loadNeighboursProfiles(task)
                }

    }

    private fun loadNeighboursProfiles(task: Task<DataSnapshot>){
        val checkSize = task.result.children.toList().size
        task.result.children.forEach{
            FirebaseDatabase.getInstance()
                    .reference
                    .child("users")
                    .child(it.key.toString())
                    .get()
                    .addOnCompleteListener{task->
                        val tmpUser = task.result.getValue(User::class.java)
                        if (tmpUser != null) {
                            currentState.userNeighbours.add(tmpUser)
                            if (currentState.userNeighbours.size == checkSize){
                                currentState.userNeighboursProfileState = LoadingState.SUCCESS
                                submitState()
                            }
                        }
                    }
        }
    }

    private fun loadUser() {
        currentState.initLoadingState = LoadingState.LOADING
        currentState.userNeighboursState = LoadingState.LOADING
        currentState.userNeighboursProfileState = LoadingState.LOADING
        submitState()
        FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(authFirebase.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                currentUser = task.result.getValue(User::class.java)
                currentState.profile = currentUser
                currentState.profile?.let {
                    loadNeighboursArray(it.room)
                }
                currentState.initLoadingState = LoadingState.SUCCESS
                submitState()
            }
    }

    private fun submitState() {
        val newState = currentState.copy()
        mutableFlowState.value = newState
        currentState = newState
    }


}