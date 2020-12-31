package es.manuel.recyclerviewexample.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import es.manuel.recyclerviewexample.R
import es.manuel.recyclerviewexample.model.local.database.DataSource
import es.manuel.recyclerviewexample.model.local.entity.Student
import es.manuel.recyclerviewexample.utils.Event

private const val STATE_FILTER: String  = "STATE_FILTER"

class ActivityMainViewModel(
    private val repository: DataSource,
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val legalAgeOnly: MutableLiveData<Boolean> = savedStateHandle.getLiveData(STATE_FILTER, false)

    val students: LiveData<List<Student>> = legalAgeOnly.switchMap {
        if (it) {
            repository.queryLegalAgeStudents()
        } else {
            repository.queryStudent()
        }
    }

    val filterText: LiveData<String> = legalAgeOnly.map {
        if (it) {
            application.getString(R.string.all_students)
        } else {
            application.getString(R.string.only_legal_age)
        }
    }

    private val _onEmptyListMessage: MutableLiveData<Event<String>> = MutableLiveData()
    val onEmptyListMessage: LiveData<Event<String>>
        get() = _onEmptyListMessage

    val emptyViewStatus: LiveData<Int> = students.map {
        if(it.isEmpty()) View.VISIBLE else View.GONE
    }

    fun filterStudents() {
        legalAgeOnly.value = legalAgeOnly.value?.not() ?: true
    }

    fun addStudent(student: Student): Boolean =
        repository.addStudent(student)

    fun deleteStudent(student: Student): Boolean =
        repository.deleteStudent(student)

    fun deleteAllUser() {
        if (students.value?.isNotEmpty() == true) {
            repository.deleteAllStudent()
        } else {
            _onEmptyListMessage.value =
                Event<String>(application.resources.getString(R.string.no_students))
        }
    }

    fun notifyChange(student: Student) =
        repository.getStudentPosition(student)
}