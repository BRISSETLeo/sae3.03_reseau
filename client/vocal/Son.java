package client.vocal;

import javax.sound.sampled.*;

import client.Main;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Son extends Thread {

    private Main main;
    public static boolean recording;
    private final String receiver;

    public Son(Main main, String receiver) {
        this.main = main;
        this.receiver = receiver;
        recording = false;
    }

    @Override
    public void run() {
        Son.recording = true;
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

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            long time = System.currentTimeMillis();
            while ((System.currentTimeMillis() - time < (1000 * 60))) {

                if (!Son.recording) {
                    break;
                }

                int bytesRead = line.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            line.stop();
            line.close();

            byteArrayOutputStream.close();

            this.main.getClient().envoyerVocal(receiver, byteArrayOutputStream.toByteArray());

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stopperVocal() {
        Son.recording = false;
    }

    public AudioFormat getAudioFormat() {
        float sampleRate = 44100;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

}
