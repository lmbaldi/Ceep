package com.example.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.dao.NotaDAO;
import com.example.ceep.model.Nota;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import java.io.Serializable;
import java.util.List;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;
    private List<Nota> todasNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        todasNotas = notasDeExemplo();
        configurarRecyclerView(todasNotas);

        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iniciaNotaFormulario = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                startActivityForResult(iniciaNotaFormulario, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == 2 && data.hasExtra("nota")){
            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            new NotaDAO().insere(notaRecebida);
            adapter.adiciona(notaRecebida);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
      super.onResume();
    }

    private List<Nota> notasDeExemplo() {
        NotaDAO dao = new NotaDAO();
        for (int i = 1; i <= 5; i++) {
            dao.insere(new Nota(" Nota " + i, " descricao " + i));
        }
        return dao.todos();
    }

    private void configurarRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configurarAdapter(todasNotas, listaNotas);
    }

    private void configurarAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }
}