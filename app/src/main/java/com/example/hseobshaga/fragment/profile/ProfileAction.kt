package com.example.hseobshaga.fragment.profile

sealed class ProfileAction {
    data class ShowSnackBar(val message: String) : ProfileAction()
}