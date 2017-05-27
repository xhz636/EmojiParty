package beans;

import java.util.Date;

public class Image {

    private String ID;
    private String format;
    private int size;
    private int height;
    private int width;
    private Date uploadtime;

    public Image() {

    }

    public Image(String ID, String format, int size, int height, int width, Date uploadtime) {
        this.ID = ID;
        this.format = format;
        this.size = size;
        this.height = height;
        this.width = width;
        this.uploadtime = uploadtime;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getID() {
        return ID;
    }

    public String getFormat() {
        return format;
    }

    public int getSize() {
        return size;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

}
