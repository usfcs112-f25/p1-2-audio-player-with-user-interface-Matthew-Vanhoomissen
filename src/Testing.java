import javax.swing.JProgressBar;

public class Testing {
    public static void main(String[] args) {
        System.out.println("Starting test");
        Playlist playlist1 = new Playlist("Test", new JProgressBar());

        if(!playlist1.getName().equals("Test")) {
            System.out.println("Error with constructor or getter");
        }

        Song test = new Song("Test", "Me", 10, "Test", "//");
        playlist1.addSong(test);
        if(!playlist1.getCurrentSong().getSong().equals(test) || playlist1.getCurrentInt() != 1) {
            System.out.println("Error adding song or getting current song");
        } 

        Song test2 = new Song("Test2", "Me", 20, "Test", "///");
        playlist1.addSong(test2);
        playlist1.setCurrentSong(2);
        if(!playlist1.getCurrentSong().getSong().equals(test2) || playlist1.getCurrentInt() != 2) {
            System.out.println("Error setting new current song or increasing index");
        }

        playlist1.playPrev();
        playlist1.stopPlayback();
        if(!playlist1.getCurrentSong().getSong().equals(test)) {
            System.out.println("Error with setting song to previous");
        }

        playlist1.playNext();
        playlist1.stopPlayback();
        if(!playlist1.getCurrentSong().getSong().equals(test2)) {
            System.out.println("Error wit setting song to next");
        }

        playlist1.toggleRepeatSong();
        if(!playlist1.getRepeatSong()) {
            System.out.println("Error toggling repeat song");
        }
        
        playlist1.addToQueue(new SongNode(test2));
        if(playlist1.getQueue().size() != 1) {
            System.out.println("Error adding to queue");
        }

        if(playlist1.getPrevPlayed().size() != 2) {
            System.out.println("Error saving previously played");
        }

        playlist1.makeSmartPlaylist();
        if(playlist1.getNotPLayed().size() != 2) {
            System.out.println("Error creating smart shuffle playlist");
        }

        int temp = playlist1.findSongByTitle("Test");
        if(temp != 1) {
            System.out.println("Error finding song by title ");
        }

        playlist1.getSongs().removeLast();
        if(playlist1.getCurrentSong().getSongNext() != null) {
            System.out.println("Error removing last song");
        }

        playlist1.clearSongs();
        if(playlist1.getSongs().getHead() != null) {
            System.out.println("Error clearing songs from playlist");
        }
        



        System.out.println("Test done");
        System.exit(1);
    }

}
