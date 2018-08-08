package ru.prog_edu.thronethingshablon;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.prog_edu.thronethingshablon.database.AppDataBase;
import ru.prog_edu.thronethingshablon.database.TaskEntry;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class TaskListActivity extends AppCompatActivity implements TaskListAdapter.ItemClickListener {

    private TaskListAdapter mAdpter;
    private AppDataBase myDb;
    private FirebaseAuth mAuth;
    private static DatabaseReference mRef;
    private List<TaskEntry> taskEntriesFromFirebase;

    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String USER_ID = "user_id";
    private static String userId;



    private String TIME_PERIOD_KEY = "time_period_key";
    private static final String TIME_PERIOD_DAY = "day";
    private static final String TIME_PERIOD_WEEK = "week";
    private static final String TIME_PERIOD_MONTH = "month";
    private static final String TIME_PERIOD_YEAR = "year";

    private String timePeriodParameter = TIME_PERIOD_DAY;

    private static List<TaskEntry> taskEntriesforFB = new ArrayList<>();

    private static final String JOB_TAG = "job_tag";
    private FirebaseJobDispatcher firebaseJobDispatcher;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ActionBar toolbar =  this.getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle("");
        }

        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPref.contains(USER_ID)) {
            userId = sharedPref.getString(USER_ID, "");
        }

        RecyclerView mRacyclerView = findViewById(R.id.tasks_recycler);
        mRacyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdpter = new TaskListAdapter(this, this);
        mRacyclerView.setAdapter(mAdpter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRacyclerView.addItemDecoration(decoration);

        myDb = AppDataBase.getInstance(getApplicationContext());

        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        setupViewModelforFB();
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.type_tasks_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NetworkState networkState = new NetworkState(this);
        boolean isOnline = networkState.isOnline();

        int id = item.getItemId();

        switch (id){
            case R.id.year:
                timePeriodParameter = TIME_PERIOD_YEAR;
                setupViewModel();
                return true;

            case R.id.month:
                timePeriodParameter = TIME_PERIOD_MONTH;
                setupViewModel();
                return true;

            case R.id.week:
                timePeriodParameter = TIME_PERIOD_WEEK;
                setupViewModel();
                return true;

            case R.id.day:
                timePeriodParameter = TIME_PERIOD_DAY;
                setupViewModel();
                return true;

            case R.id.firebase_download:
                if(isOnline){
                    downloadFromFirebase();
                    setupViewModel();
                }else{
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.firebase_upload:
                if(isOnline){
                    uploadToFirebase();
                    setupViewModel();
                }else{
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.start_job:

                if(isOnline){
                    Job job = firebaseJobDispatcher.newJobBuilder()
                            .setService(TaskJobService.class)
                            .setLifetime(Lifetime.FOREVER)
                            .setRecurring(true)
                            .setTag(JOB_TAG)
                            .setTrigger(Trigger.executionWindow(10, 15))
                            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                            .setReplaceCurrent(false)
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();
                    firebaseJobDispatcher.mustSchedule(job);
                    Toast.makeText(this, "job run!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.stop_job:

                firebaseJobDispatcher.cancel(JOB_TAG);
                Toast.makeText(this, "job canceled", Toast.LENGTH_LONG).show();
                return true;
            default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private static void uploadToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child(userId).child("Tasks");
        for (int i = 0; i < taskEntriesforFB.size(); i++) {
            mRef.child(String.valueOf(taskEntriesforFB.get(i).getId())).setValue(taskEntriesforFB.get(i));
        }
        
    }

    private void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntry> taskEntries) {
                List<TaskEntry> taskEntriesSelected = new ArrayList<>();
                for (int i = 0; i < taskEntries.size(); i++) {
                    if(taskEntries.get(i).getTimePeriod().equals(timePeriodParameter)){
                        taskEntriesSelected.add(taskEntries.get(i));
                    }
                }
                mAdpter.setTasks(taskEntriesSelected);
            }
        });
    }

    private void setupViewModelforFB(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntry> taskEntries) {

                for (int i = 0; i < taskEntries.size(); i++) {
                        taskEntriesforFB.add(taskEntries.get(i));
                }
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(TaskListActivity.this, TaskComliteActivity.class);
        intent.putExtra(TaskComliteActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }

    private void downloadFromFirebase(){

        mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();
        mRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskEntriesFromFirebase = new ArrayList<>();
                for(DataSnapshot postSnapshot: dataSnapshot.child("Tasks").getChildren() ){
                    TaskEntry post = postSnapshot.getValue(TaskEntry.class);
                    taskEntriesFromFirebase.add(post);
                }

                for (int i = 0; i <taskEntriesFromFirebase.size(); i++) {
                    myDb.taskDao().insertTask(taskEntriesFromFirebase.get(i));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class TaskJobService extends JobService {

        @Override
        public boolean onStartJob(final JobParameters job) {
            uploadToFirebase();
            jobFinished(job, false);
            return true;
        }

        @Override
        public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
            return true;
        }
    }
}
