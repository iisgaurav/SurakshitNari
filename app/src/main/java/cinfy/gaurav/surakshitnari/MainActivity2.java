package cinfy.gaurav.surakshitnari;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;

import cinfy.gaurav.surakshitnari.R;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity2 extends AppCompatActivity {


    Button b1, b2;
    private FusedLocationProviderClient client;
    DatabaseHandler myDB;
    private final int REQUEST_CHECK_CODE = 8989;
    private LocationSettingsRequest.Builder builder;
    String x = " ", y = " ";
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        myDB = new DatabaseHandler(this);
        final MediaPlayer mp = MediaPlayer.create( getApplicationContext(),R.raw.emergency_alarm );


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            startTrack();
        }


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(), Register.class);
                startActivity(i);


            }
        });
        b2.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mp.start();
                Toast.makeText(MainActivity2.this,"Emergency Button CLicked",Toast.LENGTH_LONG).show();
                return false;
            }
        } );


        b2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                loadData();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void loadData() {

        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contacts !!", Toast.LENGTH_LONG).show();
        } else {
            String msg = "I NEED HELP !! I AM IN DANGER !! MY LOCATION IS: LATITUDE-" + x + " LONGITUDE-" + y;
            String number = "";
            while (data.moveToNext()) {
                theList.add(data.getString(1));
                number = number + data.getString(1) + (data.isLast() ? "" : ";");
//                sendSMS(data.getString(1),msg);
                call();

            }
            if (!theList.isEmpty()) {
                sendSMS(number, msg, true);
            }

        }


    }

    private void sendSMS(String number, String msg, boolean call) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        smsIntent.putExtra("sms_body", msg);
        startActivity(smsIntent);
    }


    public void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void startTrack(){

        if (ActivityCompat.checkSelfPermission(
                MainActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity2.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                x = String.valueOf(lat);
                y = String.valueOf(longi);

            } else {
                Toast.makeText(this, "Unable To Find Location.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void call() {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:100"));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(i);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }
        }


    }}