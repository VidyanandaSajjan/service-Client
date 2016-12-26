package com.example.administrator.cweather;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.administrator.sweather.IAdd;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Weather;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText etValue1;
    Button bAdd,servc;
      String[] pss=new String[4];
    protected IAdd AddService;
    ServiceConnection AddServiceConnection;
    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;

    Weather weather=new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etValue1 = (EditText) findViewById(R.id.value1);
        bAdd = (Button) findViewById(R.id.add);
        servc=(Button)findViewById(R.id.serv);
        bAdd.setOnClickListener(this);

        //sets auto flipping

        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
        fade_in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();


//service connection start

        servc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initConnection();
            }
        });

    }

    void initConnection()
    {
        AddServiceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                AddService = null;

                Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();

                Log.d("IRemote", "Binding - Service disconnected");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                Log.i("RemoteClient","In onServicen binded...");

                AddService = IAdd.Stub.asInterface(service);

                Toast.makeText(getApplicationContext(), "Addition Service Connected", Toast.LENGTH_SHORT).show();

                Log.d("IRemote", "Binding is done - Service connected");
            }
        };



        if (AddService == null)
        {
            Intent it = new Intent();

            it.setClassName("com.example.administrator.sweather", "com.example.administrator.sweather.AdditionService");

            bindService(it, AddServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(AddServiceConnection);
    };

   //service connection end


//button
    @Override
    public void onClick(View v)
    {
        String cityName = etValue1.getText().toString();

if(!cityName.equals("")) {

    try {
        String rec = AddService.add(cityName);

        weather = JSONClass.getWeather(rec);

        pss[0] = cityName;

        int i = weather.main.getTemp();
        int temp = (i - 273);

        pss[1] = String.valueOf(temp);

        int seconds = weather.main.getDate();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date(seconds * 1000L));
        pss[2] = String.valueOf(dateString);


        pss[3] = String.valueOf(weather.coord.getDescription());

        Intent intent = new Intent(MainActivity.this, CityTemp.class);
        intent.putExtra("key", pss);
        startActivity(intent);

        //   Log.d("IRemote", "Binding - Add operation");


        Toast.makeText(getApplicationContext(), "" + cityName, Toast.LENGTH_SHORT).show();
    } catch (Exception e) {

        e.printStackTrace();
    }

}else {
    Toast.makeText(MainActivity.this, "enter city", Toast.LENGTH_SHORT).show();
}

        }

    }

