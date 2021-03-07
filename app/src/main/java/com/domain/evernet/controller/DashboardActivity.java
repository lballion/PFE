package com.domain.evernet.controller;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.domain.evernet.R;

import java.io.IOException;

import static com.domain.evernet.controller.MainActivity.PREF_PSEUDO;
import static com.domain.evernet.controller.MainActivity.getDefaults;

public class DashboardActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private String pseudo; //Current user pseudo
    private SharedPreferences preferences;
    private Spinner contactSpinner; //Spinner to choose the contact
    //private TextView pseudoView;

    private String messageToSend = "Hello World !";

    private TextView displayPseudo; //TextView to display the user's pseudo
    private ImageButton loadImage;
    private ImageButton sendButton;

    private Button contactButton;
    private Button imageButton;

    private ImageView displayLoadImage;
    private String dest;

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int SELECT_PICTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dashboard);

        //Display user pseudo on the main window
        pseudo = getDefaults(PREF_PSEUDO, getApplicationContext());


        if (pseudo == null) {
            Intent dashboardActivityIntent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(dashboardActivityIntent);
            finish();
        } else {
            // sinon on dans la DashboardActivity
        }

        pseudo = '@' + pseudo;


        displayPseudo = findViewById(R.id.viewPseudo);
        displayPseudo.setText(pseudo);

        imageButton = findViewById(R.id.imageButton);



        sendButton = findViewById(R.id.sendButton);
        displayLoadImage = (ImageView) findViewById(R.id.displayLoadPicture);

        contactSpinner = findViewById(R.id.contactSpinner);

        loadImage = findViewById(R.id.loadButton);
        findViewById(R.id.loadButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                setResult(1, intent);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        contactButton = findViewById(R.id.contactButton);
      /*  contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactActivityIntent = new Intent(DashboardActivity.this, AddContactActivity.class);
                startActivity(contactActivityIntent);
            }
        });*/

        resetDisplay();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                selectedImageUri = data == null ? null : selectedImage;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                displayLoadImage.setImageBitmap(bitmap);
                Toast.makeText(getBaseContext(), "Image load from the phone !", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Send an SMS to a phone number, after will be used to send an image to the contect choose in the spinner
    public void sendMessage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 2);
            }
        }

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
            if (messageToSend != null)
                //!!!!!! Add your phone number here !!!!!!
                smsMgr.sendTextMessage("", null, messageToSend, sentPI, null);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(getBaseContext(), "Image send to : " + dest, Toast.LENGTH_LONG).show();
    }

    //make everything invisible at the start off the main window
    public void resetDisplay() {
        loadImage.setVisibility(View.INVISIBLE);
        sendButton.setVisibility(View.INVISIBLE);
        displayLoadImage.setVisibility(View.INVISIBLE);
        contactSpinner.setVisibility(View.INVISIBLE);

    }

    //Load image page invisible
    public void setSendImageInvisible(View view) {
        loadImage.setVisibility(View.INVISIBLE);
        sendButton.setVisibility(View.INVISIBLE);
        displayLoadImage.setVisibility(View.INVISIBLE);
        contactSpinner.setVisibility(View.INVISIBLE);

        imageButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
        contactButton.setBackgroundColor(getResources().getColor(R.color.blue));

    }

    //Load image page visible
    public void setSendImageVisible(View view) {
        loadImage.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.VISIBLE);
        displayLoadImage.setVisibility(View.VISIBLE);
        contactSpinner.setVisibility(View.VISIBLE);

        imageButton.setBackgroundColor(getResources().getColor(R.color.blue));
        contactButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = new String(contactSpinner.getSelectedItem().toString());
        String[] idItems = getResources().getStringArray(R.array.phoneArray);


        if (selectedItem.equals(idItems[0])) {
            dest = "contact 1";
        }
        if (selectedItem.equals(idItems[1])) {
            dest = "contact 2";
        }
        if (selectedItem.equals(idItems[2])) {
            dest = "contact 3";
        }
        if (selectedItem.equals(idItems[3])) {
            dest = "contact 4";
        }

        Toast.makeText(getBaseContext(), "Contact selected :," + dest, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        contactSpinner.setSelection(0);
    }

    //Launch the exit windows when the user want to leave the app
    public void launchExitDialog(View view) {
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.show(getSupportFragmentManager(), "Exit");
    }

}


