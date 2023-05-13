package com.monster.bill.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class JwtResponse {
	private Long id;
	private String token;
	private String type = "Basic";
	private String username;
	private String email;
	private List<String> roles;
	private boolean isFirstTimeLogin;
	private Long employeeId;

	public JwtResponse(String token, Long id, String username, String email, List<String> roles, Long employeeId, boolean isFirstTimeLogin) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.isFirstTimeLogin = isFirstTimeLogin;
		this.employeeId = employeeId;
	}
}
