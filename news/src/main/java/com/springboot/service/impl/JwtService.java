package com.springboot.service.impl;

import com.springboot.dto.JwtRequest;
import com.springboot.dto.JwtResponse;
import com.springboot.dto.UserDTO;
import com.springboot.entity.UserEntity;
import com.springboot.service.IUserService;
import com.springboot.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Load user when authenticate and access via json web token
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userService.findOneByUsername(username);

        if (user != null) {
            return new User(user.getUsername(), user.getPassword(), getAuthority(user));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    /**
     * authenticate by username and password
     *
     * @param jwtRequest
     * @return
     * @throws Exception
     */
    public JwtResponse authenticateAndCreateJwt(JwtRequest jwtRequest) throws Exception {
        String username = jwtRequest.getUsername();
        String userPassword = jwtRequest.getPassword();
        authenticate(username, userPassword);

        UserEntity user = userService.findOneByUsername(username);
        UserDTO userDto = new UserDTO();
        modelMapper.map(user, userDto);

        String newGeneratedToken = jwtUtil.generateToken(userDto);

        return new JwtResponse(userDto, newGeneratedToken);
    }

    private Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
        });
        return authorities;
    }

    private void authenticate(String username, String password) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
