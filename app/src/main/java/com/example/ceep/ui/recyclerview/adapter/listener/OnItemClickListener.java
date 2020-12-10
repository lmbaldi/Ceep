package com.example.ceep.ui.recyclerview.adapter.listener;

import com.example.ceep.model.Nota;

//interface criada para permitir que quem chame implemente esse comportamento
public interface OnItemClickListener {
    void onItemClick(Nota nota, int posicao);
}
