import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static boolean paused = false;
    public static void main(String[] args) {
        Playlist songs = new Playlist();
        SongPlayer songPlayer = songs.getSongPlayer();
        songs.addSong(new Song("Test1", "T", 10, "T", "../music/running-night-393139.mp3"));
        songs.addSong(new Song("Test2", "T", 10, "T", "///"));


        

        JFrame frame = new JFrame("Spotify Playlist (Swing)");

        JTextArea output = new JTextArea(5, 20);
        JTextField input = new JTextField(15);
        JButton button = new JButton("Start");
        JButton pause = new JButton("Pause");
        JButton stop = new JButton("Stop");

        JPanel test = new JPanel();
        
        test.add(button, BorderLayout.SOUTH);
        

        button.addActionListener(e -> {
            songs.playCurrentSong();
        });

        pause.addActionListener(e -> {
            if(paused) {
                pause.setText("Pause");
                songs.getSongPlayer().unpauseSong();
                
            }
            else {
                pause.setText("Unpause");
                songs.getSongPlayer().pauseSong();
            }
            paused = !paused;
        });

        stop.addActionListener(e -> {
            songs.getSongPlayer().stopSong();
        });

        
        output.setText("Current " + songs.getCurrentSong().toString());

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row1.add(output);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row2.add(input);
        

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row3.add(button);
        row3.add(pause);
        row3.add(stop);

        test.setLayout(new BoxLayout(test, BoxLayout.Y_AXIS));
        test.add(row1);
        test.add(row2);
        test.add(row3);
        frame.setLayout(new BorderLayout());

        
        
        frame.add(test);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setVisible(true);

    }
}
