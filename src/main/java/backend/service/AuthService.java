package backend.service;

import backend.dto.LoginRequest;
import backend.dto.UserDTO;
import backend.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class AuthService {


    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("files/users.json");
            InputStream is = resource.getInputStream();

            if (is == null) {
                System.out.println("Ne mogu da učitam users.json fajl!");
                return ResponseEntity.status(500).body("Fajl sa korisnicima nije pronađen.");
            }

            System.out.println("InputStream je: " + is);

            List<User> users = mapper.readValue(is, new TypeReference<List<User>>() {});

            for (User user : users) {
                System.out.println("Proveravam: " + user.getUsername() + " / " + user.getPassword());
                if (user.getUsername().equals(loginRequest.getUsername())
                        && user.getPassword().equals(loginRequest.getPassword())) {

                    System.out.println("Korisnik pronađen!");

                    UserDTO dto = new UserDTO(
                            user.getId(),
                            user.getEmailAddress(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getRole()
                    );
                    return ResponseEntity.ok(dto);
                }
            }

            System.out.println("Nema korisnika sa datim kredencijalima.");
            return ResponseEntity.status(401).body("Pogrešan username ili lozinka.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Greška prilikom autentifikacije.");
        }
    }
}
