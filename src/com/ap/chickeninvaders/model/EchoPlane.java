package com.ap.chickeninvaders.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EchoPlane {
    private final List<PlayerSnapshot> snapshots;
    private final long firstCaptureTime;
    private final long replayStartTime;
    private int nextSnapshotIndex;
    private int x;
    private int y;
    private boolean active = true;

    public EchoPlane(List<PlayerSnapshot> snapshots, long replayStartTime) {
        this.snapshots = new ArrayList<>(snapshots);
        this.replayStartTime = replayStartTime;
        this.firstCaptureTime = snapshots.get(0).getCapturedAt();
        this.x = snapshots.get(0).getX();
        this.y = snapshots.get(0).getY();
    }

    public List<Bullet> update(long now) {
        List<Bullet> newBullets = new ArrayList<>();
        long replayElapsed = now - replayStartTime;

        while (nextSnapshotIndex < snapshots.size()) {
            PlayerSnapshot snapshot = snapshots.get(nextSnapshotIndex);
            long snapshotOffset = snapshot.getCapturedAt() - firstCaptureTime;
            if (snapshotOffset > replayElapsed) {
                break;
            }

            x = snapshot.getX();
            y = snapshot.getY();
            if (snapshot.isShot()) {
                newBullets.addAll(createBullets(snapshot.getFireCount()));
            }
            nextSnapshotIndex++;
        }

        if (nextSnapshotIndex >= snapshots.size()) {
            long replayLength = snapshots.get(snapshots.size() - 1).getCapturedAt() - firstCaptureTime;
            if (replayElapsed > replayLength + 150) {
                active = false;
            }
        }
        return newBullets;
    }

    private List<Bullet> createBullets(int fireCount) {
        List<Bullet> bullets = new ArrayList<>();
        int spacing = 12;
        int firstX = x + 23 - ((fireCount - 1) * spacing) / 2;
        for (int i = 0; i < fireCount; i++) {
            bullets.add(new Bullet(firstX + i * spacing, y, true));
        }
        return bullets;
    }

    public boolean isActive() {
        return active;
    }

    public void draw(Graphics2D g) {
        Composite oldComposite = g.getComposite();
        g.setComposite(AlphaComposite.SrcOver.derive(0.48f));

        Polygon body = new Polygon();
        body.addPoint(x + 23, y);
        body.addPoint(x + 46, y + 42);
        body.addPoint(x + 23, y + 30);
        body.addPoint(x, y + 42);

        g.setColor(new Color(255, 100, 220));
        g.fillPolygon(body);
        g.setColor(new Color(140, 255, 255));
        g.fillOval(x + 17, y + 12, 12, 12);
        g.drawOval(x - 8, y - 8, 62, 58);

        g.setComposite(oldComposite);
    }
}
