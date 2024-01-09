package client.son;

import javax.sound.sampled.*;

import client.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Son extends Thread {

    private Main main;
    private boolean recording;
    private static ByteArrayOutputStream byteArrayOutputStream;
    private Clip clip;
    private int debut;
    private int max;

    public Son(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        this.recording = true;
        this.debut = 0;
        this.max = 0;
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Le microphone n'est pas pris en charge");
                System.exit(0);
            }

            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            long time = System.currentTimeMillis();
            while (this.recording && System.currentTimeMillis() - time < (1000 * 60)) {

                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            this.main.vocalFini();

            line.stop();
            line.close();

            byteArrayOutputStream.close();

            byte[] data = this.getAudioData();
            this.main.nouveauVocal(data);
            AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(data), getAudioFormat(), data.length);
            this.clip = AudioSystem.getClip();
            this.clip.open(audioInputStream);
            this.max = (int) (this.clip.getMicrosecondLength() / 1_000_000);

            this.main.tempsVocal(this.tempsVocal());

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void stopVocal() {
        this.recording = false;
    }

    public byte[] getAudioData() {
        return byteArrayOutputStream.toByteArray();
    }

    public void jouerSon() {
        if (byteArrayOutputStream == null || this.clip != null && this.clip.isRunning())
            return;
        try {
            byte[] data = this.getAudioData();
            AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(data), getAudioFormat(), data.length);
            this.clip = AudioSystem.getClip();
            this.clip.open(audioInputStream);
            this.afficherTempsActuelEtMax();
            this.clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void arreterSon() {
        if (this.clip != null && this.clip.isRunning()) {
            this.clip.stop();
            this.clip.close();
        }
    }

    public void mettreEnPauseSon() {
        if (this.clip != null && this.clip.isRunning()) {
            this.clip.stop();
        }
    }

    public void reprendreSon() {
        if (this.clip != null && !this.clip.isRunning()) {
            this.clip.start();
        }
    }

    public void afficherTempsActuelEtMax() {
        if (this.clip != null) {
            LineListener listener = new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.START) {
                        new Thread(() -> {
                            while (clip.isRunning()) {
                                long currentTime = (clip.getMicrosecondPosition() / 1_000_000) + 1;
                                max = (int) (clip.getMicrosecondLength() / 1_000_000);
                                debut = (int) currentTime;
                                main.tempsVocal(tempsVocal());
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            };

            this.clip.addLineListener(listener);
        }
    }

    private String tempsVocal() {
        return this.debut + "/" + this.max;
    }

}
