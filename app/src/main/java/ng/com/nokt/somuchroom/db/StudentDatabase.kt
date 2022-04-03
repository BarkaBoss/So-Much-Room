package ng.com.nokt.somuchroom.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student::class], version = 1)
abstract class StudentDatabase: RoomDatabase() {

    abstract val  studentDAO: StudentDAO

    //Singleton of Database instance
    companion object{
        @Volatile
        private var INSTANCE : StudentDatabase? = null
        fun getInstance(context: Context): StudentDatabase{
            synchronized(this){
                var instance: StudentDatabase? = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StudentDatabase::class.java,
                    "attendance_database"
                    ).build()
                }
                return instance
            }
        }
    }
}