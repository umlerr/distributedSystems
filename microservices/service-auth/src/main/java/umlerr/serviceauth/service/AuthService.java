package umlerr.serviceauth.service;

import java.util.List;
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

    @Transactional
    public List<Users> getAllUsers() {
        return authRepository.findAll();
    }

    public String generateToken(String email) {
        return jwtService.generateToken(email);
    }

    public String verify(AuthDTO authDTO) {
        try {
            Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),
                    authDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return generateToken(authDTO.getEmail());
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
