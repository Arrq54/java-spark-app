package controller;

import model.Photo;

import java.util.HashMap;

public interface PhotoService {
    Photo getPhotoById(String id);
    Photo getPhotoByName(String name);

    Boolean deleteById(String id);

    HashMap<String, Photo> getAllPhotos();
}
