package com.blackapp.wajeezandroiddevelopertask.presentation.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blackapp.wajeezandroiddevelopertask.adapter.UserListAdapter
import com.blackapp.wajeezandroiddevelopertask.common.data_state.DataState
import com.blackapp.wajeezandroiddevelopertask.common.extension_fun.displayError
import com.blackapp.wajeezandroiddevelopertask.common.extension_fun.displayLoadingAnimation
import com.blackapp.wajeezandroiddevelopertask.common.extension_fun.displaySuccessAnimation
import com.blackapp.wajeezandroiddevelopertask.common.extension_fun.displayTryAgainAnimation
import com.blackapp.wajeezandroiddevelopertask.common.util.RVItemDecoration
import com.blackapp.wajeezandroiddevelopertask.databinding.ActivityMainBinding
import com.blackapp.wajeezandroiddevelopertask.databinding.AddUserDialogCustomeLayoutBinding
import com.blackapp.wajeezandroiddevelopertask.databinding.FilterResultDialogCustomeLayoutBinding
import com.blackapp.wajeezandroiddevelopertask.domain.model.User
import com.google.firebase.firestore.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var userListAdapter: UserListAdapter

    @Inject
    lateinit var itemDecoration: RVItemDecoration

    var selectedImageUri: Uri? = null

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRV()
        loadUsers()
        initSearchViewListener()
        initUIListener()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRV() {
        binding.rvUserListActivityMain.apply {
            adapter = userListAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            addItemDecoration(itemDecoration)
        }
    }

    private fun initUIListener() {
        binding.imgBtnResultFilterActivityMain.setOnClickListener {
            showFilterResultDialog()
        }

        binding.imgBtnAddNewUserActivityMain.setOnClickListener {
            showAddNewUserDialog()
        }
    }

    private fun loadUsers() {
        lifecycleScope.launchWhenStarted {
            viewModel.getAllUsers().collect { dataState ->

                when (dataState) {
                    is DataState.Loading -> {
                        displayLoadingAnimation(this@MainActivity, true)
                    }

                    is DataState.Success -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        appendUsers(dataState.data)
                    }

                    is DataState.Failed -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        displayTryAgainAnimation(this@MainActivity)
                        displayError(this@MainActivity, dataState.exception.message.toString())
                    }
                }
            }
        }
    }

    private fun uploadUserImage(username: String, selectedImage: Uri) {
        lifecycleScope.launchWhenStarted {
            viewModel.uploadImageToStore(selectedImage).collect { dataState ->

                when (dataState) {
                    is DataState.Loading -> {
                        displayLoadingAnimation(this@MainActivity, true)
                    }

                    is DataState.Success -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        addUser(User(username, dataState.data))
                        loadUsers() //load users again (refresh)
                    }

                    is DataState.Failed -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        displayTryAgainAnimation(this@MainActivity)
                        displayError(this@MainActivity, dataState.exception.message.toString())
                    }
                }
            }
        }
    }

    private fun filterUserHasImageOrNot(hasImage: Boolean) {

        lifecycleScope.launchWhenStarted {
            viewModel.filterUserHasImageOrNot(hasImage).collect { dataState ->

                when (dataState) {
                    is DataState.Loading -> {
                        displayLoadingAnimation(this@MainActivity, true)
                    }

                    is DataState.Success -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        appendUsers(dataState.data)
                    }

                    is DataState.Failed -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        displayTryAgainAnimation(this@MainActivity)
                        displayError(this@MainActivity, dataState.exception.message.toString())
                    }
                }
            }
        }
    }

    private fun initSearchViewListener() {
        binding.searchviewActivityMain.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                /*  binding.searchviewActivityMain.clearFocus()

                  if (!query.isNullOrBlank()) {
                      query?.let { search(it) }
                  } else {
                      loadUsers() // if blank or empty -> get all user
                  }*/

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (!newText.isNullOrBlank()) {
                    newText?.let { search(it) }
                } else {
                    loadUsers() // if blank or empty  -> get all user
                }
                return false
            }

        })
    }

    private fun appendUsers(userList: List<User>) {
        userListAdapter.submitData(userList)
    }

    private fun showFilterResultDialog() {

        var binding = FilterResultDialogCustomeLayoutBinding.inflate(layoutInflater)

        val customDialog = AlertDialog.Builder(this, 0).create()

        customDialog.apply {
            setView(binding?.root)
            setCancelable(false)
        }.show()

        val rbtnWithImage = binding.rbtnWithImageFilterResultDialogCustomLayout
        val rbtnWithoutImage = binding.rbtnWithoutImageFilterResultDialogCustomLayout

        binding?.btnConfirmFilterResultDialogCustomLayout?.setOnClickListener {
            if (rbtnWithImage.isChecked) {
                filterUserHasImageOrNot(true)
            } else {
                filterUserHasImageOrNot(false)
            }
            customDialog.dismiss()
        }
    }

    private fun showAddNewUserDialog() {

        var binding = AddUserDialogCustomeLayoutBinding.inflate(layoutInflater)

        val customDialog = AlertDialog.Builder(this, 0).create()

        customDialog.apply {
            setView(binding?.root)
            setCancelable(false)
        }.show()

        val etxtUsername = binding.etxtUsernameAddNewNameDialogCustomLayout
        val imgBtnSelectImage = binding.imgBtnSelectImageAddNewNameDialogCustomLayout
        val btnDone = binding.btnDoneAddNewUserCustomLayout

        // select user image listener
        imgBtnSelectImage.setOnClickListener {
            // select image from
            getContent.launch("image/*")

        }

        // button (done) click listener
        btnDone.setOnClickListener {

            etxtUsername.error = null // remove error message from edittext

            if (etxtUsername.text.toString().isNullOrBlank()) {
                etxtUsername.error = "field required"
                etxtUsername.requestFocus()
                return@setOnClickListener

            } else {

                if (selectedImageUri != null) { // check if imageUri not null then user selected image
                    uploadUserImage(etxtUsername.text.toString(), selectedImageUri!!)
                } else {
                    addUser(User(etxtUsername.text.toString(), null))
                    loadUsers() //load users again (refresh)
                }
            }

            customDialog.dismiss()
        }
    }

    private fun addUser(user: User) {
        lifecycleScope.launchWhenStarted {
            viewModel.addUser(user).collect { dataState ->
                when (dataState) {
                    is DataState.Loading -> {
                        displayLoadingAnimation(this@MainActivity, true)
                    }

                    is DataState.Success -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        displaySuccessAnimation(this@MainActivity)

                        selectedImageUri = null
                    }

                    is DataState.Failed -> {
                        displayLoadingAnimation(this@MainActivity, false)
                        displayTryAgainAnimation(this@MainActivity)
                        displayError(this@MainActivity, dataState.exception.message.toString())

                        selectedImageUri = null
                    }
                }
            }
        }
    }

    private fun search(username: String) {

        lifecycleScope.launchWhenStarted {
            viewModel.searchByUsername(username).collect { dataState ->

                when (dataState) {
                    is DataState.Loading -> {
                    }


                    is DataState.Success -> {
                        appendUsers(dataState.data)
                    }

                    is DataState.Failed -> {
                        displayTryAgainAnimation(this@MainActivity)
                        displayError(this@MainActivity, dataState.exception.message.toString())
                    }
                }
            }
        }
    }
}