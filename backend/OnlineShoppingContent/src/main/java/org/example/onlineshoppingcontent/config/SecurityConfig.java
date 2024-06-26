package org.example.onlineshoppingcontent.config;

import org.example.onlineshoppingcontent.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtFilter jwtFilter;

    @Autowired
    public void setJwtFilter(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/products/*", "/orders/*").access("hasAuthority('user_read') or hasAuthority('admin_read')")
                .antMatchers("/orders/*/cancel").access("hasAuthority('user_update') or hasAuthority('admin_update')")
                .antMatchers("watchlist/products/all", "products/frequent/3", "/products/recent/3").hasAuthority("user_read")
                .antMatchers("/orders", "watchlist/products/(?<productId>\\d+)").hasAuthority("user_write")
                .antMatchers("/watchlist/product/(?<productId>\\d+)").hasAuthority("user_delete")
                .antMatchers("/profit/3", "/popular/3").hasAuthority("admin_read")
                .anyRequest()
                .authenticated();
    }
}
