package backend.controller;

import backend.model.FriendRequest;
import backend.model.User;
import backend.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/received/{userId}")
    public ResponseEntity<?> getPendingRequests(@PathVariable String userId) {
        List<FriendRequest> pending = friendRequestService.getPendingRequestsForUser(userId);
        return ResponseEntity.ok(pending);
    }

    @PostMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptRequest(@PathVariable String requestId) {
        boolean success = friendRequestService.acceptRequest(requestId);
        return success ? ResponseEntity.ok("Accepted") : ResponseEntity.badRequest().body("Invalid ID");
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectRequest(@PathVariable String requestId) {
        boolean success = friendRequestService.rejectRequest(requestId);
        return success ? ResponseEntity.ok("Rejected") : ResponseEntity.badRequest().body("Invalid ID");
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendRequest(@RequestBody FriendRequest request) {
        FriendRequest newRequest = friendRequestService.sendFriendRequest(request.getSenderId(), request.getReceiverId());
        if (newRequest != null) {
            return ResponseEntity.ok(newRequest);
        } else {
            return ResponseEntity.badRequest().body("Failed to send friend request. Users may not exist or request already pending.");
        }
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<?> getSentRequests(@PathVariable String userId) {
        List<FriendRequest> sent = friendRequestService.getSentRequestsForUser(userId);
        return ResponseEntity.ok(sent);
    }

}
