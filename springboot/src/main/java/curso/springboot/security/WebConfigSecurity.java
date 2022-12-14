package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	@Override // Configura as solicitações de acesso por Http
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable() // Desativa as configurações padrão de memória.
		.authorizeRequests() // Pertimir restringir acessos
		.antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a pagina inicial
		.antMatchers("/materialize/**").permitAll()
		.antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll() // permite qualquer usuário
		.loginPage("/login") // página de login
		.defaultSuccessUrl("/cadastropessoa") // página padrão se efetuou o login
		.failureUrl("/login?error=true") // página padrão se falhou o login
		.and().logout().logoutSuccessUrl("/login") // página padrão após fazer o logout
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	
	}
	
	@Override // Cria autenticação do usuário com banco de dados ou em memória
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		
		
		/*auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
		.withUser("gui")
		.password("$2a$10$qUChMSjXlN7ysQjzBmbCput/nQzVcewSMVplzBk0RxxKzlyQTdYq2")
		.roles("ADMIN");*/
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	          web.ignoring().antMatchers("/materialize/**")
	         .antMatchers(HttpMethod.GET,"/resources/**","/static/**", "/**", "/materialize/**", "**/materialize/**");
	}

}
