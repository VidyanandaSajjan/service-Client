package com.example.administrator.cweather;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CityTemp extends AppCompatActivity
{
    private DBHelper mydb ;
    int id_To_Update = 0;
    TextView tcity;
    TextView ttemp;
    TextView tdate;
    TextView tdescr;
    ImageView imgvw;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_temp);

        tcity=(TextView)findViewById(R.id.city_textView);
        ttemp=(TextView)findViewById(R.id.temp_textView);
        tdate=(TextView)findViewById(R.id.date_textView);
        tdescr=(TextView)findViewById(R.id.desc_textView);
        imgvw =(ImageView)findViewById(R.id.imageView);

        mydb = new DBHelper(this);

        String[] value= getIntent().getStringArrayExtra("key");


        //image view
        String imgs=value[3];

        if(imgs.equals("clear sky"))
        {
            imgvw.setImageResource(R.drawable.hot);
        }
        else if(imgs.equals("few clouds"))
        {
            imgvw.setImageResource(R.drawable.normal);
        }
        else if(imgs.equals("smoke"))
        {
            imgvw.setImageResource(R.drawable.humid);
        }
        else if(imgs.equals("haze"))
        {
            imgvw.setImageResource(R.drawable.rain);
        }
        else if(imgs.equals("few clouds"))
        {
            imgvw.setImageResource(R.drawable.humid);
        }
        else
        {
            imgvw.setImageResource(R.drawable.normal);
        }

        //set the data

        tcity.setText(value[0]);
        ttemp.setText(value[1]+(char) 0x00B0+"C");
        tdate.setText(value[2]);
        tdescr.setText(value[3]);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

//store data
     public void run(View view)
    {
        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            int Value = extras.getInt("id");

            if(Value>0)
            {
                if(mydb.updateContact(id_To_Update,tcity.getText().toString(), ttemp.getText().toString(), tdate.getText().toString(), tdescr.getText().toString()))
                {
                    Toast.makeText(CityTemp.this, "Updated", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CityTemp.this,MainActivity.class);

                    startActivity(intent);
                }

                else
                {
                    Toast.makeText(CityTemp.this, "not Updated", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                if(mydb.insertContact(tcity.getText().toString(), ttemp.getText().toString(), tdate.getText().toString(), tdescr.getText().toString()))
                {
                    Toast.makeText(CityTemp.this, "done",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(CityTemp.this, "not done",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(CityTemp.this,MainActivity.class);
                startActivity(intent);
            }
        }
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder(getApplicationContext()).setContentTitle("weather").setContentText("saved your data").
        setContentTitle("report").setSmallIcon(R.drawable.img).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }

}
