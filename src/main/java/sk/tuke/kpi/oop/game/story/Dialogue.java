package sk.tuke.kpi.oop.game.story;
import java.util.List;
public class Dialogue {
    private String id;
    private List<Line> lines;
    private float time;

    public static class Line {
        public String speaker;
        public String text;
        public float time;
    }
    public List<Line> getLines() {
        return lines;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setLines(List<Line> lines) {
        this.lines = lines;
    }
}
