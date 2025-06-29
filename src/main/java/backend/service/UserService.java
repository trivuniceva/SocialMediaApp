package backend.service;

import backend.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class UserService {

    private final String USERS_FILE_PATH = "src/main/resources/files/users.json";

    public void updateUserInfo(String id, User updatedUser) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 1. Učitaj sve korisnike
            File file = new File(USERS_FILE_PATH);
            List<User> users = mapper.readValue(file, new TypeReference<List<User>>() {});

            // 2. Pronađi i ažuriraj korisnika
            boolean found = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(id)) {
                    users.set(i, updatedUser);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Korisnik sa id " + id + " nije pronađen.");
            }

            // 3. Sačuvaj nazad u fajl
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
            System.out.println("Korisnik uspešno ažuriran: " + id);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom ažuriranja korisnika: " + e.getMessage());
        }
    }
}
