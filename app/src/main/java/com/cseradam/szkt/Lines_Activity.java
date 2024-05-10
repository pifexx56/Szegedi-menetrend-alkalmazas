package com.cseradam.szkt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lines_Activity extends AppCompatActivity {

    private static final String TAG = "android.os.Bundle";
    private ArrayList<Line> lines;
    private RecyclerView recyclerView;
    private LineAdapter lineAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lines);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lines = new ArrayList<Line>();
        Log.d(TAG, getIntent().getStringExtra("stop_name"));

        recyclerView = findViewById(R.id.recyclerviewLines);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        lineAdapter = new LineAdapter(this, lines);
        recyclerView.setAdapter(lineAdapter);



        String stopName = getIntent().getStringExtra("stop_name");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Stops").document(stopName).collection("lines")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String number = document.getId();
                            String terminus = (String) document.get("terminus");
                            ArrayList<String> times = (ArrayList<String>) document.get("time");
                            lines.add(new Line(number, terminus, times));
                        }
                        lineAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error getting documents", e);
                    }
                });



//// Létrehozunk egy listát az időpontokkal
//        List<String> times = Arrays.asList("12:15", "13:45");
//
//// Létrehozunk egy Map-et az időpontokkal
//        Map<String, Object> timesMap = new HashMap<>();
//        timesMap.put("times", times);
//        Map<String, String> asd = new HashMap<>();
//        asd.put("Ez egy", "Az egy");
//
//// Feltöltjük az adatbázist
//        db.collection("Stops").document("Vásárhelyi Pál utca").collection("90").document("times").set(timesMap);
//        db.collection("Stops").document("Vásárhelyi Pál utca").collection("2").document("times").set(timesMap);
//        db.collection("Stops").document("Annakút").collection("90").document("times").set(timesMap);
//        db.collection("Stops").document("Annakút").set(asd);
//
//        Map<String, String> terminusMap90 = new HashMap<>();
//        terminusMap90.put("terminus", "Szegedi Vasútállomás");
//        db.collection("Stops").document("Vásárhelyi Pál utca").collection("90").document("terminus").set(terminusMap90);
//
//        Map<String, String> terminusMap2 = new HashMap<>();
//        terminusMap2.put("terminus", "Európa liget");
//        db.collection("Stops").document("Vásárhelyi Pál utca").collection("2").document("terminus").set(terminusMap2);
//        db.collection("Stops").document("Annakút").collection("2").document("terminus").set(terminusMap2);



    }
}