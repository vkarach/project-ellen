package sk.tuke.kpi.oop.game.utils;

import sk.tuke.kpi.gamelib.Scene;
import sk.tuke.kpi.gamelib.graphics.Color;
import sk.tuke.kpi.gamelib.graphics.Font;
import sk.tuke.kpi.gamelib.graphics.Overlay;

public class PauseManager {
    private enum State {MENU, SETTINGS}
    private static State state;
    private static Scene scene;
    private static boolean paused = false;
    private static String[] items;
    private static Runnable[] actions;
    private static int selected = 0;
    public static void init(Scene scene) {
        PauseManager.scene = scene;
    }
    public static boolean isPaused() {
        return paused;
    }
    public static void setPaused(boolean value) {
        paused = value;
    }
    public static void toggle() {
        setMainMenu();
        paused = !paused;
    }

    private static void setMainMenu() {
        state = State.MENU;
        items = new String[] { "Continue", "Settings", "Exit" };
        actions = new Runnable[] {
            PauseManager::toggle,
            PauseManager::setSettingsMenu,
            () -> scene.getGame().stop()
        };
        selected = 0;
    }
    private static final Setting[] settings = {
        new Setting("Sound", 50, 0, 100),
    };
    private static void setSettingsMenu() {
        state = State.SETTINGS;
        items = new String[settings.length + 1];
        for (int i = 0; i < settings.length; i++) {
            items[i] = settings[i].name;
            actions[i] = () -> {};
        }
        items[settings.length] = "Back";
        actions[settings.length] = PauseManager::setMainMenu;

        selected = 0;
    }

    public static void selectNext() {
        selected = (selected + 1) % items.length;
    }
    public static void selectPrevious() {
        selected = (selected - 1 + items.length) % items.length;
    }
    public static void select() {
        actions[selected].run();
    }

    public static void changeSettingValue(int change) {
        if (state == State.SETTINGS) {
            settings[selected].value += change;
            if (settings[selected].value > settings[selected].max) {
                settings[selected].value = settings[selected].max;
            }
            else if (settings[selected].value < settings[selected].min) {
                settings[selected].value = settings[selected].min;
            }
            if (settings[selected].name.equals("Sound")) {
                System.out.println("changeSettingValue -> setVolumeCoef at " + System.nanoTime());
                SoundUtil.setVolumeCoef(settings[selected].value / 50f);
                SoundUtil.updateAllVolumes();
            }
        }
    }

    public static void drawPauseMenu() {
        Overlay overlay = scene.getGame().getOverlay();
        int W = scene.getGame().getWindowSetup().getWidth();
        int H = scene.getGame().getWindowSetup().getHeight();

        overlay.drawRectangle(0, 0, W, H, new Color(0, 0, 0, 0.6f));

        int panelW = W * 3 / 5;          // 60%
        int panelH = H * 2 / 5;          // 40%
        int px = (W - panelW) / 2;
        int py = (H - panelH) / 2;
        overlay.drawRectangle(px, py, panelW, panelH,
            new Color(0, 0, 0, 0.85f), 2f, new Color(1, 1, 1, 0.15f));

        Font itemFont = new Font(26, Color.WHITE, Font.Style.NORMAL);
        Font selectedItemFont = new Font(26, Color.WHITE, Font.Style.BOLD);
        int pad = 24;
        int step = itemFont.getSize() + 14;

        int tx = px + pad;
        int ty = py + panelH - pad;

        overlay.drawText("Paused", tx, ty, new Font(30, Color.WHITE, Font.Style.BOLD));
        ty -= step * 2;

        for (int i = 0; i < items.length; i++) {
            Font font;
            if (i == selected) {
                font = selectedItemFont;
                int bh = selectedItemFont.getSize() + 5;
                overlay.drawRectangle(tx - 8, ty - 6, panelW - 2*pad, bh,
                    new Color(1, 1, 1, 0.08f), 1.5f, new Color(1, 1, 1, 0.15f));
            }
            else {
                font = itemFont;
            }
            overlay.drawText(items[i], tx, ty, font);
            if (state == State.SETTINGS && i < items.length - 1) {
                overlay.drawText(""+settings[i].value, panelW - 2*pad, ty, font);
            }

            ty -= step;
        }
    }
    public static final class Setting {
        public final String name;
        public int value;
        public final int min;
        public final int max;

        public Setting(String name, int value, int min, int max) {
            this.name = name;
            this.value = value;
            this.min = min;
            this.max = max;
        }

        public void change(int delta) {
            int v = value + delta;
            if (v < min) v = min;
            if (v > max) v = max;
            value = v;
        }
    }
}
