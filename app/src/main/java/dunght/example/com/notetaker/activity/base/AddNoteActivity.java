package dunght.example.com.notetaker.activity.base;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dunght.example.com.notetaker.R;
import dunght.example.com.notetaker.config.Define;
import dunght.example.com.notetaker.controller.OnClickInterface;
import dunght.example.com.notetaker.custom.adapter.PhotoAdapter;
import dunght.example.com.notetaker.db.db.table.NoteData;
import dunght.example.com.notetaker.db.db.table.PhotoData;
import dunght.example.com.notetaker.model.Note;
import dunght.example.com.notetaker.receiver.NoteReceiver;
import petrov.kristiyan.colorpicker.ColorPicker;

public class AddNoteActivity extends AppCompatActivity implements OnClickInterface{

    private android.support.v7.widget.Toolbar toolbar;
    private TextView tvDatetime, tvDate, tvTime;
    private ImageView ivBack;
    private EditText etTitle, etNote;
    private GridView gvPhoto;
    private Calendar calendar;
    private int day, month, year, hour, minute;
    private int colorNote = -1;
    private ArrayList<String> listPhoto;
    private PhotoAdapter photoAdapter;
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        gvPhoto = (GridView)findViewById(R.id.gv_photo);
        tvDatetime = (TextView)findViewById(R.id.tv_date_time);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvTime = (TextView)findViewById(R.id.tv_time);
        etTitle = (EditText)findViewById(R.id.et_title);
        etNote = (EditText)findViewById(R.id.et_note);
        ivBack = (ImageView)findViewById(R.id.iv_back);

        tvDatetime.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm ").format(new Date()));

        calendar = Calendar.getInstance();
        initDatetime();

        tvDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()) + " • ");
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        tvTime.setText(new SimpleDateFormat("hh:mm a").format(calendar.getTime()));
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
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


        listPhoto = new ArrayList<>();
        photoAdapter = new PhotoAdapter(this,this, listPhoto);
        gvPhoto.setAdapter(photoAdapter);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tb_done:
                saveNote();
                break;

            case R.id.tb_color:
                ColorPicker colorPicker = new ColorPicker(AddNoteActivity.this);
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
                final Dialog dialog = new Dialog(AddNoteActivity.this);
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
        final Dialog dialog = new Dialog(AddNoteActivity.this);
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

    public void saveNote() {
        String title = etTitle.getText().toString();
        String content = etNote.getText().toString();
        String datetimeCreate = tvDatetime.getText().toString();
        String dateAlarm = tvDate.getText().toString();
        String timeAlarm = tvTime.getText().toString();

        Note note = new Note(title, content, colorNote, datetimeCreate, dateAlarm, timeAlarm);
        note.setListImage(listPhoto);
        NoteData.Instance(this).add(note);

        for(int i=0;i<note.getListImage().size();i++){
            PhotoData.Instance(this).add(note.getListImage().get(i), NoteData.Instance(this).getLastId());
        }

        Define.REQUEST_CODE = NoteData.Instance(this).getLastId();

        Intent intent = new Intent(AddNoteActivity.this, NoteReceiver.class);

        intent.putExtra(Define.TITLE_NOTE, title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), Define.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }
}
