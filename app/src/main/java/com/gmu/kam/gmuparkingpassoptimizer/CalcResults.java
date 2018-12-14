package com.gmu.kam.gmuparkingpassoptimizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

public class CalcResults extends AppCompatActivity {

    private String priString;
    private String walString;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("ShortestPath");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_results);

        TextView text = findViewById(R.id.textView2);

        //gets the values passed from the previous activity
        Intent in = getIntent();
        int[] weekBuilds = in.getIntArrayExtra("BIDARRAY");
        boolean checkGRapp = in.getBooleanExtra("RAPPG", false);

        //creates the array of distances to lots for each day
        int[][] dists = new int[6][14];
        int[] empty = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        for(int i = 0; i<6; i++){
            if(weekBuilds[i]!= 0){
                dists[i] = getDistances(weekBuilds[i]);
            }
            else{
                dists[i] = empty;
            }
        }

        //west campus pass init
        Pass west = new Pass();
        west.passName = "West Campus Pass";
        west.price = 150;
        west.walk = getWeekDist(dists, new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, true});

        //M&P pass init
        Pass mp = new Pass();
        mp.passName = "Lots M & P Pass";
        mp.price = 265;
        mp.walk = getWeekDist(dists, new boolean[]{false, false, false, false, false, false, true, false, true, false, false, false, false, false});

        //general pass init
        Pass gen = new Pass();
        gen.passName = "General Lots Pass";
        gen.price = 445;
        if(checkGRapp){
            gen.walk = getWeekDist(dists, new boolean[]{true, false, false, false, false, true, true, true, true, true, true, true, true, false});
        }else{
            gen.walk = getWeekDist(dists, new boolean[]{false, false, false, false, false, true, true, true, true, true, true, true, true, false});
        }
        //J pass init
        Pass j = new Pass();
        j.passName = "Lot J Pass";
        j.price = 550;
        j.walk = getWeekDist(dists, new boolean[]{false, false, false, false, true, true, true, true, true, true, true, true, true, false});

        //I pass init
        Pass i = new Pass();
        i.passName = "Lot I Pass";
        i.price = 610;
        i.walk = getWeekDist(dists, new boolean[]{false, false, false, true, false, true, true, true, true, true, true, true, true, false});

        //mason pond pass init
        Pass pond = new Pass();
        pond.passName = "Mason Pond Deck Pass";
        pond.price = 725;
        pond.walk = getWeekDist(dists, new boolean[]{false, false, true, false, false, true, true, true, true, true, true, true, true, false});

        //shenandoah pass init
        Pass shen = new Pass();
        shen.passName = "Shenandoah Deck Pass";
        shen.price = 725;
        shen.walk = getWeekDist(dists, new boolean[]{false, true, false, false, false, true, true, true, true, true, true, true, true, false});

        //rappahannock pass init
        Pass rapp = new Pass();
        rapp.passName = "Rappahannock Deck Pass";
        rapp.price = 630;
        rapp.walk = getWeekDist(dists, new boolean[]{true, false, false, false, false, true, true, true, true, true, true, true, true, false});


        ArrayList<Pass> priceSortPasses = new ArrayList<>();
        priceSortPasses.add(west);
        priceSortPasses.add(mp);
        priceSortPasses.add(gen);
        priceSortPasses.add(j);
        priceSortPasses.add(i);
        priceSortPasses.add(rapp);
        priceSortPasses.add(pond);
        priceSortPasses.add(shen);

        ArrayList<Pass> walkSortPasses = new ArrayList<>();
        walkSortPasses.add(west);
        walkSortPasses.add(mp);
        walkSortPasses.add(gen);
        walkSortPasses.add(j);
        walkSortPasses.add(i);
        walkSortPasses.add(rapp);
        walkSortPasses.add(pond);
        walkSortPasses.add(shen);
        Collections.sort(walkSortPasses);


        StringBuilder wString = new StringBuilder();
        for(int x = 0; x<8; x++){
            wString.append(x + 1);
            wString.append(" : ");
            wString.append("Name: ");
            wString.append(walkSortPasses.get(x).passName);
            wString.append("\n");
            wString.append("    Price: $");
            wString.append(walkSortPasses.get(x).price);
            wString.append("\n");
            wString.append("    Weekly Feet Walked: ");
            wString.append(walkSortPasses.get(x).walk);
            wString.append("\n");
            wString.append("\n");
        }
        walString = wString.toString();

        StringBuilder pString = new StringBuilder();
        for(int x = 0; x<8; x++){
            pString.append(x + 1);
            pString.append(" : ");
            pString.append("Name: ");
            pString.append(priceSortPasses.get(x).passName);
            pString.append("\n");
            pString.append("    Price: $");
            pString.append(priceSortPasses.get(x).price);
            pString.append("\n");
            pString.append("    Weekly Feet Walked: ");
            pString.append(priceSortPasses.get(x).walk);
            pString.append("\n");
            pString.append("\n");
        }
        priString = pString.toString();

        text.setText(walString);
    }

    public void sortPrice(View v){
        TextView text = findViewById(R.id.textView2);
        text.setText(priString);
    }

    public void sortWalk(View v){
        TextView text = findViewById(R.id.textView2);
        text.setText(walString);
    }

    public int getWeekDist(int[][] dists, boolean[] avail) {
        int weekDist = 0;
        for (int j = 0; j < 6; j++) {
            int dayMin = 999999999;
            for (int i = 0; i < 14; i++) {
                if (avail[i]) {
                    if (dists[j][i] < dayMin) {
                        dayMin = dists[j][i];

                    }
                }
            }
            weekDist = weekDist + dayMin;
            System.out.println(weekDist);
        }
        return weekDist;
    }

    public native int[] getDistances(int index);

    public static class Pass implements Comparable<Pass>{

        String passName;
        int price;
        int walk;

        Pass()
        {
            this.passName="";
            this.price = 0;
            this.walk = 0;
        }

        @Override
        public int compareTo(Pass o) {
            if(this.walk == o.walk){
                return 0;
            }
            else if(this.walk < o.walk){
                return -1;
            }
            else{
                return 1;
            }
        }
    }
}
