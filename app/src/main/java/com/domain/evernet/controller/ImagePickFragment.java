package com.domain.evernet.controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.domain.evernet.R;

public class ImagePickFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImagePickFragmentListener listener;

    private ImageView loadImageDisplay;

    private ImageButton sendButton;
    private ImageButton loadButton;

    private Spinner contactSpinner;

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int SELECT_PICTURE = 1;

    public interface ImagePickFragmentListener {
        public void onClickLoad();
        public void onClickSent();
        public void onSpinnerSelect(String destination);
    }

    public ImagePickFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_pick, container, false);

        loadImageDisplay = view.findViewById(R.id.displayLoadPicture);

        sendButton = view.findViewById(R.id.sendButton);
        loadButton = view.findViewById(R.id.loadButton);

        contactSpinner = view.findViewById(R.id.contactSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.phoneArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactSpinner.setAdapter(adapter);

        contactSpinner.setOnItemSelectedListener(this);


        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickLoad();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSent();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ImagePickFragmentListener){
            listener = (ImagePickFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ImagePickFragmentListener !!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setImage(Bitmap bitmap){
        loadImageDisplay.setImageBitmap(bitmap);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String dest = null;
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

        listener.onSpinnerSelect(dest);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        contactSpinner.setSelection(0);
    }
}