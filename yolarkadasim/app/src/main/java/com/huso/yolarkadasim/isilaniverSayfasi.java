package com.huso.yolarkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class isilaniverSayfasi extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText isinadiedittext,aciklamaedittext,varisisinadiedittext;
    internetdegistirlistener internetdegistirlistener=new internetdegistirlistener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isilaniver_sayfasi);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        isinadiedittext=findViewById(R.id.isinadi_edittext);
        varisisinadiedittext=findViewById(R.id.varisisinadi_edittext);
        aciklamaedittext=findViewById(R.id.aciklama_edittext);
    }

    public void isilaniverbutonu(View view){
        String isinadi=isinadiedittext.getText().toString();
        String varisadresi=varisisinadiedittext.getText().toString();
        String aciklama=aciklamaedittext.getText().toString();
        String isilaniniverenkisi=firebaseUser.getUid();

        Date date = Calendar.getInstance().getTime();//anlik tarihi almamimizi sagliyor
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String tarih = dateFormat.format(date);//tarih degerini stringe ceviriyor


        if (isinadi.matches("") || aciklama.matches("")){//isin adi ve aciklamasi bos kontrolu yapiliyor
            Toast.makeText(isilaniverSayfasi.this,"Bos Alan Mevcut",Toast.LENGTH_LONG).show();
        }else {//girilen degerleri veritabanina aktariyor her bir is ilanina ayri deger atiyor
            new AlertDialog.Builder(isilaniverSayfasi.this)
                    .setTitle("İs ilanını ver")
                    .setMessage("İs İlanını Vermek İstermisiniz?")
                    .setNegativeButton("Hayır", null)
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String,Object> isilanlaridata=new HashMap<>();
                            isilanlaridata.put("isinadi",isinadi);
                            isilanlaridata.put("varisadresi",varisadresi);
                            isilanlaridata.put("aciklama",aciklama);
                            isilanlaridata.put("isilaniniverenkisi",isilaniniverenkisi);
                            isilanlaridata.put("date", FieldValue.serverTimestamp());
                            isilanlaridata.put("tarih",tarih);
                            firebaseFirestore.collection("isilanlari").add(isilanlaridata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(isilaniverSayfasi.this,"İs ilani basariyla verildi...",Toast.LENGTH_LONG).show();
                                    isinadiedittext.setText("");
                                    aciklamaedittext.setText("");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(isilaniverSayfasi.this,"İş ilanı veremiyoruz daha sonra tekrar deneyiniz!",Toast.LENGTH_LONG).show();
                                }
                            });



                        }
                    }).show();

        }

    }
    @Override
    protected void onStart() {//internet baglanti kontrolu saglaniyor
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