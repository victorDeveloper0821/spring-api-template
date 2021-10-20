package idv.victor.vo;
/**
 *  JWT 驗證結果
 * @author Victor.Tsai
 *
 */
public class AuthResult {

	private String status;
	private String message;
	private String token;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
