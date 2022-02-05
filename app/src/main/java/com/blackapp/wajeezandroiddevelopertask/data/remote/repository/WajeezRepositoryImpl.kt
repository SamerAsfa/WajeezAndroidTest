package com.blackapp.wajeezandroiddevelopertask.data.remote.repository


import android.net.Uri
import com.blackapp.wajeezandroiddevelopertask.common.Constants
import com.blackapp.wajeezandroiddevelopertask.common.data_state.DataState
import com.blackapp.wajeezandroiddevelopertask.domain.datasource.remote.WajeezRepository
import com.blackapp.wajeezandroiddevelopertask.domain.model.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WajeezRepositoryImpl
@Inject constructor(): WajeezRepository {

    private val mUserCollection =
        FirebaseFirestore.getInstance().collection(Constants.COLLECTION_USER)

    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val fileName = formatter.format(now)

    val storageRef = FirebaseStorage.getInstance().getReference("images/$fileName")


    /**
     * Returns Flow of [DataState] which retrieves all users from cloud firestore collection.
     */
    override  fun getAllUsers() = flow<DataState<List<User>>> {

        // Emit loading state
        emit(DataState.Loading)

        val snapshot = mUserCollection.get().await()
        val users = snapshot.toObjects(User::class.java)

        // Emit success state with data
        emit(DataState.Success(users))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(DataState.GeneralError(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Adds user [user] into the cloud firestore collection.
     * @return The Flow of [DataState] which will store state of current action.
     */
    override  fun addUser(user: User) = flow<DataState<DocumentReference>> {

        // Emit loading state
        emit(DataState.Loading)

        val userRef = mUserCollection.add(user).await()

        // Emit success state with post reference
        emit(DataState.Success(userRef))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(DataState.GeneralError(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Search by user .
     * @return The Flow of [DataState] which will store state of current action.
     */
    override fun searchByUsername(username:String) = flow<DataState<List<User>>> {

        // Emit loading state
        emit(DataState.Loading)

        val snapshot = mUserCollection.
        whereEqualTo(Constants.FIELD_NAME , username).get().await()

        val users = snapshot.toObjects(User::class.java)

        // Emit success state with data
        emit(DataState.Success(users))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(DataState.GeneralError(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Filter  by username .
     * @return The Flow of [DataState] which will store state of current action.
     */
    override fun filterUserHasImageOrNot(filterHasImage:Boolean) = flow<DataState<List<User>>> {

        // Emit loading state
        emit(DataState.Loading)

        val snapshot = if(filterHasImage){
            mUserCollection.whereNotEqualTo("profilePicUrl" ,null).get().await()
        }else{
            mUserCollection.whereEqualTo("profilePicUrl" ,null).get().await()
        }

        val users = snapshot.toObjects(User::class.java)

        // Emit success state with data
        emit(DataState.Success(users))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(DataState.GeneralError(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Upload Image to firebase store .
     * @return The Flow of [DataState] which will store state of current action.
     */
    override fun uploadImageToStore(selectedImage: Uri) = flow<DataState<String>> {

        // Emit loading state
        emit(DataState.Loading)

         storageRef.putFile(selectedImage).await()
         emit(DataState.Success(storageRef.downloadUrl.await().toString()))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(DataState.GeneralError(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}