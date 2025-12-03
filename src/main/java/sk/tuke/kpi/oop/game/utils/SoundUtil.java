package sk.tuke.kpi.oop.game.utils;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.io.BufferedInputStream;

public class SoundUtil {
    private final Clip clip;

    public SoundUtil(String path) {
        try {
            InputStream raw = SoundUtil.class.getClassLoader().getResourceAsStream(path);
            if (raw == null) throw new RuntimeException("File not found: " + path);

            BufferedInputStream buf = new BufferedInputStream(raw);
            AudioInputStream in = AudioSystem.getAudioInputStream(buf);

            clip = AudioSystem.getClip();
            clip.open(in);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void setVolume(float volume) {
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            volume = Math.max(0f, Math.min(volume, 1f));

            float dB;
            if (volume == 0f) {
                dB = gain.getMinimum();
            }
            else {
                dB = (float) (20.0 * Math.log10(volume));
            }
            gain.setValue(dB);
        }
        catch (Exception ignored) {
        }
    }
    public void play(float volume) {
        setVolume(volume);
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void play() {
        play(1f);
    }
    public void loop() {
        setVolume(1f);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }
    public void loop(float volume) {
        setVolume(volume);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }
    public void stop() {
        clip.stop();
    }
}
