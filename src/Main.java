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

        JTextArea output = new JTextArea(5, 20);
        JTextField input = new JTextField(15);
        JButton button = new JButton("Testing");
        JPanel test = new JPanel();
        test.add(button);

        button.addActionListener(e -> {
            output.setText(input.getText());
        });
        //output.setText("Current " + testing.getSongs().getHead().toString());

        
        frame.setLayout(new BorderLayout());

        
        test.add(input);
        test.add(output);
        frame.add(test, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setVisible(true);

    }
}
