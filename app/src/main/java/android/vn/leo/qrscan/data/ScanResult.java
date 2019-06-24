package android.vn.leo.qrscan.data;

import android.graphics.Bitmap;

import com.google.zxing.client.result.ParsedResultType;

import java.io.Serializable;
import java.util.Date;

public class ScanResult implements Serializable {

    private int id;
    private String result;
    private Bitmap image;
    private Date date;
    private ParsedResultType type;

    public ScanResult() {
        this.result = "";
        this.image = null;
        this.date = new Date();
        this.type = ParsedResultType.TEXT;
    }

    public ScanResult(String result, Bitmap image) {
        this.result = result;
        this.image = image;
        this.date = new Date();
        this.type = ParsedResultType.TEXT;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public ParsedResultType getType() {
        return type;
    }

    public void setType(ParsedResultType type) {
        this.type = type;
    }
}
