package com.example.crud_fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListaActivity extends AppCompatActivity {

    List<Model>modelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    FloatingActionButton btnAdd;

    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore db ;

    CustomAdapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        ActionBar actionBar = getSupportActionBar();
        //assert actionBar != null;
        actionBar.setTitle("listar dado");

        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recycler_View);
        btnAdd = findViewById(R.id.btnAdd);

        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        pd = new ProgressDialog(this);

        showData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ListaActivity.this,MainActivity.class));
                finish();

            }
        });

    }

    public void showData() {
        modelList.clear();
             pd.setTitle("Carregando dados...");
             pd.show();



        db.collection("documento")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pd.dismiss();
                        for(DocumentSnapshot doc: task.getResult()){
                            Model model = new Model(
                                    doc.getString("id"),
                                    doc.getString("titulo"),
                                    doc.getString("descricao"));
                            modelList.add(model);

                        }

                        adapter = new CustomAdapter(ListaActivity.this,modelList);
                        mRecyclerView.setAdapter(adapter);

                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(ListaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public void deleteData(int index){
        pd.setTitle("Deletando dados...");
        pd.show();

        db.collection("documento").document(modelList.get(index).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ListaActivity.this, "Deletado", Toast.LENGTH_SHORT).show();
                        showData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void buscarDados(String query) {

        pd.setTitle("Cosultando");
        pd.show();

        db.collection("documento").whereEqualTo("consultar", query.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        modelList.clear();
                        pd.dismiss();

                        for (DocumentSnapshot doc : task.getResult()) {
                            Model model = new Model(
                                    doc.getString("id"),
                                    doc.getString("titulo"),
                                    doc.getString("descricao"));
                            modelList.add(model);

                        }
                        adapter = new CustomAdapter(ListaActivity.this, modelList);
                        mRecyclerView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }

    //menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item =menu.findItem(R.id.consu);


        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                buscarDados(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_config){
            Toast.makeText(this, "Configuração", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }
}
