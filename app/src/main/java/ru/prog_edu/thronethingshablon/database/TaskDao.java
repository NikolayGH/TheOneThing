package ru.prog_edu.thronethingshablon.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id")
    LiveData<List<TaskEntry>> loadAllTasks();

    @Query("SELECT * FROM tasks WHERE id = :id")
    LiveData<TaskEntry> loadTaskById(int id);

    @Query("SELECT * FROM tasks WHERE time_period = :timePeriod ORDER BY id")
    LiveData<List<TaskEntry>> loadTimePeriodTasks(String timePeriod);

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);
}
