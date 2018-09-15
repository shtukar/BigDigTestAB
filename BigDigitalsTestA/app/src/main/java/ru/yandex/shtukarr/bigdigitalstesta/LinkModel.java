package ru.yandex.shtukarr.bigdigitalstesta;

import java.sql.Date;
import java.util.Comparator;

public class LinkModel {
    private int id;
    private String url;
    private Date date;
    private int status;

    public LinkModel(int id, String url, Date date, int status) {
        this.id = id;
        this.url = url;
        this.date = date;
        this.status = status;
    }

    public LinkModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static final Comparator<LinkModel> COMPARE_BY_DATE = (o1, o2) -> (int) (o1.getDate().getTime() - o2.getDate().getTime());

    public static final Comparator<LinkModel> COMPARE_BY_STATUS = (o1, o2) -> o1.getStatus() - o2.getStatus();
}
