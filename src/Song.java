/*
 * Creates the song and saves the title, artist, duration, genre and filepath
 */
public class Song {
    private String title;
    private String artist;
    private int duration;
    private String filePath;
    private String genre;

    /*Constructor
     * 
     */
    public Song(String title, String artist, int duration, String genre, String filePath) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath = filePath;
        this.genre = genre;
    }

    /*
     * Getters
     */
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public int getDuration() {
        return duration;
    }
    public String getFilePath() {
        return filePath;
    }
    public String getGenre() {
        return genre;
    }

    /*
     * Setters
     */
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /*
     * To string that make sit readable
     * @returns readable string
     */
    public String toString() {
        String temp = "Song: " + title + "\n - Artist: " + artist + "\n - Duration: " + duration + "\n - File Path: " + filePath;
        return temp;
    }
} 
