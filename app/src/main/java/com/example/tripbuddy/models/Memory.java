package com.example.tripbuddy.models;

import android.net.Uri;

public class Memory {
    private int id;
    private Uri photoUri;
    private int audioId;
    private String destination;
    private String startDate;
    private String endDate;
    private String notes;
    private String mood;
    private String bgm;

    public Memory(int id, Uri photoUri, int audioId, String destination,
                  String startDate, String endDate, String notes,
                  String mood, String bgm) {
        this.id = id;
        this.photoUri = photoUri;
        this.audioId = audioId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.mood = mood;
        this.bgm = bgm;
    }

    public Memory(Uri photoUri, int audioId, String destination,
                  String startDate, String endDate, String notes,
                  String mood, String bgm) {
        this(-1, photoUri, audioId, destination, startDate, endDate, notes, mood, bgm);
    }

    public Memory(Uri photoUri, int audioId) {
        this(-1, photoUri, audioId,
                "",
                "",
                "",
                "",
                "",
                ""
        );
    }

    public int getId() { return id; }
    public Uri getPhotoUri() { return photoUri; }
    public int getAudioId() { return audioId; }
    public String getDestination() { return destination; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getNotes() { return notes; }
    public String getMood() { return mood; }
    public String getBgm() { return bgm; }


    public void setId(int id) { this.id = id; }
    public void setPhotoUri(Uri photoUri) { this.photoUri = photoUri; }
    public void setAudioId(int audioId) { this.audioId = audioId; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setMood(String mood) { this.mood = mood; }
    public void setBgm(String bgm) { this.bgm = bgm; }
}
