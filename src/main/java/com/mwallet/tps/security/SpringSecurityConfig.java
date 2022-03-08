package com.mwallet.tps.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${jwt.public.key}")
	RSAPublicKey publicKey;
	
	@Value("${jwt.private.key}")
	RSAPrivateKey privateKey;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().and()
		.csrf().disable()
		.authorizeHttpRequests(authorize -> authorize
		        .antMatchers("/mwallet/auth/**")
		        .permitAll()
		        .antMatchers(HttpMethod.GET, "/api/subreddit")
		        .permitAll()
		        .antMatchers("/v2/api-docs",
		                "/configuration/ui",
		                "/swagger-resources/**",
		                "/configuration/security",
		                "/swagger-ui.html",
		                "/webjars/**")
		        .permitAll()
		        .anyRequest()
		        .authenticated())
		.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.exceptionHandling(exceptions -> exceptions
		        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
		        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
		);

	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		// auth.inMemoryAuthentication().withUser("user").password("user123").roles("ADMIN");

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
	
	 @Bean
	    JwtDecoder jwtDecoder() {
	        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
	    }

}