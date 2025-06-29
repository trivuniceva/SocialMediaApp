package backend.storage;

import backend.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserFileStorage {
    private final String filePath = "src/main/resources/files/users.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<User> users;


    public UserFileStorage() {
        loadUsers();
    }

    public void loadUsers() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                users = new ArrayList<>();
                return;
            }
            users = mapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
    }

    public void saveUsers() {
        try {
            File file = new File(filePath);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User findByUsername(String username) {

        System.out.println(username);
        System.out.println("find");
        System.out.println(users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null));
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }


}
