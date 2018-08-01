package dunght.example.com.notetaker.activity.base;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import dunght.example.com.notetaker.R;
import dunght.example.com.notetaker.config.Define;
import dunght.example.com.notetaker.config.OnClickInterface;
import dunght.example.com.notetaker.custom.adapter.PhotoAdapter;
import dunght.example.com.notetaker.db.db.table.NoteData;
import dunght.example.com.notetaker.db.db.table.PhotoData;
import dunght.example.com.notetaker.model.Note;
import dunght.example.com.notetaker.receiver.NoteReceiver;
import petrov.kristiyan.colorpicker.ColorPicker;

public class EditNoteActivity extends AppCompatActivity implements OnClickInterface {

    private android.support.v7.widget.Toolbar toolbar;
    private TextView tvDatetime, tvDate, tvTime, tvTitle;
    private ImageView ivBackTB, ivBackItem, ivShareItem, ivDeleteItem, ivNextItem;
    private EditText etTitle, etNote;
    private GridView gvPhoto;
    private Calendar calendar;
    private int day, month, year, hour, minute;
    private int colorNote = -1;
    private int  posNote;
    private ArrayList<String> listPhoto;
    private ArrayList<Note> listNote;
    private PhotoAdapter photoAdapter;
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        toolbar = (Toolbar)findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        gvPhoto = (GridView)findViewById(R.id.gv_photo);
        tvDatetime = (TextView)findViewById(R.id.tv_date_time);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvTime = (TextView)findViewById(R.id.tv_time);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        etTitle = (EditText)findViewById(R.id.et_title);
        etNote = (EditText)findViewById(R.id.et_note);
        ivBackTB = (ImageView)findViewById(R.id.iv_back_tb);
        ivBackItem = (ImageView)findViewById(R.id.iv_back_item);
        ivShareItem = (ImageView)findViewById(R.id.iv_share_item);
        ivDeleteItem = (ImageView)findViewById(R.id.iv_delete_item);
        ivNextItem = (ImageView)findViewById(R.id.iv_next_item);

        posNote = getIntent().getIntExtra(Define.POSITION,0);

        listNote = NoteData.Instance(this).getAllNote();

        calendar = Calendar.getInstance();
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        initDatetime();

        detailItem(posNote);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year + " • ");
                        calendar.set(Calendar.YEAR, view.getYear());
                        calendar.set(Calendar.MONTH, view.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tvTime.setText(hourOfDay + ":" + minute);
                        calendar.set(Calendar.HOUR_OF_DAY, view.getCurrentHour());
                        calendar.set(Calendar.MINUTE, view.getCurrentMinute());
                        calendar.set(Calendar.SECOND, 0);
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });

        ivBackTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        ivNextItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedataPhoto(listNote.get(posNote).getId());
                posNote++;
                detailItem(posNote);

            }
        });

        ivBackItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedataPhoto(listNote.get(posNote).getId());
                posNote--;
                detailItem(posNote);
            }
        });

        ivDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm(listNote.get(posNote).getId());
                NoteData.Instance(getApplicationContext()).delete(listNote.get(posNote));
                PhotoData.Instance(getApplicationContext()).delete(listNote.get(posNote).getId());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        ivShareItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString().trim();
                String content = etNote.getText().toString().trim();
                Intent intentSharing = new Intent(Intent.ACTION_SEND);
                intentSharing.setType("text/plain");
                intentSharing.putExtra(Intent.EXTRA_TEXT, title + "\n" + content);
                startActivity(Intent.createChooser(intentSharing, "Share with"));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_edit_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Define.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() != null) {
            Uri uri = data.getData();
            listPhoto.add(uri.toString());
            photoAdapter.notifyDataSetChanged();
        }else if(requestCode == Define.PICK_CAMERA_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() != null) {
            Uri uri = data.getData();
            listPhoto.add(uri.toString());
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_done:
                saveNote();
                break;

            case R.id.tb_color:
                ColorPicker colorPicker = new ColorPicker(EditNoteActivity.this);
                colorPicker.show();
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        getWindow().getDecorView().setBackgroundColor(color);
                        colorNote = color;
                    }

                    @Override
                    public void onCancel() {
                    }
                });

                break;

            case R.id.tb_camera:
                final Dialog dialog = new Dialog(EditNoteActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_insert_picture);
                LinearLayout llTakePhoto = (LinearLayout)dialog.findViewById(R.id.ll_take_picture);
                LinearLayout llChoosePhoto = (LinearLayout)dialog.findViewById(R.id.ll_choose_picture);
                dialog.show();

                llTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, Define.PICK_CAMERA_REQUEST);
                        dialog.cancel();
                    }
                });

                llChoosePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , Define.PICK_IMAGE_REQUEST);
                        dialog.cancel();
                    }
                });

                break;

            case R.id.newnote:
                startActivity(new Intent(getApplicationContext(), AddNoteActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initDatetime() {
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    @Override
    public void click(final int pos) {
        final Dialog dialog = new Dialog(EditNoteActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);;
        dialog.setTitle("Confirm Delete");
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();

        TextView tvOk = (TextView)dialog.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView)dialog.findViewById(R.id.tv_cancel);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPhoto.remove(pos);
                photoAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    public void cancelAlarm(int requestCode) {
        Intent myIntent = new Intent(getApplicationContext(), NoteReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    public void detailItem(int pos) {
        tvTitle.setText(listNote.get(pos).getTitle());
        tvDatetime.setText(listNote.get(pos).getDatetimeCreate());
        tvDate.setText(listNote.get(pos).getDateAlarm());
        tvTime.setText(listNote.get(pos).getTimeAlarm());
        etTitle.setText(listNote.get(pos).getTitle());
        etNote.setText(listNote.get(pos).getContent());
        getWindow().getDecorView().setBackgroundColor(listNote.get(pos).getColor());

        listPhoto = PhotoData.Instance(this).getAllPhotoByIDNote(listNote.get(pos).getId());
        photoAdapter = new PhotoAdapter(this,this, listPhoto);
        gvPhoto.setAdapter(photoAdapter);

//        updateGVPhoto(pos);

        if(listNote.size() == 1) {
            ivBackItem.setAlpha(0.1f);
            ivNextItem.setAlpha(0.1f);
            ivNextItem.setEnabled(false);
            ivBackItem.setEnabled(false);
        }else if(pos == 0 && listNote.size() > 1) {
            ivBackItem.setAlpha(0.1f);
            ivNextItem.setAlpha(1f);
            ivBackItem.setEnabled(false);
            ivNextItem.setEnabled(true);
        }else if(pos == listNote.size()-1 && listNote.size() > 1) {
            ivNextItem.setAlpha(0.1f);
            ivBackItem.setAlpha(1f);
            ivNextItem.setEnabled(false);
            ivBackItem.setEnabled(true);
        }else {
            ivNextItem.setAlpha(1f);
            ivBackItem.setAlpha(1f);
            ivNextItem.setEnabled(true);
            ivBackItem.setEnabled(true);
        }
    }

    public void changedataPhoto(int id) {
        PhotoData.Instance(this).delete(id);
        for(String s : listPhoto) {
            PhotoData.Instance(this).add(s, listNote.get(posNote).getId());
        }
    }

//    public void updateGVPhoto(final int pos) {
//        new Thread(){
//            @Override
//            public void run() {
//                listPhoto = PhotoData.Instance(getApplicationContext()).getAllPhotoByIDNote(listNote.get(pos).getId());
//                handler.sendEmptyMessage(0);
//            }
//        }.start();
//    }

    public void saveNote() {
        String title = etTitle.getText().toString();
        String content = etNote.getText().toString();
        String dateAlarm = tvDate.getText().toString();
        String timeAlarm = tvTime.getText().toString();

        Note note = listNote.get(posNote);
        changedataPhoto(note.getId());

        if(!note.getTimeAlarm().equals(timeAlarm)) {
            Intent intent = new Intent(EditNoteActivity.this, NoteReceiver.class);
            intent.putExtra(Define.TITLE_NOTE, title);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(), note.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            );
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        note.setTitle(title);
        note.setContent(content);
        note.setDateAlarm(dateAlarm);
        note.setTimeAlarm(timeAlarm);
        note.setColor(colorNote);
        NoteData.Instance(this).update(note);

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }
}
