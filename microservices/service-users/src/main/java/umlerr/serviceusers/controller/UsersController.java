package umlerr.serviceusers.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umlerr.serviceusers.dto.RegisterDTO;
import umlerr.serviceusers.model.Users;
import umlerr.serviceusers.service.UsersService;
import static umlerr.serviceusers.util.UsersUtils.getUsersRegistered;

@RestController
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        usersService.registerUser(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getUsersRegistered(registerDTO.getName()));
    }

    @GetMapping("/auth/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users users) {
        return ResponseEntity.ok(usersService.verify(users));
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok("");
    }
}
