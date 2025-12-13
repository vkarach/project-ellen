package sk.tuke.kpi.oop.game;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.gamelib.framework.AbstractActor;
import sk.tuke.kpi.gamelib.graphics.Animation;
import sk.tuke.kpi.gamelib.messages.Topic;
import sk.tuke.kpi.oop.game.characters.Ripley;
import sk.tuke.kpi.oop.game.items.Usable;
import sk.tuke.kpi.oop.game.utils.SoundUtil;

public class Computer extends AbstractActor implements EnergyConsumer, Usable<Ripley> {
    public static final Topic<Computer> KILL137 = Topic.create("door opened", Computer.class);
    private SoundUtil diconnectSound = new SoundUtil("sounds/Comms_Disconnect.wav");
    private boolean isPowerOn;
    public Computer() {
        Animation normalAnimation = new Animation("sprites/computer.png", 80, 48, 0.4f, Animation.PlayMode.LOOP_PINGPONG);
        setAnimation(normalAnimation);
    }
    @Override
    public void setPowered(boolean power) {
        isPowerOn = power;
    }
    public int add(int a, int b) {
        if (!isPowerOn) {
            return 0;
        }
        return a + b;
    }
    public float add(float a, float b) {
        if (!isPowerOn) {
            return 0;
        }
        return a + b;
    }
    public int sub(int a, int b) {
        if (!isPowerOn) {
            return 0;
        }
        return a - b;
    }
    public float sub(float a, float b) {
        if (!isPowerOn) {
            return 0;
        }
        return a - b;
    }
    @Override
    public void useWith(Ripley ripley) {
        String cmd = javax.swing.JOptionPane.showInputDialog(
            null,
            "Enter command:",
            "Computer Terminal",
            javax.swing.JOptionPane.PLAIN_MESSAGE
        );
        if (cmd == null) {
            return;
        }
        if (cmd.contains("add")) {
            String[] parts = cmd.split(" ");
            if (parts.length < 3) {
                javax.swing.JOptionPane.showMessageDialog(null, "Wrong add usage: add num1 num2");
                return;
            }
            try {
                float a = Float.parseFloat(parts[1]);
                float b = Float.parseFloat(parts[2]);
                javax.swing.JOptionPane.showMessageDialog(null, cmd + " = " + add(a, b));
            }
            catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(null, cmd + "wrong add usage: add num1 num2");
            }
        }
        else if (cmd.contains("sub")) {
            String[] parts = cmd.split(" ");
            if (parts.length < 3) {
                javax.swing.JOptionPane.showMessageDialog(null, "Wrong sun usage: sub num1 num2");
                return;
            }
            try {
                float a = Float.parseFloat(parts[1]);
                float b = Float.parseFloat(parts[2]);
                javax.swing.JOptionPane.showMessageDialog(null, cmd + " = " + sub(a, b));
            }
            catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(null, "wrong sub usage: sub num1 num2");
            }
        }
        else if (cmd.contains("kill")) {
            String[] parts = cmd.split(" ");
            if (parts.length < 2) {
                javax.swing.JOptionPane.showMessageDialog(null, "wrong kill usage: kill code");
                return;
            }
            try {
                int command = Integer.parseInt(parts[1]);
                if (command == 137) { // SIGKILL :)
                    assert getScene() != null;
                    for (Actor actor : getScene().getActors()) {
                        if (actor instanceof SmartCooler) {
                            System.out.println("turn off");
                            ((Cooler) actor).turnOff();
                        }
                        getScene().getMessageBus().publish(KILL137, this);
                    }
                }
                else {
                    javax.swing.JOptionPane.showMessageDialog(null, "unknown kill code \"" + parts[1] + "\"");
                }
            }
            catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(null, "wrong kill usage: kill code");
            }
        }
        else {
            javax.swing.JOptionPane.showMessageDialog(null, "Unknown command: " + cmd);
        }
    }
    public Class<Ripley> getUsingActorClass() {
        return Ripley.class;
    }
}
