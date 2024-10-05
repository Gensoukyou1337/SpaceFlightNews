package com.ivan.spaceflightnews

import androidx.lifecycle.ViewModel
import com.ivan.spaceflightnews.common.LoginCore

class MainNavHostViewModel(private val loginCore: LoginCore): ViewModel() {
    fun getCurrentIDTokenFlow() = loginCore.currentIDTokenStateFlow
}