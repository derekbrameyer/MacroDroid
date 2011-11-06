package com.doomonafireball.macrodroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.util.Pair;

public class ADay implements Comparable<ADay> {
	public long id;
	private ArrayList<Pair<Float, AFood>> foods;
	private Calendar date;
	private boolean isTraining;
	private float weight;
	private float height;

	// TODO Array of pictures
	
	public ADay(long id) {
		this.id = id;
	}

	public ADay(Calendar date, boolean isTraining) {
		this.date = date;
		this.isTraining = isTraining;
		foods = new ArrayList<Pair<Float, AFood>>();
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isTraining() {
		// Default to false
		return isTraining;
	}

	public void setTraining(boolean isTraining) {
		this.isTraining = isTraining;
	}

	public ArrayList<Pair<Float, AFood>> getFoods() {
		return foods;
	}

	public void setFoods(ArrayList<Pair<Float, AFood>> foods) {
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

	@Override
	public String toString() {
		String retVal = "";
		retVal += "id: " + id + "\n";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		retVal += "date: " + sdf.format(date.getTime()) + "\n";
		retVal += "isTraining: " + isTraining + "\n";
		retVal += "foods:" + "\n";
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				Pair<Float, AFood> pair = foods.get(i);
				Float serving = pair.first;
				AFood food = pair.second;
				retVal += "servings: " + Float.toString(serving) + ", food: "
						+ food.getFoodName() + "\n";
			}			
		} else {
			retVal += "null!" + "\n";
		}
		return retVal;
	}
}
