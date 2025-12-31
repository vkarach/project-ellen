package sk.tuke.kpi.oop.game.story;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class DialogueLoader {
    private static final Map<String, Dialogue> DATA = new HashMap<>();

    public static void load(String resource) throws IOException {
        InputStream in = DialogueLoader.class.getClassLoader().getResourceAsStream(resource);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + resource);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        List<String> lines = br.lines().collect(java.util.stream.Collectors.toList());
        parse(lines);
    }

    public static Dialogue get(String id) {
        return DATA.get(id);
    }

    private static void parse(List<String> raw) {
        Dialogue current = null;

        for (String s : raw) {
            s = s.trim();

            // "id": "first_meeting"
            if (s.startsWith("\"id\"")) {
                String id = s.split(":")[1].replace("\"", "").replace(",", "").trim();
                current = new Dialogue();
                current.setId(id);
                current.setLines(new ArrayList<>());
                DATA.put(id, current);
            }

            // { "speaker": "...", "text": "...", "time": X }
            if (s.startsWith("{") && s.contains("speaker")) {
                String speaker = extract(s, "speaker");
                String text    = extract(s, "text");
                String t       = extract(s, "time");

                float time = (t == null) ? 1.0f : Float.parseFloat(t);

                Dialogue.Line line = new Dialogue.Line();
                line.speaker = speaker;
                line.text = text;
                line.time = time;

                if (current != null) {
                    current.getLines().add(line);
                }
            }
        }
    }

    private static String extract(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        if (i == -1) {
            return null;
        }

        int colon = json.indexOf(":", i) + 1;
        while (colon < json.length() && json.charAt(colon) == ' ') {
            colon++;
        }

        if (json.charAt(colon) == '"') {
            int end = json.indexOf('"', colon + 1);
            return json.substring(colon + 1, end);
        }

        int end = colon;
        while (end < json.length() && "0123456789.".indexOf(json.charAt(end)) >= 0) {
            end++;
        }
        return json.substring(colon, end);
    }
}
