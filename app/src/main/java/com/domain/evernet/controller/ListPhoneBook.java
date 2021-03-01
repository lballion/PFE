package com.domain.evernet.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.domain.evernet.R;
import com.domain.evernet.adapter.PhoneBookAdapter;
import com.domain.evernet.model.Contact;
import com.domain.evernet.model.PhoneBook;

public class ListPhoneBook extends Fragment {

    private PhoneBook phoneBook;



    public ListPhoneBook() {
        Contact test = new Contact(415, "George");
        phoneBook = new PhoneBook();
        phoneBook.addContact(test);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_phone_book, container, false);

        ListView phoneBookList = view.findViewById(R.id.PhoneBookList);
        phoneBookList.setAdapter(new PhoneBookAdapter(this.getContext(), phoneBook));

        return view;
    }

    public void setPhoneBook(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }
}