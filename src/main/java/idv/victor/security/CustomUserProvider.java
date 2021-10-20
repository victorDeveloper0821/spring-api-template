package idv.victor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomUserProvider implements AuthenticationProvider{

	@Autowired
	private CustomUsrDetailService userLoginService;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		String account = authentication.getName();
		String password = authentication.getCredentials().toString();
		String actualPasswd = password;
		UserDetails user =  userLoginService.loadUserByUsername(account);
		// 帳號密碼驗證邏輯
		if (account.equals(user.getUsername()) && actualPasswd.equals(user.getPassword())) {

			// 生成Authentication令牌
			Authentication auth = new UsernamePasswordAuthenticationToken(account, password, user.getAuthorities());
			return auth;
		} else {
			throw new BadCredentialsException("Password error");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
