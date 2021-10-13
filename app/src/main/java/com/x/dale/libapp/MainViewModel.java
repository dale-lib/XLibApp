package com.x.dale.libapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public final MutableLiveData<List<Person>> liveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        liveData = new MutableLiveData<>();
    }

    public void setData(Person person){
        List<Person> list = liveData.getValue();
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(person);
        liveData.setValue(list);
    }
}
