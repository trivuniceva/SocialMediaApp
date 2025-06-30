package backend.controller;

import backend.model.Image;
import backend.storage.CommentFileStorage;
import backend.storage.ImageFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:5173")
public class ImageController {

    @Autowired
    private ImageFileStorage imageFileStorage;

    @Autowired
    private CommentFileStorage commentFileStorage;

    @Autowired
    private UserFileStorage userFileStorage;

    @GetMapping("/user/{userId}")
    public List<Image> getImagesByUser(@PathVariable String userId) {
        var user = userFileStorage.findById(userId);
        if (user == null || user.getImageIds() == null || user.getImageIds().isEmpty()) {
            System.out.println("User or imageIds list is null/empty for userId: " + userId);
            return List.of();
        }
        List<Image> userImages = imageFileStorage.getImagesByIds(user.getImageIds());
        System.out.println("Found " + userImages.size() + " images for userId: " + userId);
        return userImages;
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(@PathVariable String imageId) {
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