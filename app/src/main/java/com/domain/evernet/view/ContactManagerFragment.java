package com.domain.evernet.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.domain.evernet.R;

public class ContactManagerFragment extends Fragment {

    private Button saveContactButton;
    private Button rstButton;

    private EditText idTextEntry;
    private EditText nameTextEntry;

    interface ContactManagerFragmentListener{
        void saveEvent(String name, String id);
        // Debug Purpose
        void rstContact();
    }

    ContactManagerFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactmanager, container, false);

        saveContactButton = view.findViewById(R.id.addContactSaveButton);
        rstButton = view.findViewById(R.id.rstbtn);

        idTextEntry = view.findViewById(R.id.addContactTextFieldID);
        nameTextEntry = view.findViewById(R.id.addContactTextFieldName);


        saveContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {sendDataToActivity();}}
        );

        rstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {listener.rstContact();}}
        );

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ContactManagerFragmentListener){
            listener = (ContactManagerFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ContactManagerFragmentListener !!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void sendDataToActivity(){
        String name = nameTextEntry.getText().toString();
        String id = idTextEntry.getText().toString();
        listener.saveEvent(name, id);

        nameTextEntry.setText("");
        idTextEntry.setText("");
    }

}
