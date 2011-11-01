package com.doomonafireball.macrodroid;

import java.util.ArrayList;
import java.util.Calendar;

public class ADay implements Comparable<ADay> {
    public long id;
    private ArrayList<AFood> foods;
    private Calendar date;
    // TODO Array of pictures
    
    public ArrayList<AFood> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<AFood> foods) {
        this.foods = foods;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public int compareTo(ADay arg0) {
        // TODO Auto-generated method stub
        if ((this.getDate() != null) && (arg0.getDate() != null)) {
            return arg0.getDate().compareTo(this.getDate());
        }
        return 0;
    }
}
