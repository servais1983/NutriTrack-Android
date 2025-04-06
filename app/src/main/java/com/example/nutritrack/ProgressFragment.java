package com.example.nutritrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutritrack.database.entity.WeightHistoryEntity;
import com.example.nutritrack.viewmodel.WeightViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressFragment extends Fragment {

    private TextView tvCurrentWeight;
    private EditText etNewWeight;
    private Button btnUpdateWeight;
    private LineChart lineChartWeight;
    
    private WeightViewModel weightViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        
        tvCurrentWeight = view.findViewById(R.id.tv_current_weight);
        etNewWeight = view.findViewById(R.id.et_new_weight);
        btnUpdateWeight = view.findViewById(R.id.btn_update_weight);
        lineChartWeight = view.findViewById(R.id.line_chart_weight);
        
        // Configurer le ViewModel
        weightViewModel = new ViewModelProvider(this).get(WeightViewModel.class);
        
        // Charger le poids actuel
        loadCurrentWeight();
        
        // Configurer le graphique
        setupChart();
        
        btnUpdateWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWeight();
            }
        });
        
        return view;
    }
    
    private void loadCurrentWeight() {
        weightViewModel.getLatestWeight().observe(getViewLifecycleOwner(), new Observer<WeightHistoryEntity>() {
            @Override
            public void onChanged(WeightHistoryEntity weightHistoryEntity) {
                if (weightHistoryEntity != null) {
                    tvCurrentWeight.setText(String.format(Locale.getDefault(), "%.1f kg", weightHistoryEntity.getWeight()));
                } else {
                    // Si aucun poids n'est enregistré, utiliser les données utilisateur
                    UserPreferences userPrefs = new UserPreferences(getContext());
                    User user = userPrefs.getUser();
                    
                    if (user != null) {
                        tvCurrentWeight.setText(String.format(Locale.getDefault(), "%.1f kg", user.getWeight()));
                    } else {
                        tvCurrentWeight.setText("Pas de données");
                    }
                }
            }
        });
    }
    
    private void setupChart() {
        // Configurer l'apparence du graphique
        lineChartWeight.setDragEnabled(true);
        lineChartWeight.setScaleEnabled(true);
        lineChartWeight.setPinchZoom(true);
        
        XAxis xAxis = lineChartWeight.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            
            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value;
                return mFormat.format(new Date(millis));
            }
        });
        
        Description description = new Description();
        description.setText("Évolution du poids (kg)");
        lineChartWeight.setDescription(description);
        
        // Charger les données de poids
        loadWeightHistoryData();
    }
    
    private void loadWeightHistoryData() {
        weightViewModel.getLastMonthWeightHistory().observe(getViewLifecycleOwner(), new Observer<List<WeightHistoryEntity>>() {
            @Override
            public void onChanged(List<WeightHistoryEntity> weightHistoryEntities) {
                if (weightHistoryEntities != null && !weightHistoryEntities.isEmpty()) {
                    List<Entry> entries = new ArrayList<>();
                    
                    for (WeightHistoryEntity entry : weightHistoryEntities) {
                        // Utiliser le timestamp comme valeur X et le poids comme valeur Y
                        entries.add(new Entry(entry.getDate().getTime(), entry.getWeight()));
                    }
                    
                    LineDataSet dataSet = new LineDataSet(entries, "Poids (kg)");
                    dataSet.setColor(getResources().getColor(R.color.colorPrimary));
                    dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    dataSet.setCircleColor(getResources().getColor(R.color.colorPrimary));
                    dataSet.setLineWidth(2f);
                    dataSet.setCircleRadius(4f);
                    dataSet.setDrawValues(true);
                    
                    LineData lineData = new LineData(dataSet);
                    lineChartWeight.setData(lineData);
                    lineChartWeight.invalidate(); // Rafraîchir le graphique
                } else {
                    // Aucune donnée à afficher
                    lineChartWeight.setNoDataText("Aucune donnée d'historique de poids");
                    lineChartWeight.invalidate();
                }
            }
        });
    }
    
    private void updateWeight() {
        String weightStr = etNewWeight.getText().toString();
        if (!weightStr.isEmpty()) {
            try {
                float newWeight = Float.parseFloat(weightStr);
                
                // Vérifier que le poids est dans une plage raisonnable
                if (newWeight > 30 && newWeight < 300) {
                    // Créer une nouvelle entrée d'historique
                    WeightHistoryEntity weightHistory = new WeightHistoryEntity(newWeight, new Date());
                    weightViewModel.insert(weightHistory);
                    
                    // Mettre à jour également l'utilisateur
                    UserPreferences userPrefs = new UserPreferences(getContext());
                    User user = userPrefs.getUser();
                    if (user != null) {
                        user.setWeight(newWeight);
                        userPrefs.saveUser(user);
                    }
                    
                    // Actualiser l'affichage
                    tvCurrentWeight.setText(String.format(Locale.getDefault(), "%.1f kg", newWeight));
                    etNewWeight.setText("");
                    
                    Toast.makeText(getContext(), "Poids mis à jour !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Veuillez entrer un poids valide (30-300 kg)", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Format de poids invalide", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Veuillez entrer un poids", Toast.LENGTH_SHORT).show();
        }
    }
}