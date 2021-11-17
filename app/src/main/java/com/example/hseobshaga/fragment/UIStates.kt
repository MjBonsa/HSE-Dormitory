package com.example.hseobshaga.fragment.profile

import com.example.hseobshaga.data.User
import com.example.hseobshaga.data.UserRequestUI

//userAvatar лучше внести в пользователя, но пока бд не хочу модифицировать
data class ProfileUIState(
    var initLoadingState: LoadingState? = null,
    var userRequestsLoading: LoadingState? = null,
    var userAvatarLoading: LoadingState? = null,
    var profile: User? = null,
    var userRequests: List<UserRequestUI>? = null,
    var userAvatar: String? = null,
)

data class HomeUiState(
    var initLoadingState: LoadingState? = null,
    var userNeighboursState: LoadingState? = null,
    var userNeighboursProfileState : LoadingState? = null,
    var profile: User? = null,
    var userNeighbours: ArrayList<User> = ArrayList<User>()
)


enum class LoadingState{
    LOADING, ERROR, SUCCESS
}
