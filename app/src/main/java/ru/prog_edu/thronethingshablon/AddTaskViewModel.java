package ru.prog_edu.thronethingshablon;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import ru.prog_edu.thronethingshablon.database.AppDataBase;
import ru.prog_edu.thronethingshablon.database.TaskEntry;

public class AddTaskViewModel extends ViewModel {
    private LiveData<TaskEntry> task;

    public AddTaskViewModel(AppDataBase dataBase, int taskId) {
        task = dataBase.taskDao().loadTaskById(taskId);
    }

    public LiveData<TaskEntry> getTask() {
        return task;
    }
}
