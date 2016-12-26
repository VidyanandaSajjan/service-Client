package com.example.administrator.sweather;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;




public class AdditionService extends Service {



   static String op=null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {

        return mBinder;
    }
    private final IAdd.Stub mBinder = new IAdd.Stub()
    {

        public String add(String cityName) throws RemoteException

        {
                op=null;
            return cityMethod(cityName);
        }


     //API connection

        private String cityMethod(String cityName)
        {
            try {

                if (cityName != null) {
                    String URL = "http://api.openweathermap.org/data/2.5/weather?q=simla,in&appid=0f3357f1766089f012e21ea025feb45e";
                    URL = URL.replaceAll("simla", String.valueOf(cityName));
                    new ExecuteTask().execute(URL);

                   while (op == null)
                    {
                        Thread.sleep(10000);
                    }
                }
            }
            catch (Exception e)
            {

            }
                return op;


        }
        //connection end

        class ExecuteTask extends AsyncTask<String, Object, String>
        {


            @Override
            protected String doInBackground(String... params)
            {

                String res= String.valueOf(new HTTPClass().getWeatherData(params[0]));
                return res;
            }

            protected void onPostExecute(String w)
            {

                super.onPostExecute(w);


                op= w;
            }

        }

    };
}
