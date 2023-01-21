import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.PhotoServiceImpl;
import model.Photo;
import response.ResponseEntity;
import response.ResponseStatus;
import spark.Request;
import spark.Response;
import java.util.HashMap;

import static java.lang.Integer.parseInt;
import static spark.Spark.*;

public class AppREST {

        public static void main(String[] args) {
            port(7777);

            get("/api/photos", (req, res) ->getAllPhotos(req,res));

            get("/api/photos/:param", (req, res) ->paramForGet(req,res));

            delete("/api/photos/:id", (req, res) -> deleteUserById(req,res));


            get("/api/photos/", (req, res) ->paramForGet(req,res));
//            get("/api/photos/:id", (req, res) ->getPhotoById(req,res));
//
//            get("/api/photos/:name", (req, res) ->getPhotoByName(req,res));

//
//            //user z danym id
//            get("/api/users/:id", (req, res) -> getUserById(req,res));
//
//            //dodaj usera
//            post("/api/users", (req, res) ->  add(req,res));
//
//
//            //edycja usera z danym id
//            put("/api/users", (req, res) ->  editUser(req,res));
//
//            //sprawdz czy istnieje
//            options("/api/users/:id", (req, res) -> userExists(req,res));
//
//            //usuwanie
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
}