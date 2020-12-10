package com.example.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.dao.NotaDAO;
import com.example.ceep.model.Nota;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import com.example.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

import java.util.List;

import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = retornaTodasAsNotas();
        configurarRecyclerView(todasNotas);
        configuarBotaoInserirNota();
    }

    private List<Nota> retornaTodasAsNotas() {
        NotaDAO dao = new NotaDAO();
        for(int i = 0; i < 10; i++){
            dao.insere(new Nota("Titulo " + (i + 1), "Descricao " + (i + 1)));
        }
        return dao.todos();
    }

    private void configuarBotaoInserirNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaFormularioActivity();
            }
        });
    }

    private void configurarRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configurarAdapter(todasNotas, listaNotas);
    }

    private void configurarAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
                abreFormularioComNota.putExtra("posicao", posicao);
                startActivityForResult(abreFormularioComNota, 2);
            }
        });
    }

    private void irParaFormularioActivity() {
        Intent iniciaNotaFormulario = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaNotaFormulario, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ehUmResultadoComNota(requestCode, resultCode, data)) {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adicionar(notaRecebida);
        }
        //verifica se um item da lista de notas foi alterada
        if (requestCode == 2 && resultCode == CODIGO_RESULTADO_NOTA_CRIADA && temNota(data) && data.hasExtra("posicao")) {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoRecebida = data.getIntExtra("posicao", -1);
            new NotaDAO().altera(posicaoRecebida, notaRecebida);
            adapter.alterar(posicaoRecebida, notaRecebida);
        }
    }

    private void adicionar(Nota notaRecebida) {
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private boolean ehUmResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && ehCodigoResultadoNotaCriada(resultCode) && temNota(data);
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private boolean ehCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    @Override
    protected void onResume() {
      super.onResume();
    }
}