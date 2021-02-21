package com.example.evernet;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    
    public String dst;
    private String messageToSend; // SMS content
    private Spinner spinner;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int SELECT_PICTURE = 1;
    public ImageView img;

    //https://stackoverflow.com/questions/3875354/android-sms-message-delivery-report-intent
    private void sendSms() {
        try {
            String SENT = "SMS_SENT";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1) {
                    int resultCode = getResultCode();
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            SmsManager smsMgr = SmsManager.getDefault();
            if(messageToSend != null)
                smsMgr.sendTextMessage(dst, null, messageToSend, sentPI, null);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            //Toast.makeText(getBaseContext(), dst, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = new String(spinner.getSelectedItem().toString());
        String[] idItems = getResources().getStringArray(R.array.idArray);

        if(selectedItem.equals(idItems[0]))
            dst = "0638861404";
        if(selectedItem.equals(idItems[1]))
            dst = "0638861404";
        if(selectedItem.equals(idItems[2]))
            dst = "0638861404";
        if(selectedItem.equals(idItems[3]))
            dst = "0638861404";

        //Toast.makeText(getBaseContext(), selectedItem, Toast.LENGTH_SHORT).show();
        FloatingActionButton fab = findViewById(R.id.fab);
        findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.plain_text_input);
                messageToSend = editText.getText().toString();
                sendSms();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Toast.makeText(getBaseContext(), "rien", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        Button loadImage = (Button) findViewById(R.id.button);
        //Toast.makeText(getBaseContext(),"on create",Toast.LENGTH_LONG).show();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                setResult(1,intent);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });
    }

    //https://stackoverflow.com/questions/38471963/setimagebitmap-not-displaying
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                selectedImageUri = data == null ? null : selectedImage;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap); //trying bitmap
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}