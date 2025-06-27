package backend.controller;


import backend.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

@CrossOrigin(origins = "http://172.20.10.4:5173")
@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        System.out.println("<33333");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Resource resource = new ClassPathResource("files/users.json");
            InputStream is = resource.getInputStream();
            List<User> users = mapper.readValue(is, new TypeReference<List<User>>() {});
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Greška prilikom čitanja korisnika.");
        }
    }
}

