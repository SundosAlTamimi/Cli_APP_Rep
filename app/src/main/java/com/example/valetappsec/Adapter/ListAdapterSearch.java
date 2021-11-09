package com.example.valetappsec.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.valetappsec.DriverMapsActivity;
import com.example.valetappsec.Json.ImportJson;
import com.example.valetappsec.Model.ClientOrder;
import com.example.valetappsec.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ListAdapterSearch extends BaseAdapter {
    private Context context;
    List<String> itemsList;

    public ListAdapterSearch(Context context, List<String> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public ListAdapterSearch() {

    }

    public void setItemsList(List<String> itemsList) {
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
        LinearLayout searchLinear;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.search_list_raw, null);

        holder.locationName =  view.findViewById(R.id.locationName);
        holder.searchLinear=view.findViewById(R.id.searchLinear);

        holder.locationName.setText(itemsList.get(i));

        holder.searchLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DriverMapsActivity driverMapsActivity=(DriverMapsActivity)context;
                driverMapsActivity.searchText(i);

            }
        });

        return view;
    }




}

