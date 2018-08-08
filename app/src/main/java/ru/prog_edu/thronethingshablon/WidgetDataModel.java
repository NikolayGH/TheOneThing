package ru.prog_edu.thronethingshablon;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

class WidgetDataModel {
    private  static final String PREF_KEY_TASKS = "tasksData";

    public static Set<String> getDataFromSharedPrefs(Context context){
        Set<String> list = new HashSet<>();
        if(list.isEmpty()){
            SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
            list = sharedPreferences.getStringSet(PREF_KEY_TASKS, list);
        }
        return list;
    }
}
