/*
 * package com.monster.bill.security;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.config.annotation.authentication.builders.
 * AuthenticationManagerBuilder; import
 * org.springframework.security.config.annotation.method.configuration.
 * EnableGlobalMethodSecurity; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.annotation.web.configuration.
 * WebSecurityConfigurerAdapter; import
 * org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter;
 * 
 * import com.monster.bill.security.jwt.AuthEntryPointJwt; import
 * com.monster.bill.security.jwt.AuthTokenFilter; import
 * com.monster.bill.service.impl.UserDetailsServiceImpl;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity
 * 
 * @EnableGlobalMethodSecurity( // securedEnabled = true, // jsr250Enabled =
 * true, prePostEnabled = true) public class WebSecurityConfig extends
 * WebSecurityConfigurerAdapter {
 * 
 * @Autowired UserDetailsServiceImpl userDetailsService;
 * 
 * @Autowired private AuthEntryPointJwt unauthorizedHandler;
 * 
 * @Autowired private PasswordEncoder passwordEncoder;
 * 
 * @Bean public AuthTokenFilter authenticationJwtTokenFilter() { return new
 * AuthTokenFilter(); }
 * 
 * @Override public void configure(AuthenticationManagerBuilder
 * authenticationManagerBuilder) throws Exception {
 * authenticationManagerBuilder.userDetailsService(userDetailsService).
 * passwordEncoder(passwordEncoder); }
 * 
 * @Bean
 * 
 * @Override public AuthenticationManager authenticationManagerBean() throws
 * Exception { return super.authenticationManagerBean(); }
 * 
 * // @Bean // public PasswordEncoder passwordEncoder() { // return new
 * BCryptPasswordEncoder(); // }
 * 
 * @Override protected void configure(HttpSecurity http) throws Exception {
 * http.cors().disable();
 * http.csrf().disable().exceptionHandling().authenticationEntryPoint(
 * unauthorizedHandler).and()
 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
 * and().authorizeRequests() .antMatchers("/**").permitAll()
 * .antMatchers("/api/auth/**").permitAll().antMatchers("/api/test/admin**").
 * hasAnyRole("ADMIN") .antMatchers("/api/test/mod**",
 * "/api/v1/case/get-cases**").hasAnyRole("ADMIN", "MODERATOR")
 * .antMatchers("/api/test/user**").hasAnyRole("ADMIN", "MODERATOR",
 * "USER").anyRequest().permitAll() .and().formLogin().permitAll()
 * .and().logout();
 * 
 * http.addFilterBefore(authenticationJwtTokenFilter(),
 * UsernamePasswordAuthenticationFilter.class); } }
 */