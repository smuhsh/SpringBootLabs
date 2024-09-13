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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import kr.or.kosa.security.CustomerAccessDeniedHandler;
import kr.or.kosa.security.CustomerDetailService;
import kr.or.kosa.security.LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private DataSource datasource;
	
	@Autowired
	private CustomerDetailService customerDetailService;  //사용자 정의 방식으로 만든 객체 (mybatis 연동)
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		  http.authorizeHttpRequests(auth -> auth
		            .requestMatchers("/admin/**").hasRole("ADMIN")
		            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
		            .anyRequest().permitAll())
		            .formLogin(withDefaults()) // 기본 폼 로그인 사용
		            .logout(withDefaults()); // 기본 로그아웃 사용
		  
		    //로그아웃 설정
			//logoutSuccessUrl :
		    //logoutUrl :
		  
		  http
	        	.logout(logout -> logout
	            .logoutUrl("/logout") // 로그아웃 요청을 받을 URL
	            .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
	            .deleteCookies("JSESSIONID") // 로그아웃 시 삭제할 쿠키 설정 (선택 사항)
	            .invalidateHttpSession(true) // 세션 무효화
	            //.permitAll()
	        ); 
		  
		  
		  //사용자 정의 인증 방식 (mybatis 연동)
		  http.userDetailsService(customerDetailService);
		  
		  
		  
		  //자동 로그인 설정하기
		  /*
		  http
              .rememberMe(rememberMe -> rememberMe
              .key("kosa")  // 고유한 키 설정
              .tokenValiditySeconds(60 * 60 * 24 * 7)  // 토큰 유효기간: 7일
              .userDetailsService(customerDetailService)  // 사용자 세부 정보 서비스
              .tokenRepository(tokenRepository())  // 토큰 저장소
          );
          */
		  
		  //폼 인증 방식 (사용자 정의 설정하기 )
		 
		  http
		  .formLogin(form -> form
			        .loginPage("/login")  // 커스텀 로그인 페이지 요청 경로
			        .loginProcessingUrl("/loginPro")  // 커스텀 로그인 처리 경로 지정
			        .defaultSuccessUrl("/")  // 로그인 성공 시 이동할 경로
			        .usernameParameter("id")  // 사용자 이름 파라미터 설정
			        .passwordParameter("pw")  // 패스워드 파라미터 설정
			        .successHandler(authenticationSuccessHandler())  // 성공 시 핸들러 설정
			        .permitAll()  // 모든 사용자에게 로그인 페이지 접근 허용
			       
			    );
		
		 // http
		 //   .csrf(csrf -> csrf.disable());  // CSRF 보호 비활성화
		  //인증 예외 처리
		  //accessDeiedPage() 접근 거부시 이동 경로 지정
		  //테스트 시 http://localhost:8090/admin
			
			//http.exceptionHandling().accessDeniedPage("/exception");
		   http.exceptionHandling(exceptions -> exceptions
	           .accessDeniedHandler(accessDeniedHandler())
	            );
		  return http.build();

	}
	
	
	
	//인증 처리
	//3단계 사용자 정의 (Mybatis , jpa)
	
	
	/*
	2단계 JDBC
	 
	@Bean
	public UserDetailsService userDetailsService() {
		
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(datasource);
		
		//사용자 인증 쿼리
		String sql1 = "select user_id as username , user_pw as password , enabled from user where user_id=?";
		
		//사용자 권한 쿼리
		String sql2 = "select user_id as username , auth from user_auth where user_id=?";
		
		userDetailsManager.setUsersByUsernameQuery(sql1); //계정 비번 확인
		userDetailsManager.setAuthoritiesByUsernameQuery(sql2); //권한 처리 
		
		return userDetailsManager;
	}
	*/
	/*
	1단계 in - memory
	
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
	public AccessDeniedHandler accessDeniedHandler(){
		return  new CustomerAccessDeniedHandler();
	}
	
	/**
	 * 자동로그인 저장소 빈 등록
	 * 데이터 소스 지정하기 ....
	 */
	@Bean
	public PersistentTokenRepository tokenRepository() {
		// JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
		JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();  //ctrl + H
		
		
		//자동 로그인 테이블
		//repositoryImpl.setCreateTableOnStartup(true);
		//처음 시에만 안그러면  매번 create 충돌 
		
		repositoryImpl.setDataSource(datasource);   // 토큰 저장소를 사용하는 데이터 소스 지정
		return repositoryImpl;
		
	   //mysql 토큰을 저장할 수 있는 테이블 생성과 저장을 자동화 한다 
		/*
		 CREATE TABLE persistent_logins (
		    username VARCHAR(64) NOT NULL,
		    series VARCHAR(64) PRIMARY KEY,
		    token VARCHAR(64) NOT NULL,
		    last_used TIMESTAMP NOT NULL
		);
    	 */
	}
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
			return authenticationConfiguration.getAuthenticationManager();
		}
		
		
		//암호화 방식 (BCrypt)
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		//로그인 성공시 필요한 부분 처리 
		@Bean
		public AuthenticationSuccessHandler authenticationSuccessHandler(){
			return new LoginSuccessHandler();
		}

}
