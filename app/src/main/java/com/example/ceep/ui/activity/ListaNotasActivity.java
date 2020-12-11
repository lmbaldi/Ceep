package com.example.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.R;
import com.example.ceep.dao.NotaDAO;
import com.example.ceep.model.Nota;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import java.util.List;

import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERAR_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERIR_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

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
                irParaFormularioNotaActivityInserir();
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
        adapter.setOnItemClickListener((nota, posicao) -> {
            irParaFormularioNotaActivityAlterar(nota, posicao);
        });
    }

    private void irParaFormularioNotaActivityAlterar(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERAR_NOTA);
    }

    private void irParaFormularioNotaActivityInserir() {
        Intent iniciaNotaFormulario = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaNotaFormulario, CODIGO_REQUISICAO_INSERIR_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ehResultadoInserirNota(requestCode,  data)) {
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adicionar(notaRecebida);
            }
        }
        //verifica se um item da lista de notas foi alterada
        if (ehResultadoAlterarNota(requestCode, data)) {
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if(ehPosicaoValida(posicaoRecebida)){
                    alterar(notaRecebida, posicaoRecebida);
                }else{
                    Toast.makeText(this, "Ocorreu um problema na alteracao da Nota", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void alterar(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.alterar(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlterarNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoAlterarNota(requestCode) && temNota(data);
    }

    private boolean ehCodigoRequisicaoAlterarNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERAR_NOTA;
    }

    private void adicionar(Nota notaRecebida) {
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private boolean ehResultadoInserirNota(int requestCode,  @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode)  && temNota(data);
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERIR_NOTA;
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    @Override
    protected void onResume() {
      super.onResume();
    }
}