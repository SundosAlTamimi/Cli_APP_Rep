package com.example.valetappsec.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.valetappsec.Json.ImportJson;
import com.example.valetappsec.Model.ClientOrder;
import com.example.valetappsec.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ListAdapterOrder extends BaseAdapter {
    private Context context;
    List<ClientOrder> itemsList;

    public ListAdapterOrder(Context context, List<ClientOrder> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public ListAdapterOrder() {

    }

    public void setItemsList(List<ClientOrder> itemsList) {
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
        TextView capName, toLoc;
        Button accept,rej;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder = new ViewHolder();
        view = View.inflate(context, R.layout.captain_order_raw, null);

        holder.capName =  view.findViewById(R.id.capName);
       // holder.toLoc =  view.findViewById(R.id.toLoc);
      holder.accept=view.findViewById(R.id.accept);
        holder.rej=view.findViewById(R.id.rej);
//holder.fromLoc.setText(itemsList.get(i).getFromLoc());
//        holder.toLoc.setText(itemsList.get(i).getToLocation());


        holder.capName.setText(itemsList.get(i).getCaptainName());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ClientOrder clientOrder = new ClientOrder();

                    clientOrder = itemsList.get(i);
                    ImportJson exportJson = new ImportJson(context);
                    exportJson.updateStatus(context, clientOrder);
                }catch (Exception e){

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("ERROR");
                sweetAlertDialog.setContentText("Please Try Again");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        sDialog.dismissWithAnimation();
                    }
                });
                sweetAlertDialog.show();

                }
            }
        });

        holder.rej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClientOrder clientOrder=new ClientOrder();
                clientOrder=itemsList.get(i);
                ImportJson exportJson=new ImportJson(context);
                exportJson.updateStatusRej(context,clientOrder);
            }
        });

        return view;
    }




}

