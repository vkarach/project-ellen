package sk.tuke.kpi.oop.game.weapons;

import sk.tuke.kpi.gamelib.Actor;
import sk.tuke.kpi.oop.game.items.Ammo;

public abstract class Firearm {
    private int curAmmo;
    private final int maxAmmo;
    public Firearm(int startAmmo, int maxAmmo) {
        this.curAmmo = startAmmo;
        this.maxAmmo = maxAmmo;
    }
    public Firearm(int ammo) {
        this.curAmmo = ammo;
        this.maxAmmo = ammo;
    }
    public int getAmmo() {
        return curAmmo;
    }
    public void reload(int newAmmo) {
        curAmmo += newAmmo;
        if (curAmmo > maxAmmo) {
            curAmmo = maxAmmo;
        }
    }
    public Fireable fire() {
        if (curAmmo == 0) {
            return null;
        }
        curAmmo--;
        return createBullet();
    }
    protected abstract Fireable createBullet();
}
