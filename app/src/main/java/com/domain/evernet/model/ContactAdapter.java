package com.domain.evernet.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.domain.evernet.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Contact> contactItemList;
    private LayoutInflater inflater;

    public ContactAdapter(Context context,ArrayList<Contact> contactItemList) {

        this.context = context;
        this.contactItemList = contactItemList;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return contactItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.contact_item_layout,null);

        Contact currentItem = (Contact) getItem(position);
        String contactName = currentItem.getName();
        int id = currentItem.getId();
        Integer idInteger = new Integer(id);
        String contactId = idInteger.toString();

        TextView contactView = convertView.findViewById(R.id.contact_name);
        contactView.setText(contactName);

        TextView idView = convertView.findViewById(R.id.contact);
        idView.setText("#" + contactId);

        return convertView;
    }
}
