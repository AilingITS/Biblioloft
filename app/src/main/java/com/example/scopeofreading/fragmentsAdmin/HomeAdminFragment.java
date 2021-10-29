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
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarPoeticosFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarInfantilesFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarLiteraturaFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarMisteriosFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarRomanticosFragment;
import com.example.scopeofreading.fragmentsAdmin.agregarCategorias.agregarTerrorFragment;

public class HomeAdminFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View vista;
    private Button imgCientificos, imgLiteratura, imgAventura, imgTerror, imgMisterio, imgInfantiles, imgPoeticos, imgRomanticos, imgFiccion;

    public HomeAdminFragment() {
        // Required empty public constructor
    }

    public static HomeAdminFragment newInstance(String param1, String param2) {
        HomeAdminFragment fragment = new HomeAdminFragment();
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
        vista = inflater.inflate(R.layout.fragment_home_admin, container, false);

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
            public void onClick(View view) {
                replaceFragment(new agregarCientificosFragment());
            }
        });
        imgLiteratura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarLiteraturaFragment());
            }
        });
        imgAventura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarAventuraFragment());
            }
        });
        imgTerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarTerrorFragment());
            }
        });
        imgMisterio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarMisteriosFragment());
            }
        });
        imgInfantiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarInfantilesFragment());
            }
        });
        imgPoeticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarPoeticosFragment());
            }
        });
        imgRomanticos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarRomanticosFragment());
            }
        });
        imgFiccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new agregarFiccionFragment());
            }
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