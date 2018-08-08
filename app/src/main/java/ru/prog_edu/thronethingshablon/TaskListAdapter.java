package ru.prog_edu.thronethingshablon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import ru.prog_edu.thronethingshablon.database.AppDataBase;
import ru.prog_edu.thronethingshablon.database.TaskEntry;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>  {

    private List<TaskEntry> mTaskEntries;
    private Context mContext;
    final private ItemClickListener itemClickListener;

    public TaskListAdapter(Context context, ItemClickListener itemClickListener){
        this.mContext = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        TaskEntry taskEntry = mTaskEntries.get(position);
        String titleTask = taskEntry.getTaskTitle();
        String descriptionTask = taskEntry.getTaskDescription();
        String dateTask = taskEntry.getTaskDay() + "/" + (taskEntry.getTaskMonth() + 1) + "/" + taskEntry.getTaskYear() + "/";
        int isDone = taskEntry.getTaskIsDone();
        if(isDone==1){
            holder.doneUndoneTaskButton.setSelected(true);
        }else{
            holder.doneUndoneTaskButton.setSelected(false);
        }

        int taskCompleteness = taskEntry.getTaskCompleteness();
        int emotionalAssessmentOfTask = taskEntry.getEmotionalAssessmentOfTask();
        switch (emotionalAssessmentOfTask){
            case 1: holder.emotionsImage.setImageResource(R.drawable.circular_face_frown_button_selected);
            break;
            case 2: holder.emotionsImage.setImageResource(R.drawable.circular_face_meh_button_selected);
            break;
            case 3: holder.emotionsImage.setImageResource(R.drawable.circular_face_smile_button_selected);
            break;
        }

        holder.titleTaskTextView.setText(titleTask);
        holder.descriptionTaskTextView.setText(descriptionTask);
        holder.dateTaskTextView.setText(dateTask);
        String taskCompletenessString = ""+taskCompleteness+"%";
        holder.completionTaskTextView.setText(taskCompletenessString);
    }

    @Override
    public int getItemCount() {
        if (mTaskEntries == null) {
            return 0;
        }
        return mTaskEntries.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView titleTaskTextView;
        TextView descriptionTaskTextView;
        TextView dateTaskTextView;
        TextView completionTaskTextView;
        ImageView emotionsImage;
        Button doneUndoneTaskButton;

        TaskViewHolder(View itemView) {
            super(itemView);

            titleTaskTextView = itemView.findViewById(R.id.tv_title_in_item);
            descriptionTaskTextView = itemView.findViewById(R.id.tv_item_description);
            dateTaskTextView = itemView.findViewById(R.id.tv_date_in_item);
            completionTaskTextView = itemView.findViewById(R.id.tv_completion_in_item);
            emotionsImage = itemView.findViewById(R.id.img_emotions_on_item);
            doneUndoneTaskButton = itemView.findViewById(R.id.btn_done_in_item);
            doneUndoneTaskButton.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId()==doneUndoneTaskButton.getId()){
                doneUndoneTaskButton.setSelected(!doneUndoneTaskButton.isSelected());
                TaskEntry task = mTaskEntries.get(getAdapterPosition());

                AppDataBase myDb;
                myDb = AppDataBase.getInstance(v.getContext().getApplicationContext());

                if(task!=null){
                    if(doneUndoneTaskButton.isSelected()){
                        task.setTaskIsDone(1);
                    }else{
                        task.setTaskIsDone(0);
                    }
                    myDb.taskDao().updateTask(task);
                }
            }

            int elementId = mTaskEntries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(elementId);
        }
    }

    public void setTasks(List<TaskEntry> taskEntries) {
        mTaskEntries = taskEntries;
        notifyDataSetChanged();
    }

}
