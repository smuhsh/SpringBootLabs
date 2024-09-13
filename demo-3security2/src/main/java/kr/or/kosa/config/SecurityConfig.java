package kr.or.kosa.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private DataSource datasource;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		  http.authorizeHttpRequests(auth -> auth
		            .requestMatchers("/admin/**").hasRole("ADMIN")
		            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
		            .anyRequest().permitAll())
		            .formLogin(withDefaults()) // 기본 폼 로그인 사용
		            .logout(withDefaults()); // 기본 로그아웃 사용
		  
		  
		  
		  http
	        	.logout(logout -> logout
	            .logoutUrl("/logout") // 로그아웃 요청을 받을 URL
	            .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
	            .deleteCookies("JSESSIONID") // 로그아웃 시 삭제할 쿠키 설정 (선택 사항)
	            .invalidateHttpSession(true) // 세션 무효화
	            //.permitAll()
	        ); 
		  
		  
		        return http.build();
		        
				//로그아웃 설정
				//logoutSuccessUrl :
			    //logoutUrl :
		       

	}
	//인증 처리
	@Bean
	public UserDetailsService userDetailsService() {
		
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(datasource);
		
		//사용자 인증 쿼리
		String sql1 = "select user_id as username , user_pw as password , enabled from user where user_id=?";
		
		//사용자 권한 쿼리
		String sql2 = "select user_id as username , auth from user_auth where user_id=?";
		
		userDetailsManager.setUsersByUsernameQuery(sql1); //계정 비번 확인
		userDetailsManager.setAuthoritiesByUsernameQuery(sql2);
		
		return userDetailsManager;
	}
	/*
	@Bean
	public UserDetailsService userDetailsService() {
			
			UserDetails user = User.builder()
					           .username("user")
					           //.password("1004")
					           .password(passwordEncoder().encode("1004"))
					           .roles("USER")
					           .build();
			
			UserDetails admin = User.builder()
					            .username("admin")
					            //.password("1004")
					            .password(passwordEncoder().encode("1007"))
					            .roles("USER","ADMIN")
					            .build();
			
			return new InMemoryUserDetailsManager(user,admin);
		}
	 */
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
			return authenticationConfiguration.getAuthenticationManager();
		}
		
		
		//암호화 방식 (BCrypt)
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
}
