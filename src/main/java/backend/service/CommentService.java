package backend.service;

import backend.dto.CommentDTO;
import backend.model.Comment;
import backend.model.Image;
import backend.model.User;
import backend.storage.CommentFileStorage;
import backend.storage.ImageFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private ImageFileStorage imageStorage;
    @Autowired
    private UserFileStorage userStorage;
    @Autowired
    private CommentFileStorage commentStorage;

    public List<CommentDTO> getCommentsForObject(String objectId) {
        List<Comment> comments = commentStorage.findByObjectId(objectId);

        return comments.stream()
                .map(comment -> {
                    CommentDTO dto = new CommentDTO();
                    dto.setId(comment.getId());
                    dto.setObjectId(comment.getObjectId());
                    dto.setUserId(comment.getUserId());
                    dto.setText(comment.getText());
                    dto.setCommentDate(comment.getCommentDate());

                    User user = userStorage.findById(comment.getUserId());
                    if (user != null) {
                        dto.setUsername(user.getUsername());
                    } else {
                        dto.setUsername("Nepoznat korisnik");
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Comment addComment(Comment newComment) {
        Comment addedComment = commentStorage.addComment(newComment);

        if ("IMAGE".equals(newComment.getObjectType())) {
            Image imageToUpdate = imageStorage.findById(newComment.getObjectId());
            if (imageToUpdate != null) {
                if (imageToUpdate.getCommentIds() == null) {
                    imageToUpdate.setCommentIds(new ArrayList<>());
                }
                imageToUpdate.getCommentIds().add(addedComment.getId());
                imageStorage.saveImages();
            }
        }

        return addedComment;
    }

    public void deleteComment(String id) {
        commentStorage.deleteComment(id);
    }
}
