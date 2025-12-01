package sk.tuke.kpi.oop.game.story;
import java.util.List;
public class Dialogue {
    public String id;
    public List<Line> lines;
    public float time;

    public static class Line {
        public String speaker;
        public String text;
        public float time;
    }
}
