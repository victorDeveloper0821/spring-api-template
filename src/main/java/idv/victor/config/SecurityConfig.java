package idv.victor.config;

import idv.victor.security.JWTAuthenticationFilter;
import idv.victor.security.LoginFilter;
import idv.victor.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JWTUtil jwtUtil;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("victor")
                .password("{noop}12345678")
                .authorities("USER")
                .and()
                .withUser("John")
                .password("{noop}87654321")
                .authorities("MANAGER")
                .and()
                .withUser("Milkool")
                .password("{noop}liz")
                .authorities("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                // url for h2-console
                .antMatchers("/h2-console/**").permitAll()
                // login url
                .antMatchers("/auth/login").permitAll()
                // api prefix
                .antMatchers("/api/v1/**").hasAnyAuthority("USER","MANAGER")
                .and()
                .addFilterBefore(new LoginFilter("/auth/login", authenticationManager(),jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                // 添加過濾器，針對其他請求進行JWT的驗證
                .addFilterBefore(new JWTAuthenticationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // open resource for h2-console
        web.ignoring().antMatchers("/h2-console/**");
    }
}
