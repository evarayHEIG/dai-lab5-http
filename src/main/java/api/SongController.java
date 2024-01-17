package api;

import io.javalin.http.Context;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The SongController class is responsible for handling http requests to the api
 *
 * @author Rafael Dousse
 * @author Eva Ray
 */
public class SongController{

    private final ConcurrentHashMap<Integer, Song> songs = new ConcurrentHashMap<>();
    private int nextId = 1;

    /**
     * The default constructor is used to populate the songs map.
     */
    public SongController(){

        songs.put(nextId++, new Song("Green Day", "Holiday"));
        songs.put(nextId++, new Song("Taylor Swift", "Coney Island"));
        songs.put(nextId++, new Song("Red Hot Chili Peppers", "Scar Tissue"));
        songs.put(nextId++, new Song("Taylor Swift", "All Too Well"));
        songs.put(nextId++, new Song("BTS", "Dis-ease"));
    }

    /**
     * The getOne method returns a song with the given id.
     * @param ctx The context of the request.
     */
    public void getOne(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(songs.get(id));
    }

    /**
     * The getAll method returns all songs.
     * @param ctx The context of the request.
     */
    public void getAll(Context ctx) {
        ctx.json(songs);
    }

    /**
     * The create method creates a new song using the data in the request body.
     * @param ctx The context of the request.
     */
    public void create(Context ctx) {
        Song song = ctx.bodyAsClass(Song.class);
        songs.put(nextId++, song);
        ctx.status(201);
    }

    /**
     * The delete method deletes a song with the given id.
     * @param ctx The context of the request.
     */
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        songs.remove(id);
        ctx.status(204);
    }

    /**
     * The update method updates a song with the given id using the data in the request body.
     * @param ctx The context of the request.
     */
    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Song song = ctx.bodyAsClass(Song.class);
        songs.put(id, song);
        ctx.status(200);
    }

}
