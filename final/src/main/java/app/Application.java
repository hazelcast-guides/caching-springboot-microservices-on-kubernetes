package io.openliberty.guides.hazelcast;

import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

    
    @Value("#{environment.MY_POD_NAME}")
    private String podName;

    @RequestMapping("/")
    public String homepage(){
	return "Homepage hosted by: " + podName + "\n";
    }
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
