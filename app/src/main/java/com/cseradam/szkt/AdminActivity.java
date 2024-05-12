package com.cseradam.szkt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final String TAG = "Paprikas";
    private EditText stopNameET, lineNumberET, terminusET, timesET;
    private TextView error;
    private String stopName, lineNumber, terminus, times;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = FirebaseFirestore.getInstance();
        stopNameET = findViewById(R.id.stop_name);
        lineNumberET = findViewById(R.id.line_number);
        terminusET = findViewById(R.id.terminus);
        timesET = findViewById(R.id.times);
        error = findViewById(R.id.errorbox);

        auth = FirebaseAuth.getInstance();
        if (!auth.getCurrentUser().getEmail().contains("@admin.com"))
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }
    private void stringUpdate()
    {
        stopName = stopNameET.getText().toString();
        lineNumber = lineNumberET.getText().toString();
        terminus = terminusET.getText().toString();
        times = timesET.getText().toString();
    }

    public void deleteLine(View view)
    {
        stringUpdate();
        if (stopName.isEmpty() || stopName.length() == 0 || lineNumber.isEmpty() || lineNumber.length() == 0){
            error.setText("Nem adtál meg megálló vagy vonal nevet!");
            return;
        }
        db.collection("Stops").document(stopName).collection("lines").document(lineNumber)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        error.setText("Sikeres törlés");

                        // Check if there are any more lines at the stop
                        db.collection("Stops").document(stopName).collection("lines")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot querySnapshot = task.getResult();
                                            if (querySnapshot.isEmpty()) {
                                                // If no more lines, delete the stop
                                                db.collection("Stops").document(stopName)
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "Stop successfully deleted!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error deleting stop", e);
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        error.setText("Sikertelen törlés, valószínűleg hálózati hiba!");
                    }
                });


    }

    public void updateLine(View view)
    {
        stringUpdate();
        if (stopName.isEmpty() || stopName.length() == 0 || lineNumber.isEmpty() || lineNumber.length() == 0 || terminus.isEmpty() || times.isEmpty()){
            error.setText("Nem adtál meg megálló vagy vonal nevet!");
            return;
        }

        // Az időpontokat tartalmazó String felbontása ArrayList<String>-gé
        ArrayList<String> timesList = new ArrayList<>();
        String[] timesArray = times.split(",");
        for (String time : timesArray) {
            timesList.add(time.trim());
        }

        Map<String, Object> line = new HashMap<>();
        line.put("lineNumber", lineNumber);
        line.put("terminus", terminus);
        line.put("time", timesList);

        db.collection("Stops").document(stopName).collection("lines").document(lineNumber)
                .update(line)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        error.setText("Vonal sikeresen frissítve!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        error.setText("Vonal frissítése sikertelen, valószínűleg hálózati hiba!");
                    }
                });
    }
    public void createLine(View view)
    {
        stringUpdate();
        if (stopName.isEmpty() || stopName.length() == 0 || lineNumber.isEmpty() || lineNumber.length() == 0 || terminus.isEmpty() || times.isEmpty()){
            error.setText("Nem adtál meg megálló vagy vonal nevet!");
            return;
        }

        // Az időpontokat tartalmazó String felbontása ArrayList<String>-gé
        ArrayList<String> timesList = new ArrayList<>();
        String[] timesArray = times.split(",");
        for (String time : timesArray) {
            timesList.add(time.trim());
        }

        Map<String, Object> line = new HashMap<>();
        line.put("lineNumber", lineNumber); // vonalszám hozzáadása
        line.put("terminus", terminus);
        line.put("time", timesList);

        db.collection("Stops").document(stopName).collection("lines").document(lineNumber)
                .set(line)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        error.setText("Vonal sikeresen felvéve!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        error.setText("Vonal felvétele sikertelen, valószínűleg hálózati hiba!");
                    }
                });

        Map<String, Object> stop = new HashMap<>();
        stop.put("createdBy", "admin által");

        db.collection("Stops").document(stopName)
                .set(stop)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        error.setText("Megálló sikeresen felvéve!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        error.setText("Megálló felvétele sikertelen, valószínűleg hálózati hiba!");
                    }
                });
    }

    public void callBack(View view)
    {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }
}