package umlerr.serviceusers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umlerr.serviceusers.model.Users;
import umlerr.serviceusers.model.UsersPrincipal;
import umlerr.serviceusers.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class CustomUsersDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким email не найден: " + email));
        return new UsersPrincipal(users);
    }
}
