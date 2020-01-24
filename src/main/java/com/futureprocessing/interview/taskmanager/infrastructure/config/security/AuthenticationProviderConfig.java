package com.futureprocessing.interview.taskmanager.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
class AuthenticationProviderConfig {

    private static final String SELECT_USER_QUERY = "SELECT username, password, 'true' FROM user WHERE username = ?";
    private static final String USER_ROLES_QUERY = "SELECT DISTINCT role.id, role as authority FROM role " +
            "LEFT JOIN user_role ON user_role.role_id = role.id " +
            "LEFT JOIN user ON user.id = user_role.user_id WHERE user.username = ? ";

    //@Bean
    AuthenticationProvider authenticationProvider(DataSource dataSource) {
        UserDetailsService userDetailsService = new JdbcUserDetailsManagerConfigurer()
                .dataSource(dataSource)
                .usersByUsernameQuery(SELECT_USER_QUERY)
                .authoritiesByUsernameQuery(USER_ROLES_QUERY)
                .getUserDetailsService();
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    AuthenticationProvider inMemoryAuthenticationProvider() {
        List<UserDetails> users = Stream.of(InMemoryUser.values())
                                        .map(InMemoryUser::toUser)
                                        .collect(Collectors.toList());
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(users));
        daoAuthenticationProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        return daoAuthenticationProvider;
    }

}
