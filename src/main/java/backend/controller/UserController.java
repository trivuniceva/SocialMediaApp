package backend.controller;


import backend.model.User;
import backend.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

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

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        System.out.println("Updating user with id: " + id);
        System.out.println(updatedUser);

        userService.updateUserInfo(id, updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}

