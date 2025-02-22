package umlerr.serviceusers.enums;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(List.of()),
    ADMIN(List.of());

    private final List<GrantedAuthority> authorities;
}
