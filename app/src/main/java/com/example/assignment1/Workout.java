package com.example.assignment1;

public class Workout {
    private String name;
    private int duration;
    private String category;
    private String description;
    private boolean favorite;
    private int id;

    public Workout(String name, int duration, String category, String description, int id) {
        this.name = name;
        this.duration = duration;
        this.category = category;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getFavorite() { return this.favorite; }

    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return name+", "+category;
    }
}
