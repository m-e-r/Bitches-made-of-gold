package worldofzuul;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class is an AudioPlayer.
 * The class takes wav files and plays it.
 * Exceptions is catched
 */
public class AudioPlayer {

    /**
     * This method is for playing bagground sound. The sound is free and has no copyright.
     * It takes the sound and loops it when the file is done. 
     */
    public void playMusic() { 
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("data/music/music.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Something went wrong");
        }
    }
    
    /**
     * This method is for playing sound when you fly in the game. The sound is free and has no copyright. 
     */
    public void playFly() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("data/music/fly.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Something went wrong");
        }
    }

    /**
     * This method is for playing sound when you warp in the game. The sound is free and has no copyright. 
     */
    public void playWarp() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("data/music/Warp.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Something went wrong");
        }
    }

    /**
     * This method is for playing sound when you end in the game. The sound is free and has no copyright. 
     */
    public void playThanks() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("data/music/Thanks.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
            Thread.sleep(1500);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException |  InterruptedException ex) {
            System.out.println("Something went wrong");
        } 
    }
}