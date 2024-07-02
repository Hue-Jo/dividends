package zerobase.dividends.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization"; // 어떤 키를 기준으로 토큰을 주고 받을거니?
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.resolveTokenFromRequest(request);

        // 토큰 유효성 검증
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) { // 헤더에 토큰 있니? 토큰이 유효하니?
            Authentication auth = this.tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth); // 그러면 인증정보를 컨텍스트에 담아

            // 어떤 사용자가 어떤 경로에 접근했는지에 대한 로그
            log.info(String.format("[%s] -> %s",
                                    this.tokenProvider.getUsername(token),
                                    request.getRequestURL()));
        }
        filterChain.doFilter(request, response);
    }


    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
