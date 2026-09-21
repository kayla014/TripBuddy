package com.example.tripbuddy.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tripbuddy.R;
import com.example.tripbuddy.models.Memory;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private final Context context;
    private final ArrayList<Memory> memoryList;
    private OnItemClickListener listener;
    private MediaPlayer mediaPlayer;

    public PhotoAdapter(Context context, ArrayList<Memory> memoryList) {
        this.context = context;
        this.memoryList = memoryList;
    }

    public interface OnItemClickListener {
        void onItemClick(Memory memory);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Memory memory = memoryList.get(position);
        try {
            String uriStr = memory.getPhotoUri().toString();
            if (uriStr.startsWith("android.resource://")) {
                int resId = Integer.parseInt(uriStr.substring(uriStr.lastIndexOf("/") + 1));
                holder.imageView.setImageResource(resId);
            } else {
                holder.imageView.setImageURI(memory.getPhotoUri());
            }
        } catch (Exception e) {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground); // fallback
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(memory);
            }

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            if (memory.getAudioId() != -1) {
                mediaPlayer = MediaPlayer.create(context, memory.getAudioId());
                mediaPlayer.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return memoryList.size();
    }
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
