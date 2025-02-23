package umlerr.serviceusers.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umlerr.serviceusers.dto.RegisterDTO;
import umlerr.serviceusers.dto.UsersMapper;
import umlerr.serviceusers.model.Users;
import umlerr.serviceusers.repository.UsersRepository;
import static umlerr.serviceusers.util.UsersUtils.getUsersAlreadyExist;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Transactional
    public void registerUser(RegisterDTO registerDTO) {
        usersRepository.findByEmail(registerDTO.getEmail())
            .ifPresent(_ -> {
                throw new DataIntegrityViolationException(getUsersAlreadyExist(registerDTO.getEmail()));
            });
        Users users = usersMapper.toEntity(registerDTO);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        usersRepository.save(users);
    }

    @Transactional
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Transactional
    public String verify(Users users) {
        Authentication authentication =
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getEmail(),
                users.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(users.getEmail());
        }
        return "Fail";
    }
}
