package com.oauthclient.demo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class OAuthClientApplication extends WebSecurityConfigurerAdapter{

	@Autowired
	HttpServletRequest request;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	// @formatter:off
        http
            .authorizeRequests(a -> a
                .antMatchers("/", "/error", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            .logout(l -> l
                    .logoutSuccessUrl("/").permitAll())
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .csrf().disable()
            .oauth2Login();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(OAuthClientApplication.class, args);
	}
	
	@GetMapping("/")
	public String home(@AuthenticationPrincipal OAuth2User principal) {
		try {
			request.getSession().setAttribute("name", principal.getAttribute("name"));
		}catch (Exception e) {
			// Handle NullPointer If User Not Authrized		
		}
		return "home";
	}
	
	/*
	 * Code for Extra condition checking in git based on organization
	 */
	/*
	 * @Bean public OAuth2UserService<OAuth2UserRequest, OAuth2User>
	 * oauth2UserService(WebClient rest) { DefaultOAuth2UserService delegate = new
	 * DefaultOAuth2UserService(); return request -> { OAuth2User user =
	 * delegate.loadUser(request); if
	 * (!"github".equals(request.getClientRegistration().getRegistrationId())) {
	 * return user; }
	 * 
	 * OAuth2AuthorizedClient client = new OAuth2AuthorizedClient
	 * (request.getClientRegistration(), user.getName(), request.getAccessToken());
	 * String url = user.getAttribute("organizations_url"); List<Map<String,
	 * Object>> orgs = rest .get().uri(url)
	 * .attributes(oauth2AuthorizedClient(client)) .retrieve()
	 * .bodyToMono(List.class) .block();
	 * 
	 * if (orgs.stream().anyMatch(org ->
	 * "spring-projects".equals(org.get("login")))) { return user; }
	 * 
	 * throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token",
	 * "Not in Spring Team", "")); }; }
	 * 
	 * @Bean public WebClient rest(ClientRegistrationRepository clients,
	 * OAuth2AuthorizedClientRepository authz) {
	 * ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new
	 * ServletOAuth2AuthorizedClientExchangeFilterFunction(clients, authz); return
	 * WebClient.builder() .filter(oauth2).build(); }
	 */
	
}
