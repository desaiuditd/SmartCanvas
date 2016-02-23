package in.incognitech.smartcanvas.model;

/**
 * Created by udit on 06/02/16.
 */
public class Note {

    private int id;
    private String photoPath;

    public Note(int id, String photoPath) {
        this.id = id;
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
