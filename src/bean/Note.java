package bean;

import dbwin.BeanState;
import dbwin.HbnWinBean;
import java.io.Serializable;
import java.util.Date;

public class Note implements HbnWinBean, Serializable {
    private static final long serialVersionUID = -8363339156398741610L;
    private int id;
    private String title;
    private String content;
    private String typeName;
    private int del;
    private int show;
    private NoteType noteType;
    private BeanState beanState;
    private Date createDate;
    private Date updateDate;

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


    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public BeanState getState() {
        return beanState;
    }

    public void setState(BeanState beanState) {
        this.beanState = beanState;
    }

    @Override
    public String getClassName() {
        return "Note";
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
