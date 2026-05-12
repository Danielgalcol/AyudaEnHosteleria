package com.example.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Map;

public class ComandaAdapter extends RecyclerView.Adapter<ComandaAdapter.ViewHolder> {

    private List<Comanda> comandas;
    private OnBorrarListener listener;

    public interface OnBorrarListener {
        void onBorrar(Comanda comanda);
    }

    public ComandaAdapter(List<Comanda> comandas, OnBorrarListener listener) {
        this.comandas = comandas;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comanda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comanda comanda = comandas.get(position);
        holder.tvMesa.setText("Mesa " + comanda.getMesa());
        holder.tvTotal.setText(String.format("%.2f €", comanda.getTotal()));
        holder.tvAlergias.setText("Alergias: " + comanda.getAlergias());

        // Mostrar platos
        StringBuilder platos = new StringBuilder("Platos: ");
        for (Map.Entry<String, Integer> entry : comanda.getPlatos().entrySet()) {
            platos.append(entry.getKey()).append(" x").append(entry.getValue()).append(", ");
        }
        holder.tvPlatos.setText(platos.toString());

        holder.btnBorrar.setOnClickListener(v -> listener.onBorrar(comanda));
    }

    @Override
    public int getItemCount() { return comandas.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMesa, tvTotal, tvAlergias, tvPlatos;
        MaterialButton btnBorrar;

        public ViewHolder(View view) {
            super(view);
            tvMesa = view.findViewById(R.id.tvMesa);
            tvTotal = view.findViewById(R.id.tvTotal);
            tvAlergias = view.findViewById(R.id.tvAlergias);
            tvPlatos = view.findViewById(R.id.tvPlatos);
            btnBorrar = view.findViewById(R.id.btnBorrar);
        }
    }
}