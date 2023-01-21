package controller;

import model.Photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PhotoServiceImpl implements PhotoService {
    HashMap<String, Photo> photos = new HashMap<>();

    @Override
    public Photo getPhotoById(String id) {
        getAllPhotos();
        Photo found = null;
        for (Map.Entry<String, Photo> entry : photos.entrySet()) {
           if(entry.getValue().getId().equals(id)){
               found = entry.getValue();
           }
        }

        return found;
    }



    @Override
    public Photo getPhotoByName(String name) {
        getAllPhotos();
        Photo found = null;
        for (Map.Entry<String, Photo> entry : photos.entrySet()) {
            if(entry.getValue().getName().equals(name)){
                found = entry.getValue();
            }
        }

        return found;
    }

    @Override
    public Boolean deleteById(String id) {
        for (Map.Entry<String, Photo> entry : photos.entrySet()) {
            if (entry.getValue().getId().equals(id)) {
                try {
                    Files.delete(Path.of("images/"+entry.getValue().getName()));
                    getAllPhotos();
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public HashMap<String, Photo> getAllPhotos() {
        photos = new HashMap<>();
        File f = new File("images");
        for(int i=0;i<f.listFiles().length;i++){
            photos.put(String.valueOf(i),new Photo(String.valueOf(i), f.listFiles()[i].getName(), f.listFiles()[i].getPath()));
        }
        return photos;
    }
}
