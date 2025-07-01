package backend.service;

import backend.model.Post;
import backend.storage.PostFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private PostFileStorage postFileStorage;
    private UserFileStorage userFileStorage;

    public List<Post> getPostsByUser(String userId) {
        var user = userFileStorage.findById(userId);
        if (user == null || user.getPostIds() == null || user.getPostIds().isEmpty()) {
            System.out.println("User or postIds list is null/empty for userId: " + userId);
            return List.of();
        }
        List<Post> userPosts = postFileStorage.getPostsByIds(user.getPostIds());
        System.out.println("Found " + userPosts.size() + " posts for userId: " + userId);
        return userPosts;

    }

    public void addPost(Post post) {
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
