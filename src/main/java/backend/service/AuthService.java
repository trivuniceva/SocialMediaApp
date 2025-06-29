package backend.service;

import backend.dto.LoginRequest;
import backend.dto.UserDTO;
import backend.model.User;
import backend.storage.UserFileStorage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserFileStorage userFileStorage;


    public ResponseEntity<?> login(LoginRequest loginRequest) {

        try {
            User user = userFileStorage.findByUsername(loginRequest.getUsername());

            if (user == null || user.getPassword() == null || !user.getPassword().equals(loginRequest.getPassword())) {
                System.out.println("Nema korisnika sa datim kredencijalima ili je lozinka null.");
                return ResponseEntity.status(401).body("Pogrešan username ili lozinka.");
            }

            System.out.println("Korisnik pronađen!");

            UserDTO dto = new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmailAddress(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getDateOfBirth(),
                    user.getGender(),
                    user.getRole(),
                    user.getProfilePicturePath(),
                    user.getFriendListIds(),
                    user.getPostIds(),
                    user.getImageIds(),
                    user.getFriendRequestsSent(),
                    user.getFriendRequestsReceived(),
                    user.isPrivateAccount(),
                    user.isLogicallyDeleted(),
                    user.isBlocked()
            );

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Greška prilikom autentifikacije.");
        }
    }
}
