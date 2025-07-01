package backend.controller;

import backend.model.Image;
import backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:5173")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/user/{userId}")
    public List<Image> getImagesByUser(@PathVariable String userId) {
        return imageService.getImagesByUser(userId);
    }

    @PostMapping("/add")
    public Image addImage(@RequestBody Image newImage) {
        return imageService.addImage(newImage);
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(@PathVariable String imageId) {
        imageService.deleteImage(imageId);
    }
}