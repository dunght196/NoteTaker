package dunght.example.com.notetaker.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import dunght.example.com.notetaker.R;
import dunght.example.com.notetaker.activity.base.MainActivity;
import dunght.example.com.notetaker.config.Define;

public class NoteService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String title = intent.getExtras().getString(Define.TITLE_NOTE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.tb_note);
        builder.setContentTitle("Note notification");
        builder.setContentText(title);
        builder.setAutoCancel(true);

        Intent intentAction = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intentAction}, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(100, builder.build());


        return START_NOT_STICKY;
    }
}
