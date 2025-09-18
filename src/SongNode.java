
/*
 * Creates song node with song stored as data
 */
public class SongNode {
    private Song song;
    private SongNode next;
    private SongNode prev;

    /*
     * Constructor
     */
    public SongNode(Song song) {
        this.song = song;
        this.next = null;
        this.prev = null;
    }

    /*setters */
    public void setSong(Song song) {
        this.song = song;
    }
    public void setNext(SongNode next) {
        this.next = next;
    }
    public void setPrev(SongNode prev) {
        this.prev = prev;
    }

    /*
     * Getters
     */
    public Song getSong() {
        return song;
    }
    public SongNode getSongNext() {
        return next;
    }
    public SongNode getPrev() {
        return prev;
    }

    /*
     * to string
     * @returns string
     */
    public String toString() {
        return song + "";
    }
}
