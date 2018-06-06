package edu.skku.se3.beanhappy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class beacon extends Service implements Runnable{

    private int count=0;

    @Override
    public void onCreate(){
        super.onCreate();
        //service에서 가장 먼저 호출됨(최초에 한번
        Thread thread=new Thread(this);
        thread.start();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
        //service 객체->화면단(Activity) 사이에 데이터 주고 받을때 사용됨
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //service 가 종료될때 (stopService() call된 후) 실행
    }

    @Override
    public void run(){

    }

    //serviceIntent=new Intent(this, beacon.class);
    //serviceIntent.putExtra("count","1");
    //startService(serviceIntent); //stopService(ServiceIntent)
//

}
