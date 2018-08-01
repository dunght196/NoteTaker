package dunght.example.com.notetaker.model;

import java.util.List;

public class Note {

    private int id;
    private String title;
    private String content;
    private int color;
    private String datetimeCreate;
    private String dateAlarm;
    private String timeAlarm;
    private List<String> listImage;

    public Note() {
    }

    public Note(String title, String content, int color, String datetimeCreate, String dateAlarm, String timeAlarm) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.datetimeCreate = datetimeCreate;
        this.dateAlarm = dateAlarm;
        this.timeAlarm = timeAlarm;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }

    public String getDatetimeCreate() {
        return datetimeCreate;
    }

    public void setDatetimeCreate(String datetimeCreate) {
        this.datetimeCreate = datetimeCreate;
    }

    public String getDateAlarm() {
        return dateAlarm;
    }

    public void setDateAlarm(String dateAlarm) {
        this.dateAlarm = dateAlarm;
    }

    public String getTimeAlarm() {
        return timeAlarm;
    }

    public void setTimeAlarm(String timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
