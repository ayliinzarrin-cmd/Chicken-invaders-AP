package com.ap.chickeninvaders.model;

public class PlayerSnapshot {
    private final long capturedAt;
    private final int x;
    private final int y;
    private final int fireCount;
    private final boolean shot;

    public PlayerSnapshot(long capturedAt, int x, int y, int fireCount, boolean shot) {
        this.capturedAt = capturedAt;
        this.x = x;
        this.y = y;
        this.fireCount = fireCount;
        this.shot = shot;
    }

    public long getCapturedAt() {
        return capturedAt;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFireCount() {
        return fireCount;
    }

    public boolean isShot() {
        return shot;
    }
}
