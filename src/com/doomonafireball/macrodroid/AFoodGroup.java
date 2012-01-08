package com.doomonafireball.macrodroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.util.Pair;

public class AFoodGroup implements Comparable<AFoodGroup> {
	public long id;
	private ArrayList<Pair<Float, AFood>> foods;
	private String name;
	private Float totalKCal;
	private Float totalProtein;
	private Float totalCarbs;
	private Float totalFat;

	// TODO Array of pictures
	
	public AFoodGroup(long id) {
		this.id = id;
	}
	
	public AFoodGroup() {
		this.name = "";
		this.foods = new ArrayList<Pair<Float, AFood>>();
		recalculateValues();
	}

	public AFoodGroup(String name, ArrayList<Pair<Float, AFood>> foods) {
		this.name = name;
		this.foods = foods;
		recalculateValues();
	}
	
	public Float getTotalKCal() {
		return totalKCal;
	}

	public Float getTotalProtein() {
		return totalProtein;
	}

	public Float getTotalCarbs() {
		return totalCarbs;
	}

	public Float getTotalFat() {
		return totalFat;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Pair<Float, AFood>> getFoods() {
		return foods;
	}

	public void setFoods(ArrayList<Pair<Float, AFood>> foods) {
		this.foods = foods;
		recalculateValues();
	}

	@Override
	public int compareTo(AFoodGroup arg0) {
		// TODO Auto-generated method stub
		if ((this.getName() != null) && (arg0.getName() != null)) {
			return arg0.getName().compareTo(this.getName());
		}
		return 0;
	}
	
	private void recalculateValues() {
		totalKCal = 0.0f;
		totalProtein = 0.0f;
		totalCarbs = 0.0f;
		totalFat = 0.0f;
		if (this.foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				Pair<Float, AFood> currPair = foods.get(i);
				Float currServings = currPair.first;
				AFood currFood = currPair.second;
				totalKCal += currServings * currFood.getFoodKCal();
				totalProtein += currServings * currFood.getFoodGramsProtein();
				totalCarbs += currServings * currFood.getFoodGramsCarbs();
				totalFat += currServings * currFood.getFoodGramsFat();
			}
		}
	}

	@Override
	public String toString() {
		String retVal = "";
		retVal += "id: " + id + "\n";
		retVal += "name: " + this.name + "\n";
		retVal += "kCal: " + this.totalKCal + "\n";
		retVal += "protein: " + this.totalProtein + "\n";
		retVal += "carbs: " + this.totalCarbs + "\n";
		retVal += "fat: " + this.totalFat + "\n";
		retVal += "foods:" + "\n";
		if (this.foods != null) {
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
