package com.example.nutritrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialiser la session utilisateur
        userSession = UserSession.getInstance(this);
        
        // Vérifier si l'utilisateur est connecté
        if (!userSession.isLoggedIn()) {
            // Rediriger vers l'activité de connexion
            navigateToLogin();
            return;
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        int itemId = item.getItemId();
                        if (itemId == R.id.action_dashboard) {
                            selectedFragment = new DashboardFragment();
                        } else if (itemId == R.id.action_food) {
                            selectedFragment = new FoodFragment();
                        } else if (itemId == R.id.action_scan) {
                            selectedFragment = new ScanFragment();
                        } else if (itemId == R.id.action_progress) {
                            selectedFragment = new ProgressFragment();
                        } else if (itemId == R.id.action_profile) {
                            selectedFragment = new ProfileFragment();
                        }

                        if (selectedFragment != null) {
                            loadFragment(selectedFragment);
                            return true;
                        }

                        return false;
                    }
                });

        // Charger le fragment par défaut (Tableau de bord)
        loadFragment(new DashboardFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        // Passer l'ID de l'utilisateur au fragment
        Bundle args = new Bundle();
        args.putInt("userId", userSession.getUserId());
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void logout() {
        userSession.logout();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}