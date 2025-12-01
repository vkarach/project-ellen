package sk.tuke.kpi.oop.game.story;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DialogueLoader {

    private static final Map<String, Dialogue> dialogues = new HashMap<>();

    // внутренняя обертка (только для парсинга JSON)
    private static class DialogueFile {
        List<Dialogue> dialogues;
    }

    public static void loadAll() {
        dialogues.clear();

        try {
            InputStream is = DialogueLoader.class.getResourceAsStream("/dialogues/dialogues.json");
            if (is == null) {
                System.out.println("dialogues.json not found!");
                return;
            }

            var reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            var gson = new Gson();

            DialogueFile file = gson.fromJson(reader, DialogueFile.class);

            if (file == null || file.dialogues == null) {
                System.out.println("dialogues.json malformed");
                return;
            }

            for (Dialogue d : file.dialogues) {
                dialogues.put(d.id, d);
                System.out.println("Loaded dialogue: " + d.id);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dialogue get(String id) {
        return dialogues.get(id);
    }
}
