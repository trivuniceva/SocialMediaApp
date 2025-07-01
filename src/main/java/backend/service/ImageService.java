package backend.service;

import backend.model.Image;
import backend.model.User;
import backend.storage.CommentFileStorage;
import backend.storage.ImageFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageFileStorage imageFileStorage;
    @Autowired
    private CommentFileStorage commentFileStorage;
    @Autowired
    private UserFileStorage userFileStorage;

    public List<Image> getImagesByUser(String userId) {
        var user = userFileStorage.findById(userId);
        if (user == null || user.getImageIds() == null || user.getImageIds().isEmpty()) {
            System.out.println("User or imageIds list is null/empty for userId: " + userId);
            return List.of();
        }
        List<Image> userImages = imageFileStorage.getImagesByIds(user.getImageIds());
        System.out.println("Found " + userImages.size() + " images for userId: " + userId);
        return userImages;
    }

    public Image addImage(Image newImage) {
        if (newImage.getId() == null || newImage.getId().isEmpty()) {
            newImage.setId(UUID.randomUUID().toString());
        }
        newImage.setUploadDate(LocalDateTime.now());
        newImage.setCommentIds(new ArrayList<>());
        newImage.setLogicallyDeleted(false);

        Image addedImage = imageFileStorage.addImage(newImage);

        User user = userFileStorage.findById(newImage.getUserId());
        if (user != null) {
            if (user.getImageIds() == null) {
                user.setImageIds(new ArrayList<>());
            }
            user.getImageIds().add(addedImage.getId());
            userFileStorage.saveUsers();
        }

        return addedImage;
    }

    public void deleteImage(String imageId) {
        Image imageToDelete = imageFileStorage.findById(imageId);

        if (imageToDelete != null) {
            System.out.println(imageToDelete.getCommentIds());
            if (imageToDelete.getCommentIds() != null) {
                for (String commentId : imageToDelete.getCommentIds()) {
                    commentFileStorage.deleteComment(commentId);
                }
            }
            imageFileStorage.deleteImage(imageId);
        }
    }
}
