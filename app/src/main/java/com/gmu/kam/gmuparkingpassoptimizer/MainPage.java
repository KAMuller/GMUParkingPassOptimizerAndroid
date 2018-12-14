package com.gmu.kam.gmuparkingpassoptimizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainPage extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

    }

    public void newCalc(View v){


        RadioButton rappRad = findViewById(R.id.useRappRad);

        Boolean isRapp = rappRad.isChecked();

        Intent intent = new Intent(MainPage.this, GetSchedule.class);

        intent.putExtra("RAPP", isRapp);
        startActivity(intent);
    }

    public void clearRads(View v){

        RadioButton rappRad = findViewById(R.id.useRappRad);
        rappRad.setChecked(false);

    }



}
