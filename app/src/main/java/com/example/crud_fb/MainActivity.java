package com.example.crud_fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnSalvar, btnLista;
    EditText edtTitulo, edtDesc;


    ProgressDialog pd;
    FirebaseFirestore db;

    String pId,pTitulo ,pDesc;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getSupportActionBar();
        //assert actionBar != null;
        //actionBar.setTitle("Salvar dado");

        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);


        edtTitulo = findViewById(R.id.edtTitulo);
        edtDesc = findViewById(R.id.edtDesc);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnLista = findViewById(R.id.btnLista);



        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            assert actionBar != null;
            actionBar.setTitle("atualizar dados");


            btnLista.setText("Voltar");
            pId = bundle.getString("pId");
            pTitulo = bundle.getString("pTitulo");
            pDesc = bundle.getString("pDesc");

            edtTitulo.setText(pTitulo);
            edtDesc.setText(pDesc);

        }
        else {
            assert actionBar != null;
            actionBar.setTitle("Salvar dado");
            btnSalvar.setText("Salvar");

        }

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(MainActivity.this,ListaActivity.class));
                finish();

            }
        });



         btnSalvar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 Bundle bundle1 = getIntent().getExtras();
                 if(bundle1 != null){

                     String id=pId;

                     String titulo = edtTitulo.getText().toString().trim();
                     String descricao = edtDesc.getText().toString().trim();


                     updateData(id,titulo,descricao);

                 }
                 else{

                     String titulo = edtTitulo.getText().toString().trim();
                     String descricao = edtDesc.getText().toString().trim();

                     uploadData(titulo, descricao);

                 }




             }


         });



    }

    private void updateData(String id, String titulo, String descricao) {

        pd.setTitle("Atualizando dados...");
        pd.show();

        db.collection("documento").document(id)
                .update("titulo",titulo,
                        "consultar",titulo.toLowerCase(),
                        "descricao",descricao)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, "Atualizado", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void uploadData(String titulo, String descricao) {
        pd.setTitle("Salvando no Firebase");
        pd.show();

        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();

        doc.put("id",id);
        doc.put("titulo",titulo);
        doc.put("consultar",titulo.toLowerCase());
        doc.put("descricao",descricao);

        db.collection("documento").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,"Salvo...",
                                Toast.LENGTH_SHORT).show();

                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,e.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });



    }




}
