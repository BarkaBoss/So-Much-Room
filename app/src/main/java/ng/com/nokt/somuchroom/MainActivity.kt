package ng.com.nokt.somuchroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ng.com.nokt.somuchroom.databinding.ActivityMainBinding
import ng.com.nokt.somuchroom.db.Student
import ng.com.nokt.somuchroom.db.StudentDatabase
import ng.com.nokt.somuchroom.db.StudentRepo
import ng.com.nokt.somuchroom.viewmodel.StudentViewModel
import ng.com.nokt.somuchroom.viewmodel.StudentViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var studentViewModel: StudentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = StudentDatabase.getInstance(application).studentDAO
        val repository = StudentRepo(dao)
        val factory = StudentViewModelFactory(repository)
        studentViewModel = ViewModelProvider(this, factory).get(StudentViewModel::class.java)

        binding.myViewModel = studentViewModel
        binding.lifecycleOwner = this

        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.recStudent.layoutManager = LinearLayoutManager(this)
        displayStudentsList()
    }

    private fun displayStudentsList(){
        studentViewModel.students.observe(this, Observer {
            Log.i("Students", it.toString())
            binding.recStudent.adapter = StudentRecyclerViewAdapter(it, {selectedItem: Student->listItemClicked(selectedItem)})
        })
    }

    private fun listItemClicked(student: Student){
        Toast.makeText(this, "Selected student is ${student.matric}", Toast.LENGTH_LONG).show()
        studentViewModel.initUpdateAndDelete(student)
    }
}