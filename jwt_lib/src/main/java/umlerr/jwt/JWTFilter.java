package umlerr.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JWTService jwtService;

    @Override @SneakyThrows
    protected void doFilterInternal(
        HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain chain
    ) {
        final String requestTokenHeader = request.getHeader(HEADER_STRING);

        String userId = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
            jwtToken = requestTokenHeader.replace(TOKEN_PREFIX, "");
            userId = jwtService.extractUserId(jwtToken);
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.validateToken(jwtToken, userId)) {
                Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
                if (existingAuth == null) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");

                    UsernamePasswordAuthenticationToken userIdPasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                            userId, null, List.of(authority));

                    userIdPasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(userIdPasswordAuthenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
