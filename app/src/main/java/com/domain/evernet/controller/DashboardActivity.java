package com.domain.evernet.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.domain.evernet.R;

import java.io.IOException;

import static com.domain.evernet.controller.MainActivity.PREF_PSEUDO;
import static com.domain.evernet.controller.MainActivity.getDefaults;

public class DashboardActivity extends AppCompatActivity  implements ImagePickFragment.ImagePickFragmentListener {

    ImagePickFragment imageFragment;

    FragmentManager fragmentManager;

    private String pseudo; //Current user pseudo
    private SharedPreferences preferences;
    private Spinner contactSpinner; //Spinner to choose the contact
    //private TextView pseudoView;

    private String messageToSend = "Hello World !";

    private TextView displayPseudo; //TextView to display the user's pseudo
    private ImageButton loadImage;
    private ImageButton sendButton;

    private ContactManagerFragment addContactFragment;

    private ImageView displayLoadImage;
    private String dest;

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int SELECT_PICTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        //Display user pseudo on the main window
        pseudo = '@' + getDefaults(PREF_PSEUDO, getApplicationContext());


        if (pseudo == null) {
            Intent dashboardActivityIntent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(dashboardActivityIntent);
            finish();
        } else {
            // sinon on dans la DashboardActivity
        }


        displayPseudo = findViewById(R.id.viewPseudo);
        displayPseudo.setText(pseudo);



        sendButton = findViewById(R.id.sendButton);
        displayLoadImage = (ImageView) findViewById(R.id.displayLoadPicture);

        contactSpinner = findViewById(R.id.contactSpinner);

        imageFragment = new ImagePickFragment();

        fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentDisplay, imageFragment)
                .commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                selectedImageUri = data == null ? null : selectedImage;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageFragment.setImage(bitmap);
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

    //Launch the exit windows when the user want to leave the app
    public void launchExitDialog(View view) {
        ExitDialog exitDialog = new ExitDialog();
        exitDialog.show(getSupportFragmentManager(), "Exit");
    }

    //Listener on the loadButton of the ImagePickFragment
    @Override
    public void onClickLoad() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        setResult(1, intent);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    //Listener on the sendButton of the ImagePickFragment
    @Override
    public void onClickSent() {
        sendMessage(findViewById(R.id.dashboard_root));
    }

    @Override
    public void onSpinnerSelect(String destination) {
        dest = destination;
        Toast.makeText(getBaseContext(), "Contact selected :," + dest, Toast.LENGTH_LONG).show();
    }

}


