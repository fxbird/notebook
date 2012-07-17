package bean;

import dbwin.BeanState;
import dbwin.HbnWinBean;
import dbwin.sql.OperateType;

import java.io.Serializable;
import java.sql.Timestamp;

public class HbnNote implements HbnWinBean,Serializable {
    private static final long serialVersionUID = -8363339156398741610L;
    private int id;
    private String title;
    private String content;
    private Integer typeId;
    private String typeName;
    private Timestamp ts;
    private OperateType operateType;
    private int del;
    private int show;
    private NoteType noteType;
    private BeanState beanState;

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

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public BeanState getState() {
        return beanState ;
    }

    @Override
    public void setState(BeanState state) {
        this.beanState = state;
    }

    @Override
    public String getClassName() {
        return "Note";
    }
}
