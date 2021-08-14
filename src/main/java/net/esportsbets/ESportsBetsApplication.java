package net.esportsbets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ESportsBetsApplication {

    /**
     * Get the port assigned by Heroku, or return 4567 if ran locally
     * @return default port if heroku-port isn't set (i.e. on localhost)
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

	public static void main(String[] args) {
		SpringApplication.run(ESportsBetsApplication.class, args);
	}

}
