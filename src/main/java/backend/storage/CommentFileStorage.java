package backend.storage;

import backend.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CommentFileStorage {
    private final String filePath = "src/main/resources/files/comments.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Comment> comments;

    public CommentFileStorage() {
        // Registracija modula za LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        loadComments();
    }

    // Učitava komentare iz JSON datoteke
    public void loadComments() {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.length() == 0) {
                comments = new ArrayList<>();
                return;
            }
            comments = mapper.readValue(file, new TypeReference<List<Comment>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            comments = new ArrayList<>();
        }
    }

    // Čuva komentare u JSON datoteku
    public void saveComments() {
        try {
            File file = new File(filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, comments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Vraća sve komentare
    public List<Comment> getAllComments() {
        return new ArrayList<>(comments); // Vraća kopiju liste da bi se izbegle eksterne modifikacije
    }

    // Pronalazi komentar po ID-u
    public Comment findById(String id) {
        return comments.stream()
                .filter(c -> c.getId().equals(id) && !c.isLogicallyDeleted())
                .findFirst()
                .orElse(null);
    }

    // Vraća sve komentare za određeni objekat (sliku/post)
    public List<Comment> findByObjectId(String objectId) {
        return comments.stream()
                .filter(c -> c.getObjectId().equals(objectId) && !c.isLogicallyDeleted())
                .collect(Collectors.toList());
    }

    // Dodaje novi komentar
    public Comment addComment(Comment newComment) {
        if (newComment.getId() == null || newComment.getId().isEmpty()) {
            newComment.setId(UUID.randomUUID().toString()); // Generiše jedinstveni ID
        }
        comments.add(newComment);
        saveComments();
        return newComment;
    }

    // Ažurira postojeći komentar
    public void updateComment(Comment updatedComment) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getId().equals(updatedComment.getId())) {
                comments.set(i, updatedComment);
                saveComments();
                return;
            }
        }
    }

    // Logički briše komentar (postavlja logicallyDeleted na true)
    public void deleteComment(String id) {
        Comment commentToDelete = findById(id);
        if (commentToDelete != null) {
            commentToDelete.setLogicallyDeleted(true);
            saveComments();
        }
    }
}