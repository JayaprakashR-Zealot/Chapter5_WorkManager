package com.truedreamz.jetpackworkmanager.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.truedreamz.jetpackworkmanager.R;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Created by Jayaprakash on 2/18/2019.
 */
public class NotificationWorker extends Worker {

    public static final String TASK_DESC = "Desc";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        //getting the input data
        String taskDesc = getInputData().getString(TASK_DESC);
        displayNotification("Hello", "Welcome to my world!"+"\n"+taskDesc);

        //setting output data
        Data data = new Data.Builder()
                .putString(TASK_DESC, "The notification is shown.")
                .build();

        setOutputData(data);

        return Result.SUCCESS;
    }

    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("truedreamz", "truedreamz", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "truedreamz")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }
}
