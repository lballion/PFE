package com.domain.evernet.controller;

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
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.domain.evernet.R;
import com.domain.evernet.model.Client;
import com.domain.evernet.model.FileManager;
import com.domain.evernet.model.Packet;
import com.domain.evernet.model.ReadWriteFile;
import com.domain.evernet.model.Contact;
import com.domain.evernet.model.PhoneBook;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.domain.evernet.controller.MainActivity.PREF_PSEUDO;
import static com.domain.evernet.controller.MainActivity.getDefaults;

public class DashboardActivity extends AppCompatActivity  implements ImagePickFragment.ImagePickFragmentListener, ContactManagerFragment.ContactManagerFragmentListener {

    private ImagePickFragment imageFragment;
    private ContactManagerFragment addContactFragment;

    private FragmentManager fragmentManager;
    private FrameLayout fragmentDisplay;

    private Button imageButton;
    private Button contactButton;

    private String pseudo; //Current user pseudo
    private SharedPreferences preferences;

    private String messageToSend = "Hello World !";

    private TextView displayPseudo; //TextView to display the user's pseudo

    private String dest = "0758107468";
    private int destId;

    private PhoneBook phoneBook;
    private ReadWriteFile readWriteFile = new ReadWriteFile();

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int SELECT_PICTURE = 1;

    private final String PHONEBOOK_FILE_NAME = "savedPhoneBook.txt";

    private FileManager fileManager = null;
    private Bitmap finalBitmap = null;
    private static DashboardActivity dashboardActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Check and ask for permissions to the user, will display pop up
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS,  Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.INTERNET},
                    0);
            onCreate(savedInstanceState);
        }
        else {
            setContentView(R.layout.activity_dashboard);

            Intent intent = getIntent();

            if(intent.hasExtra("id") && intent.hasExtra("contact")){
                Bundle bundle = intent.getExtras();
                if(!bundle.getString("contact").equals(null)){
                    this.dest = bundle.getString("contact");
                }
                this.destId = bundle.getInt("id");
                Toast.makeText(getBaseContext(), "Contact selected : " + this.dest, Toast.LENGTH_LONG).show();

            }

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

            fragmentDisplay = findViewById(R.id.fragmentDisplay);

            imageFragment = new ImagePickFragment();
            addContactFragment = new ContactManagerFragment();

            fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(fragmentDisplay.getId(), imageFragment)
                    .commit();

            imageButton = findViewById(R.id.imageButton);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendImgButtonMenuClick();
                }
            });

            contactButton = findViewById(R.id.contactButton);
            contactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addContactButtonMenuClick();
                }
            });

            phoneBook = loadPhoneBookFromFile(PHONEBOOK_FILE_NAME);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        dashboardActivity = this;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                selectedImageUri = data == null ? null : selectedImage;
                fileManager=new FileManager();
                finalBitmap= fileManager.getResizedBitmap(this.getContentResolver(),selectedImageUri);
               // imageFragment.setImage(finalBitmap);
                Toast.makeText(getBaseContext(), " "+fileManager.getSizeOfBytesArray(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public  ArrayList<Contact> changeContactMapIntoList(Map<Integer, Contact> contactMap){
        ArrayList<Contact> contactItemList = new ArrayList<Contact>();

        for (Contact i : contactMap.values()){
            contactItemList.add(i);
        }

        return contactItemList;

    }

    public void launchContactList(){
        Map<Integer, Contact> contactMap =  this.phoneBook.getContacts();

        ArrayList<Contact> contactItemList = changeContactMapIntoList(contactMap);
        contactItemList.add(new Contact(1254,"test"));
        contactItemList.add(new Contact(1244,"test2"));


        Intent contactListActivity = new Intent(DashboardActivity.this, DisplayContact.class);
        //contactListActivity.putParcelableArrayListExtra("contact",contactItemList);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("contact",contactItemList);
        contactListActivity.putExtras(bundle);

        startActivity(contactListActivity);

    }

    //Send an SMS to a phone number, after will be used to send an image to the contact chosen in the spinner
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
                            //Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_LONG).show();
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
                smsMgr.sendTextMessage(this.dest, "", messageToSend, sentPI, null);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClickSent() {

        sendAllFragments(findViewById(R.id.dashboard_root));
    }

    private void addContactButtonMenuClick(){

        fragmentManager.beginTransaction()
                .replace(fragmentDisplay.getId(), addContactFragment)
                .commit();

        contactButton.setBackgroundColor(getResources().getColor(R.color.blue));
        imageButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
    }

    private void sendImgButtonMenuClick(){

        fragmentManager.beginTransaction()
                .replace(fragmentDisplay.getId(), imageFragment)
                .commit();

        contactButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
        imageButton.setBackgroundColor(getResources().getColor(R.color.blue));
    }

    @Override
    public void saveEvent(String name, String id) {

        String newContactData = id + "," + name + "\n";
        System.out.println(id + "," + name );
        Contact newContact = new Contact(Integer.parseInt(id), name);
        phoneBook.addContact(newContact);
        readWriteFile.writeToFile(newContactData,getApplicationContext(),PHONEBOOK_FILE_NAME,MODE_APPEND);
        phoneBook = loadPhoneBookFromFile(PHONEBOOK_FILE_NAME);
    }

    @Override
    public void rstContact() {

        File dir = getFilesDir();
        File file = new File(dir, PHONEBOOK_FILE_NAME);
        boolean delete = file.delete();

        System.out.println("File deletion = " + delete);
    }

    public PhoneBook loadPhoneBookFromFile(String fileName){

        PhoneBook savedPhoneBook = new PhoneBook();
        String data;
        data = readWriteFile.readFromFile(getApplicationContext(), fileName);

        String[] contactEntries;

        String name;
        String id;

        for(String savedContact : data.split("\n")){

            contactEntries = savedContact.split(",");
            if(contactEntries.length <2){
                continue;
            }
            id = contactEntries[0];
            name = contactEntries[1];
            System.out.println("Contact Entries : " + id + "," + name);
            savedPhoneBook.addContact(new Contact(Integer.parseInt(id),name));
        }

        return savedPhoneBook;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendAllFragments(View view) {
        this.dest="0758107468";
        fileManager.setMaxOfCharsToSendBySms(100);
        int ttl = 2;
        while (!fileManager.allFragmentsHaveBeenRecovered()) {
            String fragment=fileManager.getNextFragment();
            int position=fileManager.getPosOfThisFragment();
            int nb_packets=fileManager.getNbPackets();
            Packet p=new Packet(getMyPhonenumber(),"0605831895", position,nb_packets,ttl, fileManager.getNameOfPicture(), fragment);
            messageToSend=p.getPacket();
            sendMessage(view); // function to use without callBack server
            //  sendTo(p.getPacket(),null); //function with callBack server

        }
    }

    private String getMyPhonenumber() {
        return "0758107468";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void sendTo(String sms,String dest) {
        messageToSend=sms;
        if(dest==null){
            Set<String> allTargets= this.getIntermediatesNumbers(""+4);
            if (allTargets.size()== 0) {
                this.sendMessage(null);
            } else {
                for (String target : allTargets) {
                    this.dest=target;
                    sendMessage(null);

                }
            }
        } else {
            this.dest=dest;
            sendMessage(null);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Set<String> getIntermediatesNumbers(String size) {
        InetAddress i = null;
        try {
            i = InetAddress.getByName("109.215.55.162");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Client c = new Client(i, 50000);
        c.openSocket();
        HashMap<String, String> numbersList=new HashMap<>();

        try {
            numbersList=c.getPhoneNumList(size);
            c.closeSocket();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return numbersList.keySet();
    }

    public static DashboardActivity instance() {
        return dashboardActivity;
    }

}


