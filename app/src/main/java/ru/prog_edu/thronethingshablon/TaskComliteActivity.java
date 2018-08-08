package ru.prog_edu.thronethingshablon;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import ru.prog_edu.thronethingshablon.database.AppDataBase;
import ru.prog_edu.thronethingshablon.database.TaskEntry;

public class TaskComliteActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private EditText titleTaskEditText;
    private EditText descriptionTaskEditText;
    private TextView percentCompleteness;
    private TextView taskDate;
    private SeekBar seekBar;
    private ImageView addedPhoto;
    File pictureFile = null;
    private String pictureFilePath;
    static final int REQUEST_PICTURE_CAPTURE = 18;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;

    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private int year;
    private int month;
    private int week;
    private int day;
    private Date date = new Date();

    private Calendar calendar = Calendar.getInstance();

    private Button faceFrownButton;
    private Button faceMehButton;
    private Button faceSmileButton;
    private Button doneButton;

    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String USER_ID = "user_id";
    private String userId;

    private TaskEntry currentTask;

    private String TIME_PERIOD = TIME_PERIOD_DAY;
    private String TIME_PERIOD_KEY = "time_period_key";

    private static final String TIME_PERIOD_DAY = "day";
    public static final String TIME_PERIOD_WEEK = "week";
    public static final String TIME_PERIOD_MONTH = "month";
    public static final String TIME_PERIOD_YEAR = "year";

    private String photoUrl = "";

    private AppDataBase myDb;
    public static final String EXTRA_TASK_ID = "extraTaskId";
    private static final int DEFAULT_TASK_ID = -1;
    private int mTaskId = DEFAULT_TASK_ID;
    private static final String INSTANCE_TASK_ID = "instanceTaskId";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_complete_photo);
        myDb = AppDataBase.getInstance(getApplicationContext());

        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPref.contains(USER_ID)) {
            userId = sharedPref.getString(USER_ID, "");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child(userId).child("Tasks");

        titleTaskEditText = findViewById(R.id.et_title_task);
        descriptionTaskEditText = findViewById(R.id.et_description_task);
        taskDate = findViewById(R.id.tv_date_edit_in_compl);

        percentCompleteness = findViewById(R.id.percent_of_completeness);
        seekBar = findViewById(R.id.seekBar);

        faceFrownButton = findViewById(R.id.btn_face_frown_in_compl);
        faceMehButton = findViewById(R.id.btn_face_meh_in_compl);
        faceSmileButton = findViewById(R.id.btn_face_smile_in_compl);
        doneButton = findViewById(R.id.btn_done_in_compl);
        FloatingActionButton saveEditButton = findViewById(R.id.btn_add_task_in_complitted);
        addedPhoto = findViewById(R.id.imageView);
        Button addPhotoButton = findViewById(R.id.btn_add_photo);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        week = calendar.get(Calendar.WEEK_OF_YEAR);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date = new Date();


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(TIME_PERIOD_KEY)){
            TIME_PERIOD = intent.getStringExtra(TIME_PERIOD_KEY);
            currentTask = new TaskEntry(TIME_PERIOD, titleTaskEditText.getText().toString(), descriptionTaskEditText.getText().toString(),
                    year, month, week, day, 0, 0, 0, "");
            taskDate.setText(day + "/" + (month + 1) + "/" + year);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {

            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                AddTaskViewModelFactory factory = new AddTaskViewModelFactory(myDb, mTaskId);

                // for that use the factory created above AddTaskViewModel
                final AddTaskViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddTaskViewModel.class);

                viewModel.getTask().observe(this, new Observer<TaskEntry>() {
                    @Override
                    public void onChanged(@Nullable TaskEntry taskEntry) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(taskEntry);
                    }
                });
            }
        }

        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doneButton.setSelected(!doneButton.isSelected());
                    if(doneButton.isSelected()){
                        currentTask.setTaskIsDone(1);
                    }else{
                        currentTask.setTaskIsDone(0);
                    }
            }
        });

        faceFrownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceFrownButton.setSelected(!faceFrownButton.isSelected());
                if(faceFrownButton.isSelected()){
                    faceMehButton.setSelected(false);
                    faceSmileButton.setSelected(false);
                    currentTask.setEmotionalAssessmentOfTask(1);
                }else{
                    currentTask.setEmotionalAssessmentOfTask(0);
                }
            }
        });

        faceMehButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceMehButton.setSelected(!faceMehButton.isSelected());
                if(faceMehButton.isSelected()){
                    faceFrownButton.setSelected(false);
                    faceSmileButton.setSelected(false);
                    currentTask.setEmotionalAssessmentOfTask(2);
                }else{
                    currentTask.setEmotionalAssessmentOfTask(0);
                }
            }
        });

        faceSmileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceSmileButton.setSelected(!faceSmileButton.isSelected());
                if(faceSmileButton.isSelected()){
                    faceMehButton.setSelected(false);
                    faceFrownButton.setSelected(false);
                    currentTask.setEmotionalAssessmentOfTask(3);
                }else{
                    currentTask.setEmotionalAssessmentOfTask(0);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(this);

        taskDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskComliteActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                currentTask.setTaskYear(calendar.get(Calendar.YEAR));
                                currentTask.setTaskMonth(calendar.get(Calendar.MONTH));
                                currentTask.setTaskWeek(calendar.get(Calendar.WEEK_OF_YEAR));
                                currentTask.setTaskDay(calendar.get(Calendar.DAY_OF_MONTH));
                                taskDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year + "/");
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textTitle = titleTaskEditText.getText().toString();
                currentTask.setTaskTitle(textTitle);

                String textDescription = descriptionTaskEditText.getText().toString();
                currentTask.setTaskDescription(textDescription);


                currentTask.setPhotoUrl(photoUrl);

                if (mTaskId == DEFAULT_TASK_ID) {
                    // insert new task
                    myDb.taskDao().insertTask(currentTask);
                } else {
                    //update task
                    currentTask.setId(mTaskId);
                    myDb.taskDao().updateTask(currentTask);
                }
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        percentCompleteness.setText(String.valueOf(progress)+" %");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        percentCompleteness.setText(String.valueOf(seekBar.getProgress())+" %");
        currentTask.setTaskCompleteness(seekBar.getProgress());
    }

    @SuppressLint("SetTextI18n")
    private void populateUI(TaskEntry task) {
        if (task == null) {
            return;
        }

        currentTask = task;

        titleTaskEditText.setText(currentTask.getTaskTitle());
        descriptionTaskEditText.setText(currentTask.getTaskDescription());
        percentCompleteness.setText(String.valueOf(currentTask.getTaskCompleteness())+" %");
        seekBar.setProgress(currentTask.getTaskCompleteness());

        if(!currentTask.getPhotoUrl().equals("")){
            addedPhoto.setImageURI(Uri.parse(String.valueOf(currentTask.getPhotoUrl())));
        }


        switch(currentTask.getEmotionalAssessmentOfTask()){
            case 1: faceFrownButton.setSelected(true);
                break;
            case 2: faceMehButton.setSelected(true);
                break;
            case 3: faceSmileButton.setSelected(true);
                break;
        }

        if(currentTask.getTaskIsDone()==1){
            doneButton.setSelected(true);
        }else{
            doneButton.setSelected(false);
        }
        taskDate.setText(currentTask.getTaskDay() + "/" + (currentTask.getTaskMonth() + 1) + "/" + currentTask.getTaskYear() + "/");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureFilePath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ru.prog_edu.android.fileprovider",
                        photoFile);
                pictureFilePath = photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists())            {
                photoUrl=String.valueOf(Uri.fromFile(imgFile));
                addedPhoto.setImageURI(Uri.fromFile(imgFile));
                currentTask.setPhotoUrl(photoUrl);
            }
        }
    }
}
