package com.springboot.config;

import com.springboot.api.impl.BaseAPI;
import com.springboot.service.impl.JwtService;
import com.springboot.util.Constants;
import com.springboot.util.JwtUtil;
import com.springboot.util.MessageKeys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private MessageSource messageSource;

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);


  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader(
        Constants.AUTHORIZATION_STRING);

    String username = null;
    String jwtToken = null;

    if (requestTokenHeader != null && requestTokenHeader.startsWith(
        Constants.BEARER_START)) {
      jwtToken = requestTokenHeader.substring(7);
      try {
        username = jwtUtil.getUsernameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        LOGGER.error(Constants.ERROR_MESSAGE_LOGGER,
            messageSource.getMessage(MessageKeys.JWT_UNABLE, null, request.getLocale()));
      } catch (ExpiredJwtException e) {
        LOGGER.error(Constants.ERROR_MESSAGE_LOGGER,
            messageSource.getMessage(MessageKeys.JWT_EXPIRED, null, request.getLocale()));
      } catch (SignatureException e) {
        LOGGER.error(Constants.ERROR_MESSAGE_LOGGER,
            messageSource.getMessage(MessageKeys.JWT_SIGNATURE_NOT_MATCH, null,
                request.getLocale()));
      }
    } else {
      LOGGER.info(Constants.ERROR_MESSAGE_LOGGER,
          messageSource.getMessage(MessageKeys.JWT_NOT_START_BEARER, null,
              request.getLocale()));
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = jwtService.loadUserByUsername(username);

      if (jwtUtil.validateToken(jwtToken, userDetails)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    filterChain.doFilter(request, response);

  }

}
