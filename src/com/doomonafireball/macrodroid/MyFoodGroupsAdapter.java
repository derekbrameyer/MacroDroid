package com.doomonafireball.macrodroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyFoodGroupsAdapter extends BaseAdapter {
    private ArrayList<AFoodGroup> items;
    private Context mContext;

    public MyFoodGroupsAdapter(Context context, ArrayList<AFoodGroup> items) {
        super();
        this.mContext = context;
        this.items = items;
    }
    
    @Override
    public int getCount() {
        return items.size();
    }
    
    @Override
    public Object getItem(int i) {
        return items.get(i);
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
            convertView = inflater.inflate(R.layout.my_food_groups_list_item, parent, false);            
        }
        TextView nameTV = (TextView) convertView.findViewById(R.id.TV_food_groups_name);
        TextView kCalTV = (TextView) convertView.findViewById(R.id.TV_food_groups_total_kCal_value);
        TextView servingSizeTV = (TextView) convertView.findViewById(R.id.TV_food_groups_total_servings_value);
        TextView proteinTV = (TextView) convertView.findViewById(R.id.TV_food_groups_total_protein_value);
        TextView carbsTV = (TextView) convertView.findViewById(R.id.TV_food_groups_total_carbs_value);
        TextView fatTV = (TextView) convertView.findViewById(R.id.TV_food_groups_total_fat_value);
        
        AFoodGroup foodGroup = items.get(position);
        
        nameTV.setText(foodGroup.getName());
        kCalTV.setText(Float.toString(foodGroup.getTotalKCal()));
        servingSizeTV.setText(Integer.toString(foodGroup.getFoods().size()));
        proteinTV.setText(Float.toString(foodGroup.getTotalProtein()));
        carbsTV.setText(Float.toString(foodGroup.getTotalCarbs()));
        fatTV.setText(Float.toString(foodGroup.getTotalFat()));
        
        return convertView;
    }
}
