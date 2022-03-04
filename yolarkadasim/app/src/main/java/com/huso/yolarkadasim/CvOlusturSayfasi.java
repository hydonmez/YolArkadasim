package com.huso.yolarkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CvOlusturSayfasi extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private AutoCompleteTextView cinsiyetspinner,ogrenimspinner;
    private TextInputEditText advesoyadedittext,yasedittext,yabancidiledittext,sehiredittext,emailedittext,telefonedittext,tecrubeedittext,digeredittext;
    internetdegistirlistener internetdegistirlistener=new internetdegistirlistener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_olustur_sayfasi);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        advesoyadedittext=findViewById(R.id.advesoyad_cv_edittext);
        yasedittext=findViewById(R.id.yas_cv_edittext);
        cinsiyetspinner=findViewById(R.id.cinsiyet_spinner);
        yabancidiledittext=findViewById(R.id.yabancidil_cv_edittext);
        sehiredittext=findViewById(R.id.sehir_cv_edittext);
        emailedittext=findViewById(R.id.email_cv_edittext);
        telefonedittext=findViewById(R.id.telefon_cv_edittext);
        ogrenimspinner=findViewById(R.id.ogrenimdurumu_spinner);
        tecrubeedittext=findViewById(R.id.tecrube_cv_edittext);
        digeredittext=findViewById(R.id.diger_cv_edittext);

        //String de yazilan spinner icin degerleri xml de tanimlanan spinnera ekler
        ArrayAdapter<String> cinsiyetadapter=new ArrayAdapter<String>(CvOlusturSayfasi.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.cinsiyet));
        cinsiyetadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cinsiyetspinner.setAdapter(cinsiyetadapter);

        ArrayAdapter<String> ogrenimadapter=new ArrayAdapter<String>(CvOlusturSayfasi.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.ogrenimdurumlari));
        ogrenimadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ogrenimspinner.setAdapter(ogrenimadapter);
    }

    public void cvkaydetbutonu(View view){
        String advesoyad=advesoyadedittext.getText().toString();//edit text den alinan verileri stringe cevirir
        String yas=yasedittext.getText().toString();
        String cinsiyet=cinsiyetspinner.getText().toString();
        String yabancidil=yabancidiledittext.getText().toString();
        String sehir=sehiredittext.getText().toString();
        String email=emailedittext.getText().toString();
        String telefon=telefonedittext.getText().toString();
        String ogrenim=ogrenimspinner.getText().toString();
        String tecrube=tecrubeedittext.getText().toString();
        String diger=digeredittext.getText().toString();

        String sayfadakikullanici=firebaseUser.getUid();//sayfadaki kisinin id si stringe atar

        //Girilen degerleri sayfadaki kisinin idsine gore firestore kayitlanma islemleri gerceklestirilir
        DocumentReference documentReference=firebaseFirestore.collection("Profiller").document(sayfadakikullanici);
        HashMap<String,Object> cvdata=new HashMap<>();
        cvdata.put("advesoyad",advesoyad);
        cvdata.put("yas",yas);
        cvdata.put("cinsiyet",cinsiyet);
        cvdata.put("yabancidil",yabancidil);
        cvdata.put("sehir",sehir);
        cvdata.put("email",email);
        cvdata.put("telefon",telefon);
        cvdata.put("ogrenim",ogrenim);
        cvdata.put("tecrube",tecrube);
        cvdata.put("diger",diger);
        documentReference.set(cvdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CvOlusturSayfasi.this,"Profiliniz basariyla kaydedildi...",Toast.LENGTH_SHORT) .show();
                Intent intent=new Intent(CvOlusturSayfasi.this,CvSayfasi.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CvOlusturSayfasi.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT) .show();
            }
        });

    }
    @Override
    protected void onStart() {//internet baglanti kontrolu gerceklestirilir
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetdegistirlistener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(internetdegistirlistener);
        super.onStop();
    }
}