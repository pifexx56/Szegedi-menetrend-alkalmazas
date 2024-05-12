package com.cseradam.szkt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {

    private static final String TAG = "Kolbász";
    FirebaseAuth auth;
    RecyclerView recyclerView;
    ArrayList<Stop> stopList;
    private StopsAdapter mStopAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null)
        {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        stopList = new ArrayList<>();
        mStopAdapter = new StopsAdapter(this, stopList);
        recyclerView.setAdapter(mStopAdapter);
        progressDialog = new ProgressDialog(this);

        Log.w("asd", "asd");
        progressDialog.setTitle("Kérem várjon!");
        progressDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        intializeData();
    }

    private void intializeData()
    {
        stopList.clear();

        db.collection("Stops")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> stopNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d(TAG, document.getId());
                            stopList.add(new Stop(document.getId()));
                        }
                        mStopAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_page_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        MenuItem admin = menu.findItem(R.id.admin);
        if (auth.getCurrentUser().getEmail().contains("admin.com")) {
            admin.setVisible(true);
        } else {
            admin.setVisible(false);
        }

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mStopAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout)
        {
            auth.signOut();
            startActivity(new Intent(MainPage.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        if (item.getItemId() == R.id.admin)
        {
            Intent intent = new Intent(MainPage.this, AdminActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.Reminder)
        {
            Intent intent = new Intent(MainPage.this, ReminderActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}