package com.batdaulaptrinh.completedictionary;

public class History {

    private String en_word;
    private String en_def;
    private int is_favourite;

    public History(String en_word, String en_def, int is_favourite) {
        this.en_word = en_word;
        this.en_def = en_def;
        this.is_favourite = is_favourite;
    }

    public void setIs_favourite(int is_favourite) {
        this.is_favourite = is_favourite;
    }

    public int get_is_favourite() {
        return is_favourite;
    }

    public String get_en_word() {
        return en_word;
    }

    public String get_def() {
        return en_def;
    }
}
