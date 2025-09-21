import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        Playlist testing = new Playlist();
        testing.addSong(new Song("Test1", "T", 10, "T", "///"));
        testing.addSong(new Song("Test2", "T", 10, "T", "///"));

        JFrame frame = new JFrame("Spotify Playlist (Swing)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setVisible(true);

    }
}
