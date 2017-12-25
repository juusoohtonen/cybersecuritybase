package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // OWASP: Top 10 2013-A8-Cross-Site Request Forgery (CSRF)
        // http://resources.infosecinstitute.com/csrf-proof-of-concept-with-owasp-zap/#gref
        // CSRF token is included in the forms, you then just have to enable the token validation here by commenting out
        // or removing the following line
        http.csrf().disable();

        // OWASP: Top 10 2013-A7-Missing Function Level Access Control
        // also the requests to pages describing signed up pages should be authorized, probably so that only the admin
        // could view the content. The fix would be to include http://www.baeldung.com/spring-security-expressions-basic
        // so that the line would include
        //      .hasRole("ADMIN")
        // Another fix could be to totally remove the lines. This would fix
        // OWASP: Top 10 2013-A4-Insecure Direct Object References
        // This issue means that "page/user" displays the name and address information for the specific page/user.
        // a malicious user may find out the address information of a user if she can guess the user's name correctly.
        http.authorizeRequests().antMatchers("/page/**", "**/page/**")
                .permitAll();
        http.authorizeRequests()
                .anyRequest().authenticated().and().formLogin().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
