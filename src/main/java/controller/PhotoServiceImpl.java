package controller;

import model.Photo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PhotoServiceImpl implements PhotoService {
    HashMap<String, Photo> photos = new HashMap<>();

    @Override
    public HashMap<String, Photo> getAllPhotos() {
        photos = new HashMap<>();
        File f = new File("images");
        for(int i=0;i<f.listFiles().length;i++){
                photos.put(String.valueOf(i),new Photo(String.valueOf(i), f.listFiles()[i].getName(), f.listFiles()[i].getPath()));

        }
        return photos;
    }

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
    public Boolean rename(String id, String name) {
        Boolean flag = false;
        for (Map.Entry<String, Photo> entry : photos.entrySet()) {
            if(entry.getValue().getId().equals(id)){


                Path source = Paths.get("images/"+entry.getValue().getName());
                try {
                    Files.move(source, source.resolveSibling(name));
                    flag=true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }



            }
        }
        return flag;
    }


}
