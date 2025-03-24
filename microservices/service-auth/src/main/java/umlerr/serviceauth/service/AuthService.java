package umlerr.serviceauth.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umlerr.jwt.JWTService;
import umlerr.serviceauth.dto.AuthDTO;
import umlerr.serviceauth.dto.RegisterDTO;
import umlerr.serviceauth.dto.UsersMapper;
import umlerr.serviceauth.model.Users;
import umlerr.serviceauth.repository.AuthRepository;
import static umlerr.serviceauth.util.UsersUtils.getUsersAlreadyExist;

@Service
public class AuthService implements UserDetailsService {

    private final UsersMapper usersMapper;
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public AuthService(
        UsersMapper usersMapper,
        AuthRepository authRepository,
        BCryptPasswordEncoder passwordEncoder,
        @Lazy AuthenticationManager authenticationManager,
        JWTService jwtService
    ) {
        this.usersMapper = usersMapper;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

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

    @Transactional(readOnly = true)
    public List<Users> getAllUsers() {
        return authRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Users> getUserById(String userId) {
        return authRepository.findById(UUID.fromString(userId));
    }

    public String generateToken(String userId) {
        return jwtService.generateToken(userId);
    }

    public String verify(AuthDTO authDTO) {
        try {
            Optional<Users> user = authRepository.findByEmail(authDTO.getEmail());
            UUID userId = user.map(Users::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),
                    authDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return generateToken(String.valueOf(userId));
        } catch (BadCredentialsException e) {
            throw e;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = authRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким email не найден: " + email));
        return User.withUsername(users.getEmail()).password(users.getPassword()).build();
    }
}
