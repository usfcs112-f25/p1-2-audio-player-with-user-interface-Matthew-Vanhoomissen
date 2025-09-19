public class Main {
    public static void main(String[] args) {
        Playlist testing = new Playlist();
        testing.addSong(new Song("Test1", "T", 10, "T", "///"));
        testing.addSong(new Song("Test2", "T", 10, "T", "///"));

        System.out.println(testing.getSongs().get(2).getPrev());

    }
}
