package com.learnboot.universalpetcare.services.photo;

import com.learnboot.universalpetcare.models.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface IPhotoService {

    Photo savePhoto(MultipartFile file, long userId) throws IOException, SQLException;
    Photo getPhotoById(long id);
    void deletePhoto(long id, long userId) throws SQLException;
    //Photo updatePhoto(byte[] imageData, long id) throws SQLException;

    Photo updatePhoto(MultipartFile file, long id) throws SQLException, IOException;

    byte[] getImageData(long id) throws SQLException;
}
