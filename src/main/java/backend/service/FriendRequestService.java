package backend.service;

import backend.model.FriendRequest;
import backend.model.User;
import backend.storage.FriendRequestFileStorage;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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


    public FriendRequest sendFriendRequest(String senderId, String receiverId) {
        // Provera da li korisnici postoje
        User sender = userFileStorage.findById(senderId);
        User receiver = userFileStorage.findById(receiverId);

        if (sender == null || receiver == null) {
            System.out.println("Sender or receiver not found.");
            return null; // Nije moguće poslati zahtev ako korisnici ne postoje
        }

        // Provera da li već postoji pending ili accepted zahtev
        boolean requestExists = friendRequestFileStorage.getAllRequests().stream()
                .anyMatch(req ->
                        (req.getSenderId().equals(senderId) && req.getReceiverId().equals(receiverId) && !req.getStatus().equals("rejected")) ||
                                (req.getSenderId().equals(receiverId) && req.getReceiverId().equals(senderId) && !req.getStatus().equals("rejected"))
                );
        if (requestExists) {
            System.out.println("Friend request or friendship already exists.");
            return null; // Ne dozvoli slanje duplog zahteva
        }

        // Kreiranje novog zahteva
        FriendRequest newRequest = new FriendRequest();
        newRequest.setId(UUID.randomUUID().toString()); // Generiši jedinstveni ID
        newRequest.setSenderId(senderId);
        newRequest.setReceiverId(receiverId);
        newRequest.setStatus("pending");
        newRequest.setDate(LocalDateTime.now());

        // Ažuriranje korisnika
        sender.getFriendRequestsSent().add(newRequest.getId());
        receiver.getFriendRequestsReceived().add(newRequest.getId());

        userFileStorage.updateUser(sender);
        userFileStorage.updateUser(receiver);

        // Čuvanje zahteva u fajlu
        friendRequestFileStorage.saveNewRequest(newRequest); // Dodajte ovu metodu u FriendRequestFileStorage

        System.out.println("Friend request sent from " + senderId + " to " + receiverId);
        return newRequest;
    }

    // Dodajte ovu metodu u FriendRequestService.java
    public List<FriendRequest> getSentRequestsForUser(String userId) {
        List<FriendRequest> allRequests = friendRequestFileStorage.getAllRequests();
        // Vraćamo sve zahteve gde je korisnik pošiljalac, bez obzira na status (pending, accepted, rejected)
        // ali za frontend nam treba samo 'pending' status
        return allRequests.stream()
                .filter(fr -> fr.getSenderId().equals(userId))
                .toList();
    }
}
