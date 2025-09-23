import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.MouseEvent;

public class Main {
    public static boolean paused = false;
    public static String name;
    public static String artist;
    public static int duration;
    public static String genre;
    public static String filePath = "";
    public static void main(String[] args) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        Playlist songs = new Playlist("Playlist1");
        playlists.add(songs);
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
        JPanel left = new JPanel();
        
        test.add(button, BorderLayout.SOUTH);

        DefaultListModel<String> list = new DefaultListModel<>();
        JList<String> playlist = new JList<>(list);
        JScrollPane scrollPane = new JScrollPane(playlist);
        SongNode temp = songs.getSongs().getHead();
        while(temp != null) {
            list.addElement(temp.getSong().getTitle());
            temp = temp.getSongNext();
        }

        button.addActionListener(e -> {
            songs.playCurrentSong();
            if(songs.getCurrentSong() != null) {
               output.setText("Current " + songs.getCurrentSong().toString()); 
            }
            
            playlist.repaint();
            
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
            if(songs.getCurrentSong() != null) {
               output.setText("Current " + songs.getCurrentSong().toString()); 
            }
            paused = false;
            pause.setText("Pause");
            playlist.repaint();

        });

        prev.addActionListener(e -> {
            songs.playPrev();
            if(songs.getCurrentSong() != null) {
               output.setText("Current " + songs.getCurrentSong().toString()); 
            }
            paused = false;
            pause.setText("Pause");
            playlist.repaint();
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

        JButton clear = new JButton("Clear songs");
        clear.addActionListener(e -> {
            songs.clearSongs();
            output.setText("");
            for(int i = list.size() - 1; i >= 0; i--) {
                list.remove(i);
            } 
        });



        playlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int ind = playlist.locationToIndex(e.getPoint());
                    if(ind >= 0) {
                        String song = list.getElementAt(ind);
                        int temp = songs.findSongByTitle(song);
                        songs.setCurrentSong(temp);
                        songs.stopPlayback();
                        songs.playCurrentSong();
                        output.setText("Current " + songs.getCurrentSong().toString());
                        paused = false;
                        pause.setText("Pause");
                        playlist.repaint();

                    }
                }
            }
        });

        playlist.setCellRenderer((jlist, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            if(value.equals(songs.getCurrentSong().getSong().getTitle())) {
                label.setOpaque(true);
                label.setBackground(Color.LIGHT_GRAY);
            }
            else {
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
            }
            return label;
        });

        JButton exit = new JButton("Quit");
        exit.addActionListener(e -> {
            System.exit(1);
        });

        JComboBox menu = new JComboBox<>(new String[]{"Menu Options","New Playlist", "Load playlist", "Save playlist"});
        menu.setPreferredSize(new Dimension(120, 25));

        
        DefaultComboBoxModel<String> playlistNames = new DefaultComboBoxModel<>();
        JComboBox editPlaylist = new JComboBox<>(playlistNames);
        for(Playlist p : playlists) {
            playlistNames.addElement(p.toString());
            
        }

        editPlaylist.setPreferredSize(new Dimension(120, 25));

        JButton topLeftSubmit = new JButton("Enter");
        topLeftSubmit.setPreferredSize(new Dimension(70, 25));

        topLeftSubmit.addActionListener(e -> {
            if(menu.getSelectedItem().equals("New Playlist")) {
                String inputN = JOptionPane.showInputDialog(frame, "Enter new playlist name: ");
                if(inputN != null) {
                    playlists.add(new Playlist(inputN));
                    playlistNames.addElement(inputN);
                }
            }
            else if(menu.getSelectedItem().equals("Save playlist")) {
                String inputF = JOptionPane.showInputDialog(frame, "Enter file name to save to: ");
                if(inputF !=null) {
                    saveToFile(songs, inputF, output);
                } 
            }
            else if(menu.getSelectedItem().equals("Load playlist")) {
                String inputL = JOptionPane.showInputDialog(frame, "Enter a name to load playlist");
                if(inputL != null) {
                    JFileChooser fileChoice = new JFileChooser();
                    int choice = fileChoice.showOpenDialog(frame);
                    if(choice == JFileChooser.APPROVE_OPTION) {
                        File chosenFile = fileChoice.getSelectedFile();
                        filePath = chosenFile.getAbsolutePath();
                        playlists.add(createPlaylist(loadPlaylist(filePath, output), inputL, output));
                        playlistNames.addElement(inputL);
                        
                    }
                }
            }
        });


        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftSide.add(menu);
        leftSide.add(editPlaylist);
        leftSide.add(topLeftSubmit);
        
        

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
        row5.add(clear);
        

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
        right.add(exit);

        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(leftSide);


        frame.setLayout(new BorderLayout());

        
        
        frame.add(test, BorderLayout.CENTER);
        frame.add(right, BorderLayout.EAST);
        frame.add(left, BorderLayout.WEST);
     
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setVisible(true);

    }

    public static void saveToFile(Playlist songs, String filePath, JTextArea output) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("../playlists/" + filePath));
            SongNode temp = songs.getSongs().getHead();
            while(temp != null) {
                Song s = temp.getSong();
                String writing = s.getTitle() + "," + s.getArtist() + ","+ s.getDuration() + "," + s.getGenre() + "," + s.getFilePath(); 
                writer.write(writing);
                writer.newLine();
                temp = temp.getSongNext();
            }
        }
        catch(IOException e) {
            output.setText(e.getMessage());
        }
        finally {
            if(writer != null) {
                try {
                    writer.close();
                }
                catch(IOException e) {
                    output.setText(e.getMessage());
                }
            }
        }
    }

    public static Playlist createPlaylist(ArrayList<String> lines, String name, JTextArea output) {
        Playlist temp = new Playlist(name);
        for(String s : lines) {
            try {
                String[] items = s.split(",");
                temp.addSong(new Song(items[0], items[1], Integer.parseInt(items[2]), items[3], items[4]));   
            }
            catch(NumberFormatException e) {
                output.setText(e.getMessage());
            }
        }
        return temp;
    }

    public static ArrayList<String> loadPlaylist(String file, JTextArea output) {
        BufferedReader reader = null;
        ArrayList<String> lines = new ArrayList<>();
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        catch(IOException e) {
            output.setText(e.getMessage());
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                }
                catch(IOException e) {
                    output.setText(e.getMessage());
                }
            }
        }
        return lines;
    }
}
