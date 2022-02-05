package com.blackapp.wajeezandroiddevelopertask.domain.datasource.remote

import android.net.Uri
import com.blackapp.wajeezandroiddevelopertask.common.data_state.DataState
import com.blackapp.wajeezandroiddevelopertask.domain.model.User
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface WajeezRepository {

     fun getAllUsers(): Flow<DataState<List<User>>>

    fun addUser(user: User) : Flow<DataState<DocumentReference>>

    fun searchByUsername(username:String) : Flow<DataState<List<User>>>

    fun filterUserHasImageOrNot(filterHasImage:Boolean) :Flow<DataState<List<User>>>

    fun uploadImageToStore(selectedImage: Uri) : Flow<DataState<String>>

}