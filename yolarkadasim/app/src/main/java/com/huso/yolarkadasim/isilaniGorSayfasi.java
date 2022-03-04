package com.huso.yolarkadasim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class isilaniGorSayfasi extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private TextView isinaditextview,aciklamatextview;
    private Button basvurbutonu;
    internetdegistirlistener internetdegistirlistener=new internetdegistirlistener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isilani_gor_sayfasi);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        aciklamatextview=findViewById(R.id.aciklama_gor_textview);
        isinaditextview=findViewById(R.id.isinadi_gor_textview);
        basvurbutonu=findViewById(R.id.basvur_got_butonu);

        Intent intent=getIntent();//isilanlari adapterdaki gonderilen degeri burada aliyoruz
        String isilaniid=intent.getStringExtra("isilaniid");
        isilaninigor(isilaniid);

    }
    public void isilaninigor(String isilaniid){//Gonderilen id ye gore veritabanindan o bilgileri cekiyoruz
        firebaseFirestore.collection("isilanlari").document(isilaniid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot!=null){
                    String isinadi=documentSnapshot.getString("isinadi");
                    String varisisinadi=documentSnapshot.getString("varisadresi");
                    String aciklama=documentSnapshot.getString("aciklama");
                    String isilaniniverenkullanici=documentSnapshot.getString("isilaniniverenkisi");

                    isinaditextview.setText(isinadi+"-"+varisisinadi);
                    aciklamatextview.setText(aciklama);


                    String sayfadakikisi=firebaseUser.getUid();

                    if (isilaniniverenkullanici!=null) {//is ilanini veren kullanicinin bos olmamasinin kontrolu saglaniyor
                        if (sayfadakikisi.matches(isilaniniverenkullanici)) {//Sayfadaki kisi eger is ilanini veren kisi ise basvur butonunun gosterilip gosterilmemesinin kontrolu saglanir
                            basvurbutonu.setVisibility(View.INVISIBLE);
                        }
                        else {
                            basvurbutonu.setVisibility(View.VISIBLE);

                            basvurbutonu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(isilaniGorSayfasi.this);
                                    builder.setTitle("Basvuru Yap");
                                    builder.setMessage("Basvuru Yapmak İstermisiniz ?");
                                    builder.setNegativeButton("HAYIR",null);
                                    builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //basvur butonuna tiklaninca tiklayan kisinin bilgileri ve is ilani idleri ile veritabanina istek listesi olusturulur
                                            String basvurbutonunabasankisi = firebaseUser.getUid();
                                            HashMap<String, Object> istekdata = new HashMap<>();
                                            istekdata.put("isilaniniverenkisi", isilaniniverenkullanici);
                                            istekdata.put("basvurbutonunabasankisi", basvurbutonunabasankisi);
                                            istekdata.put("basvurulanisilani", isilaniid);
                                            firebaseFirestore.collection("isteklistesi").add(istekdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(isilaniGorSayfasi.this, "İs İlanina Basarili Bir Sekilde Basvuruldu...", Toast.LENGTH_LONG).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(isilaniGorSayfasi.this, "İş ilanına şuan başvurulamıyor daha sonra tekrar deneyiniz!", Toast.LENGTH_LONG).show();


                                                }
                                            });

                                        }
                                    });
                                    builder.show();

                                }
                            });
                            basvurbutonunubasvurulduyap(isilaniid);
                        }


                    }
                }

            }
        });
    }

    public void basvurbutonunubasvurulduyap(String isilaniid){//basvur butonunu basvuran kisi hangi ilana basvurduysa o ilani basvuruldu yapar
        String sayfadakikisi=firebaseUser.getUid();
        firebaseFirestore.collection("isteklistesi").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String basvurankisi=(String) snapshot.getString("basvurbutonunabasankisi");
                        String basvurulanilan=(String) snapshot.getString("basvurulanisilani");
                        if (sayfadakikisi.matches(basvurankisi) && isilaniid.matches(basvurulanilan)){//sayfadaki eger basvuran kisi ise ve ilanin idsi basvurulan id ise o ilandakini basvuruldu yapar
                            basvurbutonu.setText("BASVURULDU");
                            basvurbutonu.setEnabled(false);
                        }
                    }
                }
            }
        });

    }
    @Override
    protected void onStart() {//internet baglanti kontolu gerceklestirilir
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