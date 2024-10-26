package com.learnboot.universalpetcare.services.photo;

import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.Photo;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.PhotoRepository;
import com.learnboot.universalpetcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService implements IPhotoService {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    @Override
    public Photo savePhoto(MultipartFile file, long userId) throws IOException, SQLException {
        Optional<User> user = userRepository.findById(userId);
        Photo photo = new Photo();
        if(file != null && !file.isEmpty()) {
            //from the front end - we get photo as Bytes -> convert byte to Blog and save inDB
            byte[] bytes = file.getBytes();
            Blob photoBlob = new SerialBlob(bytes);
            photo.setImage(photoBlob);
            photo.setFileType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
        }
        Photo savedPhoto = photoRepository.save(photo);
        user.ifPresent(value -> value.setPhoto(savedPhoto));
        userRepository.save(user.get());
        return savedPhoto;
    }

    @Override
    public Photo getPhotoById(long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo Not Found!"));
    }

    @Transactional //if one fails whole transaction get's roolback
    @Override
    public void deletePhoto(long id, long userId) {
        userRepository.findById(userId)
                        .ifPresentOrElse(User::removeUserPhoto,()->{throw new ResourceNotFoundException("User Not Found!");});

        photoRepository.findById(id)
                .ifPresentOrElse(photoRepository::delete,
                        () -> {throw new ResourceNotFoundException("Photo Not Found!");});
    }

    //if u put only the byte -> postman -> body->binary -> put photo over therte, only photo will get updated
    //becuase it is in the form of bytes
//    @Override
//    public Photo updatePhoto(byte[] imageData, long id) throws SQLException {
//        Optional<Photo> photo = getPhotoById(id);
//        if(photo.isPresent()) {
//           Photo thePhoto = photo.get();
//           Blob photoBlog = new SerialBlob(imageData);
//           thePhoto.setImage(photoBlog);
//           thePhoto.setFileName(imageData.);
//           return photoRepository.save(thePhoto);
//        }
//        throw new ResourceNotFoundException("Photo Not Found!");
//    }

    @Override
    public Photo updatePhoto(MultipartFile file, long id) throws SQLException, IOException {
        Photo photo = getPhotoById(id);
        if(file!=null && !file.isEmpty()) {
           byte[] photoBytes =  file.getBytes();
           Blob photoBlog = new SerialBlob(photoBytes);
           photo.setImage(photoBlog);
          photo.setFileType(file.getContentType());
          photo.setFileName(file.getOriginalFilename());
           return photoRepository.save(photo);
        }
        throw new ResourceNotFoundException("Photo Not Found!");
    }

    @Override
    public byte[] getImageData(long id) throws SQLException {
        Photo photo = getPhotoById(id);
        if(photo!=null) {
            Blob photoBlob = photo.getImage();
            int photoLength = (int) photoBlob.length();
            return photoBlob.getBytes(1, photoLength);
        }
        return null;
    }
}
