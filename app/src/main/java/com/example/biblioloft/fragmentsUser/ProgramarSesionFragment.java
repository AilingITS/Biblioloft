package com.example.biblioloft.fragmentsUser;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.biblioloft.R;
import com.example.biblioloft.fragmentsUser.alarm.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProgramarSesionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
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
    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();

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

        settings = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        String hour, minute;

        hour = settings.getString("hour","");
        minute = settings.getString("minute","");

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
        }

        change_notification.setOnClickListener(new View.OnClickListener() {
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
}