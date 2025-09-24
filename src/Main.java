import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
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
    /*
     * Creates static variables that are used inside addActionListeners
     * Or global variables that need to get changed
     */
    public static boolean paused = false;
    public static String name;
    public static String artist;
    public static int duration;
    public static String genre;
    public static String filePath = "";
    public static boolean muted = false;
    public static JProgressBar progressBar = new JProgressBar();
    public static Playlist songs = new Playlist("Playlist1", progressBar);
    
    /*
     * Main method that generates the GUI and processes requests
     */
    public static void main(String[] args) {
        /*
         * Creates an ArrayList of playlists to store the playlists used in the program
         * You can save a playlist to a file but this is the preset playlist
         * 
         */
        ArrayList<Playlist> playlists = new ArrayList<>();
        songs.addSong(new Song("Testing1", "Me", 4, "Test", "../music/file_example_WAV_1MG.wav"));
        songs.addSong(new Song("Testing2", "You", 94, "Test2", "../music/running-night-393139.mp3"));

        playlists.add(songs);
        SongPlayer songPlayer = songs.getSongPlayer();
        
        /*
         * Variables or components that are utilized throughout the program
         * The three columns of components are first initialized here
         */
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

        /*
         * Creates a JList that displays all the songs in the playlist
         */
        DefaultListModel<String> list = new DefaultListModel<>();
        JList<String> playlist = new JList<>(list);
        JScrollPane scrollPane = new JScrollPane(playlist);
        SongNode temp = songs.getSongs().getHead();
        while(temp != null) {
            list.addElement(temp.getSong().getTitle());
            temp = temp.getSongNext();
        }

        /*
         * Enables the ability to drag on drop items in the JList
         * by creating a new TransferHandler
         */
        playlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        playlist.setDragEnabled(true);
        playlist.setDropMode(DropMode.INSERT);
        playlist.setTransferHandler(new TransferHandler() {
            //Sets previous index
            private int prevIndex = -1;

            //Gets where the item is getting moved to
            /*
             * @returns the int location
             * @params is the component that is being moved
             */
            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            //Creates a transferable object that makes the data able to change location
            /*
             * @returns the Transferable object
             * @params is the componenet that the data needs to get transfered from
             */
            @Override
            public Transferable createTransferable(JComponent c) {
                prevIndex = playlist.getSelectedIndex();
                return new StringSelection(playlist.getSelectedValue());
            }

            //Finds whether the data being transfered is supported in the TransferHandler
            /*
             * @returns boolean if the data type is supported
             * @params an extention of the TransferHandler that determins suitability of transfer
             */
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            //Imports the data and component and changes the location along with the data
            /*
             * @returns boolean if the data is imported and transfered correctly
             * @params same as above
             */
            @Override 
            public boolean importData(TransferSupport support) {
                try {
                    JList.DropLocation location1 = (JList.DropLocation) support.getDropLocation();
                    int index = location1.getIndex();

                    if(prevIndex != -1 && index != prevIndex) {
                        String element = list.getElementAt(prevIndex);
                        list.remove(prevIndex);

                        if(index > prevIndex) {
                            index--;
                        }

                        list.add(index, element);
                        playlist.setSelectedIndex(index);
                        return true;
                    } 

                }
                catch(Exception e) {
                    output.setText(e.getMessage());
                }
                return false;
            }
        });

        /*
         * When button clicked the current song is played and the song is output on the screen
         */
        button.addActionListener(e -> {
            songs.playCurrentSong();
            recentlyPlayed.setText(songs.getRecentlyPlayed());
            if(songs.getCurrentSong() != null) {
               output.setText("Current " + songs.getCurrentSong().toString()); 
            }
            
            playlist.repaint();
            
        });

        //Pauses song and if pressed it toggles between paused and unpaused
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

        //Stops song being played also retoggles the pause button
        stop.addActionListener(e -> {
            songs.getSongPlayer().stopSong();
            paused = false;
            pause.setText("Pause");
            playlist.repaint();
        });

        //Goes to next song and retoggles pause button
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

        //goes to previous song
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

        
        //Allows selection of file from computer and saves filepath to String
        JButton file = new JButton("Select file");

        file.addActionListener(e -> {
            
            JFileChooser fileChoice = new JFileChooser();
            int choice = fileChoice.showOpenDialog(frame);
            if(choice == JFileChooser.APPROVE_OPTION) {
                File chosenFile = fileChoice.getSelectedFile();
                filePath = chosenFile.getAbsolutePath();
                
                
            }
            
        });
        

        //Creates a new song if all items are input correctly
        //@throws NumberFormatException if invalid duration is put in
        JTextField inputName = new JTextField("Enter name(delete this text)");
        inputName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JTextField inputArtist = new JTextField("Enter artist");
        inputArtist.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JTextField inputDuration = new JTextField("Enter duration");
        inputDuration.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JTextField inputGenre = new JTextField("Enter genre");
        inputGenre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JButton submit = new JButton("Add song");

        submit.addActionListener(e -> {
            if(inputName.getText().isBlank() || inputArtist.getText().isBlank() || inputDuration.getText().isBlank() || inputGenre.getText().isBlank() || filePath.isBlank()) {
                output.setText("Could not add new song. Input box left blank");                
            }
            else {
                try {
                    songs.addSong(new Song(inputName.getText(), inputArtist.getText(), Integer.parseInt(inputDuration.getText()), inputGenre.getText(), filePath));
                    output.setText("Song added");
                    list.addElement(inputName.getText());
                }
                catch(NumberFormatException d) {
                    output.setText(d.getMessage());
                }
                
                
            }
        });

        //Removes current song and updates the playlist songs shown
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

        //Clears playlist of songs and resets songs shown
        JButton clear = new JButton("Clear songs");
        clear.addActionListener(e -> {
            songs.clearSongs();
            output.setText("");
            for(int i = list.size() - 1; i >= 0; i--) {
                list.remove(i);
            } 
        });


        //if triple clicked adds that songs to queue
        //Double click just plays the song
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

        //Changes the display of the song being played
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

        //Exits app
        JButton exit = new JButton("Quit");
        exit.addActionListener(e -> {
            System.exit(1);
        });

        //Allows playlist options
        //Can create new playlist, load one from file that makes one with existing
        // data, or saves the current playlist to file
        
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
                    playlists.add(new Playlist(inputN, progressBar));
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
                //Switches from one playlist to the next
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

        //Toggles repeating song or playlist
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

        //Sets volume depending on where slider is
        //Can also mute
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

        //Can search for a song and displays in the playlist part
        //Only shows song that matches input
        //Can reset to all songs
        JButton reset = new JButton("Reset");
        JTextField input = new JTextField("Enter name of song to search(delete this text)");
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

        //Allows different kinds of sorts
        //Displays new sorted list
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
        //Shuffle and smart shuffle
        //Toggles shuffle to only play random songs
        //If on, then the option for smart shuffle appears
        //That only plays random songs no repeating until done
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

        //Allows cross playlist song movement
        //You can either move a song from one to another or just copy and paste
        //Copy and paste doesn't remove from current playlist
        JComboBox crossPlaylist = new JComboBox<>(new String[]{"Cross Playlist", "Move current song", "Copy and paste current song"});
        JButton crossButton = new JButton("Sent");

        crossButton.addActionListener(e -> {
            if(!crossPlaylist.getSelectedItem().equals("Cross Playlist")) {
                String inputC = JOptionPane.showInputDialog(frame, "Enter playlist to move song to"); 
                if(crossPlaylist.getSelectedItem().equals("Move current song")) {
                    for(Playlist p : playlists) {
                        if(p.getName().equals(inputC) && !songs.getName().equals(inputC)) {
                            p.addSong(songs.getCurrentSong().getSong());
                            songs.getSongs().removeAt(songs.getCurrentInt());
                            songs.setCurrentSong(1);

                            for(int i = list.size() - 1 ; i >= 0; i--) {
                                if(list.get(i).equals(songs.getSongs().get(songs.getCurrentInt()).getSong().getTitle())) {
                                    list.remove(i);
                                }
                            }
                            SongNode temp0 = songs.getSongs().getHead();
                            while(temp0 != null) {
                                list.addElement(temp0.getSong().getTitle());
                                temp0 = temp0.getSongNext();
                            }
                            playlist.repaint();
                            break;
                        }
                    } 
                }
                else {
                    for(Playlist p : playlists) {
                        if(p.getName().equals(inputC) && !songs.getName().equals(inputC)) {
                            p.addSong(songs.getCurrentSong().getSong());
                            break;
                        }
                    }    
                }
            }
            

        });

        //Speed slider to change playback speed
        JSlider speed = new JSlider(0, 2, 1);
        JLabel speedLabel = new JLabel("Playback speed");

        speed.addChangeListener(e -> {
            if(speed.getValue() == 0) {
                songPlayer.setSpeed(.5);
            }
            else if(speed.getValue() == 1) {
                songPlayer.setSpeed(1);
            }
            else {
                songPlayer.setSpeed(2);
            }
        });

        //Deletes duplicates
        JButton duplicates = new JButton("Delete duplicates");

        duplicates.addActionListener(e -> {
            boolean result = songs.removeDuplicates();
            if(result) {
                output.setText("Duplicates removed");
            }
            else {
                output.setText("No duplicates removed");
            }
            for(int i = list.size() - 1 ; i >= 0; i--) {
                list.remove(i);
            }
            SongNode temp0 = songs.getSongs().getHead();
            while(temp0 != null) {
                list.addElement(temp0.getSong().getTitle());
                temp0 = temp0.getSongNext();
            }
            playlist.repaint();
            songs.setCurrentSong(1);
            

        });

       

        //Makes the rows for the needed side

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

        JPanel leftRow3_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow3_5.add(speed);
        leftRow3_5.add(speedLabel);

        JPanel leftRow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow4.add(label);
        leftRow4.add(recentlyPlayed);

        JPanel leftRow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftRow5.add(crossPlaylist);
        leftRow5.add(crossButton);


        JPanel rightRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightRow.add(sort);
        rightRow.add(sortEnter);
        

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row1.add(output);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        row2.add(input);
        
        JPanel row2_25 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        if(progressBar != null) {
            row2_25.add(progressBar);
        }

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

        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row6.add(duplicates);
        

        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightSide.add(file);
        rightSide.add(submit);


        //Adds those rows to respective side

        test.setLayout(new BoxLayout(test, BoxLayout.Y_AXIS));
        test.add(row1);
        test.add(row2);
        test.add(row2_25);
        test.add(row2_5);
        test.add(row3);
        test.add(row4);
        test.add(row5);
        test.add(row6);
        


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
        left.add(leftRow3_5);
        left.add(leftRow4);
        left.add(leftRow5);
        


        frame.setLayout(new BorderLayout());

        
        //adds all three columns to the frame
        frame.add(test, BorderLayout.CENTER);
        frame.add(right, BorderLayout.EAST);
        frame.add(left, BorderLayout.WEST);
     
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

    }

    /*
     * Saves playlist and songs to file
     * @throws IOException if error saving
     * @params playlist used to save
     */
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

    /*
     * Creates playlist
     * @throws NNumberFormatException if parsing fails
     * @params ArrayList of lines
     * @returns playlist after everything is added
     */
    public static Playlist createPlaylist(ArrayList<String> lines, String name, JTextArea output) {
        Playlist temp = new Playlist(name, progressBar);
        for(String s : lines) {
            try {
                String[] items = s.split(",");
                temp.addSong(new Song(items[0], items[1], Integer.parseInt(items[2]), items[3], items[4]));   
            }
            catch(NumberFormatException d) {
                output.setText(d.getMessage());
            }
        }
        return temp;
    }

    /*
     * Loads playlist from csv
     * @throws IOException if error reading
     * @returns ArrayList of the lines
     * @params of the file String
     */
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
