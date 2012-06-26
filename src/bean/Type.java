package bean;

import dbwin.WinBean;

import java.io.Serializable;

public class Type implements WinBean,Comparable, Serializable {
    private Integer id;
    private String name;
    private int sumOfNote;
    private int status;
    private int show;
    private Integer parentTypeNo;
    private int del;
    private static final long serialVersionUID = -7513793018927978287L;

    public Type() {
    }

    public Type(String name) {
        this.name = name;
    }

    public Type(Integer id, String name) {
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
        this.status=status;
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
    
    
    public Integer getParentTypeNo() {
		return parentTypeNo;
	}

	public void setParentTypeNo(Integer parentTypeNo) {
		this.parentTypeNo = parentTypeNo;
	}
	

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	@Override
    public boolean equals(Object obj) {
        Type t=(Type)obj;
        return id==t.id;
    }

	
	public int compareTo(Object o) {
		Type t2=(Type)o;
		return getName().toLowerCase().compareTo(t2.getName().toLowerCase());
	}

//    public String toString() {
//        return "Type{" +
//                "del=" + del +
//                ", id=" + id +
//                ", name='" + name + '\'' +
//                ", sumOfNote=" + sumOfNote +
//                ", status=" + status +
//                ", show=" + show +
//                ", parentTypeNo=" + parentTypeNo +
//                ", isShowRecSum=" + isShowRecSum +
//                '}';
//    }

       public String toString() {
        return name+(isShowRecSum?"("+sumOfNote+")":"");
    }
}
