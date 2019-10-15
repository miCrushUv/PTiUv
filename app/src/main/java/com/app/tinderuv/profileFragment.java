package com.app.tinderuv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class profileFragment extends Fragment {

    //Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    //views from xml
    ImageView avatarIv;
    TextView userName;

    ImageView profilePhoto;

    TextView nombreTxt, fraseTxt, universidadTxt, facultadTxt, edadTxt, estadoTxt, carreraTxt, ejercicioTxt, hijosTxt, estaturaTxt, complexionTxt,
            orientacionTxt, generoTxt;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Start firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");

        //Find views to load info
        avatarIv = view.findViewById(R.id.avatarIv);
        setupInfo(view);
        return view;
    }
    private void setupInfo(View view) {

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");

        //Init views
        profilePhoto = view.findViewById(R.id.profileImage);

        nombreTxt = view.findViewById(R.id.nombreProfileTxt);

        ejercicioTxt = view.findViewById(R.id.ejercicioProfileTxt);

        universidadTxt = view.findViewById(R.id.universidadProfileTxt);

        fraseTxt = view.findViewById(R.id.fraseProfileTxt);

        facultadTxt = view.findViewById(R.id.facultadProfileTxt);

        edadTxt = view.findViewById(R.id.edadProfileTxt);

        estadoTxt = view.findViewById(R.id.estadoProfileTxt);

        carreraTxt = view.findViewById(R.id.carreraProfileTxt);

        hijosTxt = view.findViewById(R.id.hijosProfileTxt);

        estaturaTxt = view.findViewById(R.id.estaturaProfileTxt);

        complexionTxt = view.findViewById(R.id.complexionProfileTxt);

        orientacionTxt = view.findViewById(R.id.orientacionProfileTxt);

        generoTxt = view.findViewById(R.id.generoProfileTxt);

        //connect to database

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String estado = ""+ds.child("Estado").getValue();
                    String genero = ""+ds.child("Genero").getValue();
                    String orientacion= ""+ds.child("Orientacion").getValue();
                    String carrera = ""+ds.child("carrera").getValue();
                    String complexion = ""+ds.child("complexion").getValue();
                    String ejercicio = ""+ds.child("ejercicio").getValue();
                    String estatura = ""+ds.child("estatura").getValue();
                    String facultad = ""+ds.child("facultad").getValue();
                    String foto = ""+ds.child("foto").getValue();
                    String frase = ""+ds.child("frase").getValue();
                    String hijos = ""+ds.child("hijos").getValue();
                    String nombre = ""+ds.child("nombre").getValue();
                    String universidad = ""+ds.child("universidad").getValue();

                    //set data into the view
                    nombreTxt.setText(nombre);
                    estadoTxt.setText("Estado: " + estado);
                    carreraTxt.setText("Carrera: " + carrera);
                    ejercicioTxt.setText("Ejercicio : " + ejercicio);
                    facultadTxt.setText("Facultad: " + facultad);
                    fraseTxt.setText("Frase: " + frase);
                    hijosTxt.setText("Hijos: " + hijos);
                    universidadTxt.setText("Universidad: " + universidad);
                    estaturaTxt.setText("Estatura: "+ estatura);
                    generoTxt.setText("Genero: "+ genero);
                    orientacionTxt.setText("Orientacion: "+  orientacion);
                    complexionTxt.setText("Complexion: "+  complexion);


                    //Load profile photo to the imageView
                    try{
                        Picasso.get().load(foto).into(profilePhoto);
                    }
                    catch (Exception e){
                        //do something whit the profile photo if it was impossible to load it
                        Picasso.get().load(foto).into(profilePhoto);
                        System.out.println(e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
