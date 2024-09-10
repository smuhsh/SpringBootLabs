package kr.or.kosa.config;

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
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity   //이 클래스는 스프링 시큐리티 설정 가능 (함수는 빈으로 등록) //  5.4 이전 와 5.5 이후 버전부터 변경 ....
public class SecurityConfig {   

	//권한 처리
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
			
				/*
					5.5.4 이하  
					 http.authorizeRequests()
					.antMatchers("/admin","/admin/**").hasRole("ADMIN")
					.antMatchers("/user","/user/**").hasAnyRole("USER","ADMIN")
					.antMatchers("/css/**" , "/js/**" , "/imges/**").permitAll()
					.antMatchers("/**").permitAll()
					.anyRequest().authenticated();
					//설정하지 않는 모든 경로 요청에 대해서 인증된 사용자만 접근 가능
				    http.formLogin();
			        return http.build();

					
	                5.5.5 이상
					http.authorizeHttpRequests()
					.requestMatchers("/admin","/admin/**").hasRole("ADMIN")
					.requestMatchers("/user","/user/**").hasAnyRole("USER","ADMIN")
					.requestMatchers("/css/**" , "/js/**" , "/images/**").permitAll()
					.requestMatchers("/**").permitAll()
					.anyRequest().authenticated() // 혹시 라도 설정하지 모든 경로를 인증된 사용자만 접근 
					.and()
					.formLogin(); //폼 로그인 방식 (제공하는 로그창 과 인증 방식 사용)
				    return http.build();

	                6.x 버전
	                람다식 강제
	                import static org.springframework.security.config.Customizer.withDefaults; // 추가

			 */
	        http.authorizeHttpRequests(auth -> auth
	            .requestMatchers("/admin/**").hasRole("ADMIN")
	            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
	            .anyRequest().permitAll())
	            .formLogin(withDefaults()) // 기본 폼 로그인 사용
	            .logout(withDefaults()); // 기본 로그아웃 사용
	        return http.build();
			
		}	
}

/*
1. implementation 'org.springframework.boot:spring-boot-starter-security'  추가
1.1 security 적용 (form 인증) -> 로그인 창 -> user (비번)

2. 기본 레거시 : security.xml -> boot  -> java 파일 -> 설정파일 

3. in memory 방식 
3.1 계정 암호 미리 등록 테스트 

4. https://bcrypt-generator.com/ 암호화 사이트 
4.2 비밀번호 암호화 알고리즘 적용
    BCrypt 방식 기준 암호화 알고리즘
    
5. 스프링 시큐리티 주요 인증 방식
5.1 인 메모리
5.2 JDBC 인증 
5.3 사용자 정의 인증
   
    
6.  이 클래스는 스프링 시큐리티 설정 가능 (함수는 빈으로 등록) 
    5.4 이전 와 5.5 이후 버전부터 변경 ....  
    
    스프링 시큐리티 5.4 이전 버전에서 WebSecurityConfigurerAdapter를 extends한 것이 문제가 되는 것이 거의 없었습니다.
      
    
    
*/