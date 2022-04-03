package ng.com.nokt.somuchroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ng.com.nokt.somuchroom.db.StudentRepo
import java.lang.IllegalArgumentException

class StudentViewModelFactory(private val studentRepo: StudentRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)){
            return StudentViewModel(studentRepo) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}