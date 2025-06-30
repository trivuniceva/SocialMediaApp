package backend.controller;

import backend.dto.CommentDTO;
import backend.model.Comment;
import backend.model.User;
import backend.storage.CommentFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private UserFileStorage userStorage;

    private final CommentFileStorage commentStorage;

    @Autowired
    public CommentController(CommentFileStorage commentStorage) {
        this.commentStorage = commentStorage;
    }

    @GetMapping("/object/{objectId}")
    public List<CommentDTO> getCommentsForObject(@PathVariable String objectId) {
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

    @PostMapping("/add")
    public Comment addComment(@RequestBody Comment newComment) {
        return commentStorage.addComment(newComment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id) {
        commentStorage.deleteComment(id);
    }

}