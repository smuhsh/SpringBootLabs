package kr.or.kosa.config;


import kr.or.kosa.security.CustomerAccessDeniedHandler;
import kr.or.kosa.security.CustomerDetailService;
import kr.or.kosa.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
//설정파일 (기존 : xml : in memory , jdbc , 사용자정의(JPA, Mybatis))
//@Configuration 안에 함수는   @Bean public Object method( return new Object)

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	//in memory 
	/*
	@Bean
	public UserDetailsService userDetailsService() {
		
		UserDetails user = User.builder()
				          .username("user")
				          .password(passwordEncoder().encode("1004")) //boot 기본적으로 암호화된 비번을 사용 
				          .roles("USER")
				          .build();
		
		UserDetails admin = User.builder()
		          .username("admin")
		          .password(passwordEncoder().encode("1004"))
		          .roles("USER","ADMIN")
		          .build();
		//new InMemoryUserDetailsManager();
		return new InMemoryUserDetailsManager(user,admin);
		
	}
	*/
	
	//DB연결 객체 (DataSource)
	@Autowired
	private DataSource datasource;

	@Autowired
	private CustomerDetailService customerDetailService;
	/**
	 *
	 * @param http
	 * @return
	 * @throws Exception
	 */
	//권한한 요청 처리
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		//http.csrf().disable();
		//http.formLogin().disable();


		http.authorizeRequests()
				.antMatchers("/admin","/admin/**").hasRole("ADMIN")
				.antMatchers("/user","/user/**").hasAnyRole("USER","ADMIN")
				.antMatchers("/css/**" , "/js/**" , "/imges/**").permitAll()
				.antMatchers("/**").permitAll()
				.anyRequest().authenticated();
				//설정하지 않는 모든 경로 요청에 대해서 인증된 사용자만 접근 가능
			
		
		//http.formLogin();
		//로그인 사용자 정의 설정 하기
		//커스텀 설정

		http.formLogin()
				.defaultSuccessUrl("/")   //로그인 성공시
				.loginPage("/login")      //커스텀 로그인 페이지 요청 경로
				.loginProcessingUrl("/loginPro")  //커스텀 로그인 처리 경로 지정
				.usernameParameter("id")
				.passwordParameter("pw")
				.successHandler(authenticationSuccessHandler())
				.permitAll();

		/*
		http.formLogin((login) -> login
				.defaultSuccessUrl("/")
				.loginPage("")
		);
		*/
		
		//사용자 정의 인증 설정하기 *******************************************
		http.userDetailsService(customerDetailService);

		//로그아웃 설정
		//logoutSuccessUrl :
		   //logoutUrl :
		http.logout().logoutSuccessUrl("/")
					     .logoutUrl("/logout")
					     .permitAll();
		//.anyRequest().authenticated(); 때문에 문제 발생

		//CSRF 방지 비화성화로
		//해야 요청 처리
		//http.csrf().disable();



		//4. 자동 로그인 설정
		// key()   : 자동 로그인에서 토근 생성 / 검증에 사용되는 식별키
		// tokenRespository() : 토큰 저장할 저장소 지정( 데이터 소스 포함한 저장소 객체)
		// tokenValiditySeconds() : 토큰 유효시간 설정 ( 7일)

		/*
		http.rememberMe().key("kosa")
				.tokenRepository(tokenRepository())
				.tokenValiditySeconds(60*60*24*7);
		*/
		http.rememberMe()
				.key("kosa")
				// DataSource 가 등록된 PersistentRepository 토큰정보 객체
				//.tokenRepository( tokenRepository() )
				// 토큰 유효기간 지정 : 7일 (초 단위)
				.tokenValiditySeconds( 60 * 60 * 24 * 7 )
				.userDetailsService(customerDetailService)
				.tokenRepository(tokenRepository());
		//테스트는 세션을 삭제후에도 로그인 상태 유지 하는 것 확인



		//인증 예외 처리
		//accessDeiedPage() 접근 거부시 이동 경로 지정
		//테스트 시 http://localhost:8090/admin
		//http.exceptionHandling().accessDeniedPage("/exception");
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());


		return http.build();
	}





	/**
	 *
	 * @return
	 */
	//JDBC 인증방식
	/*
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
	*/
	//사용자 정의 인증 방식(JPA , Mybatis)
    /*
    1. 권한 설정
    2. 로그인 설정
    3. 로그아웃 설정
    4. 자동 로그인 설정
    5. 예외 처리 설정
    6. CSRF 방지 기능 설정


     */

	/**
	 * 자동로그인 저장소 빈 등록
	 *
	 */
	@Bean
	public PersistentTokenRepository tokenRepository() {
		// JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
		JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();  //ctrl + H
		repositoryImpl.setDataSource(datasource);   // 토큰 저장소를 사용하는 데이터 소스 지정
		return repositoryImpl;
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler(){
		return new LoginSuccessHandler();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		return  new CustomerAccessDeniedHandler();
	}


	
	//AuthenticationManager 관리 매니저 빈객체 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	//암호화 방식 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
