package crud.repository.admin;

import crud.model.entities.User;

import java.util.UUID;

public interface AdminRepository {

    User findByEmail(String email);
    User findById(UUID id);
}
