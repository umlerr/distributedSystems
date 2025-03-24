package umlerr.serviceauth.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umlerr.serviceauth.dto.AuthDTO;
import umlerr.serviceauth.dto.RegisterDTO;
import umlerr.serviceauth.service.AuthService;
import java.util.UUID;
import static umlerr.serviceauth.util.UsersUtils.getUsersRegistered;

@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        authService.registerUser(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(getUsersRegistered(registerDTO.getName()));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getAllUsers());
    }

    @GetMapping("/user-by-id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getUserById(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.verify(authDTO));
    }
}
