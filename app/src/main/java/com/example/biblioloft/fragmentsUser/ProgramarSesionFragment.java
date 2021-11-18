package com.example.biblioloft.fragmentsUser;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.biblioloft.MainActivity;
import com.example.biblioloft.Prevalent.Prevalent;
import com.example.biblioloft.R;
import com.example.biblioloft.fragmentsUser.alarm.NotificationService;
import com.example.biblioloft.fragmentsUser.alarm.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProgramarSesionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    EditText elegir_fecha, elegir_hora;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener setListener;
    private String finalHour, finalMinute;

    private Button notification_establecer, libroleyendo_eliminar;

    private int alarmID = 1;
    private SharedPreferences settings;
    String strDate;

    //Libro leyendo
    private ImageView img_libro_leyendo;
    private TextView leyendo_titulo;
    private DatabaseReference dbRef_libroLeyendo;

    public ProgramarSesionFragment() {
        // Required empty public constructor
    }

    public static ProgramarSesionFragment newInstance(String param1, String param2) {
        ProgramarSesionFragment fragment = new ProgramarSesionFragment();
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
        view = inflater.inflate(R.layout.fragment_programar_sesion, container, false);

        settings = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        dbRef_libroLeyendo = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getNombre()).child("libroleyendo");

        img_libro_leyendo = (ImageView) view.findViewById(R.id.img_libro_leyendo);
        leyendo_titulo = (TextView) view.findViewById(R.id.leyendo_titulo);
        libroleyendo_eliminar = (Button) view.findViewById(R.id.libroleyendo_eliminar);

        dbRef_libroLeyendo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child("imageLibro").exists()) {
                        String image = snapshot.child("imageLibro").getValue().toString();
                        Picasso.get().load(image).into(img_libro_leyendo);
                    }

                    String titulo = snapshot.child("nombreLibro").getValue().toString();
                    leyendo_titulo.setText(titulo);

                    //autor = snapshot.child("autorLibro").getValue().toString();
                    //viewBook_author.setText(autor);

                    //String pages = snapshot.child("paginasLibro").getValue().toString();
                    //viewBook_pages.setText(pages);

                    //description = snapshot.child("descripcionLibro").getValue().toString();
                    //viewBook_description.setText(description);

                    //libroID = snapshot.child("libroID").getValue().toString();
                    //tipoLibro = snapshot.child("tipoLibro").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        libroleyendo_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef_libroLeyendo.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Toast.makeText(getActivity(),"Se ha borrado correctamente",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        elegir_fecha = view.findViewById(R.id.elegir_fecha);
        elegir_hora = view.findViewById(R.id.elegir_hora);

        Calendar calendarActual = Calendar.getInstance();

        final int yearActual = calendarActual.get(Calendar.YEAR);
        final int monthActual = calendarActual.get(Calendar.MONTH);
        final int dayActual = calendarActual.get(Calendar.DAY_OF_MONTH);
        final int hourActual = calendarActual.get(Calendar.HOUR);
        final int minuteActual = calendarActual.get(Calendar.MINUTE);


        elegir_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, yearActual, monthActual, dayActual);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                elegir_fecha.setText(date);
                Toast.makeText(getContext(), "Fecha establecida", Toast.LENGTH_SHORT).show();
            }
        };

        elegir_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        /*Hour = hourOfDay;
                        Minute = minute;
                        calendar.set(0, 0, 0, Hour, Minute);*/

                        finalHour = "" + hourOfDay;
                        finalMinute = "" + minute;
                        if (hourOfDay < 10) finalHour = "0" + hourOfDay;
                        if (minute < 10) finalMinute = "0" + minute;

                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                        SharedPreferences.Editor edit = settings.edit();
                        edit.putString("hour", finalHour);
                        edit.putString("minute", finalMinute);

                        //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
                        edit.putInt("alarmID", alarmID);
                        edit.putLong("alarmTime", calendar.getTimeInMillis());

                        edit.commit();

                        elegir_hora.setText(finalHour+":"+finalMinute);
                        Toast.makeText(getContext(), "Hora establecida", Toast.LENGTH_SHORT).show();
                    }
                },hourActual, minuteActual, true
                );

                timePickerDialog.updateTime(hourActual, minuteActual);
                timePickerDialog.show();
            };
        });

        // NOTIFICACIÓN
        notification_establecer = view.findViewById(R.id.notification_establecer);
        notification_establecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setAlarm(alarmID, calendar.getTimeInMillis(), getActivity());
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                strDate = format.format(calendar.getTime());
                Toast.makeText(getActivity(), getString(R.string.changed_to, finalHour + ":" + finalMinute + " para el dia " + strDate), Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

}