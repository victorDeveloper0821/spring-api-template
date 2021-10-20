package idv.victor.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import idv.victor.utils.JWTUtil;
import idv.victor.vo.AuthResult;
import idv.victor.vo.LoginBody;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private JWTUtil jwtUtil;
	
	public LoginFilter(String url, AuthenticationManager authMgr,JWTUtil util) {
		 super(new AntPathRequestMatcher(url));
	     setAuthenticationManager(authMgr);
	     this.jwtUtil = util ;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		// TODO Auto-generated method stub
		logger.debug("-- Enter to LoginFilter.attemptAuthentication --");
		String body =IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
		ObjectMapper om = new ObjectMapper();
		LoginBody login = om.readValue(body, LoginBody.class);
		String username = login.getUsername();
        String password = login.getPassword();

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();
		return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                		username,password
                )
        );
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		logger.debug("-- Enter to LoginFilter.successfulAuthentication --");
		jwtUtil.addAuthentication(response, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		logger.debug("-- Enter to LoginFilter.unsuccessfulAuthentication --");
		AuthResult auth = new AuthResult();
		ObjectMapper om = new ObjectMapper();
		auth.setStatus("unAuthorized");
		auth.setMessage("登入失敗");
		response.setContentType("application/json; charset=UTF-8");
	    response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		String json = om.writeValueAsString(auth);
		writer.println(json);
	}
	
	

}
