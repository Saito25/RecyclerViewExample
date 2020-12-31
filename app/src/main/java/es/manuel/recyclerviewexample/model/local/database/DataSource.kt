package es.manuel.recyclerviewexample.model.local.database

import androidx.lifecycle.LiveData
import es.manuel.recyclerviewexample.model.local.entity.Student

interface DataSource {
    fun addStudent(student: Student): Boolean
    fun deleteStudent(student: Student): Boolean
    fun deleteAllStudent(): Boolean
    fun queryStudent(): LiveData<List<Student>>
    fun getStudentPosition(student: Student): Int
    fun queryLegalAgeStudents(): LiveData<List<Student>>
}