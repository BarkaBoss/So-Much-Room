package ng.com.nokt.somuchroom.db

import androidx.room.*

@Dao
interface StudentDAO {

    @Insert
    suspend fun insertStudent(student: Student) :Long

    @Update
    suspend fun updateStudent(student: Student) : Int

    @Delete
    suspend fun deleteStudent(student: Student) :Int

    @Query(value = "DELETE FROM student_attendance_tbl")
    suspend fun deleteAll() : Int

    @Query(value = "SELECT * FROM student_attendance_tbl")
    fun getAllStudents(): kotlinx.coroutines.flow.Flow<List<Student>>
}