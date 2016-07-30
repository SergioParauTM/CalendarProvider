package company.calendarprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    Button addEventButton;
    EditText titulo;
    EditText descripcion;
    EditText lugar;
    Button recuperar;
    EditText evento;

    long calID = 3;
    long startMillis = 0;
    long endMillis = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEventButton = (Button) findViewById(R.id.addEventButton);
        titulo = (EditText) findViewById(R.id.Titulo);
        descripcion = (EditText) findViewById(R.id.desc);
        lugar = (EditText) findViewById(R.id.lugar);
        recuperar = (Button) findViewById(R.id.recuperar);
        evento = (EditText) findViewById(R.id.evento);


        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrircalendario();
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titulo.length() != 0 && descripcion.length() != 0 && lugar.length() != 0) {
                    onAddEventClicked();
                    Toast.makeText(getBaseContext(), "se ha añadido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "falta completar campo/s", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onAddEventClicked() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2014, 9, 14, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2016, 9, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();


        String tit = titulo.getText().toString();
        String desc = descripcion.getText().toString();
        String lug = lugar.getText().toString();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE,tit);
        values.put(CalendarContract.Events.DESCRIPTION, desc);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, lug);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());


        }

        public void abrircalendario(){

          int eventoid = Integer.parseInt(evento.getText().toString());
            long eventID = eventoid;

            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            Intent intent = new Intent(Intent.ACTION_VIEW)
                    .setData(uri);
            startActivity(intent);
        }

    }
