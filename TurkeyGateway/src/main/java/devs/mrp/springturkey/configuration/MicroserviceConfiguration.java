package devs.mrp.springturkey.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class MicroserviceConfiguration {

	private String turkeyMainService = "lb://MAIN-SERVICE";
	private String authService = "lb://AUTH-SERVICE";

	public String getTurkeyMainService() {
		return turkeyMainService;
	}

	public String getAuthService() {
		return authService;
	}
}
