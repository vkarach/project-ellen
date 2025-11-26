package sk.tuke.kpi.oop.game.weapons;

public class Gun extends Firearm {
    public Gun(int ammo) {
        super(ammo);
    }
    public Gun(int startAmmo, int maxAmmo) {
        super(startAmmo, maxAmmo);
    }
    @Override
    public Fireable createBullet() {
        return new Bullet();
    }
}
