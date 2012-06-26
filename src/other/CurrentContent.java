package other;

public class CurrentContent {
	private String content;
	private int pos;
	public CurrentContent(String content, int pos) {
		super();
		this.content = content;
		this.pos = pos;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return "CurrentContent [content=" + content + ", pos=" + pos + "]";
	}
	
	
	
}
