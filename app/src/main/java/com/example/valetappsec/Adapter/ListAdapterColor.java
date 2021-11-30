package com.example.valetappsec.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.valetappsec.DriverMapsActivity;
import com.example.valetappsec.Model.ColorCarModel;
import com.example.valetappsec.R;
import com.example.valetappsec.SingUpValetActivityApp;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ListAdapterColor extends BaseAdapter {
    private Context context;
    List<ColorCarModel> itemsList;

    public ListAdapterColor(Context context, List<ColorCarModel> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public ListAdapterColor() {

    }

    public void setItemsList(List<ColorCarModel> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView locationName;
        CircleImageView colorCar;
        LinearLayout colorLinear;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.color_list_raw, null);

        holder.locationName =  view.findViewById(R.id.colorName);
        holder.colorCar=view.findViewById(R.id.color);
        holder.colorLinear=view.findViewById(R.id.colorLinear);

        holder.colorCar.setBackgroundColor(Color.parseColor(itemsList.get(i).getCarColorNo()));
        holder.locationName.setText(itemsList.get(i).getCarColorName());

        holder.colorLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SingUpValetActivityApp singUpValetActivityApp=(SingUpValetActivityApp) context;
                singUpValetActivityApp.ColorInText(itemsList.get(i).getCarColorName(),itemsList.get(i).getCarColorNo());
            }
        });
        return view;
    }




}

