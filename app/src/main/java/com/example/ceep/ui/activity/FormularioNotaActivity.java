package com.example.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ceep.R;
import com.example.ceep.model.Nota;

import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static com.example.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        inicializarCampos();
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA) ){
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            preencherCampos(notaRecebida);
        }
    }

    private void preencherCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializarCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(ehMenuSalvarNota(item)){
            Nota notaCriada = criarNota();
            retornarNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean ehMenuSalvarNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salvar;
    }

    private Nota criarNota() {
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private void retornarNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }
}