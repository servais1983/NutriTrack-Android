package com.example.nutritrack.nutrition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.nutritrack.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NutritionScannerActivity extends AppCompatActivity {

    private static final String TAG = "NutritionScanner";
    public static final String EXTRA_NUTRITION_INFO = "com.example.nutritrack.NUTRITION_INFO";

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Button captureButton;
    private ExecutorService cameraExecutor;
    private TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_scanner);

        previewView = findViewById(R.id.preview_view);
        captureButton = findViewById(R.id.button_capture);

        // Set up ML Kit text recognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        captureButton.setOnClickListener(view -> captureImage());

        cameraExecutor = Executors.newSingleThreadExecutor();
        startCamera();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = 
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error starting camera: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases(ProcessCameraProvider cameraProvider) {
        // Configure preview
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Use back camera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Set up image capture
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build();

        // Unbind any bound use cases before rebinding
        cameraProvider.unbindAll();

        // Bind all use cases to camera
        Camera camera = cameraProvider.bindToLifecycle(
                (LifecycleOwner) this, 
                cameraSelector, 
                preview, 
                imageCapture);
    }

    private void captureImage() {
        if (imageCapture == null) {
            return;
        }

        // Create temporary file for the image
        File outputFile = new File(getCacheDir(), "nutrition_label.jpg");

        // Set up image capture options
        ImageCapture.OutputFileOptions outputFileOptions = 
                new ImageCapture.OutputFileOptions.Builder(outputFile).build();

        // Take the picture
        imageCapture.takePicture(
                outputFileOptions, 
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        processImage(outputFile);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.e(TAG, "Image capture failed: " + exception.getMessage());
                        Toast.makeText(NutritionScannerActivity.this, 
                                "Capture d'image échouée", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processImage(File imageFile) {
        try {
            InputImage inputImage = InputImage.fromFilePath(this, android.net.Uri.fromFile(imageFile));
            
            textRecognizer.process(inputImage)
                    .addOnSuccessListener(text -> {
                        // Extract nutrition info from the recognized text
                        Map<String, Float> nutritionInfo = extractNutritionInfo(text);
                        
                        if (!nutritionInfo.isEmpty()) {
                            // Return the nutrition info to the calling activity
                            Intent data = new Intent();
                            data.putExtra(EXTRA_NUTRITION_INFO, (HashMap<String, Float>) nutritionInfo);
                            setResult(RESULT_OK, data);
                            
                            Toast.makeText(NutritionScannerActivity.this, 
                                    "Information nutritionnelle extraite", 
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(NutritionScannerActivity.this, 
                                    "Aucune information nutritionnelle détectée. Veuillez réessayer.", 
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Text recognition failed: " + e.getMessage());
                        Toast.makeText(NutritionScannerActivity.this, 
                                "Reconnaissance du texte échouée", 
                                Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error processing image: " + e.getMessage());
            Toast.makeText(this, 
                    "Erreur lors du traitement de l'image", 
                    Toast.LENGTH_SHORT).show();
        }
    }

    private Map<String, Float> extractNutritionInfo(Text text) {
        Map<String, Float> nutritionInfo = new HashMap<>();
        String fullText = text.getText().toLowerCase();
        
        Log.d(TAG, "Recognized text: " + fullText);
        
        // Extract values using regex patterns
        extractNutrient(fullText, "calories?\\D+(\\d+)", "calories", nutritionInfo);
        extractNutrient(fullText, "protéines?\\D+(\\d+([.,]\\d+)?)\\s*g", "protein", nutritionInfo);
        extractNutrient(fullText, "glucides?\\D+(\\d+([.,]\\d+)?)\\s*g", "carbs", nutritionInfo);
        extractNutrient(fullText, "lipides?\\D+(\\d+([.,]\\d+)?)\\s*g", "fat", nutritionInfo);
        
        // Also try English terms
        extractNutrient(fullText, "protein\\D+(\\d+([.,]\\d+)?)\\s*g", "protein", nutritionInfo);
        extractNutrient(fullText, "carb(s|ohydrate)\\D+(\\d+([.,]\\d+)?)\\s*g", "carbs", nutritionInfo);
        extractNutrient(fullText, "fat\\D+(\\d+([.,]\\d+)?)\\s*g", "fat", nutritionInfo);
        
        // Try for amount/serving sizes
        extractNutrient(fullText, "pour\\s+(\\d+([.,]\\d+)?)\\s*g", "serving_size", nutritionInfo);
        extractNutrient(fullText, "portion\\D+(\\d+([.,]\\d+)?)\\s*g", "serving_size", nutritionInfo);
        
        return nutritionInfo;
    }
    
    private void extractNutrient(String text, String pattern, String nutrientName, Map<String, Float> nutritionInfo) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);
        
        if (matcher.find()) {
            try {
                // Replace comma with dot for decimal parsing
                String valueStr = matcher.group(1).replace(',', '.');
                float value = Float.parseFloat(valueStr);
                nutritionInfo.put(nutrientName, value);
                Log.d(TAG, "Extracted " + nutrientName + ": " + value);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing " + nutrientName + " value: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}