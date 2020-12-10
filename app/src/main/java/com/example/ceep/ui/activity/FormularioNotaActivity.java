package com.example.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ceep.R;
import com.example.ceep.model.Nota;

import java.io.Serializable;

import static com.example.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.example.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicaoRecebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA) && dadosRecebidos.hasExtra("posicao")){
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dadosRecebidos.getIntExtra("posicao", -1);
            TextView titulo = findViewById(R.id.formulario_nota_titulo);
            titulo.setText(notaRecebida.getTitulo());
            TextView descricao = findViewById(R.id.formulario_nota_descricao);
            descricao.setText(notaRecebida.getDescricao());
        }
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
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private void retornarNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra("posicao", posicaoRecebida);
        setResult(CODIGO_RESULTADO_NOTA_CRIADA, resultadoInsercao);
    }
}