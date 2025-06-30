// ImageFileStorage.java
package backend.storage;

import backend.model.Image;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageFileStorage {
    private final String filePath = "src/main/resources/files/images.json";

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private List<Image> images;

    public ImageFileStorage() {
        loadImages();
    }

    private void loadImages() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                images = new ArrayList<>();
                return;
            }
            images = mapper.readValue(file, new TypeReference<List<Image>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            images = new ArrayList<>();
        }
    }

    public List<Image> getAllImages() {
        return images;
    }

    public List<Image> getImagesByIds(List<String> ids) {
        List<Image> result = new ArrayList<>();
        for (String id : ids) {
            images.stream()
                    .filter(img -> img.getId().equals(id) && !img.isLogicallyDeleted())
                    .findFirst()
                    .ifPresent(result::add);
        }
        return result;
    }

    public void saveImages() {
        try {
            File file = new File(filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, images);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}