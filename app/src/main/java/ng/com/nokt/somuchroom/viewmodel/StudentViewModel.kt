package ng.com.nokt.somuchroom.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import ng.com.nokt.somuchroom.db.Student
import ng.com.nokt.somuchroom.db.StudentRepo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StudentViewModel(private val studentRepo: StudentRepo): ViewModel() {

    val students = studentRepo.students.asLiveData()

    private var isUpdateOrDelete = false
    private lateinit var studentToUpdateOrDelete: Student

    val inputMatric = MutableLiveData<String?>()
    val inputCourse = MutableLiveData<String?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrUpdate(){
        if (isUpdateOrDelete) {
            studentToUpdateOrDelete.matric = inputMatric.value!!
            studentToUpdateOrDelete.course = inputCourse.value!!
            update(studentToUpdateOrDelete)
        }else{
            val matric = inputMatric.value!!
            val course = inputCourse.value!!
            val date = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formattedDate = date.format(formatter).toString()

            insert(Student(0, matric, course, formattedDate))

            inputMatric.value = null
            inputCourse.value = null
        }

    }
    fun clearOrDelete(){
        if (isUpdateOrDelete){
            delete(studentToUpdateOrDelete)
        }else{
            clearALl()
        }
    }

    fun insert(student: Student): Job = viewModelScope.launch{
            studentRepo.insert(student)
        }

    fun update(student: Student): Job = viewModelScope.launch{
        studentRepo.update(student)

        inputMatric.value = null
        inputCourse.value = null
        isUpdateOrDelete = false
        studentToUpdateOrDelete = student
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

    fun delete(student: Student): Job = viewModelScope.launch{
        studentRepo.delete(student)

        inputMatric.value = null
        inputCourse.value = null

        isUpdateOrDelete = false
        studentToUpdateOrDelete = student

        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

    fun clearALl(): Job = viewModelScope.launch{
        studentRepo.deleteAll()
    }

    fun initUpdateAndDelete(student: Student){
        inputMatric.value = student.matric
        inputCourse.value = student.course

        isUpdateOrDelete = true
        studentToUpdateOrDelete = student

        saveOrUpdateButtonText.value = "Update"
        clearOrDeleteButtonText.value = "Delete"
    }
}