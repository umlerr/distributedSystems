package umlerr.serviceusers.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import umlerr.serviceusers.service.CustomUsersDetailsService;
import umlerr.serviceusers.service.JWTService;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JWTService jwtService;
    private final CustomUsersDetailsService customUsersDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain
    )
        throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HEADER_STRING);
        String token = null;
        String email = null;

        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
            assert authorizationHeader != null;
            token = authorizationHeader.replace(TOKEN_PREFIX, "");
            email = jwtService.extractEmail(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = customUsersDetailsService.loadUserByUsername(email);

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
