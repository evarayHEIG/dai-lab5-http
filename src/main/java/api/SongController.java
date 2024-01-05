package api;


import io.javalin.http.Context;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SongController{

    private final ConcurrentHashMap<Integer, Song> songs = new ConcurrentHashMap<>();
    private int nextId = 1;

    public SongController(){

        songs.put(nextId++, new Song("Holiday", "Green Day"));
        songs.put(nextId++, new Song("Coney Island", "Taylor Swift"));
        songs.put(nextId++, new Song("Scar Tissue", "Red Hot Chili Peppers"));
        songs.put(nextId++, new Song("All Too Well", "Taylor Swift"));
        songs.put(nextId++, new Song("Dis-ease", "BTS"));
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
