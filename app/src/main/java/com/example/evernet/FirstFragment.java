package com.example.evernet;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    //"0674139706"
                    smsManager.sendTextMessage("0758107468","0758107468", "Salut bg, ceci est un test", null, null);
                    Toast.makeText(getActivity(),"Sent data to someone",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(getActivity(),"Failed to send sms",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
