package com.example.democamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



public class MainActivity extends AppCompatActivity {
    EditText mShareTextEditText;
    private static final int requestCode=15;
    private static final int CAMERA_CONSTANT = 156;
    Bitmap object;
    Button captureImage;
    ImageView imgDisplay;

    EditText txtEmail,txtTitle,txtStart,txtEnd,txtDescription;
    Button submitDate;
    CheckBox allDay;

    TextView startTime, endTime;
    Button btnStart,btnEnd;
    long starttimeMilli,endtimeMilli;


    private View view,vuew1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShareTextEditText = findViewById(R.id.share_edittext);

        imgDisplay =findViewById(R.id.imgCaptured);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        txtTitle=findViewById(R.id.txtTitle);
        txtEmail=findViewById(R.id.txtEmail);
        txtDescription=findViewById(R.id.txtDescription);

        Calendar calendar=Calendar.getInstance();
        allDay=(CheckBox)findViewById(R.id.checkBoxAllDay);
        startTime=findViewById(R.id.txtStartTime);
        endTime=findViewById(R.id.txtEndTime);
        btnStart=findViewById(R.id.btnStartTime);
        btnEnd=findViewById(R.id.btnEndTime);

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1=Calendar.getInstance();
                int hours=calendar.get(Calendar.HOUR_OF_DAY);
                int mins=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format=new SimpleDateFormat("k:mm a");

                        String time=format.format(c.getTime());
                        endtimeMilli = c.getTimeInMillis();
                        endTime.setText(time);
                    }
                },hours,mins,false);
                timePickerDialog.show();

            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1=Calendar.getInstance();
                int hours=calendar.get(Calendar.HOUR_OF_DAY);
                int mins=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format=new SimpleDateFormat("k:mm a");

                        String time=format.format(c.getTime());
                        starttimeMilli = c.getTimeInMillis();
                        startTime.setText(time);
                    }
                },hours,mins,false);
                timePickerDialog.show();
            }
        });



    }

    public void captureImage(View view) {
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        String imgPath = file.getAbsolutePath();
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(cameraIntent,CAMERA_CONSTANT);
        Toast.makeText(getApplicationContext(),"Save in Storage", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_CONSTANT){
            object= (Bitmap) data.getExtras().get("data");
            imgDisplay.setImageBitmap(object);
            BitmapDrawable bitMapDrawable=(BitmapDrawable)imgDisplay.getDrawable();
            Bitmap bitmap=bitMapDrawable.getBitmap();
            FileOutputStream outputStream=null;
            File file=Environment.getExternalStorageDirectory();
            File dir=new File(file.getAbsolutePath()+"/PicturesAndroidClass");
            dir.mkdir();
            String fileName=String.format("%d.png",System.currentTimeMillis());
            File outFile=new File(dir,fileName);
            try {
                    outputStream=new FileOutputStream(outFile);
            }catch (Exception e){
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            try {
                outputStream.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }



        }
    }
    public void addEvent(View view) {
        if (!txtTitle.getText().toString().isEmpty() && !txtDescription.toString().isEmpty() && !txtEmail.toString().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_INSERT);

            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE,txtTitle.getText().toString());
            intent.putExtra(Intent.EXTRA_EMAIL,txtEmail.getText().toString());
            intent.putExtra(CalendarContract.Events.DESCRIPTION,txtDescription.getText().toString());
            if(allDay.isChecked()){
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
            }
            else{
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, starttimeMilli);
//                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
            }



//            int EVENT_BEGIN_TIME_IN_MILLIS = Calendar.getInstance().timeInMillis;
//            int EVENT_END_TIME_IN_MILLIS = Calendar.getInstance().add(Calendar.HOUR_OF_DAY, 2).timeInMillis

//            val insertCalendarIntent = Intent(Intent.ACTION_INSERT)
//                    .setData(CalendarContract.Events.CONTENT_URI)
//                    .putExtra(CalendarContract.Events.TITLE, "TITLE") // Simple title
//                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
//                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,startCalendarInstance) // Only date part is considered when ALL_DAY is true; Same as DTSTART
//                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTimeInMillis) // Only date part is considered when ALL_DAY is true
//                    .putExtra(CalendarContract.Events.EVENT_LOCATION, "Hong Kong")
//                    .putExtra(CalendarContract.Events.DESCRIPTION, "DESCRIPTION") // Description
//                    .putExtra(Intent.EXTRA_EMAIL, "fooInviteeOne@gmail.com,fooInviteeTwo@gmail.com")
//                    .putExtra(CalendarContract.Events.RRULE, getRRule()) // Recurrence rule
//                    .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
//                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE)
//            startActivity(insertCalendarIntent)

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else
            {
                Toast.makeText(MainActivity.this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(MainActivity.this, "Please fill all the fields",
                    Toast.LENGTH_SHORT).show();
        }

    }


    public void shareText(View view) {

        String txt = mShareTextEditText.getText().toString();
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share this text with: ")
                .setText(txt)
                .startChooser();
    }
}