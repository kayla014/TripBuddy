package com.example.tripbuddy.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tripbuddy.R;
import com.example.tripbuddy.adapter.PhotoAdapter;
import com.example.tripbuddy.models.Memory;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private final int[] photoIds = {
            R.drawable.photo1,
            R.drawable.photo2,
            R.drawable.photo3,
            R.drawable.photo4,
            R.drawable.photo5
    };
    private final int[] audioIds = {
            R.raw.audio1,
            R.raw.audio2,
            R.raw.audio3,
            R.raw.audio4,
            R.raw.audio5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<Memory> memoryList = new ArrayList<>();
        for (int i = 0; i < photoIds.length; i++) {
            // Convert drawable resource ID to Uri
            Uri photoUri = Uri.parse("android.resource://" + getPackageName() + "/" + photoIds[i]);
            memoryList.add(new Memory(photoUri, audioIds[i]));
        }

        PhotoAdapter adapter = new PhotoAdapter(this, memoryList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(memory -> {
            Toast.makeText(this, "Clicked memory with audio: " + memory.getAudioId(), Toast.LENGTH_SHORT).show();
        });
    }
}
