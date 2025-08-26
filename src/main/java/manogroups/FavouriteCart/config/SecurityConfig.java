package manogroups.FavouriteCart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import manogroups.FavouriteCart.Jwt.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private CustomAccessDeniedHandlerConfig accessDeniedHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.
        csrf(csrf -> csrf.disable())
        .formLogin(form -> form.disable())
        .authorizeHttpRequests(auth ->
        auth.requestMatchers(
                    "/api/cart/add",
                    "/api/cart/cart/{storeName}",
                    "/api/cart/carts/{storeName}",
                    "/api/cart/update/{cartId}",
                    "/api/cart/delete/{storeName}",
                    "/api/favourite/add",
                    "/api/favourite/favourite/{storeName}",
                    "/api/favourite/favourites/{storeName}",
                    "/api/favourite/delete/{storeName}"
        ).hasAnyRole("ADMIN","STAFF","USER")
        .anyRequest().authenticated())
        .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }
}
