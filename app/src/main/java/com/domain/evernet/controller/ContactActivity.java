package com.domain.evernet.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.domain.evernet.R;
import com.domain.evernet.model.Contact;
import com.domain.evernet.model.PhoneBook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ContactActivity extends AppCompatActivity {

    private PhoneBook myPhoneBook;

    Button saveContactButton;
    Button rstButton;

    EditText idTextEntry;
    EditText nameTextEntry;
    ListPhoneBook listPhoneBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        saveContactButton = findViewById(R.id.addContactSaveButton);
        rstButton = findViewById(R.id.rstbtn);

        idTextEntry = findViewById(R.id.addContactTextFieldID);
        nameTextEntry = findViewById(R.id.addContactTextFieldName);

        myPhoneBook = new PhoneBook();
        listPhoneBook = new ListPhoneBook();

        loadFromXML("savedPhoneBook.txt");

        listPhoneBook.setPhoneBook(myPhoneBook);

        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = Integer.parseInt(idTextEntry.getText().toString());
                String name = nameTextEntry.getText().toString();
                Contact contactToAdd = new Contact(id, name);
                System.out.println(name);
                saveToXML("savedPhoneBook.txt",contactToAdd);
                idTextEntry.setText("");
                nameTextEntry.setText("");
                loadFromXML("savedPhoneBook.txt");
            }
        });

        rstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File dir = getFilesDir();
                File file = new File(dir, "savedPhoneBook.txt");
                boolean delete = file.delete();

                System.out.println("File deletion = " + delete);
            }
        });
    }

    public void saveToXML(String name, Contact contactToAdd){

        try {
            FileOutputStream saveFile = openFileOutput(name, MODE_APPEND);

            String contactEntry = contactToAdd.getId() + "," + contactToAdd.getName() + "\n";
            byte[] byteArray = contactEntry.getBytes();

            saveFile.write(byteArray);
            System.out.println("Data Write!!");
            saveFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromXML(String fileName){

        try {
            FileInputStream saveFile = openFileInput(fileName);
            Scanner fileReader = new Scanner(saveFile);
            String contact;
            int contactId;
            String contactName;
            String[] contactData;

            while(fileReader.hasNextLine()){

                contact = fileReader.nextLine();
                contactData = contact.split(",");
                contactId = Integer.parseInt(contactData[0]);
                contactName = contactData[1];

                Contact extractedContact = new Contact(contactId, contactName);

                System.out.println("ligne lu : " + contact);

                myPhoneBook.addContact(extractedContact);
            }
            System.out.println("Data Read!!");
            saveFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PhoneBook getMyPhoneBook() {
        return myPhoneBook;
    }
}