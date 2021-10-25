package com.example.scopeofreading.fragmentsAdmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scopeofreading.R;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarAventuraFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarCientificosFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarFiccionFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarFragmentPoeticos;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarInfantilesFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarLiteraturaFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarMisteriosFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarRomanticosFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarTerrorFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroAventuraFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroCientificosFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroFiccionFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroInfantilesFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroLiteraturaFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroMisterioFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroPoeticosFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroRomanticosFragment;
import com.example.scopeofreading.fragmentsAdmin.registroLibros.registroTerrorFragment;

public class RegistroLibrosFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    View vista;
    private Button imgCientificos, imgLiteratura, imgAventura, imgTerror, imgMisterio, imgInfantiles, imgPoeticos, imgRomanticos, imgFiccion;

    public RegistroLibrosFragment() {
        // Required empty public constructor
    }

    public static RegistroLibrosFragment newInstance(String param1, String param2) {
        RegistroLibrosFragment fragment = new RegistroLibrosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_registro_libros, container, false);
        imgCientificos = (Button) vista.findViewById(R.id.imgCientificos);
        imgLiteratura = (Button) vista.findViewById(R.id.imgLiteratura);
        imgAventura = (Button) vista.findViewById(R.id.imgAventura);
        imgTerror = (Button) vista.findViewById(R.id.imgTerror);
        imgMisterio = (Button) vista.findViewById(R.id.imgMisterio);
        imgInfantiles = (Button) vista.findViewById(R.id.imgInfantiles);
        imgPoeticos = (Button) vista.findViewById(R.id.imgPoeticos);
        imgRomanticos = (Button) vista.findViewById(R.id.imgRomanticos);
        imgFiccion = (Button) vista.findViewById(R.id.imgFiccion);

        imgCientificos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroCientificosFragment()); }
        });
        imgLiteratura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroLiteraturaFragment()); }
        });
        imgAventura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroAventuraFragment()); }
        });
        imgTerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroTerrorFragment()); }
        });
        imgMisterio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroMisterioFragment()); }
        });
        imgInfantiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroInfantilesFragment()); }
        });
        imgPoeticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroPoeticosFragment()); }
        });
        imgRomanticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroRomanticosFragment()); }
        });
        imgFiccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { replaceFragment(new registroFiccionFragment()); }
        });

        return vista;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body_container,fragment);
        fragmentTransaction.commit();
    }
}