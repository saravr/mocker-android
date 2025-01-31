package com.scribd.android.mocker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scribd.android.mocker.model.Account
import com.scribd.android.mocker.model.Users
import com.scribd.android.mocker.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val testRepository: TestRepository,
): ViewModel() {
    private val _users = MutableStateFlow<List<Users.UsersItem>>(emptyList())
    val users = _users.asStateFlow()

    private val _account = MutableStateFlow<Account?>(null)
    val account = _account.asStateFlow()

    init {
        viewModelScope.launch {
            refresh()
        }
    }

    suspend fun refresh() {
        _users.emit(testRepository.getUsers())
        _account.emit(testRepository.getAccount())
    }
}
