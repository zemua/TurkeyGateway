package devs.mrp.springturkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import devs.mrp.springturkey.configuration.MicroserviceConfiguration;

@SpringBootApplication
@EnableConfigurationProperties(MicroserviceConfiguration.class)
public class TurkeyGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurkeyGatewayApplication.class, args);
	}

}
