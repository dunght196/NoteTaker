package dunght.example.com.notetaker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dunght.example.com.notetaker.config.Define;
import dunght.example.com.notetaker.service.NoteService;

public class NoteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String titleNote = intent.getExtras().getString(Define.TITLE_NOTE);
        Intent intent1 = new Intent(context, NoteService.class);
        intent1.putExtra(Define.TITLE_NOTE, titleNote);
        context.startService(intent1);
    }
}
