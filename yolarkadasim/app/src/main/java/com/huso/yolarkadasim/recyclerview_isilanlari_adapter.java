package com.huso.yolarkadasim;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class recyclerview_isilanlari_adapter extends RecyclerView.Adapter<recyclerview_isilanlari_adapter.Postisilanlari> {
    ArrayList<isilanlaridizisi> isilaniidlist;
    ArrayList<isilanlaridizisi> getIsilaniidlist;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    //Gonderilen degerler cekilerek recyclerview ayarlanmasi saglaniyor
    public recyclerview_isilanlari_adapter(ArrayList<isilanlaridizisi> isilaniidlist,FirebaseFirestore firebaseFirestore,FirebaseUser firebaseUser) {
        this.isilaniidlist=isilaniidlist;
        this.getIsilaniidlist=isilaniidlist;
        this.firebaseFirestore=firebaseFirestore;
        this.firebaseUser=firebaseUser;

    }
    @NonNull
    @Override
    public Postisilanlari onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recyclerview_isilanlari,parent,false);
        return new recyclerview_isilanlari_adapter.Postisilanlari(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Postisilanlari holder, int position) {
        //buradaki degerler recyclerview daki textlere ayarlanma islemi yapiliyor
        Context context=holder.itemView.getContext();
        isilanlaridizisi temp=isilaniidlist.get(holder.getAdapterPosition());
        holder.isinadi.setText(" " + isilaniidlist.get(holder.getAdapterPosition()).getIsinadi()+"-"+isilaniidlist.get(holder.getAdapterPosition()).getvarisIsinadi());
        holder.isilanitarihi.setText(isilaniidlist.get(holder.getAdapterPosition()).getIsilaninintarihi());
        //isinadina,turkiyeyazisina,turkiyeyazisiimageviewa,ekiparkadasi yazisindan herhangi birine tiklanmasinda ayni islemi yapip o is ilani bilgilerine ulasiyor
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,isilaniGorSayfasi.class);
                intent.putExtra("isilaniid",temp.getIsilaniid());
                context.startActivity(intent);
            }
        });

        holder.isilanisil.setVisibility(View.INVISIBLE);
        if (isilaniidlist.get(holder.getAdapterPosition()).getIsilaniverenkullanici().matches(firebaseUser.getUid())){//is ilani veren kullanici kendi sayfasinda silme butonunun acik olacagini gosteriyor
            holder.isilanisil.setVisibility(View.VISIBLE);//is ilanı sil butonunun acilmasi
            holder.isilanisil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("İs ilanını sil")
                            .setMessage("İs İlanını Listeden Kaldırmak İstermisiniz?")
                            .setNegativeButton("Hayır", null)
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //tiklanilan is ilani id sine gore silme islemi gerceklestiriliyor
                                    firebaseFirestore.collection("isilanlari").document(isilaniidlist.get(holder.getAdapterPosition()).getIsilaniid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "İs ilanı basarı ile silindi", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return isilaniidlist.size();
    }

    public Filter getFilter() {//is ilanlarini girilen isimlere gore arama islemini gerceklestirir filtreleme islemlerini yapar
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults=new FilterResults();
            if(charSequence==null | charSequence.length()==0){
                filterResults.count=getIsilaniidlist.size();
                filterResults.values=getIsilaniidlist;
            }else {
                String searchar=charSequence.toString().toLowerCase();
                List<isilanlaridizisi> data=new ArrayList<>();

                for (isilanlaridizisi isilanlaridizisi:getIsilaniidlist){
                    if (isilanlaridizisi.getIsinadi().toLowerCase().contains(searchar)){
                        data.add(isilanlaridizisi);
                    }
                }
                filterResults.count=data.size();
                filterResults.values=data;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            isilaniidlist=(ArrayList<isilanlaridizisi>) filterResults.values;
            notifyDataSetChanged();

        }
    };


    class Postisilanlari extends RecyclerView.ViewHolder{
        TextView isinadi,isilanitarihi,turkiyeyazisi,ekiparkadasiyazisi;
        ImageView isilanisil,turkiyeyazisiimageview;
        public Postisilanlari(@NonNull View itemView) {
            super(itemView);
            isinadi=itemView.findViewById(R.id.isinadi_textview);
            isilanisil=itemView.findViewById(R.id.isilanisil_imageview);
            isilanitarihi=itemView.findViewById(R.id.isilani_tarih_textview);
            turkiyeyazisi=itemView.findViewById(R.id.Turkiye_yazisi_textview);
            ekiparkadasiyazisi=itemView.findViewById(R.id.Ekiparkadasi_yazisi_textview);
            turkiyeyazisiimageview=itemView.findViewById(R.id.Turkiye_yazisi_imageview);
        }
    }
}
