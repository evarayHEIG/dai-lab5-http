package api;

import io.javalin.*;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7000);

        SongController songController = new SongController();

        app.get("/api/songs", songController::getAll);
        app.get("/api/songs/{id}", songController::getOne);
        app.post("/api/songs/", songController::create);
        app.put("/api/songs/{id}", songController::update);
        app.delete("/api/songs/{id}", songController::delete);
    }
}