package ru.prog_edu.thronethingshablon.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;


@Entity(tableName = "tasks")
@TypeConverters(DateConverter.class)
public class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "time_period")
    private String timePeriod;
    @ColumnInfo(name = "title;")
    private String taskTitle;
    @ColumnInfo(name = "description")
    private String taskDescription;

//    @ColumnInfo(name = "date")
//    private Date taskSettingDate;

    @ColumnInfo(name = "task_of_year")
    private int taskYear;
    @ColumnInfo(name = "task_of_month")
    private int taskMonth;
    @ColumnInfo(name = "task_of_week")
    private int taskWeek;
    @ColumnInfo(name = "task_or_day")
    private int taskDay;
    @ColumnInfo(name = "task_completeness")
    private int taskCompleteness;
    @ColumnInfo(name = "task_status")
    private int taskIsDone;
    @ColumnInfo(name = "emotional_assessment")
    private int emotionalAssessmentOfTask;
    @ColumnInfo(name = "photo_url")
    private String photoUrl;


    @Ignore
    public TaskEntry(String timePeriod, String taskTitle, String taskDescription, int taskYear, int taskMonth, int taskWeek, int taskDay, int taskCompleteness, int taskIsDone, int emotionalAssessmentOfTask, String photoUrl) {
        this.timePeriod = timePeriod;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        //this.taskSettingDate = taskSettingDate;
        this.taskYear = taskYear;
        this.taskMonth = taskMonth;
        this.taskWeek = taskWeek;
        this.taskDay = taskDay;
        this.taskCompleteness = taskCompleteness;
        this.taskIsDone = taskIsDone;
        this.emotionalAssessmentOfTask = emotionalAssessmentOfTask;
        this.photoUrl = photoUrl;
    }

    public TaskEntry(int id, String timePeriod, String taskTitle, String taskDescription, int taskYear, int taskMonth, int taskWeek, int taskDay, int taskCompleteness, int taskIsDone, int emotionalAssessmentOfTask, String photoUrl) {
        this.id = id;
        this.timePeriod = timePeriod;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        //this.taskSettingDate = taskSettingDate;
        this.taskYear = taskYear;
        this.taskMonth = taskMonth;
        this.taskWeek = taskWeek;
        this.taskDay = taskDay;
        this.taskCompleteness = taskCompleteness;
        this.taskIsDone = taskIsDone;
        this.emotionalAssessmentOfTask = emotionalAssessmentOfTask;
        this.photoUrl = photoUrl;
    }
    @Ignore
    public TaskEntry() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

//    public Date getTaskSettingDate() {
//        return taskSettingDate;
//    }
//
//    public void setTaskSettingDate(Date taskSettingDate) {
//        this.taskSettingDate = taskSettingDate;
//    }

    public int getTaskYear() {
        return taskYear;
    }

    public void setTaskYear(int taskYear) {
        this.taskYear = taskYear;
    }

    public int getTaskMonth() {
        return taskMonth;
    }

    public void setTaskMonth(int taskMonth) {
        this.taskMonth = taskMonth;
    }

    public int getTaskWeek() {
        return taskWeek;
    }

    public void setTaskWeek(int taskWeek) {
        this.taskWeek = taskWeek;
    }

    public int getTaskDay() {
        return taskDay;
    }

    public void setTaskDay(int taskDay) {
        this.taskDay = taskDay;
    }

    public int getTaskCompleteness() {
        return taskCompleteness;
    }

    public void setTaskCompleteness(int taskCompleteness) {
        this.taskCompleteness = taskCompleteness;
    }

    public int getTaskIsDone() {
        return taskIsDone;
    }

    public void setTaskIsDone(int taskIsDone) {
        this.taskIsDone = taskIsDone;
    }

    public int getEmotionalAssessmentOfTask() {
        return emotionalAssessmentOfTask;
    }

    public void setEmotionalAssessmentOfTask(int emotionalAssessmentOfTask) {
        this.emotionalAssessmentOfTask = emotionalAssessmentOfTask;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
