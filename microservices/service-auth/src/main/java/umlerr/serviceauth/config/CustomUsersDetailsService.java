package umlerr.serviceauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umlerr.serviceauth.model.Users;
import umlerr.serviceauth.repository.AuthRepository;

@Service
@RequiredArgsConstructor
public class CustomUsersDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = authRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким email не найден: " + email));
        return new CustomUsersDetails(users);
    }
}
