package ua.com.univerpulse.toyshop.config.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN = "ADMIN";

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityJavaConfig(
            @Autowired RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Autowired
    public void configureGlobal(@NotNull AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER");
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles(ADMIN);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("/payments/**", "/customers/**"));
        configuration
                .setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response
        // must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration
                .setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .cors().and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api/explorer/**").permitAll()
                .antMatchers("/api/allPayments", "/api/payments/**"
                        , "/api/customerPayments/**", "/api/allCustomers", "/api/getCustomer/**")
                .hasAnyRole("USER", ADMIN)
                .antMatchers("/api/paymentCreate", "/api/paymentUpdate/**"
                        , "/api/paymentDelete/**", "/api/createCustomer", "/api/deleteCustomer/**")
                .hasRole(ADMIN)
                .antMatchers("/*").permitAll()
                .and().httpBasic().realmName("Univerpulse")
                .and()
                .formLogin()
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(@NotNull WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

//    @Bean
//    public RestTemplate restTemplate() throws KeyManagementException
//            , NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
//        SSLContext sslContext = new SSLContextBuilder()
//                .loadTrustMaterial(getClass().getClassLoader()
//                                .getResource("keystore/toyshop.p12")
//                        , "univerpulse".toCharArray()).build();
//        SSLConnectionSocketFactory sslConFactory =
//                new SSLConnectionSocketFactory(sslContext);
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLSocketFactory(sslConFactory).build();
//        ClientHttpRequestFactory requestFactory =
//                new HttpComponentsClientHttpRequestFactory(httpClient);
//        return new RestTemplate(requestFactory);
//    }
}