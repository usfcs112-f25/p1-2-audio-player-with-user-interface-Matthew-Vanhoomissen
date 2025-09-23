/*
 * Creates a playlist that a SongLinkedList is stored in
 */
public class Playlist {
    /*
     * Current node and booleans for repeat are stored here
     */
    private SongLinkedList songs;
    
    private SongNode currentNode;
    private int currentInt;
    SongPlayer songPlayer;
    private boolean repeatSong;
    private boolean repeatPlaylist;

    /*
     * Constructer
     */
    public Playlist() {
        songs = new SongLinkedList();
        currentNode = null;
        currentInt = 1;
        songPlayer = new SongPlayer();
        repeatSong = false;
        repeatPlaylist = false;
    }
    /*
     * returns each respective value
     */
    public SongLinkedList getSongs() {
        return songs;
    }
    public SongPlayer getSongPlayer() {
        return songPlayer; 
    }
    public boolean getRepeatSong() {
        return repeatSong;
    }
    public boolean getRepeatPlaylist() {
        return repeatPlaylist;
    }
    public int getCurrentInt() {
        return currentInt;
    }

    /*
     * Toggles the repeating song/playlist by switching value
     */
    public void toggleRepeatSong() {
        repeatSong = !repeatSong;
        System.out.println("Repeat song: " + repeatSong);
    }
    public void toggleRepeatPlaylist() {
        repeatPlaylist = !repeatPlaylist;
        System.out.println("Repeat playlist: " + repeatPlaylist);
    }
    
    /*
     * Adds song to back 
     * @returns boolean if successful
     * @params is the song being added
     * Sets current Node to head when stuff is added
     */
    public boolean addSong(Song song) {
        songs.addLast(song);
        if(songs.size() >= 1) {
            currentNode = songs.getHead();
        }
        return true;
    }
    /*
     * Displays playlist songs
     */
    public void displayForward() {
        SongNode temp = songs.getHead();
        int num = 1;
        while(temp != null) {
            System.out.println(num + ". " + temp.getSong());
            temp = temp.getSongNext();
            num++;
        }
    }

    public void displayBackward() {
        SongNode temp = songs.getTail();
        int num = songs.size();
        while(temp != null) {
            System.out.println(num + ". " + temp.getSong());
            temp = temp.getPrev();
            num--;
        }
    }
    /*
     * Searches songs by a keyword
     * @params keywork could be for title or artist
     */
    public void searchSongs(String keyword) {
        if(findSongByTitle(keyword) != 0) {
            System.out.println(findSongByTitle(keyword));
        }
        findSongByArtist(keyword);
        
    }
    /*
     * gets total seconds
     * @returns number of seconds
     */
    public int getTotalDuration() {
        SongNode temp = songs.getHead();
        int total = 0;
        while(temp != null) {
            total += temp.getSong().getDuration();
            temp = temp.getSongNext();
        }
        return total;
    }
    /*
     * Sets current song to index
     * Only works if index is valid
     * @returns boolean if works
     * @params index of song
     */
    public boolean setCurrentSong(int index) {
        SongNode temp = songs.getHead();
        int counter = 1;
        if(index > 0 && index <= songs.size()) {
            while(temp != null) {
                if(counter == index) {
                    currentNode = temp;
                    currentInt = index;
                    return true;
                }
                temp = temp.getSongNext();
                counter++;
            }
        }
        return false;
    }
    /*
     * gets current song
     * @returns it
     */
    public SongNode getCurrentSong() {
        return currentNode;
    }
    /*
     * Plays the current song if the current node is valid
     * If the repeat is true the song will repeat when finished
     * If not it will go to next song
     * @throws AudioException
     */
    public void playCurrentSong() {
        if(currentNode == null) {
            System.out.println("No song is selected");
            return;
        }
        
        try {
            System.out.println(partialDuration(currentNode));
        if(songPlayer.playSong(currentNode.getSong(), () -> {
            if(repeatSong) {
                playCurrentSong();
            }
            else {
                playNext();
            }
            
        })) {
            System.out.println(currentNode);
        }
        }
        catch(AudioException e) {
            System.out.println(e.getMessage());
        }
        

        
        
    }



    /*
     * prints songs within second range
     * @params first lower value and second higher value
     */
    public void filterDurationRange(int first, int second) {
        if(songs.getHead() == null) {
            System.out.println("No list");
            return;
        }
        for(int i = 1; i < songs.size() + 1; i++) {
            if(songs.get(i).getSong().getDuration() > first) {
                if(songs.get(i).getSong().getDuration() < second) {
                    System.out.println(songs.get(i));
                }
            }
        }

    }

    /*
     * Used to print how many seconds total the song is into the playlist
     * @returns that amount out of the total
     */
    public String partialDuration(SongNode song) {
        int index = songs.indexOf(song.getSong());
        int total = 0;
        for(int i = 1; i < index; i++) {
            total += songs.get(i).getSong().getDuration();
        }
        return total + "/" + getTotalDuration();
    }

    /*
     * Sets current node to head and plays
     */
    public void playFromStart() {
        currentNode = songs.getHead();
        currentInt = 1;
        playCurrentSong();
    }

    /*
     * Sets current node to random within bounds and plays
     */
    public void playRandom() {
        int num = (int)(Math.random() * songs.size()) + 1;
        currentNode = songs.get(num);
        currentInt = num;
        playCurrentSong();
    }

    /*
     * Stops the playback
     */
    public void stopPlayback() {
        songPlayer.stopSong();
    }
    /*
     * Plays the next song if it exists
     * If it does not playlist stops unless repeat playlist is on
     */
    public void playNext() {
        if(currentNode != null && currentNode.getSongNext() != null) {
            
            currentNode = currentNode.getSongNext();
            currentInt++;
            playCurrentSong();
        }
        else {
            if(repeatPlaylist && songs.getHead() != null) {
                currentNode = songs.getHead();
                playCurrentSong();
            }
            else {
                System.out.println("No song available");
            }
            
        }
    }

    public void playPrev() {
        if(currentNode != null && currentNode.getPrev() != null) {
            currentNode = currentNode.getPrev();
            currentInt--;
            playCurrentSong();
        }
    }

    /*
     * Checks if playlist has songs
     * @returns boolean if true
     */
    public boolean hasSongs() {
        if(songs.size() > 0) {
            return true;
        }
        return false;
    }


    /*
     * Find songs by title
     * @params title of song
     * @returns Song if found
     */
    public int findSongByTitle(String title) {
        SongNode temp = songs.getHead();
        int counter = 1;
        while(temp != null) {
            if(temp.getSong().getTitle().equalsIgnoreCase(title)) {
                return counter;
            }
            counter++;
            temp = temp.getSongNext();
        }
        return 0;
    }

    /*
     * Prints sounds found with artists
     * @params the artist to search by
     */
    public void findSongByArtist(String artist) {
        SongNode temp = songs.getHead();
        while(temp != null) {
            if(temp.getSong().getArtist().equalsIgnoreCase(artist)) {
                System.out.println(temp);
            }
            temp = temp.getSongNext();
        }
        
    }

    /*
     * Prints playlist statistics like size or duration
     */
    public void getPlaylistStats() {
        SongNode temp = songs.getHead();
        int duration = 0;
        while(temp != null) {
            duration += temp.getSong().getDuration();
            temp = temp.getSongNext();
        }
        System.out.println("Total songs: " + songs.size() + ", Total duration: " + duration);
    }

    /*
     * Sorts the playlist by creating a new playlist and checking if the current node is greater or less than
     * if greater it continues if less it is inserted
     * Once at end it is inserted
     * @params playlist to sort
     * @returns playlist sorted
     */
    public Playlist sortPlaylistByDuration(Playlist playlist) {
        Playlist sorted = new Playlist();
        SongNode temp = playlist.getSongs().getHead();
        while(temp != null) {
            if(sorted.getSongs().getHead() == null) {
                sorted.getSongs().addFirst(temp.getSong());
                temp = temp.getSongNext();
                continue;
            }
            int counter = 1;
            SongNode insert = sorted.getSongs().getHead();
            while(insert != null) {
                if(temp.getSong().getDuration() > insert.getSong().getDuration()) {
                    if(insert.getSongNext() == null) {
                        sorted.getSongs().addLast(temp.getSong());
                        break;
                    }
                    insert = insert.getSongNext();
                    counter++;
                }
                else {
                    sorted.getSongs().addAt(counter, temp.getSong());
                    break;
                }
            }
            temp = temp.getSongNext();
        }
        return sorted;
    }

    /*
     * Merges playlist by setting the current tail to new head and vise versa
     * @params othe playlist
     */
    public void merge(Playlist other) {
        if (other == null || other.getSongs().getHead() == null) {
            return; 
        }
        if (songs.getHead() == null) {
            
            songs.setHead(other.getSongs().getHead());
            songs.setTail(other.getSongs().getTail());
            songs.setSize(other.getSongs().size());
        } else {
            songs.getTail().setNext(other.getSongs().getHead());
            songs.setTail(other.getSongs().getTail());
            songs.setSize(songs.size() + other.getSongs().size());
        }

        other.getSongs().clear();
    }

    /*
     * Removes duplicates by checking if they have same name and artist
     */
    public boolean removeDuplicates() {
        boolean removed = false;
        SongNode current = songs.getHead();

        while (current != null) {
            SongNode runner = current;
            while (runner.getSongNext() != null) {
                Song nextSong = runner.getSongNext().getSong();

                if (current.getSong().getTitle().equalsIgnoreCase(nextSong.getTitle()) &&
                    current.getSong().getArtist().equalsIgnoreCase(nextSong.getArtist())) {
                    
                    // remove duplicate
                    runner.setNext(runner.getSongNext().getSongNext());
                    songs.setSize(songs.size() - 1);
                    removed = true;

                    if (runner.getSongNext() == null) {
                        songs.setTail(runner); 
                    }
                } 
                else {
                    runner = runner.getSongNext();
                }
            }
            current = current.getSongNext();
        }
        return removed;
    }
}
