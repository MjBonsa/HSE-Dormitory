package com.example.hseobshaga.fragment.profile

import com.example.hseobshaga.data.User
import com.example.hseobshaga.data.UserRequestUI

//userAvatar лучше внести в пользователя, но пока бд не хочу модифицировать
data class ProfileUIState(
    var initLoadingState: LoadingState? = null,
    var userRequestsLoading: LoadingState? = null,
    var profile: User? = null,
    var userRequests: List<UserRequestUI>? = null,
    var userAvatar: String? = null,
    var userDescription: String? = null
)

enum class LoadingState{
    LOADING, ERROR, SUCCESS
}
