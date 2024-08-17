package eu.midnightdust.celestria;

public class ShootingStar {
    public int progress;
    public final int type, x, y, rotation, size;
    public ShootingStar(int progress, int type, int x, int y, int rotation, int size) {
        this.progress = progress;
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.size = size;
    }

    public void tick() {
        --progress;
    }
}
