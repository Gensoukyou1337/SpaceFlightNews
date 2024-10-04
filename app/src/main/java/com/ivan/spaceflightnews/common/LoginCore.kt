package com.ivan.spaceflightnews.common

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.ivan.spaceflightnews.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginCore(
    private val appContext: Context,
    private val loginDataStorage: LoginDataStorage
) {
    private val account: Auth0 = Auth0.getInstance(
        clientId = appContext.getString(R.string.com_auth0_client_id),
        domain = appContext.getString(R.string.com_auth0_domain)
    )

    private val loginCoreCoroutineScope = CoroutineScope(Dispatchers.IO)

    private val _currentIDTokenSharedFlow: MutableStateFlow<String> = MutableStateFlow("")
    val currentIDTokenSharedFlow: StateFlow<String> get() = _currentIDTokenSharedFlow

    private val _currentUserName: MutableStateFlow<String> = MutableStateFlow("")
    val currentUserName: StateFlow<String> get() = _currentUserName

    fun login(activityContext: Context, doOnLoginSuccess: (result: Credentials) -> Unit) {
        WebAuthProvider
            .login(account)
            .withScheme(appContext.getString(R.string.com_auth0_scheme))
            .start(activityContext, object: Callback<Credentials, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    // The user either pressed the “Cancel” button
                    // on the Universal Login screen or something
                    // unusual happened.
                    error.printStackTrace()
                }

                override fun onSuccess(result: Credentials) {
                    // The user successfully logged in.
                    saveUserIDToken(result.idToken)
                    saveUserName(result.user.name ?: activityContext.getString(R.string.default_user_name))
                    doOnLoginSuccess(result)
                }
            })
    }

    fun logout(doOnLogoutSuccess: () -> Unit) {
        WebAuthProvider
            .logout(account)
            .withScheme(appContext.getString(R.string.com_auth0_scheme))
            .start(appContext, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(error: AuthenticationException) {
                    // For some reason, logout failed.
                }

                override fun onSuccess(result: Void?) {
                    // The user successfully logged out.
                    deleteUserIDToken()
                    doOnLogoutSuccess()
                }
            })
    }

    private fun saveUserIDToken(idToken: String) {
        loginCoreCoroutineScope.launch {
            loginDataStorage.saveLoginIDToken(idToken)
        }
    }

    private fun saveUserName(userName: String) {
        loginCoreCoroutineScope.launch {
            loginDataStorage.saveUserName(userName)
        }
    }

    fun deleteUserIDToken() {
        loginCoreCoroutineScope.launch {
            loginDataStorage.clearUserData()
        }
    }

    fun queryUserIDToken() {
        loginCoreCoroutineScope.launch {
            loginDataStorage.getLoginIDToken().collect {
                _currentIDTokenSharedFlow.emit(it)
            }
        }
    }

    fun getUserNameFlow() = loginDataStorage.getUserName()
}