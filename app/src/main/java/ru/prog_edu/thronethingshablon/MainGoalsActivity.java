package ru.prog_edu.thronethingshablon;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.prog_edu.thronethingshablon.database.AppDataBase;
import ru.prog_edu.thronethingshablon.database.TaskEntry;

public class MainGoalsActivity extends AppCompatActivity{

    private String TIME_PERIOD_KEY = "time_period_key";
    private static final String TIME_PERIOD_DAY = "day";
    private static final String TIME_PERIOD_WEEK = "week";
    private static final String TIME_PERIOD_MONTH = "month";
    private static final String TIME_PERIOD_YEAR = "year";

    private static final String EXTRA_TASK_ID = "extraTaskId";

    private TaskEntry yearTask;
    private TaskEntry monthTask;
    private TaskEntry weekTask;
    private TaskEntry dayTask;

    private Button deleteButtonYear;
    private Button addButtonYear;
    private Button editButtonYear;
    private Button doneButtonYear;

    private Button deleteButtonMonth;
    private Button addButtonMonth;
    private Button editButtonMonth;
    private Button doneButtonMonth;

    private Button deleteButtonWeek;
    private Button addButtonWeek;
    private Button editButtonWeek;
    private Button doneButtonWeek;

    private Button deleteButtonDay;
    private Button addButtonDay;
    private Button editButtonDay;
    private Button doneButtonDay;

    private TextView tvYearTaskTitle;
    private TextView tvMonthTaskTitle;
    private TextView tvWeekTaskTitle;
    private TextView tvDayTaskTitle;


    private AppDataBase myDb;

    private int currentYear;
    private int currentMonth;
    private int currentWeek;
    private int currentDay;

    private final Set<String> tasksList = new HashSet<>();
    private static final String PREF_KEY_TASKS = "tasksData";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_goals);

        deleteButtonYear = findViewById(R.id.btn_year_clear);
        addButtonYear = findViewById(R.id.btn_year_add);
        editButtonYear = findViewById(R.id.btn_year_edit);
        doneButtonYear = findViewById(R.id.btn_year_done_undone);

        deleteButtonMonth = findViewById(R.id.btn_month_clear);
        addButtonMonth = findViewById(R.id.btn_month_add);
        editButtonMonth = findViewById(R.id.btn_month_edit);
        doneButtonMonth = findViewById(R.id.btn_month_done_undone);

        deleteButtonWeek = findViewById(R.id.btn_week_clear);
        addButtonWeek = findViewById(R.id.btn_week_add);
        editButtonWeek = findViewById(R.id.btn_week_edit);
        doneButtonWeek = findViewById(R.id.btn_week_done_undone);

        deleteButtonDay = findViewById(R.id.btn_day_clear);
        addButtonDay = findViewById(R.id.btn_day_add);
        editButtonDay = findViewById(R.id.btn_day_edit);
        doneButtonDay = findViewById(R.id.btn_day_done_undone);

        tvYearTaskTitle = findViewById(R.id.tv_year_task_title);
        tvMonthTaskTitle = findViewById(R.id.tv_month_task_title);
        tvWeekTaskTitle = findViewById(R.id.tv_week_task_title);
        tvDayTaskTitle = findViewById(R.id.tv_day_task_title);

        java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();

        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        doneButtonYear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doneButtonYear.setSelected(!doneButtonYear.isSelected());
                if(yearTask!=null){
                    if(doneButtonYear.isSelected()){
                        yearTask.setTaskIsDone(1);
                    }else{
                        yearTask.setTaskIsDone(0);
                    }
                    myDb.taskDao().updateTask(yearTask);
                }else{
                    Toast.makeText(MainGoalsActivity.this, R.string.no_active_tasksk, Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneButtonMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doneButtonMonth.setSelected(!doneButtonMonth.isSelected());
                if(monthTask!=null){
                    if(doneButtonMonth.isSelected()){
                        monthTask.setTaskIsDone(1);
                    }else{
                        monthTask.setTaskIsDone(0);
                    }
                    myDb.taskDao().updateTask(monthTask);
                }else{
                    Toast.makeText(MainGoalsActivity.this, R.string.no_active_tasksk, Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneButtonWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doneButtonWeek.setSelected(!doneButtonWeek.isSelected());
                if(weekTask!=null){
                    if(doneButtonWeek.isSelected()){
                        weekTask.setTaskIsDone(1);
                    }else{
                        weekTask.setTaskIsDone(0);
                    }
                    myDb.taskDao().updateTask(weekTask);
                }else{
                    Toast.makeText(MainGoalsActivity.this, R.string.no_active_tasksk, Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneButtonDay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doneButtonDay.setSelected(!doneButtonDay.isSelected());

                if(dayTask!=null){
                    if(doneButtonDay.isSelected()){
                        dayTask.setTaskIsDone(1);
                    }else{
                        dayTask.setTaskIsDone(0);
                    }
                    myDb.taskDao().updateTask(dayTask);
                }else{
                    Toast.makeText(MainGoalsActivity.this, R.string.no_active_tasksk, Toast.LENGTH_SHORT).show();
                }
            }
        });

        addButtonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_YEAR);
                startActivity(intent);
            }
        });

        editButtonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_YEAR);
                if(yearTask!=null){
                    intent.putExtra(EXTRA_TASK_ID, yearTask.getId());
                }
                startActivity(intent);
            }
        });

        deleteButtonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(yearTask!=null){
                    myDb.taskDao().deleteTask(yearTask);
                    tvYearTaskTitle.setText(R.string.task_for_year);

                }
            }
        });

        addButtonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_MONTH);
                startActivity(intent);

            }
        });

        editButtonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_MONTH);
                if(monthTask!=null){
                    intent.putExtra(EXTRA_TASK_ID, monthTask.getId());
                }
                startActivity(intent);

            }
        });

        deleteButtonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monthTask!=null){
                    myDb.taskDao().deleteTask(monthTask);
                    tvMonthTaskTitle.setText(R.string.tasks_for_month);
                }
            }
        });

        addButtonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_WEEK);
                startActivity(intent);

            }
        });

        editButtonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_WEEK);
                if(weekTask!=null){
                    intent.putExtra(EXTRA_TASK_ID, weekTask.getId());
                }
                startActivity(intent);
            }
        });

        deleteButtonWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weekTask!=null){
                    myDb.taskDao().deleteTask(weekTask);
                    tvWeekTaskTitle.setText(R.string.tasks_for_week);
                }
            }
        });

        addButtonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_DAY);
                startActivity(intent);
            }
        });

        editButtonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGoalsActivity.this, TaskComliteActivity.class);
                intent.putExtra(TIME_PERIOD_KEY, TIME_PERIOD_DAY);
                if(dayTask!=null){
                    intent.putExtra(EXTRA_TASK_ID, dayTask.getId());
                }
                startActivity(intent);
            }
        });

        deleteButtonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dayTask!=null){
                    myDb.taskDao().deleteTask(dayTask);
                    tvDayTaskTitle.setText(R.string.tasks_for_day);
                }
            }
        });

        myDb = AppDataBase.getInstance(getApplicationContext());
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_tasks_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.go_to_all_task:
                Intent intent = new Intent(MainGoalsActivity.this, TaskListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntry> taskEntries) {
                int numTasks = taskEntries.size();
                for (int i = 0; i < numTasks; i++) {
                    if(taskEntries.get(i).getTimePeriod().equals(TIME_PERIOD_YEAR)&&taskEntries.get(i).getTaskYear()== currentYear){
                        yearTask = taskEntries.get(i);
                        tvYearTaskTitle.setText(yearTask.getTaskTitle());
                        tasksList.add(yearTask.getTaskTitle());
                        int statusTask = yearTask.getTaskIsDone();
                        if(statusTask == 1){
                            doneButtonYear.setSelected(true);
                        }else {
                            doneButtonYear.setSelected(false);
                        }


                    }else if(taskEntries.get(i).getTimePeriod().equals(TIME_PERIOD_MONTH)&&taskEntries.get(i).getTaskMonth()== currentMonth){
                        monthTask = taskEntries.get(i);
                        tvMonthTaskTitle.setText(monthTask.getTaskTitle());
                        tasksList.add(monthTask.getTaskTitle());
                        int statusTask = monthTask.getTaskIsDone();
                        if(statusTask == 1){
                            doneButtonMonth.setSelected(true);
                        }else {
                            doneButtonMonth.setSelected(false);
                        }

                    }else if(taskEntries.get(i).getTimePeriod().equals(TIME_PERIOD_WEEK)&&taskEntries.get(i).getTaskWeek()== currentWeek){
                        weekTask = taskEntries.get(i);
                        tvWeekTaskTitle.setText(weekTask.getTaskTitle());
                        tasksList.add(weekTask.getTaskTitle());
                        int statusTask = weekTask.getTaskIsDone();
                        if(statusTask == 1){
                            doneButtonWeek.setSelected(true);
                        }else {
                            doneButtonWeek.setSelected(false);
                        }

                    }else if(taskEntries.get(i).getTimePeriod().equals(TIME_PERIOD_DAY)&&taskEntries.get(i).getTaskDay()== currentDay){
                        dayTask = taskEntries.get(i);
                        tvDayTaskTitle.setText(dayTask.getTaskTitle());
                        tasksList.add(dayTask.getTaskTitle());
                        int statusTask = dayTask.getTaskIsDone();
                        if(statusTask == 1){
                            doneButtonDay.setSelected(true);
                        }else {
                            doneButtonDay.setSelected(false);
                        }
                    }
                }
                SharedPreferences sharedPref = MainGoalsActivity.this.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                sharedPref.edit().putStringSet(PREF_KEY_TASKS, tasksList).apply();
            }
        });
    }
}
