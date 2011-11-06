package com.doomonafireball.macrodroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DayDetailFoodsAdapter extends BaseAdapter {
    private ArrayList<AFood> foods;
    private ArrayList<Float> servings;
    private Context mContext;

    public DayDetailFoodsAdapter(Context context, ArrayList<AFood> foods, ArrayList<Float> servings) {
        super();
        this.mContext = context;
        this.foods = foods;
        this.servings = servings;
    }
    
    @Override
    public int getCount() {
        return foods.size();
    }
    
    @Override
    public Object getItem(int i) {
    	Pair<Float, AFood> pair = new Pair<Float, AFood>(servings.get(i), foods.get(i));
        return pair;
    }
    
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.day_detail_food_list_item, parent, false);            
        }
        TextView nameTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_name);
        TextView kCalTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_kCal_value);
        TextView numServingsTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_num_servings);
        TextView servingSizeTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_serving_size);
        TextView proteinTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_protein_value);
        TextView carbsTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_carbs_value);
        TextView fatTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_fat_value);
        
        AFood food = (AFood) foods.get(position);
        Float serving = (Float) servings.get(position);
        
        nameTV.setText(food.getFoodName());
        kCalTV.setText(Float.toString(serving * food.getFoodKCal()));
        numServingsTV.setText(serving.toString());
        servingSizeTV.setText(food.getFoodServingSize());
        proteinTV.setText(Float.toString(serving * food.getFoodGramsProtein()));
        carbsTV.setText(Float.toString(serving * food.getFoodGramsCarbs()));
        fatTV.setText(Float.toString(serving * food.getFoodGramsFat()));
        
        return convertView;
    }
}
