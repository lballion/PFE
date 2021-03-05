package com.domain.evernet.model;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class PhoneBook {

    private Map<Integer, Contact> contacts ;

    public PhoneBook(){
        contacts = new HashMap<Integer, Contact>();
    }

    public void addContact(Contact nContact){
        contacts.put(nContact.getId(), nContact);
    }

    public int deleteContact(Integer id){
        if(contacts.containsKey(id)){
            contacts.remove(id);
            return 1;
        }else{
            return -1;
        }
    }

    public Map<Integer, Contact> getContacts() {
        return contacts;
    }

    private void setContacts(Map<Integer, Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        PhoneBook returned = new PhoneBook();
        Map<Integer, Contact> copy = this.getContacts();
        returned.setContacts(copy);
        return returned;
    }
}
