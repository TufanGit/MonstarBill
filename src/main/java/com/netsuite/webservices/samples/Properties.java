/*
 * package com.netsuite.webservices.samples;
 * 
 * import com.netsuite.suitetalk.client.common.authentication.Passport; import
 * com.netsuite.suitetalk.client.common.authentication.TokenPassport;
 * 
 * import java.io.File; import java.io.FileInputStream; import
 * java.io.IOException; import java.net.MalformedURLException; import
 * java.net.URL;
 * 
 * import org.springframework.util.ResourceUtils;
 * 
 * import static
 * com.netsuite.suitetalk.client.common.utils.CommonUtils.composeUrl;
 * 
 * import static com.netsuite.webservices.samples.Messages.EMPTY_WS_URL;
 * 
 *//**
	 * <p>
	 * This class provides access to all properties in nsclient.properties file.
	 * </p>
	 * <p>
	 * © 2019 NetSuite Inc. All rights reserved.
	 * </p>
	 */
/*
 * public class Properties extends java.util.Properties {
 * 
 * private static final String PROPERTIES_FILE = "nsclient.properties";
 * 
 * private static final String WS_URL = "wsUrl";
 * 
 * private static final String PROMPT_FOR_LOGIN = "promptForLogin";
 * 
 * private static final String ACCOUNT = "account";
 * 
 * private static final String APPLICATION_ID = "applicationId";
 * 
 * private static final String TBA_CONSUMER_KEY = "tbaConsumerKey"; private
 * static final String TBA_CONSUMER_SECRET = "tbaConsumerSecret"; private static
 * final String TBA_TOKEN = "tbaTokenId"; private static final String
 * TBA_TOKEN_SECRET = "tbaTokenSecret";
 * 
 * private static final String USE_TCP_MONITOR = "useTcpMonitor";
 * 
 * private Passport passport;
 * 
 *//**
	 * Constructor loading all properties from {@code nsclient.properties} file.
	 *
	 * @throws IOException If it is something wrong with properties file
	 */
/*
 * public Properties() throws IOException { super(); File file =
 * ResourceUtils.getFile("classpath:"+PROPERTIES_FILE); load(new
 * FileInputStream(file)); }
 * 
 *//**
	 * @return URL for web services endpoint written in properties file and modified
	 *         to account-specific domains
	 * @throws MalformedURLException If URL in properties file has invalid format
	 */
/*
 * public URL getWebServicesUrl() throws MalformedURLException {
 * 
 * String url_string = getProperty(WS_URL); if(url_string.isEmpty()) throw new
 * MalformedURLException(EMPTY_WS_URL);
 * 
 * URL url = new URL( url_string + "/services/NetSuitePort_2020_1"); return
 * composeUrl(url.getProtocol(), url.getHost(), url.getPort()); }
 * 
 *//**
	 * Returns a token passport according to information in properties file.
	 *
	 * @return Object containing passport for authentication using TBA
	 *//*
		 * public TokenPassport getTokenPassport() { String account =
		 * getProperty(ACCOUNT); String consumerKey = getProperty(TBA_CONSUMER_KEY);
		 * String consumerSecret = getProperty(TBA_CONSUMER_SECRET); String token =
		 * getProperty(TBA_TOKEN); String tokenSecret = getProperty(TBA_TOKEN_SECRET);
		 * return new TokenPassport(account, consumerKey, consumerSecret, token,
		 * tokenSecret); }
		 * 
		 * }
		 */