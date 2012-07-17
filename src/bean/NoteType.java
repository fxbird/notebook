package bean;

import dbwin.WinBean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class NoteType implements WinBean, Comparable, Serializable {
    private Integer id;
    private String name;
    private int sumOfNote;
    private int status;
    private int show;
    private int del;
    private static final long serialVersionUID = -7513793018927978287L;
    private NoteType parentType;
    private Set<NoteType> childTypes=new HashSet<NoteType>();

    public NoteType() {
    }

    public NoteType(String name) {
        this.name = name;
    }

    public NoteType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isShowRecSum() {
        return isShowRecSum;
    }

    public void setShowRecSum(boolean showRecSum) {
        isShowRecSum = showRecSum;
    }

    private boolean isShowRecSum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSumOfNote() {
        return sumOfNote;
    }

    public void setSumOfNote(int sumOfNote) {
        this.sumOfNote = sumOfNote;
    }

//    public String toString() {
//        return name+(isShowRecSum?"("+sumOfNote+")":"");
//    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    @Override
    public boolean equals(Object obj) {
        NoteType t = (NoteType) obj;
        return id == t.id;
    }

    public int compareTo(Object o) {
        NoteType t2 = (NoteType) o;
        return getName().toLowerCase().compareTo(t2.getName().toLowerCase());
    }

    public String toString() {
        return name + (isShowRecSum ? "(" + sumOfNote + ")" : "");
    }

    public NoteType getParentType() {
        return parentType;
    }

    public void setParentType(NoteType parentType) {
        this.parentType = parentType;
    }

    public Set<NoteType> getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(Set<NoteType> childTypes) {
        this.childTypes = childTypes;
    }
}
