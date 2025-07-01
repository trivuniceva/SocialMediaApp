package backend.controller;

import backend.dto.CommentDTO;
import backend.model.Comment;
import backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/object/{objectId}")
    public List<CommentDTO> getCommentsForObject(@PathVariable String objectId) {
        return commentService.getCommentsForObject(objectId);
    }

    @PostMapping("/add")
    public Comment addComment(@RequestBody Comment newComment) {
        return commentService.addComment(newComment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
    }
}