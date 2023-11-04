package com.petlover.petsocial.config;


import com.petlover.petsocial.config.oauth.CustomOAuth2UserService;
import com.petlover.petsocial.config.oauth.OAuth2LoginSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
   private CustomOAuth2UserService oAuth2UserService;

   @Autowired
   private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandle;








   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
   {
       http.sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeHttpRequests(Authorize -> Authorize.requestMatchers("/","/register","/signin","/createUser","/oauth/**","/verify","/forgot_password","/reset_password"
                        ,"/getAllUser","/searchUser","/searchPost","/getAllPost","/ws/**"


                        ).permitAll().anyRequest().authenticated()).
//                        "api/v1/**",
//                        "/v2/api-docs",
//                        "/v3/api-docs",
//                        "v3/api-docs/**",
//                        "/swagger-resources",
//                        "/swagger-resources/**",
//                        "/configuration/ui",
//                        "/configuration/security",
//                        "/swagger-ui/**",
//                        "/webjars/**").permitAll().anyRequest().authenticated()).
                   addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class).csrf().disable().cors().configurationSource(corsConfigrationSource()).and().httpBasic().and().formLogin().
and()
               .oauth2Login()
               .loginPage("/signin")
               .userInfoEndpoint()
              .userService(oAuth2UserService)
              .and().successHandler(oAuth2LoginSuccessHandle).permitAll();


//        http.csrf().disable()
//                .authorizeHttpRequests().requestMatchers("/user/**").hasRole("USER")
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/staff/**").hasRole("STAFF")
//                .requestMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/userLogin")
//                .successHandler(sucessHandler).permitAll().and()
//                .oauth2Login().loginPage("/signin")
//                .userInfoEndpoint().userService(oAuth2UserService)
//               .and().successHandler(oAuth2LoginSuccessHandle);



        return http.build();
   }

   private CorsConfigurationSource corsConfigrationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg =  new CorsConfiguration();
                cfg.setAllowedOrigins(Arrays.asList("https://dogcat-alpha.vercel.app"));
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setExposedHeaders(Arrays.asList("Authorization"));
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
   }
   @Bean
   public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }


}
