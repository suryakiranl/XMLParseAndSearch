package edu.cmu.sv.surya.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suryakiran on 3/10/14.
 */
public class Literature {
    private long id;
    private String mDate;
    private String key;
    private List<String> editors = new ArrayList<String>();
    private List<String> authors = new ArrayList<String>();
    private String title;
    private String year;
    private String url;

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getEditors() {
        return editors;
    }

    public void setEditors(List<String> editors) {
        this.editors = editors;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[Literature : " +
                "key:" + key +
                ",mDate:" + mDate +
                ",year:" + year +
                ",url:" + url +
                ",title:" + title +
                ",authors:" + authors +
                ",editors:" + editors +
                " ]";
    }
}
