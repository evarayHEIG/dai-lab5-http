package api;


import io.javalin.http.Context;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SongController{

    private final ConcurrentHashMap<Integer, Song> songs = new ConcurrentHashMap<>();
    private int nextId = 1;

    public SongController(){

        songs.put(nextId++, new Song("Green Day", "Holiday"));
        songs.put(nextId++, new Song("Taylor Swift", "Coney Island"));
        songs.put(nextId++, new Song("Red Hot Chili Peppers", "Scar Tissue"));
        songs.put(nextId++, new Song("Taylor Swift", "All Too Well"));
        songs.put(nextId++, new Song("BTS", "Dis-ease"));
    }

    public void getOne(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ctx.json(songs.get(id));
    }

    public void getAll(Context ctx) {
        ctx.json(songs);
    }

    public void create(Context ctx) {
        Song song = ctx.bodyAsClass(Song.class);
        songs.put(nextId++, song);
        ctx.status(201);
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        songs.remove(id);
        ctx.status(204);
    }

    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Song song = ctx.bodyAsClass(Song.class);
        songs.put(id, song);
        ctx.status(200);
    }

}
