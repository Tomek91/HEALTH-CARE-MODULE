package pl.com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import pl.com.app.security.service.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;

    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/menu",
                        "/addresses",
                        "/workplaces",
                        "/specializations",
                        "/patients",
                        "/doctors",
                        "/doctors/*",
                        "/visits",
                        "/visits/*",
                        "/opinions",
                        "/opinions/*",
                        "/advices/**",
                        "/webjars/**",
                        "/registration/**",
                        "/add-visit/**",
                        "/visits/add/**",
                        "/img/**",
                        "/login/**").permitAll()
                .antMatchers(
                        "/addresses/add/**","/addresses/modify/**",
                        "/workplaces/add/**","/workplaces/modify/**",
                        "/specializations/add/**","/specializations/modify/**",
                        "/patients/add/**",
                        "/doctors/add/**",
                        "/visits/modify/**",
                        "/opinions/modify/**",
                        "/advices/modify/**",
                        "/setup/default-data/**",
                        "/advices/not-answer-adm", "/advices/answer-adm/**").hasAnyRole("ADMIN")
                .antMatchers(
                        "/opinions/add/**").hasAnyRole("USER_PATIENT")
                .antMatchers(
                        "/doctor-visits/**",
                        "/advices/not-answer/**", "/advices/answer/**").hasAnyRole("USER_DOCTOR")
                .antMatchers(
                        "/doctors/modify/**").hasAnyRole("USER_DOCTOR", "ADMIN")
                .antMatchers(
                        "/patients/modify/**").hasAnyRole("USER_PATIENT", "ADMIN")
                .anyRequest().authenticated()


                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/app-login")
                .usernameParameter("username")
                .passwordParameter("password")
                //.defaultSuccessUrl("/", true)
                .failureUrl("/login/error")
                .successHandler(mySimpleUrlAuthenticationSuccessHandler)

                .and()
                .logout().permitAll()
                .logoutUrl("/app-logout")
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.sendRedirect("/accessDenied");
        };
    }
}
