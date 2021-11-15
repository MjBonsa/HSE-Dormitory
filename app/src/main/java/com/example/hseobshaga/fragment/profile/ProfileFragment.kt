package com.example.hseobshaga.fragment.profile

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.hseobshaga.adapters.DelegateAdapter
import com.example.hseobshaga.adapters.viewtype.UserRequestViewType
import com.example.hseobshaga.data.UserRequestUI
import com.example.hseobshaga.databinding.FragmentProfileBinding
import com.example.hseobshaga.ext.load
import com.example.hseobshaga.ext.textChanges
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 *  В общем плане, здесь много magic значений, в лэйауте хардкод (оставил это, потому что пока не разобрались
 *  с неймингом ресурсов и прочими приколами)
 *  Поле aboutMe лучше засунуть в поле пользователя (в бд в таблицу к юзеру). Сам так делать не стал, потому что
 *  поломаю мб что-то у других. Пока можно так оставить, для mvp сойти должно
 *  Не помню, чтобы мы выбирали инструмент для асинхронщены, поэтому взял корутины
 *  С Firebase не работал, поэтому исправляйте если что. И как я понимаю, всю логику с ним нужно будет
 *  в дальнейшем вынести в data слой (но хз)
 *  Также здесь отсутствует пока обработка ошибок стоящая
 *  P.S. сорян за задержку, с дипломом ебусь
 */
class ProfileFragment: Fragment() {

    companion object{
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
    get() = _binding!!

    private val userRequestsAdapter: DelegateAdapter = DelegateAdapter(
        viewTypes = listOf(
            UserRequestViewType(
                onItemClick = this::onUserRequestClick
            )
        )
    )
    private val pickerActivityLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.uploadNewImage(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        binding.profileAvatar.clipToOutline = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        setListeners()
        observeAction()
        observeState()
        viewLifecycleOwner.lifecycleScope.launch {
            attachTextWatcherToUserDescription()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.profileRequests.apply {
            adapter = userRequestsAdapter
            addItemDecoration(DividerItemDecoration(
                requireContext(), DividerItemDecoration.VERTICAL
            ))
        }
    }

    private suspend fun attachTextWatcherToUserDescription() {
        binding.profileAboutMe.textChanges()
            .debounce(1500)
            .distinctUntilChanged()
            .collect {
                viewModel.userEnterNewDescription(it.toString())
            }
    }

    private fun setListeners() {
        binding.profileAvatar.setOnClickListener {
            pickerActivityLauncher.launch("image/*")
        }
    }

    private fun renderUI(state: ProfileUIState) {
        binding.apply {
            state.profile?.let { profileInfo ->
                profileRoom.text = profileInfo.room

                profileFullName.text = "${profileInfo.firstName} ${profileInfo.secondName}"
            }
            state.userRequests?.let { userRequests ->
                userRequestsAdapter.setItems(userRequests)
            }
            state.userAvatar?.let {
                binding.profileAvatar.load(it)
            }
            state.profile?.let {
                binding.profileAboutMe.setText(it.description)
            }
        }
    }

    private fun onUserRequestClick(userRequestUI: UserRequestUI) {
        Log.e("User click", "Click: $userRequestUI")
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowState.collect { state ->
                    renderUI(state)
                }
            }
        }
    }

    private fun observeAction() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowAction.collect { action ->
                    when (action) {
                        is ProfileAction.ShowSnackBar -> {
                            Snackbar.make(binding.root, action.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}