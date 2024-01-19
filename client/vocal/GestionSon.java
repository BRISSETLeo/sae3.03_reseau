package client.vocal;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;

import client.vues.PatternVocal;
import javafx.application.Platform;

public class GestionSon {

    private Clip clip;
    private byte[] actuelVocal;
    private boolean pause;

    public GestionSon() {
        this.clip = null;
        this.actuelVocal = null;
    }

    public void jouerSon(byte[] vocal, PatternVocal patternVocal) {
        if (clip != null && actuelVocal == vocal && clip.isRunning())
            return;
        else if (clip != null && !clip.isRunning() && actuelVocal == vocal && this.pause) {
            clip.start();
            return;
        }
        if (clip != null) {
            clip.close();
        }
        this.actuelVocal = vocal;
        this.pause = false;
        try {
            AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(this.actuelVocal), new AudioFormat(44100, 16, 1, true, false),
                    this.actuelVocal.length);
            this.clip = AudioSystem.getClip();
            this.clip.open(audioInputStream);
            this.clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    patternVocal.switchPlayPause();
                    if (!this.pause && this.clip != null) {
                        this.clip.close();
                        this.clip = null;
                        Platform.runLater(() -> patternVocal.resetBars());
                    }
                }
                if (event.getType() == LineEvent.Type.CLOSE) {
                    Platform.runLater(() -> patternVocal.resetBars());
                }
                if (event.getType() == LineEvent.Type.START) {
                    new Thread(() -> {
                        double current = 0;
                        while (true) {
                            if (this.clip == null || !this.clip.isOpen())
                                break;
                            if (this.clip.isRunning()) {
                                long currentTime = this.clip.getMicrosecondPosition();
                                long totalDuration = this.clip.getMicrosecondLength();

                                double mtn = ((double) currentTime / (double) totalDuration);

                                if (mtn != current) {
                                    current = mtn;
                                    Platform.runLater(() -> patternVocal.updateBars(mtn));
                                }
                            }
                        }
                    }).start();
                }
            });
            this.clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSon(byte[] vocal) {
        if (this.clip != null && this.clip.isRunning() && this.actuelVocal == vocal) {
            this.pause = true;
            this.clip.stop();
        }
    }

}
