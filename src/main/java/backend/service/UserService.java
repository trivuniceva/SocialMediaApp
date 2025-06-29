package backend.service;

import backend.model.User;
import backend.storage.UserFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserFileStorage userFileStorage;

    public void updateUserInfo(String id, User updatedUser) {

        User existingUser = userFileStorage.findById(id);

        if(existingUser == null){
            System.out.println("Korisnik nije pronadjen");
            return;
        }

        updatedUser.setId(existingUser.getId());

        if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(existingUser.getPassword());
        }

        if (updatedUser.getUsername() == null) updatedUser.setUsername(existingUser.getUsername());
        if (updatedUser.getEmailAddress() == null) updatedUser.setEmailAddress(existingUser.getEmailAddress());
        if (updatedUser.getFirstName() == null) updatedUser.setFirstName(existingUser.getFirstName());
        if (updatedUser.getLastName() == null) updatedUser.setLastName(existingUser.getLastName());
        if (updatedUser.getDateOfBirth() == null) updatedUser.setDateOfBirth(existingUser.getDateOfBirth());
        if (updatedUser.getGender() == null) updatedUser.setGender(existingUser.getGender());
        if (updatedUser.getRole() == null) updatedUser.setRole(existingUser.getRole());
        if (updatedUser.getProfilePicturePath() == null) updatedUser.setProfilePicturePath(existingUser.getProfilePicturePath());
        if (updatedUser.getFriendListIds() == null) updatedUser.setFriendListIds(existingUser.getFriendListIds());
        if (updatedUser.getPostIds() == null) updatedUser.setPostIds(existingUser.getPostIds());
        if (updatedUser.getImageIds() == null) updatedUser.setImageIds(existingUser.getImageIds());
        if (updatedUser.getFriendRequestsSent() == null) updatedUser.setFriendRequestsSent(existingUser.getFriendRequestsSent());
        if (updatedUser.getFriendRequestsReceived() == null) updatedUser.setFriendRequestsReceived(existingUser.getFriendRequestsReceived());

        updatedUser.setPrivateAccount(updatedUser.isPrivateAccount());

        userFileStorage.updateUser(updatedUser);

    }
}
