package com.huso.yolarkadasim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class isilanlariSayfasi extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private RecyclerView isilanlarirecyclerview;
    private SearchView searchViewrecyclerview;
    recyclerview_isilanlari_adapter recyclerview_isilanlari_adapter;
    private ArrayList<isilanlaridizisi> isilaniidlist;//is ilanlari dizisindekileri arraye atiyor
    internetdegistirlistener internetdegistirlistener=new internetdegistirlistener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isilanlari_sayfasi);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        searchViewrecyclerview=findViewById(R.id.search_bar_arama);
        isilanlarirecyclerview=findViewById(R.id.isilanlari_recyclerView);

        isilaniidlist=new ArrayList<>();
        isilanlarirecyclerview.setVisibility(View.INVISIBLE);


        searchview();
        isilanlari();
    }

    public void searchview(){//search view da arama islemlerinin gerceklestirilmesi saglaniyor
        searchViewrecyclerview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isilanlarirecyclerview.setVisibility(View.VISIBLE);
                if (recyclerview_isilanlari_adapter!=null){//adapterin bos olup olmaması kontolu yapiliyor
                    recyclerview_isilanlari_adapter.getFilter().filter(newText);//girilen degere gore arama islemleri gerceklestiriliyor
                }
                if (newText.matches("")){
                    isilanlarirecyclerview.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

    }
    public void isilanlari(){//veritabanindan isilanlarinin verilerini cekiyor
        CollectionReference collectionReference=firebaseFirestore.collection("isilanlari");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {//is ilanlarini tarihlerine gore siraliyor
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                isilaniidlist.clear();
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        Map<String,Object> isilanidata=snapshot.getData();
                        String isinadi=(String) isilanidata.get("isinadi");
                        String varisisinadi=(String) isilanidata.get("varisadresi");
                        String isilanlariid=snapshot.getId();
                        String isilaniniverenkullanici=(String) isilanidata.get("isilaniniverenkisi");
                        String isilaninintarihi=(String) isilanidata.get("tarih");



                        isilanlaridizisi isilanlaridizisi=new isilanlaridizisi(isilanlariid,isinadi,varisisinadi,isilaniniverenkullanici,isilaninintarihi);
                        isilaniidlist.add(isilanlaridizisi);//cekilen is ilanlarini bir liste yapisi seklinde diziye atiyor


                        isilanlarirecyclerview.setLayoutManager(new LinearLayoutManager(isilanlariSayfasi.this));//recyclerview ın kaydırılması
                        recyclerview_isilanlari_adapter=new recyclerview_isilanlari_adapter(isilaniidlist,firebaseFirestore,firebaseUser);//isilanindaki degerleri adaptera aktariyoruz
                        isilanlarirecyclerview.setAdapter(recyclerview_isilanlari_adapter);//burada recyclerview ile adaptera bağlıyoruz
                        recyclerview_isilanlari_adapter.notifyDataSetChanged();


                    }
                }
            }
        });
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