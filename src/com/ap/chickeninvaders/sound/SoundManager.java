package com.ap.chickeninvaders.sound;

import com.ap.chickeninvaders.model.User;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoundManager {
    private static final float SAMPLE_RATE = 16000f;
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(
            SAMPLE_RATE, 16, 1, true, false
    );

    private final ExecutorService effects = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable, "chicken-invaders-sound");
        thread.setDaemon(true);
        return thread;
    });
    private final Random random = new Random();
    private volatile User user;
    private volatile int musicGeneration;

    public SoundManager() {
        refreshBackgroundMusic();
    }

    public synchronized void setUser(User user) {
        this.user = user;
        refreshBackgroundMusic();
    }

    public void playShot() {
        if (user != null && user.isShotSoundOn()) {
            effects.submit(() -> playTone(1450, 720, 75, 0.16));
        }
    }

    public void playExplosion() {
        if (user != null && user.isExplosionSoundOn()) {
            effects.submit(() -> {
                playNoise(170, 0.20);
                playTone(180, 70, 130, 0.14);
            });
        }
    }

    public void playEnd(boolean won) {
        if (user != null && user.isEndSoundOn()) {
            effects.submit(() -> {
                double[] notes = won
                        ? new double[]{523.25, 659.25, 783.99, 1046.50}
                        : new double[]{392.00, 329.63, 261.63, 196.00};
                for (double note : notes) {
                    playTone(note, note, 150, 0.15);
                }
            });
        }
    }

    public void playMenuBeep() {
        if (user == null || user.isMusicOn()) {
            effects.submit(() -> {
                playTone(440, 660, 120, 0.13);
                playTone(660, 880, 120, 0.13);
            });
        }
    }

    public void playEcho() {
        if (user != null && user.isShotSoundOn()) {
            effects.submit(() -> {
                playTone(880, 1320, 120, 0.13);
                playTone(1320, 660, 180, 0.12);
            });
        }
    }

    private synchronized void refreshBackgroundMusic() {
        int generation = ++musicGeneration;
        if (user != null && !user.isMusicOn()) {
            return;
        }

        Thread musicThread = new Thread(() -> runBackgroundMusic(generation), "echo-squadron-music");
        musicThread.setDaemon(true);
        musicThread.start();
    }

    private void runBackgroundMusic(int generation) {
        double[] melody = {220.00, 277.18, 329.63, 440.00, 329.63, 277.18, 246.94, 329.63};

        while (generation == musicGeneration && isMusicEnabled()) {
            for (double note : melody) {
                if (generation != musicGeneration || !isMusicEnabled()) {
                    return;
                }
                playTone(note, note, 170, 0.055);
                pause(35);
            }
            pause(180);
        }
    }

    private boolean isMusicEnabled() {
        User currentUser = user;
        return currentUser == null || currentUser.isMusicOn();
    }

    private void playTone(double startFrequency, double endFrequency, int durationMs, double volume) {
        int sampleCount = Math.max(1, (int) (SAMPLE_RATE * durationMs / 1000.0));
        byte[] audio = new byte[sampleCount * 2];
        double phase = 0;

        for (int i = 0; i < sampleCount; i++) {
            double progress = i / (double) sampleCount;
            double frequency = startFrequency + (endFrequency - startFrequency) * progress;
            phase += 2.0 * Math.PI * frequency / SAMPLE_RATE;
            double envelope = Math.min(1.0, progress * 12.0)
                    * Math.min(1.0, (1.0 - progress) * 10.0);
            short value = (short) (Math.sin(phase) * Short.MAX_VALUE * volume * envelope);
            audio[i * 2] = (byte) (value & 0xff);
            audio[i * 2 + 1] = (byte) ((value >> 8) & 0xff);
        }

        playAudio(audio, durationMs);
    }

    private void playNoise(int durationMs, double volume) {
        int sampleCount = Math.max(1, (int) (SAMPLE_RATE * durationMs / 1000.0));
        byte[] audio = new byte[sampleCount * 2];

        for (int i = 0; i < sampleCount; i++) {
            double progress = i / (double) sampleCount;
            double envelope = 1.0 - progress;
            short value = (short) ((random.nextDouble() * 2.0 - 1.0)
                    * Short.MAX_VALUE * volume * envelope);
            audio[i * 2] = (byte) (value & 0xff);
            audio[i * 2 + 1] = (byte) ((value >> 8) & 0xff);
        }

        playAudio(audio, durationMs);
    }

    private void playAudio(byte[] audio, int fallbackDurationMs) {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, AUDIO_FORMAT);
        try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
            line.open(AUDIO_FORMAT);
            line.start();
            line.write(audio, 0, audio.length);
            line.drain();
        } catch (Exception ignored) {
            pause(fallbackDurationMs);
        }
    }

    private void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
