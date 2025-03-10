package umlerr.serviceauth.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umlerr.serviceauth.dto.AuthDTO;
import umlerr.serviceauth.dto.RegisterDTO;
import umlerr.serviceauth.dto.UsersMapper;
import umlerr.serviceauth.jwt.JWTService;
import umlerr.serviceauth.model.Users;
import umlerr.serviceauth.repository.AuthRepository;
import static umlerr.serviceauth.util.UsersUtils.getUsersAlreadyExist;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersMapper usersMapper;
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Transactional
    public void registerUser(RegisterDTO registerDTO) {
        authRepository.findByEmail(registerDTO.getEmail())
            .ifPresent(auth -> {
                throw new DataIntegrityViolationException(getUsersAlreadyExist(registerDTO.getEmail()));
            });
        Users users = usersMapper.toEntity(registerDTO);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        authRepository.save(users);
    }

    @Transactional
    public List<Users> getAllUsers() {
        return authRepository.findAll();
    }

    @Transactional
    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }

    @Transactional
    public String verify(AuthDTO authDTO) {
        Authentication authentication =
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),
                authDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return generateToken(authDTO.getEmail());
        }
        else {
            throw new RuntimeException("invalid access");
        }
    }
}
