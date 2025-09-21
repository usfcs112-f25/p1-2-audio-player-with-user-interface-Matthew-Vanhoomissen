import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Playlist testing = new Playlist();
        testing.addSong(new Song("Test1", "T", 10, "T", "///"));
        testing.addSong(new Song("Test2", "T", 10, "T", "///"));

        JFrame frame = new JFrame("Spotify Playlist (Swing)");

        JTextField input = new JTextField(15);
        frame.setLayout(new BorderLayout());
        JPanel test = new JPanel();
        test.add(input);
        frame.add(test, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setVisible(true);

    }
}
