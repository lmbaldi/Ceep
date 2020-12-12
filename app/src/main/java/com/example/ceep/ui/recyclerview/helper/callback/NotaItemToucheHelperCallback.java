package com.example.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ceep.dao.NotaDAO;
import com.example.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemToucheHelperCallback extends ItemTouchHelper.Callback {
    
    private final ListaNotasAdapter adapter;

    public NotaItemToucheHelperCallback(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int marcacaoDeDeslize =  ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                                    | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(marcacoesDeArrastar,marcacaoDeDeslize);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAbsoluteAdapterPosition();
        int posicaoFinal = target.getAbsoluteAdapterPosition();
        trocarNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocarNotas(int posicaoInicial, int posicaoFinal) {
        new NotaDAO().trocar(posicaoInicial, posicaoFinal);
        adapter.trocarPosicao(posicaoInicial, posicaoFinal);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getAbsoluteAdapterPosition();
        removerNota(posicaoDaNotaDeslizada);
    }

    private void removerNota(int posicao) {
        new NotaDAO().remove(posicao);
        adapter.remove(posicao);
    }
}
