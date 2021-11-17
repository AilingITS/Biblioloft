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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.biblioloft.MainActivity;
import com.example.biblioloft.R;
import com.example.biblioloft.fragmentsUser.alarm.NotificationService;
import com.example.biblioloft.fragmentsUser.alarm.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    private Button notification_establecer, notification_borrar;



    //private static final String channelID = "canal";
    //private PendingIntent pendingIntent;


    //Barra de progreso del libro actual que se esta leyendo
    private int CurrentProgress = 0;
    private ProgressBar progressBar;
    private Button startProgress;
    private TextView main_Nump_aginasLeidas;

    //Hora
    private TextView notificationsTime;
    private Button change_notification;
    private int alarmID = 1;
    private SharedPreferences settings;

    //Fecha
    private TextView programar_fecha;
    private static final String TAG = "CalendarActivity";
    private CalendarView calendarView;
    String date, strDate;
    //Calendar actual = Calendar.getInstance();
    //Calendar calendar = Calendar.getInstance();

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




        /*notification_establecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    showNotification();
                } else {
                    showNewNotification();
                }
            }
        });*/












        //BARRA DE PROGRESO - paginas leidas
        //progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        //startProgress = (Button) view.findViewById(R.id.startProgress);
        //main_Nump_aginasLeidas = (TextView) view.findViewById(R.id.main_Nump_aginasLeidas);

        /*startProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentProgress = CurrentProgress + 10;
                progressBar.setProgress(CurrentProgress);
                progressBar.setMax(100);
            }
        });*/

        /*settings = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        String hour, minute;

        hour = settings.getString("hour","");
        minute = settings.getString("minute","");*/

        //notificationsTime = (TextView) view.findViewById(R.id.notifications_time);
        //change_notification = (Button) view.findViewById(R.id.change_notification);
        //calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        //programar_fecha = (TextView) view.findViewById(R.id.programar_fecha);

        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        //strDate = format.format(calendar.getTime());
        //programar_fecha.setText(strDate);

        /*calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);

                date = dayOfMonth + "/" + (month+1) + "/" + year;
                programar_fecha.setText(date);
                Toast.makeText(getContext(), "Fecha: " + date, Toast.LENGTH_SHORT).show();
            }
        });

        // Al momento de dar clic en seleccionar hora se establecen los valores actuales del dispositivo
        if(hour.length() > 0) {
            notificationsTime.setText(hour + ":" + minute);
        }*/

        /*change_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // Los valores que seleccionas se guardan
                    int hour = actual.get(Calendar.HOUR_OF_DAY);
                    int minute = actual.get(Calendar.MINUTE);

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String finalHour, finalMinute;

                            finalHour = "" + selectedHour;
                            finalMinute = "" + selectedMinute;
                            if (selectedHour < 10) finalHour = "0" + selectedHour;
                            if (selectedMinute < 10) finalMinute = "0" + selectedMinute;
                            notificationsTime.setText(finalHour + ":" + finalMinute);

                            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                            calendar.set(Calendar.MINUTE, selectedMinute);
                            calendar.set(Calendar.SECOND, 0);

                            SharedPreferences.Editor edit = settings.edit();
                            edit.putString("hour", finalHour);
                            edit.putString("minute", finalMinute);

                            //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
                            edit.putInt("alarmID", alarmID);
                            edit.putLong("alarmTime", calendar.getTimeInMillis());

                            edit.commit();

                            strDate = format.format(calendar.getTime());
                            Toast.makeText(getActivity(), getString(R.string.changed_to, finalHour + ":" + finalMinute + " para el dia " + strDate), Toast.LENGTH_LONG).show();

                            Utils.setAlarm(alarmID, calendar.getTimeInMillis(), getContext());
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle(getString(R.string.programar_hora));
                    mTimePicker.show();
            }
        });*/

        return view;
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(){
        NotificationChannel channel = new NotificationChannel(channelID,
                "NEW", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        showNewNotification();
    }

    private void showNewNotification(){
        setPendingIntent(MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext(),
                channelID)
                .setWhen(calendar.getTimeInMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Sesión terminada")
                .setContentText("No olvides registrar las páginas.")
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getActivity().getApplicationContext());
        managerCompat.notify((int) calendar.getTimeInMillis(), builder.build());
    }

    private void setPendingIntent(Class<?> clsActivity){
        Intent intent = new Intent(getActivity(), clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        stackBuilder.addParentStack(clsActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }*/
}