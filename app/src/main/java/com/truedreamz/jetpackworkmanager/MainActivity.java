package com.truedreamz.jetpackworkmanager;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.truedreamz.jetpackworkmanager.worker.NotificationWorker;
import com.truedreamz.jetpackworkmanager.worker.PeriodicToastWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data = new Data.Builder()
                .putString(NotificationWorker.TASK_DESC, "The data passed from MainActivity")
                .build();

        final OneTimeWorkRequest oneTimeWorkRequest=new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(data)
                .build();

        findViewById(R.id.buttonTap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            }
        });

        //Getting the TextView
        final TextView textView = findViewById(R.id.textViewStatus);

        //Listening to the work status
        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        //receiving back the data
                        if(workInfo != null && workInfo.getState().isFinished())
                            textView.append(workInfo.getOutputData().getString(NotificationWorker.TASK_DESC) + "\n");

                        //Displaying the status into TextView
                        textView.append(workInfo.getState().name() + "\n");
                    }
                });

        // Create the actual work object:
        final PeriodicWorkRequest notificationWorkerRequest = new PeriodicWorkRequest.Builder(PeriodicToastWorker.class, 1,
                TimeUnit.HOURS).build();

        findViewById(R.id.buttonPeriodic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Then enqueue the recurring task:
                WorkManager.getInstance().enqueue(notificationWorkerRequest);
            }
        });

        //Listening to the work status
        WorkManager.getInstance().getWorkInfoByIdLiveData(notificationWorkerRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        //Displaying the status into TextView
                        textView.append(workInfo.getState().name() + "\n");
                    }
                });

        findViewById(R.id.buttonClearAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstance().cancelAllWork();
            }
        });

    }
}
