package com.example.testareonline.dto;

public class ParticipantDTO {
    private long id;
    private String username;
    private int punctaj;

    // Constructor including id, username, and score
    public ParticipantDTO(long id, String username, int punctaj) {
        this.id = id;
        this.username = username;
        this.punctaj = punctaj;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(int punctaj) {
        this.punctaj = punctaj;
    }
}
