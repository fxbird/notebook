package bean;

import java.util.Date;

public class FavoriteNote {
    private Note note;
    private Date dropInDate;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Date getDropInDate() {
        return dropInDate;
    }

    public void setDropInDate(Date dropInDate) {
        this.dropInDate = dropInDate;
    }
}
