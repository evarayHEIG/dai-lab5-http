package api;

import io.javalin.*;

/**
 * The Main class is the entry point for the application.
 * It creates a Javalin app and sets up the routes.
 *
 * @author Rafael Dousse
 * @author Eva Ray
 */
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