package ru.prog_edu.thronethingshablon;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.prog_edu.thronethingshablon.database.AppDataBase;
import ru.prog_edu.thronethingshablon.database.TaskEntry;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<TaskEntry>> tasks;

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDataBase dataBase = AppDataBase.getInstance(this.getApplication());
        tasks = dataBase.taskDao().loadAllTasks();
    }
}
