package com.example.hseobshaga.ext

import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun ImageView.load(uri: String) {
    Glide.with(this)
        .load(uri)
        .into(this)
}

@ExperimentalCoroutinesApi
fun EditText.textChanges(): Flow<CharSequence?> =
    callbackFlow {
        val listener = doOnTextChanged { text, _, _, _ -> trySend(text) }
        awaitClose {removeTextChangedListener(listener)}
    }.onStart { emit(text.toString()) }