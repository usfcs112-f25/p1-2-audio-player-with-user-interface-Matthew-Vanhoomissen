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
import java.util.Collections;
import java.awt.event.MouseEvent;

public class Main {
    public static boolean paused = false;
    public static String name;
    public static String artist;
    public static int duration;
    public static String genre;
    public static String filePath = "";
    public static boolean muted = false;
    public static Playlist songs = new Playlist("Playlist1");
    
    public static void main(String[] args) {
        ArrayList<Playlist> playlists = new ArrayList<>();
        
        playlists.add(songs);
        SongPlayer songPlayer = songs.getSongPlayer();
        songs.addSong(new Song("Test1", "T", 10, "T", "../music/running-night-393139.mp3"));
        songs.addSong(new Song("Test2", "T", 20, "T", "../music/file_example_WAV_1MG.wav"));


        

        JFrame frame = new JFrame("Spotify Playlist (Swing)");

        JTextArea output = new JTextArea(5, 20);

        
        JLabel label = new JLabel("Recently played: ");
        JTextArea recentlyPlayed = new JTextArea(5, 20);
        
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
            recentlyPlayed.setText(songs.getRecentlyPlayed());
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
            recentlyPlayed.setText(songs.getRecentlyPlayed());

            if(songs.getCurrentSong() != null) {
               output.setText("Current " + songs.getCurrentSong().toString()); 
            }
            paused = false;
            pause.setText("Pause");
            playlist.repaint();

        });

        prev.addActionListener(e -> {
            songs.playPrev();
            recentlyPlayed.setText(songs.getRecentlyPlayed());

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
                if(e.getClickCount() == 3) {
                    int ind = playlist.locationToIndex(e.getPoint());
                    if(ind >= 0) {
                        songs.addToQueue(songs.getSongs().get(ind + 1));
                        System.out.println("Successfully added to queue");
                    }
                }
                else if(e.getClickCount() == 2) {
                    int ind = playlist.locationToIndex(e.getPoint());
                    if(ind >= 0) {
                        String song = list.getElementAt(ind);
                        int temp = songs.findSongByTitle(song);
                        songs.setCurrentSong(temp);
                        songs.stopPlayback();
                        songs.playCurrentSong();
                        recentlyPlayed.setText(songs.getRecentlyPlayed());
                        output.setText("Current " + songs.getCurrentSong().toString());
                        paused = false;
                        pause.setText("Pause");
                        playlist.repaint();

                    }
                }
                
            }
        });

        playlist.setCellRenderer((jlist, value, index, isSelected, cellHasFocus) -> {
            JLabel label2 = new JLabel(value);
            if(songs.getCurrentSong() != null &&value.equals(songs.getCurrentSong().getSong().getTitle())) {
                label2.setOpaque(true);
                label2.setBackground(Color.LIGHT_GRAY);
            }
            else {
                label2.setOpaque(true);
                label2.setBackground(Color.WHITE);
            }
            return label2;
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
            else {
                String name =(String) editPlaylist.getSelectedItem();
                if(!name.equals(songs.getName())) {
                    for(Playlist p : playlists) {
                        if(p.getName().equals(name)) {
                            songs = p;
                            System.out.println("Playlist switched");
                            for(int i = list.size() - 1; i >= 0; i--) {
                                list.remove(i);
                            }
                            SongNode temp4 = songs.getSongs().getHead();
                            while(temp4 != null) {
                                list.addElement(temp4.getSong().getTitle());
                                temp4 = temp4.getSongNext();
                            }
                            break;
                        }
                    }
                }
            }
            
        });

        JLabel repeatSong = new JLabel("Repeat song");
        repeatSong.setPreferredSize(new Dimension(120, 25));
        JCheckBox repeatingSong = new JCheckBox();

        repeatingSong.addActionListener(e -> {
            songs.toggleRepeatSong();
        });

        JLabel repeatPlaylist = new JLabel("Repeat playlist");
        repeatPlaylist.setPreferredSize(new Dimension(120, 25));
        JCheckBox repeatingPlaylist = new JCheckBox();

        repeatingPlaylist.addActionListener(e -> {
            songs.toggleRepeatPlaylist();
        });

        JSlider slider = new JSlider(0, 2, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);

        slider.addChangeListener(e -> {
            int value = slider.getValue();
            if(value == 0) {
                songPlayer.setVolume(.3);
            }
            else if(value == 1) {
                songPlayer.setVolume(.6);
            }
            else {
                songPlayer.setVolume(1);
            }
        });

        JButton mute = new JButton("Mute");

        mute.addActionListener(e -> {
            if(muted) {
                songPlayer.setVolume(.6);
            }
            else {
                songPlayer.setVolume(0);
            }
            muted = !muted;
        });

        JButton reset = new JButton("Reset");
        JTextField input = new JTextField(15);
        JButton search = new JButton("Search");

        reset.setPreferredSize(new Dimension(70, 20));
        search.setPreferredSize(new Dimension(80, 20));
        search.addActionListener(e -> {
            String text = input.getText();
            if(!text.isBlank()) {
                for(int i = list.size() - 1; i >= 0; i--) {
                    list.remove(i);
                }
                SongNode temp2 = songs.getSongs().getHead();
                while(temp2 != null) {
                    if(input.getText().equalsIgnoreCase(temp2.getSong().getTitle())) {
                        list.addElement(temp2.getSong().getTitle());
                    }
                    temp2 = temp2.getSongNext();
                } 
            }
        });

        reset.addActionListener(e -> {
            for(int i = list.size() - 1; i >= 0; i--) {
                list.remove(i);
            }
            SongNode temp2 = songs.getSongs().getHead();
            while(temp2 != null) {
                list.addElement(temp2.getSong().getTitle());
                temp2 = temp2.getSongNext();
            } 
        });

        JComboBox sort = new JComboBox<String>(new String[]{"Sort by: ", "Title", "Artist", "Duration"});
        sort.setPreferredSize(new Dimension(120, 25));
        JButton sortEnter = new JButton("Enter");

        sortEnter.addActionListener(e -> {
            if(sort.getSelectedItem().equals("Title")) {
                for(int i = list.size() - 1; i >= 0; i--) {
                    list.remove(i);
                }
                ArrayList<String> array = new ArrayList<>();
                SongNode temp3 = songs.getSongs().getHead();
                while(temp3 != null) {
                    array.add(temp3.getSong().getTitle());
                    temp3 = temp3.getSongNext();
                }
                if(array != null) {
                    Collections.sort(array);
                }
                for(String s : array) {
                    list.addElement(s);
                }

            }
            else if(sort.getSelectedItem().equals("Artist")) {
                for(int i = list.size() - 1; i >= 0; i--) {
                    list.remove(i);
                }
                ArrayList<String> array = new ArrayList<>();
                SongNode temp3 = songs.getSongs().getHead();
                while(temp3 != null) {
                    array.add(temp3.getSong().getArtist());
                    temp3 = temp3.getSongNext();
                }
                if(array != null) {
                    Collections.sort(array);
                }
                for(String s : array) {
                    list.addElement(s);
                }
            }
            else if(sort.getSelectedItem().equals("Duration")) {
                for(int i = list.size() - 1; i >= 0; i--) {
                    list.remove(i);
                }
                ArrayList<SongNode> array = new ArrayList<>();
                SongNode temp3 = songs.getSongs().getHead();
                while(temp3 != null) {
                    array.add(temp3);
                    temp3 = temp3.getSongNext();
                }
               
                
                while(array.size() > 0) {
                    SongNode remove2 = null;
                    int duration = -1;
                    for(SongNode s : array) {
                        if(s.getSong().getDuration() > duration) {
                            duration = s.getSong().getDuration();
                            remove2 = s;
                        }
                    }
                    list.addElement(remove2.getSong().getTitle());
                    array.remove(remove2);
                }
            }
        });
        JPanel hidden = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hidden.setVisible(false);

        JCheckBox shuffle = new JCheckBox();
        JLabel shuffle2 = new JLabel("Shuffle");

        JCheckBox smartShuffle = new JCheckBox();
        JLabel smartShuffle2 = new JLabel("Smart shuffle");

        hidden.add(smartShuffle2);
        hidden.add(smartShuffle);
        shuffle.addActionListener(e -> {
            songs.toggleShuffle();
            hidden.setVisible(shuffle.isSelected());
            hidden.revalidate();
            hidden.repaint();
        });

        smartShuffle.addActionListener(e -> {
            if(smartShuffle.isSelected()) {
                songs.makeSmartPlaylist();
                songs.toggleSmartShuffle(true);
            }
            else {
                songs.toggleSmartShuffle(false);
            }
            
            
        });

    

        JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftSide.add(menu);
        leftSide.add(editPlaylist);
        leftSide.add(topLeftSubmit);


        JPanel leftRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow1.add(repeatSong);
        leftRow1.add(repeatingSong);

        JPanel leftRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow2.add(repeatPlaylist);
        leftRow2.add(repeatingPlaylist);

        JPanel leftRow2_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow2_5.add(shuffle2);
        leftRow2_5.add(shuffle);

        JPanel leftRow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow3.add(slider);
        leftRow3.add(mute);

        JPanel leftRow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow4.add(label);
        leftRow4.add(recentlyPlayed);


        JPanel rightRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightRow.add(sort);
        rightRow.add(sortEnter);
        

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row1.add(output);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        row2.add(input);
        

        JPanel row2_5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row2_5.add(search);
        row2_5.add(reset);

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
        test.add(row2_5);
        test.add(row3);
        test.add(row4);
        test.add(row5);
        


        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(inputName);
        right.add(inputArtist);
        right.add(inputDuration);
        right.add(inputGenre);
        right.add(rightSide);
        right.add(rightRow);
        right.add(scrollPane);
        right.add(exit);

        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setAlignmentY(Component.TOP_ALIGNMENT);
        left.add(leftSide);
        left.add(leftRow1);
        left.add(leftRow2);
        left.add(leftRow2_5);
        left.add(hidden);
        left.add(leftRow3);
        left.add(leftRow4);
        


        frame.setLayout(new BorderLayout());

        
        
        frame.add(test, BorderLayout.CENTER);
        frame.add(right, BorderLayout.EAST);
        frame.add(left, BorderLayout.WEST);
     
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
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
