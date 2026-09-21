package com.example.tripbuddy.activities;

import android.app.DatePickerDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tripbuddy.R;
import com.example.tripbuddy.database.MemoryDBHelper;
import com.example.tripbuddy.models.Memory;

import java.util.ArrayList;
import java.util.Calendar;

public class MemoriesActivity extends AppCompatActivity {

    private EditText etDestination, etStartDate, etEndDate, etNotes;
    private ImageView photo1, photo2, photo3, photo4, photo5;
    private Button btnAudio1, btnAudio2, btnAudio3, btnAudio4, btnAudio5, btnSaveMemory;
    private Spinner spinnerMood, spinnerBgm;

    private Uri selectedPhotoUri;
    private int selectedAudioId = -1;
    private String selectedMood, selectedBgm;

    private MediaPlayer mediaPlayer;
    private final int[] audioIds = {R.raw.audio1, R.raw.audio2, R.raw.audio3, R.raw.audio4, R.raw.audio5};

    private ArrayList<Memory> memoriesList = new ArrayList<>();
    private MemoryDBHelper dbHelper;

    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);

        etDestination = findViewById(R.id.etDestination);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etNotes = findViewById(R.id.etNotes);

        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        photo3 = findViewById(R.id.photo3);
        photo4 = findViewById(R.id.photo4);
        photo5 = findViewById(R.id.photo5);

        btnAudio1 = findViewById(R.id.btnAudio1);
        btnAudio2 = findViewById(R.id.btnAudio2);
        btnAudio3 = findViewById(R.id.btnAudio3);
        btnAudio4 = findViewById(R.id.btnAudio4);
        btnAudio5 = findViewById(R.id.btnAudio5);

        btnSaveMemory = findViewById(R.id.btnSaveMemory);

        spinnerMood = findViewById(R.id.spinnerMood);
        spinnerBgm = findViewById(R.id.spinnerBgm);

        dbHelper = new MemoryDBHelper(this);

        memoriesList = dbHelper.getAllMemories();

        etStartDate.setOnClickListener(v -> showDatePicker(true));
        etEndDate.setOnClickListener(v -> showDatePicker(false));

        photo1.setOnClickListener(v -> selectPhoto(R.drawable.photo1));
        photo2.setOnClickListener(v -> selectPhoto(R.drawable.photo2));
        photo3.setOnClickListener(v -> selectPhoto(R.drawable.photo3));
        photo4.setOnClickListener(v -> selectPhoto(R.drawable.photo4));
        photo5.setOnClickListener(v -> selectPhoto(R.drawable.photo5));

        btnAudio1.setOnClickListener(v -> playAudio(0));
        btnAudio2.setOnClickListener(v -> playAudio(1));
        btnAudio3.setOnClickListener(v -> playAudio(2));
        btnAudio4.setOnClickListener(v -> playAudio(3));
        btnAudio5.setOnClickListener(v -> playAudio(4));

        ArrayAdapter<String> moodAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Happy", "Sad", "Excited", "Relaxed"});
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMood.setAdapter(moodAdapter);

        ArrayAdapter<String> bgmAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"None", "Bgm1", "Bgm2", "Bgm3"});
        bgmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBgm.setAdapter(bgmAdapter);
        btnSaveMemory.setOnClickListener(v -> saveMemory());
    }

    private void showDatePicker(boolean isStart) {
        Calendar calendar = isStart ? startCalendar : endCalendar;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(this, (view, y, m, d) -> {
            calendar.set(y, m, d);
            String dateStr = String.format("%04d-%02d-%02d", y, m + 1, d);
            if (isStart) etStartDate.setText(dateStr);
            else etEndDate.setText(dateStr);
        }, year, month, day);
        dp.show();
    }
    private void selectPhoto(int drawableId) {
        selectedPhotoUri = Uri.parse("android.resource://" + getPackageName() + "/" + drawableId);
        Toast.makeText(this, "Photo selected", Toast.LENGTH_SHORT).show();
    }
    private void playAudio(int index) {
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this, audioIds[index]);
        mediaPlayer.start();
        selectedAudioId = audioIds[index];
        Toast.makeText(this, "Playing Audio " + (index + 1), Toast.LENGTH_SHORT).show();
    }
    private void saveMemory() {
        String destination = etDestination.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        selectedMood = spinnerMood.getSelectedItem().toString();
        selectedBgm = spinnerBgm.getSelectedItem().toString();

        if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || notes.isEmpty()
                || selectedPhotoUri == null || selectedAudioId == -1) {
            Toast.makeText(this, "Fill all fields, select photo and audio", Toast.LENGTH_SHORT).show();
            return;
        }

        Memory memory = new Memory(selectedPhotoUri, selectedAudioId, destination,
                startDate, endDate, notes, selectedMood, selectedBgm);

        dbHelper.insertMemory(memory);
        memoriesList.add(memory);

        Toast.makeText(this, "Memory saved!", Toast.LENGTH_SHORT).show();

        etDestination.setText("");
        etStartDate.setText("");
        etEndDate.setText("");
        etNotes.setText("");
        spinnerMood.setSelection(0);
        spinnerBgm.setSelection(0);
        selectedPhotoUri = null;
        selectedAudioId = -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }
}