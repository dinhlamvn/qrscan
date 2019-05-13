package android.vn.leo.qrscan.data;

import android.graphics.Bitmap;

import java.util.Date;

public class ScanResult {

    private String result;
    private Bitmap image;
    private Date date;

    public ScanResult(String result, Bitmap image) {
        this.result = result;
        this.image = image;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
