package com.example.tfg3.activitys.holders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg3.R;

public class CicloHolder extends RecyclerView.ViewHolder {

    TextView nombre,notas;

    public TextView getNombre() {
        return nombre;
    }

    public TextView getNotas() {
        return notas;
    }

    public CicloHolder(View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.nombreHolder);
        notas = itemView.findViewById(R.id.notaHolder);

    }
}
