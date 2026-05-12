package com.example.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.widget.Toast;

import com.example.app.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class HistorialFragment extends Fragment {
    private RecyclerView recyclerView;
    private ComandaAdapter adapter;
    private List<Comanda> listaComandas = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ComandaAdapter(listaComandas, comanda -> {
            // al pulsar borrar
            FirebaseFirestore.getInstance()
                    .collection("comandas")
                    .document(comanda.getId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        listaComandas.remove(comanda);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Comanda borrada", Toast.LENGTH_SHORT).show();
                    });
        });

        recyclerView.setAdapter(adapter);
        cargarComandas();
    }
    private void cargarComandas() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String uid = user.getUid();
        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(uid)
                .collection("comandas")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listaComandas.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Comanda comanda = doc.toObject(Comanda.class);
                        comanda = new Comanda(
                                doc.getId(),
                                doc.getString("mesa"),
                                (Map<String, Integer>) doc.get("platos"),
                                doc.getString("alergias"),
                                doc.getDouble("total")
                        );
                        listaComandas.add(comanda);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }
}