package com.example.nutritrack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ScanFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private Button btnScanBarcode;
    private Button btnScanNutrition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        
        btnScanBarcode = view.findViewById(R.id.btn_scan_barcode);
        btnScanNutrition = view.findViewById(R.id.btn_scan_nutrition);
        
        btnScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    // Lancer le scan du code-barres
                    startBarcodeScanner();
                }
            }
        });
        
        btnScanNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    // Lancer le scan de l'étiquette nutritionnelle
                    startNutritionScanner();
                }
            }
        });
        
        return view;
    }
    
    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }
    
    private void startBarcodeScanner() {
        // À implémenter: lancer l'activité de scan de code-barres
        // Utilisation possible de ML Kit ou ZXing
    }
    
    private void startNutritionScanner() {
        // À implémenter: lancer l'activité de scan d'étiquette nutritionnelle
        // Utilisation possible de ML Kit Text Recognition + analyse personnalisée
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission accordée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission refusée, impossible de scanner", Toast.LENGTH_SHORT).show();
            }
        }
    }
}