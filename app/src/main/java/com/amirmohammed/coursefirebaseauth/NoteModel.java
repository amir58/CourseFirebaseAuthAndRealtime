package com.amirmohammed.coursefirebaseauth;

public class NoteModel {
    private String note, description, finishBy;

    public NoteModel(String note, String description, String finishBy) {
        this.note = note;
        this.description = description;
        this.finishBy = finishBy;
    }

    public NoteModel() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFinishBy() {
        return finishBy;
    }

    public void setFinishBy(String finishBy) {
        this.finishBy = finishBy;
    }
}
