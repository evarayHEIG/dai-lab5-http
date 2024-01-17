package api;

/**
 * The Song class represents a song that has a singer and a title.
 *
 * @author Rafael Dousse
 * @author Eva Ray
 */
public class Song {

    public String singer = "";
    public String title = "";

    /**
     * The default constructor.
     * Javalin requires a default constructor to be able to deserialize JSON.
     */
    public Song() {}

    /**
     * The constructor sets the singer and title of the song.
     * @param singer The singer of the song.
     * @param title The title of the song.
     */
    public Song(String singer, String title) {
        this.singer = singer;
        this.title = title;
    }

}
