package es.manuel.recyclerviewexample.ui

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import es.manuel.recyclerviewexample.model.local.database.DataSource
import java.lang.IllegalArgumentException

class ActivityMainViewModelFactory(
    private val repository: DataSource,
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {


    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(ActivityMainViewModel::class.java)) {
            return  ActivityMainViewModel(repository, application, handle) as T
        } else {
            throw IllegalArgumentException("Wrong ViewModel class passed")
        }
    }
}