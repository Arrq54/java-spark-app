import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.PhotoServiceImpl;
import model.Photo;
import response.ResponseEntity;
import response.ResponseStatus;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static spark.Spark.*;

public class AppREST {

        public static void main(String[] args) {
            port(7777);

            get("/api/photos", (req, res) ->getAllPhotos(req,res));

            get("/api/photos/:param", (req, res) ->paramForGet(req,res));

            delete("/api/photos/:id", (req, res) -> deleteUserById(req,res));

            get("/api/photostream/:id", (req, res) ->getPhotoStream(req,res));

            put("/api/photos", (req, res) ->renamePhoto(req,res));
        }
    private static PhotoServiceImpl photoService = new PhotoServiceImpl();

    static String getAllPhotos(Request req, Response res) {
        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        Gson gson = new Gson();
        photoService.getAllPhotos();
        return gson.toJson(new ResponseEntity(
                ResponseStatus.SUCCESS,
                "all photos",
                gson.toJsonTree(photoService.getAllPhotos())
        ));
    }
    static String getPhotoById(Request req, Response res, String id) {
        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Photo photo = photoService.getPhotoById(id);

        if(photo == null){
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.NOT_FOUND,
                    "photo not found with ("+id+")"
            ));
        }else{
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.SUCCESS,
                    "photo found",
                    gson.toJsonTree(photo)
            ));
        }
    }

    static String paramForGet(Request req, Response res) {
        String param = req.params("param");
        try {
            parseInt(param);
            return getPhotoById(req, res, param);
        } catch (NumberFormatException nfe) {
            return getPhotoByName(req, res, param);
        }
    }


    static String getPhotoStream(Request req, Response res) throws IOException {
        String id= req.params("id");
        res.header("Content-Disposition", "attachment; filename=image.jpg");
        for (Map.Entry<String, Photo> entry : photoService.getAllPhotos().entrySet()) {
            if (entry.getValue().getId().equals(id)) {
                File file = new File("images/"+entry.getValue().getName());
                if(entry.getValue().getName().contains(".jpeg") || entry.getValue().getName().contains((".jpg"))){
                    res.type("image/jpeg");
                } else{
                    res.type("image/png");
                }
                OutputStream outputStream = null;
                outputStream = res.raw().getOutputStream();
                outputStream.write(Files.readAllBytes(Path.of("images/"+entry.getValue().getName())));
                outputStream.flush();
                return "";
            }
        }
            Gson gson = new Gson();
            res.header("Access-Control-Allow-Origin", "*");
            res.type("application/json");
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.NOT_FOUND,
                    "photo not found"
            ));


    }

    static String deleteUserById(Request req, Response res) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");

        if(photoService.deleteById(req.params("id"))){
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.SUCCESS,
                    "photo deleted"
            ));
        }else{
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.ERROR,
                    "photo file delete error"
            ));
        }
    }
    static String getPhotoByName(Request req, Response res, String name) {
        res.header("Access-Control-Allow-Origin", "*");
        res.type("application/json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Photo photo = photoService.getPhotoByName(name);

        if(photo == null){
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.NOT_FOUND,
                    "photo not found ("+name+")"
            ));
        }else{
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.SUCCESS,
                    "photo found",
                    gson.toJsonTree(photo)
            ));
        }
    }

    static String renamePhoto(Request req, Response res) {
        Gson gson = new Gson();
        String newName = gson.fromJson(req.body(), Name.class).getName();
        String id = gson.fromJson(req.body(), Name.class).getId();
        if(photoService.rename(id,newName)){
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.SUCCESS,
                    "photo renamed"
            ));
        }else{
            return gson.toJson(new ResponseEntity(
                    ResponseStatus.ERROR,
                    "photo file rename error"
            ));
        }
    }
}
