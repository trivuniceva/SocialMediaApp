package backend.service;

import backend.model.FriendRequest;
import backend.model.User;
import backend.storage.FriendRequestFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestFileStorage friendRequestFileStorage;

    @Autowired
    private UserFileStorage userFileStorage;

    public List<FriendRequest> getPendingRequestsForUser(String userId) {
        List<FriendRequest> allRequests = friendRequestFileStorage.getAllRequests();

        System.out.println("Filtering pending requests for user: " + userId);
        for (FriendRequest fr : allRequests) {
            System.out.println("Request id: " + fr.getId() + ", receiverId: " + fr.getReceiverId() + ", status: " + fr.getStatus());
        }

        return allRequests.stream()
                .filter(fr -> fr.getReceiverId().equals(userId) && "pending".equalsIgnoreCase(fr.getStatus()))
                .toList();
    }



    public boolean acceptRequest(String requestId) {
        FriendRequest request = friendRequestFileStorage.findById(requestId);
        if (request == null || !request.getStatus().equals("pending")) return false;

        request.setStatus("accepted");
        friendRequestFileStorage.updateRequest(request);

        User sender = userFileStorage.findById(request.getSenderId());
        User receiver = userFileStorage.findById(request.getReceiverId());

        if (sender == null || receiver == null) return false;

        sender.getFriendListIds().add(receiver.getId());
        receiver.getFriendListIds().add(sender.getId());

        receiver.getFriendRequestsReceived().remove(sender.getId());
        sender.getFriendRequestsSent().remove(receiver.getId());

        userFileStorage.updateUser(sender);
        userFileStorage.updateUser(receiver);

        return true;
    }

    public boolean rejectRequest(String requestId) {
        FriendRequest request = friendRequestFileStorage.findById(requestId);
        if (request == null || !request.getStatus().equals("pending")) return false;

        request.setStatus("rejected");
        friendRequestFileStorage.updateRequest(request);

        User sender = userFileStorage.findById(request.getSenderId());
        User receiver = userFileStorage.findById(request.getReceiverId());

        if (sender == null || receiver == null) return false;

        receiver.getFriendRequestsReceived().remove(sender.getId());
        sender.getFriendRequestsSent().remove(receiver.getId());

        userFileStorage.updateUser(sender);
        userFileStorage.updateUser(receiver);

        return true;
    }
}
