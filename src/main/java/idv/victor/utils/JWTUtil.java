package idv.victor.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import idv.victor.vo.AuthResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	private final long EXPIRATIONTIME = 432_000_000;     // 5天
    private final String TOKEN_PREFIX = "Bearer ";        // Token前缀
    private final String HEADER_STRING = "Authorization";// 存放Token的Header Key
    @Value("${app.secret}")
    private String key ;	//給定一組密鑰，用來解密以及加密使用
    private Key k = null;
    
    public void addAuthentication(HttpServletResponse res,Authentication auth) {
    	k =  Keys.hmacShaKeyFor(key.getBytes());
    	String jws = Jwts.builder()
    			// 在Payload放入自定義的聲明方法如下
    			.claim("role","test")
    			// 在Payload放入sub保留聲明
    			.setSubject(auth.getName())
    			// 在Payload放入exp保留聲明
    			.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
    			.setIssuedAt(new Date())
    			.setIssuer("API")
    			.setId(UUID.randomUUID().toString()).signWith(k)
    			.compact();
    	AuthResult result ;
    	ObjectMapper om = new ObjectMapper();
    	try {
    		result = new AuthResult();
    		res.setContentType("application/json; charset=UTF-8");
    		res.setCharacterEncoding("UTF-8");
    		res.setStatus(HttpServletResponse.SC_OK);
    		PrintWriter writer = res.getWriter();
    		result.setStatus("OK");
    		result.setMessage("驗證成功");
    		result.setToken(jws);
    		String json = om.writeValueAsString(result);
    		writer.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Authentication getAuthentication(HttpServletRequest req) {
    	String token = req.getHeader(HEADER_STRING);
    	k =  Keys.hmacShaKeyFor(key.getBytes());
    	if(token != null) {
    		boolean isValid = false;
    		Claims claims = Jwts.parser()
    				.setSigningKey(k)
    				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
    				.getBody();
    		String username = claims.getSubject();
    		String role = claims.get("role").toString();
    		List<GrantedAuthority> authorities =  
             		AuthorityUtils.commaSeparatedStringToAuthorityList(role);
    		isValid = (username!=null) && (claims.getExpiration().after(new Date()));
    		return isValid 
    			? new UsernamePasswordAuthenticationToken(username, null, authorities)
    			: null ;
    	}
		return null;
    }
}
