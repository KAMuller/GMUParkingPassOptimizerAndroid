package com.gmu.kam.gmuparkingpassoptimizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class GetSchedule extends AppCompatActivity {

    public Boolean checkHour;
    public Boolean checkRapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_schedule);

        Intent in = getIntent();

        checkRapp = in.getBooleanExtra("RAPP", false);

    }

    public void calculate(View v){
        Spinner monSel = findViewById(R.id.monSpi);
        Spinner tuesSel = findViewById(R.id.tuesSpi);
        Spinner wedSel = findViewById(R.id.wedSpi);
        Spinner thursSel = findViewById(R.id.thursSpi);
        Spinner friSel = findViewById(R.id.friSpi);
        Spinner satSel = findViewById(R.id.satSpi);

        int[] buildingIndex = new int[6];
        buildingIndex[0] = getBuildingID(monSel.getSelectedItem().toString());
        buildingIndex[1] = getBuildingID(tuesSel.getSelectedItem().toString());
        buildingIndex[2] = getBuildingID(wedSel.getSelectedItem().toString());
        buildingIndex[3] = getBuildingID(thursSel.getSelectedItem().toString());
        buildingIndex[4] = getBuildingID(friSel.getSelectedItem().toString());
        buildingIndex[5] = getBuildingID(satSel.getSelectedItem().toString());

        Intent calcIntent = new Intent(GetSchedule.this, CalcResults.class);
        calcIntent.putExtra("BIDARRAY", buildingIndex);
        calcIntent.putExtra("RAPPG", checkRapp);


        startActivity(calcIntent);

    }

    public int getBuildingID(String name){
        int id;
        switch(name){
            case "David J. King Hall":
                id = 14;
                break;
            case "Nguyen Engineering Building":
                id = 15;
                break;
            case "Enterprise Hall":
                id = 16;
                break;
            case "Innovation Hall":
                id = 17;
                break;
            case "Krug Hall":
                id = 18;
                break;
            case "Music Theater Building":
                id = 19;
                break;
            case "Merten Hall":
                id = 20;
                break;
            case "Planetary Hall":
                id = 21;
                break;
            case "Robinson Hall":
                id = 22;
                break;
            case "Thompson Hall":
                id = 23;
                break;
            default:
                id = 0;
        }
        return id;
    }
}
