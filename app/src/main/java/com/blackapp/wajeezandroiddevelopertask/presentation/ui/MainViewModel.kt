package com.blackapp.wajeezandroiddevelopertask.presentation.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.blackapp.wajeezandroiddevelopertask.domain.datasource.remote.WajeezRepository
import com.blackapp.wajeezandroiddevelopertask.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val repository: WajeezRepository
) : ViewModel(){

    fun getAllUsers() = repository.getAllUsers()

    fun addUser(user: User) = repository.addUser(user)

    fun searchByUsername(username:String) =repository.searchByUsername(username)

    fun filterUserHasImageOrNot(filterHasImage:Boolean) =repository.filterUserHasImageOrNot(filterHasImage)

    fun uploadImageToStore(selectedImage: Uri) = repository.uploadImageToStore(selectedImage)

}