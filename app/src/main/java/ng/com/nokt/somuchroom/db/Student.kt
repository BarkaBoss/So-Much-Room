package ng.com.nokt.somuchroom.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_attendance_tbl")
data class Student(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "student_id")
    var id: Int,

    @ColumnInfo(name = "student_matric")
    var matric: String,

    @ColumnInfo(name = "course_code")
    var course: String,

    @ColumnInfo(name = "student_date")
    var date: String
)