package dat250.group22.FeedApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class FeedAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedAppApplication.class, args);
	}

}
