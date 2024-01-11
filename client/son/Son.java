package client.son;

import javax.sound.sampled.*;

import client.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Son extends Thread {

    private Main main;
    private boolean recording;
    private static ByteArrayOutputStream byteArrayOutputStream;
    private Clip clip;
    private List<Double> intensities;

    public Son(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        this.recording = true;
        if (this.clip != null)
            this.clip.stop();
        try {
            AudioFormat format = this.getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Le microphone n'est pas pris en charge");
                System.exit(0);
            }

            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            Son.byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            long time = System.currentTimeMillis();
            while (this.recording && System.currentTimeMillis() - time < (1000 * 60)) {

                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    break;
                }
                Son.byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            this.main.vocalFini();

            line.stop();
            line.close();

            Son.byteArrayOutputStream.close();

            byte[] audioData = this.getAudioData();
            if (audioData == null)
                return;

            boolean isSilent = true;
            for (byte sample : audioData) {
                if (sample != 0) {
                    isSilent = false;
                    break;
                }
            }

            if (isSilent) {
                Son.byteArrayOutputStream = null;
                this.main.aucunSon();
            } else {
                this.main.nouveauVocal(this.playAudio(audioData));
            }

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public AudioFormat getAudioFormat() {
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
        if (Son.byteArrayOutputStream != null) {
            return byteArrayOutputStream.toByteArray();
        }
        return null;
    }

    public void jouerSon() {
        if (this.recording)
            return;
        if (byteArrayOutputStream == null || this.clip != null && this.clip.isRunning())
            return;
        try {
            byte[] data = this.getAudioData();
            AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(data), getAudioFormat(), data.length);
            this.clip = AudioSystem.getClip();
            this.clip.open(audioInputStream);
            this.updateSon();
            this.clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void arreterSon() {
        if (this.clip != null && this.clip.isRunning()) {
            this.clip.stop();
        }
    }

    public void mettreEnPauseSon() {
        if (this.clip != null && this.clip.isRunning()) {
            this.clip.stop();
        }
    }

    public boolean isEcouteSon() {
        return this.clip != null && this.clip.isRunning();
    }

    public void reprendreSon() {
        if (this.clip != null && !this.clip.isRunning()) {
            this.clip.start();
        }
    }

    public void updateSon() {
        if (this.clip != null) {
            LineListener listener = new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.START) {
                        new Thread(() -> {
                            while (clip.isRunning()) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Son.this.main.updateSon((int) (clip.getMicrosecondLength() / 1_000_000),
                                        (int) (clip.getMicrosecondPosition() / 1_000_000));
                            }
                        }).start();
                    }
                }
            };

            this.clip.addLineListener(listener);
        }
    }

    public List<Double> playAudio(byte[] audioData) {
        this.intensities = new ArrayList<>();
        try {
            AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(audioData), this.main.getAudioFormat(), audioData.length);
            AudioFormat audioFormat = audioInputStream.getFormat();
            byte[] buffer = new byte[1024 * audioFormat.getFrameSize()];

            int bytesRead;
            int totalMilliseconds = (int) (audioInputStream.getFrameLength() / audioFormat.getFrameRate() * 1000);

            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                int samplesPerBar = (int) audioFormat.getSampleRate() * bytesRead
                        / (audioFormat.getFrameSize() * totalMilliseconds);
                this.processAudioBuffer(buffer, bytesRead, samplesPerBar, audioFormat);
            }

            List<Double> averages = this.drawBars();

            audioInputStream.close();

            return averages;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processAudioBuffer(byte[] buffer, int bytesRead, int samplesPerBar, AudioFormat audioFormat) {
        int sampleSize = audioFormat.getSampleSizeInBits() / 8;
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);

        for (int i = 0; i < samplesPerBar && byteBuffer.remaining() >= sampleSize; i++) {
            double sampleValue;

            if (sampleSize == 1) {
                sampleValue = byteBuffer.get() / 128.0;
            } else {
                sampleValue = byteBuffer.getShort() / 32768.0;
            }

            intensities.add(Math.abs(sampleValue));
        }
    }

    public void supprimerVocal(){
        this.stopVocal();
        Son.byteArrayOutputStream = null;
    }

    private List<Double> drawBars() {
        int groupSize = intensities.size() / 30;
        List<Double> averages = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            double sum = 0;
            for (int j = i * groupSize; j < (i + 1) * groupSize; j++) {
                sum += intensities.get(j);
            }
            double averageAmplitude = sum / groupSize;
            averages.add(averageAmplitude);
        }
        return averages;
    }

}
