package sk.tuke.kpi.oop.game.story;

import sk.tuke.kpi.oop.game.story.Dialogue;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

//package sk.tuke.kpi.oop.game.story;
//
//import com.google.gson.Gson;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//public class DialogueLoader {
//
//    private static final Map<String, Dialogue> dialogues = new HashMap<>();
//
//    private static class DialogueFile {
//        List<Dialogue> dialogues;
//    }
//
//    public static void loadAll() {
//        dialogues.clear();
//
//        try {
//            InputStream is = DialogueLoader.class.getResourceAsStream("/dialogues/dialogues.json");
//            if (is == null) {
//                System.out.println("dialogues.json not found!");
//                return;
//            }
//
//            var reader = new InputStreamReader(is, StandardCharsets.UTF_8);
//            var gson = new Gson();
//
//            DialogueFile file = gson.fromJson(reader, DialogueFile.class);
//
//            if (file == null || file.dialogues == null) {
//                System.out.println("dialogues.json malformed");
//                return;
//            }
//
//            for (Dialogue d : file.dialogues) {
//                dialogues.put(d.id, d);
//                System.out.println("Loaded dialogue: " + d.id);
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Dialogue get(String id) {
//        return dialogues.get(id);
//    }
//}

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class DialogueLoader {
    private static final Map<String, Dialogue> DATA = new HashMap<>();

    private DialogueLoader() {}

    public static void load(String resource) throws IOException {
        InputStream in = DialogueLoader.class.getClassLoader().getResourceAsStream(resource);
        if (in == null) throw new FileNotFoundException("Resource not found: " + resource);

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
                current.id = id;
                current.lines = new ArrayList<>();
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

                if (current != null) current.lines.add(line);
            }
        }
    }

    private static String extract(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        if (i == -1) return null;

        int colon = json.indexOf(":", i) + 1;
        while (colon < json.length() && json.charAt(colon) == ' ') colon++;

        if (json.charAt(colon) == '"') {
            int end = json.indexOf('"', colon + 1);
            return json.substring(colon + 1, end);
        }

        int end = colon;
        while (end < json.length() && "0123456789.".indexOf(json.charAt(end)) >= 0) end++;
        return json.substring(colon, end);
    }
}
