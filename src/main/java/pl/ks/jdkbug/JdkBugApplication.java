package pl.ks.jdkbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdkBugApplication {

    public static void main(String[] args) {
        NukeSSLCerts.nuke();
        SpringApplication.run(JdkBugApplication.class, args);
    }

}
