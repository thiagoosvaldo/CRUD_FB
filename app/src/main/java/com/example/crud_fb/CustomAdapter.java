package com.example.crud_fb;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;




import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    ListaActivity listaActivity;
    List<Model> modelList;
    Context context;



    public CustomAdapter(ListaActivity listaActivity, List<Model> modelList) {
        this.listaActivity = listaActivity;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_layout,viewGroup,false);


        ViewHolder viewHolder = new ViewHolder(itemView);

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {


            @Override
            public void onItemClick(View view, int position) {

                String titulo =modelList.get(position).getTitulo();
                String desc = modelList.get(position).getDescricao();
                Toast.makeText(listaActivity, titulo+"\n"+desc, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(listaActivity);

                String[] options ={"Atualizar", "Deletar"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            String id = modelList.get(position).getId();
                            String titulo = modelList.get(position).getTitulo();
                            String desc = modelList.get(position).getDescricao();

                            Intent it = new Intent(listaActivity,MainActivity.class);

                            it.putExtra("pId",id);
                            it.putExtra("pTitulo",titulo);
                            it.putExtra("pDesc",desc);


                            listaActivity.startActivity(it);


                        }

                        if(which == 1){
                            listaActivity.deleteData(position);


                        }
                    }
                }).create().show();



            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int i) {

        viewholder.mTitulo.setText(modelList.get(i).getTitulo());
        viewholder.mDesc.setText(modelList.get(i).getDescricao());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
