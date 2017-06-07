package com.jasons.easyoffice.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jasons.easyoffice.MainActivity;
import com.jasons.easyoffice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Calendar;
import com.github.nkzawa.socketio.client.*;
/**
 * Created by julien-pc on 07-Jun-17.
 */

public class InteractScreenFragment extends Fragment implements View.OnClickListener {
    int floor=0;
    String building;
    Socket mSocket;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            floor = bundle.getInt("floor");
            building = bundle.getString("building");
        }
        mSocket=((MainActivity)getActivity()).getinstanceSocket();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_interact, container, false);
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        Button btnmoveup=(Button)getView().findViewById(R.id.btnUp);
        btnmoveup.setOnClickListener(this);
        Button btnmovedown=(Button)getView().findViewById(R.id.btnDown);
        btnmovedown.setOnClickListener(this);
        Button btnall=(Button)getView().findViewById(R.id.btnAll);
        btnall.setOnClickListener(this);
        final ArrayList<String> years = new ArrayList<String>();
        for (int i = 0; i <= floor; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, years);

        Spinner spinYear = (Spinner) getView().findViewById(R.id.floor);
        spinYear.setAdapter(adapter);
        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer.valueOf(years.get(position));

                try {
                    mSocket.emit("floor", createjsonobject(building,floor,"111"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       // spinner.setAdapter(adapter);
    }

    private JSONObject createjsonobject(String building,int floor,String screenid) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("Building", building);
        json.put("Floor", floor);
        json.put("ScreenId", screenid);
        return json;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnUp){

            try {
                mSocket.emit("move_up",createjsonobject(building,floor,"111"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else  if(v.getId()==R.id.btnAll){
            try {
                mSocket.emit("all",createjsonobject(building,floor,"111"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else  if(v.getId()==R.id.btnDown){
            try {
                mSocket.emit("move_down",createjsonobject(building,floor,"111"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}