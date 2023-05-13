package com.monster.bill.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthorizationNs {
	private String OAuth_realm;
	private String oauth_consumer_key;
	private String oauth_token;
	private String oauth_signature_method;
	private String oauth_timestamp;
	private String oauth_nonce;
	private String oauth_version;
	private String oauth_signature;
}
