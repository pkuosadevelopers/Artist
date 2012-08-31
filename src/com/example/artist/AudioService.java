package com.example.artist;

import android.app.Service;  
import android.content.Intent;  
import android.media.MediaPlayer;  
import android.os.Binder;  
import android.os.IBinder;   
/** 
 * Ϊ�˿���ʹ���ں�̨�������֣�������ҪService 
 * Service���������ں�̨���һЩ����Ҫ���û������Ķ��� 
 * 
 */  
public class AudioService extends Service implements MediaPlayer.OnCompletionListener{  
      
    MediaPlayer player;  
      
    private final IBinder binder = new AudioBinder();  
    @Override  
    public IBinder onBind(Intent arg0) {  
        // TODO Auto-generated method stub   
        return binder;  
    }  
    /** 
     * ��Audio�������ʱ�򴥷��ö��� 
     */  
    @Override  
    public void onCompletion(MediaPlayer player) {  
        // TODO Auto-generated method stub   
        stopSelf();//�����ˣ������Service   
    }  
      
    //������������Ҫʵ����MediaPlayer����   
    public void onCreate(){  
        super.onCreate();  
        //���Ǵ�raw�ļ����л�ȡһ��Ӧ���Դ���mp3�ļ�   
        player = MediaPlayer.create(this, R.raw.summer);  
        player.setOnCompletionListener(this);  
    }  
      
    /** 
     * �÷�����SDK2.0�ſ�ʼ�еģ����ԭ����onStart���� 
     */  
    public int onStartCommand(Intent intent, int flags, int startId){  
        if(!player.isPlaying()){  
            player.start();  
        }  
        return START_STICKY;  
    }  
      
    public void onDestroy(){  
        //super.onDestroy();   
        if(player.isPlaying()){  
            player.stop();  
        }  
        player.release();  
    }  
      
    //Ϊ�˺�Activity������������Ҫ����һ��Binder����   
    class AudioBinder extends Binder{  
          
        //����Service����   
        AudioService getService(){  
            return AudioService.this;  
        }  
    }  
      
    //���˲��Ž���   
    public void haveFun(){  
        if(player.isPlaying() && player.getCurrentPosition()>2500){  
            player.seekTo(player.getCurrentPosition()-2500);  
        }  
    }  
}  

