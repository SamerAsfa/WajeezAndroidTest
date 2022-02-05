package com.blackapp.wajeezandroiddevelopertask.domain.use_case

import com.blackapp.wajeezandroiddevelopertask.common.data_state.DataState
import com.blackapp.wajeezandroiddevelopertask.data.remote.repository.WajeezRepositoryImpl
import com.blackapp.wajeezandroiddevelopertask.domain.model.User
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetUsers
@Inject
constructor(
    private val repository: WajeezRepositoryImpl
){

    operator fun invoke() = flow {
        try {
            emit(DataState.Loading)
            val users = repository.getAllUsers()
            emit(DataState.Success(users))

        } catch (e: HttpException) {
            emit(DataState.GeneralError(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(DataState.GeneralError("Couldn't reach server. Check your internet connection."))
        }
    }
}