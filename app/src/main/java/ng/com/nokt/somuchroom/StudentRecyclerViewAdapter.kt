package ng.com.nokt.somuchroom

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ng.com.nokt.somuchroom.databinding.StudentItemsBinding
import ng.com.nokt.somuchroom.db.Student
import ng.com.nokt.somuchroom.generated.callback.OnClickListener

class StudentRecyclerViewAdapter(private val clickListener:
    (Student)-> Unit):RecyclerView.Adapter<StudentViewHolder>() {

    private val studentList = ArrayList<Student>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: StudentItemsBinding = DataBindingUtil
            .inflate(layoutInflater, R.layout.student_items, parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setList(students: List<Student>){
        studentList.clear()
        studentList.addAll(students)
    }
}

class StudentViewHolder(val binding: StudentItemsBinding,): RecyclerView.ViewHolder(binding.root){
    fun bind(student: Student,  clickListener: (Student)-> Unit){
        binding.tvMatric.text = student.matric
        binding.tvCourse.text = student.course
        binding.tvDate.text = student.date
        binding.listItemLayout.setOnClickListener {
            clickListener(student)
        }
    }
}