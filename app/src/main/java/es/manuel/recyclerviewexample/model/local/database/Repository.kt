package es.manuel.recyclerviewexample.model.local.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import es.manuel.recyclerviewexample.model.local.entity.Student

object Repository : DataSource {

    private var currentId: Int = 5;

    private val students: MutableList<Student> = mutableListOf(
        Student(1, "Manuel", 24),
        Student(2, "Juan", 17),
        Student(3, "Alberto", 12),
        Student(4, "María", 10),
        Student(5, "Ana", 15),
    )

    private val studentsMutableLiveData: MutableLiveData<List<Student>> =
        MutableLiveData(requeryStudent())

    override fun queryLegalAgeStudents(): LiveData<List<Student>> =
        studentsMutableLiveData.map { list ->
                list.filter {it.age >= 18} }

    override fun addStudent(student: Student): Boolean {
        val added = students.add(student.copy(id = ++currentId))
        if (added) {
            updateMutableLiveData()
        }
        return added
    }

    override fun deleteStudent(student: Student): Boolean {
        if(student.age > 23) {
            updateMutableLiveData()
            return false
        }
        val deleted = students.remove(student)
        if (deleted) {
            updateMutableLiveData()
        }
        return deleted
    }

    override fun queryStudent(): LiveData<List<Student>> =
        studentsMutableLiveData

    override fun getStudentPosition(student: Student): Int =
        students.toList().sortedBy { it.name }.indexOf(student)

    override fun deleteAllStudent(): Boolean {
        students.clear()
        updateMutableLiveData()
        return students.isEmpty()
    }

    private fun updateMutableLiveData() {
        studentsMutableLiveData.value = requeryStudent()
    }

    private fun requeryStudent(): List<Student> =
        students.toList().sortedBy { it.name }


}