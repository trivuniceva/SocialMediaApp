package backend.controller;

import backend.model.Post;
import backend.storage.PostFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    @Autowired
    private PostFileStorage postFileStorage;

    @Autowired
    private UserFileStorage userFileStorage;

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUser(@PathVariable String userId) {
        var user = userFileStorage.findById(userId);
        if (user == null || user.getPostIds() == null || user.getPostIds().isEmpty()) {
            System.out.println("User or postIds list is null/empty for userId: " + userId);
            return List.of();
        }
        List<Post> userPosts = postFileStorage.getPostsByIds(user.getPostIds());
        System.out.println("Found " + userPosts.size() + " posts for userId: " + userId);
        return userPosts;
    }

    @PostMapping("/add")
    public void addPost(@RequestBody Post post) {
        postFileStorage.addPost(post);

        var user = userFileStorage.findById(post.getUserId());
        if (user != null) {
            var postIds = user.getPostIds();
            if (postIds == null) {
                postIds = new ArrayList<>();
                user.setPostIds(postIds);
            }
            postIds.add(post.getId());
            userFileStorage.saveUsers();
        }
    }


}
