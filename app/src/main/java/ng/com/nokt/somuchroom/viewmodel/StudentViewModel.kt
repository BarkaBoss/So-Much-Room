package ng.com.nokt.somuchroom.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import ng.com.nokt.somuchroom.db.Student
import ng.com.nokt.somuchroom.db.StudentRepo
import kotlinx.coroutines.launch
import ng.com.nokt.somuchroom.Event
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

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
    get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveOrUpdate(){

        if(inputMatric.value == null){
            statusMessage.value = Event("Matric number can not be empty")
        }else if (inputCourse.value == null){
            statusMessage.value = Event("Course Code can not be empty")
        }else{
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
    }
    fun clearOrDelete(){
        if (isUpdateOrDelete){
            delete(studentToUpdateOrDelete)
        }else{
            clearALl()
        }
    }

    fun insert(student: Student): Job = viewModelScope.launch{
        val newRowId:Long = studentRepo.insert(student)

        if (newRowId > -1) {
            statusMessage.value = Event("Student inserted successfully at $newRowId")
        }else{
            statusMessage.value = Event("There was a problem")
        }
    }

    fun update(student: Student): Job = viewModelScope.launch{
        val numUpdatedRows:Int = studentRepo.update(student)

        if (numUpdatedRows > 0) {
            inputMatric.value = null
            inputCourse.value = null
            isUpdateOrDelete = false
            studentToUpdateOrDelete = student
            saveOrUpdateButtonText.value = "Save"
            clearOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$numUpdatedRows updated successfully")
        }else{
            statusMessage.value = Event("There was a problem")
        }
    }

    fun delete(student: Student): Job = viewModelScope.launch{
        val numRowsDeleted: Int = studentRepo.delete(student)

        if (numRowsDeleted > 0) {
            inputMatric.value = null
            inputCourse.value = null

            isUpdateOrDelete = false
            studentToUpdateOrDelete = student

            saveOrUpdateButtonText.value = "Save"
            clearOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$numRowsDeleted removed successfully")
        }else{
            statusMessage.value = Event("There was a problem")
        }
    }

    fun clearALl(): Job = viewModelScope.launch{
        val numOfRowsDeleted:Int = studentRepo.deleteAll()

        if (numOfRowsDeleted > 0) {
            statusMessage.value = Event("$numOfRowsDeleted Students removed successfully")
        }else{
            statusMessage.value = Event("There was a problem")
        }
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