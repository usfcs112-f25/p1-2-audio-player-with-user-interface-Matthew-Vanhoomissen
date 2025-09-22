import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main {
    public static boolean paused = false;
    public static String name;
    public static String artist;
    public static int duration;
    public static String genre;
    public static String filePath = "";
    public static void main(String[] args) {
        Playlist songs = new Playlist();
        SongPlayer songPlayer = songs.getSongPlayer();
        songs.addSong(new Song("Test1", "T", 10, "T", "../music/running-night-393139.mp3"));
        songs.addSong(new Song("Test2", "T", 10, "T", "../music/file_example_WAV_1MG.wav"));


        

        JFrame frame = new JFrame("Spotify Playlist (Swing)");

        JTextArea output = new JTextArea(5, 20);
        JTextField input = new JTextField(15);
        JButton button = new JButton("Start");
        JButton pause = new JButton("Pause");
        JButton stop = new JButton("Stop");

        JPanel test = new JPanel();
        JPanel right = new JPanel();
        
        test.add(button, BorderLayout.SOUTH);
        

        button.addActionListener(e -> {
            songs.playCurrentSong();
            output.setText("Current " + songs.getCurrentSong().toString());
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

        JButton next = new JButton("Next");
        JButton prev = new JButton("Prev");

        next.addActionListener(e -> {
            songs.playNext();
            output.setText("Current " + songs.getCurrentSong().toString());
            paused = false;
            pause.setText("Pause");

        });

        prev.addActionListener(e -> {
            songs.playPrev();
            output.setText("Current " + songs.getCurrentSong().toString());
            paused = false;
            pause.setText("Pause");
        });

        

        JButton file = new JButton("Select file");

        file.addActionListener(e -> {
            
            JFileChooser fileChoice = new JFileChooser();
            int choice = fileChoice.showOpenDialog(frame);
            if(choice == JFileChooser.APPROVE_OPTION) {
                File chosenFile = fileChoice.getSelectedFile();
                filePath = chosenFile.getAbsolutePath();
                
                
            }
            
        });
        DefaultListModel<String> list = new DefaultListModel<>();
        JList<String> playlist = new JList<>(list);
        JScrollPane scrollPane = new JScrollPane(playlist);
        SongNode temp = songs.getSongs().getHead();
        while(temp != null) {
            list.addElement(temp.getSong().getTitle());
            temp = temp.getSongNext();
        }

        JTextField inputName = new JTextField();
        inputName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JTextField inputArtist = new JTextField();
        inputArtist.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JTextField inputDuration = new JTextField();
        inputDuration.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JTextField inputGenre = new JTextField();
        inputGenre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JButton submit = new JButton("Add song");

        submit.addActionListener(e -> {
            if(inputName.getText().isBlank() || inputArtist.getText().isBlank() || inputDuration.getText().isBlank() || inputGenre.getText().isBlank() || filePath.isBlank()) {
                output.setText("Could not add new song. Input box left blank");                
            }
            else {
                songs.addSong(new Song(inputName.getText(), inputArtist.getText(), Integer.parseInt(inputDuration.getText()), inputGenre.getText(), filePath));
                output.setText("Song added");
                list.addElement(inputName.getText());
                
            }
        });

        JButton remove = new JButton("Remove song");

        remove.addActionListener(e -> {
            for(int i = list.size() - 1 ; i >= 0; i--) {
                if(list.get(i).equals(songs.getSongs().get(songs.getCurrentInt()).getSong().getTitle())) {
                    list.remove(i);
                }
            }
            songs.getSongs().removeAt(songs.getCurrentInt());
            
            songs.setCurrentSong(1);
        });

        
        

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row1.add(output);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row2.add(input);
        

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row3.add(button);
        row3.add(pause);
        row3.add(stop);

        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row4.add(next);
        row4.add(prev);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row5.add(remove);
        

        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightSide.add(file);
        rightSide.add(submit);

        test.setLayout(new BoxLayout(test, BoxLayout.Y_AXIS));
        test.add(row1);
        test.add(row2);
        test.add(row3);
        test.add(row4);
        test.add(row5);


        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(inputName);
        right.add(inputArtist);
        right.add(inputDuration);
        right.add(inputGenre);
        right.add(rightSide);
        right.add(scrollPane);


        frame.setLayout(new BorderLayout());

        
        
        frame.add(test, BorderLayout.CENTER);
        frame.add(right, BorderLayout.EAST);
     
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setVisible(true);

    }
}
