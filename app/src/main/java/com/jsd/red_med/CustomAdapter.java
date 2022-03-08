package com.jsd.red_med;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    MainActivity mainActivity;
    List<Model> modelList;

    public CustomAdapter(MainActivity mainActivity, List<Model> modelList) {
        this.mainActivity = mainActivity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String time = modelList.get(position).getTime();
                String name = modelList.get(position).getStr_name();
                String type = modelList.get(position).getStr_type();
                Toast.makeText(mainActivity, "Type : "+name+"\n"+"Name : "+type+"\n"+"Time : "+time, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

                String[] options = {"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0){
                            String id = modelList.get(position).getId();
                            String time = modelList.get(position).getTime();
                            String name = modelList.get(position).getStr_name();
                            String type = modelList.get(position).getStr_type();

                            Intent i = new Intent(mainActivity, AddMed.class);
                            i.putExtra("pId",id);
                            i.putExtra("pName",name);
                            i.putExtra("pType",type);
                            i.putExtra("pTime",time);
                            mainActivity.startActivity(i);
                        }
                        if(which==1){
                            mainActivity.deleteData(position);
                        }
                    }
                }).create().show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.mtime.setText("Time : "+modelList.get(i).getTime());
        //Log.d("@@@", "onBindViewHolder: "+modelList.get(i).getTime());
        viewHolder.mMedname.setText("Medicine name : "+modelList.get(i).getStr_name());
        viewHolder.mMedtype.setText("Medicine type : "+modelList.get(i).getStr_type());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
