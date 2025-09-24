import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.JProgressBar;

/*
 * Song player used to take in sound file and play sound
 */
public class SongPlayer {
    private MediaPlayer mediaPlayer;
    private JProgressBar progressBar;

    public SongPlayer(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setStringPainted(true);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /*
     * Plays the song
     * @params the song, and a trigger for when song ends
     * @throws AudioException
     * @returns boolean if successfully played
     */
    public boolean playSong(Song song, Runnable whenSongEnds) throws AudioException{
        try {
            JavaFXInitializer.initalizing();
            

            File songFile = new File(song.getFilePath());
            mediaPlayer = new MediaPlayer(new Media(songFile.toURI().toString()));
            
            mediaPlayer.setOnEndOfMedia(whenSongEnds);
            
            mediaPlayer.setOnReady(() -> {
                Duration total = mediaPlayer.getMedia().getDuration();
                progressBar.setMinimum(0);
                progressBar.setMaximum((int) total.toSeconds());
            });

            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                SwingUtilities.invokeLater(() -> {
                    if(mediaPlayer != null) {
                        Duration total = mediaPlayer.getMedia().getDuration();
                        progressBar.setValue((int) newTime.toSeconds());
                        progressBar.setString(formatTime(newTime) + "/" + formatTime(total));
                    }
                });
            });

            mediaPlayer.play();
            return true;
            
        } 
        catch(Exception e) {
            throw new AudioException("Failed to play song" + song.getTitle(), e);
            
        }
        
    }

    public JProgressBar getProgress() {
        return progressBar;
    }

    /*
     * Stops song
     */
    public void stopSong() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    /*
     * Pauses song
     */
    public void pauseSong() {
        if(mediaPlayer != null) {
            mediaPlayer.pause();
            System.out.println("Pausing song...");
        }
    }
    /*
     * Unpauses song
     */
    public void unpauseSong() {
        if(mediaPlayer != null) {
            mediaPlayer.play();
            System.out.println("Song resumed");
        }
    }

    /*
     * If it is playing, boolean
     * @returns true
     */
    public boolean playing() {
        System.out.println(mediaPlayer != null);
        if(mediaPlayer != null) {
            System.out.println(mediaPlayer.getStatus());
        }
        
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /*
     * Checks if paused
     * @returns boolean if so
     */
    public boolean paused() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED;
    }

    /*
     * Sets volume by user input
     * @params user input
     */
    public void setVolume(double vol) {
        if(mediaPlayer == null) {
            return;
        }
        mediaPlayer.setVolume(vol);
    }

    public void setSpeed(double speed) {
        mediaPlayer.setRate(speed);
    }

    public String formatTime(Duration d) {
        int secs = (int) d.toSeconds();
        int mins = secs / 60;
        secs = secs % 60;
        return String.format("%02d:%02d", mins, secs);
    }

}