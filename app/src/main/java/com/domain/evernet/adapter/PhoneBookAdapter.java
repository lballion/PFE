package com.domain.evernet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.domain.evernet.R;
import com.domain.evernet.model.Contact;
import com.domain.evernet.model.PhoneBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhoneBookAdapter extends BaseAdapter {

    private Context context;
    private PhoneBook phoneBook;
    private Map<Integer, Contact> entries;
    private List<Integer> identificators;

    private LayoutInflater inflater;

    public PhoneBookAdapter(Context context, PhoneBook phoneBook){
        this.context = context;
        this.phoneBook = phoneBook;
        entries = phoneBook.getContacts();
        identificators = new ArrayList<>(entries.keySet());
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return entries.keySet().size();
    }

    @Override
    public Contact getItem(int position) {
        Integer key = identificators.get(position);
        return entries.get(key);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.adapter_phonebook_item, null);


        return convertView;
    }
}
