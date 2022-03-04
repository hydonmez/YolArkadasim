package com.huso.yolarkadasim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisAnimasyonSayfasi extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_girisanimasyonu);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {//burada 3 sn giriş animasyonu açılıp sayfalara yönlendirilmesi sağlanıyor
                if (firebaseUser!=null){
                    Intent intent=new Intent(GirisAnimasyonSayfasi.this,Anasayfa.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(GirisAnimasyonSayfasi.this,KullaniciGirisSayfasi.class);
                    startActivity(intent);
                    finish();

                }

            }
        },3000);//kac saniye ekranda kalacagi bilgisini verir
    }
}