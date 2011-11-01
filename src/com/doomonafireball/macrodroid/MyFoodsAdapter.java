package com.doomonafireball.macrodroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyFoodsAdapter extends BaseAdapter {
    private ArrayList<AFood> items;
    private Context mContext;

    public MyFoodsAdapter(Context context, ArrayList<AFood> items) {
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
            convertView = inflater.inflate(R.layout.my_foods_list_item, parent, false);            
        }
        TextView nameTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_name);
        TextView kCalTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_kCal_value);
        TextView servingSizeTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_serving_size);
        TextView proteinTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_protein_value);
        TextView carbsTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_carbs_value);
        TextView fatTV = (TextView) convertView.findViewById(R.id.TV_food_list_item_fat_value);
        
        AFood food = items.get(position);
        
        nameTV.setText(food.getFoodName());
        kCalTV.setText(Float.toString(food.getFoodKCal()));
        servingSizeTV.setText(food.getFoodServingSize());
        proteinTV.setText(Float.toString(food.getFoodGramsProtein()));
        carbsTV.setText(Float.toString(food.getFoodGramsCarbs()));
        fatTV.setText(Float.toString(food.getFoodGramsFat()));
        
        return convertView;
    }
}
