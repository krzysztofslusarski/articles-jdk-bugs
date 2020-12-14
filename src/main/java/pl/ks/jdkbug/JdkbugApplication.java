package pl.ks.jdkbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JdkbugApplication {

    public static void main(String[] args) {
        NukeSSLCerts.nuke();
        SpringApplication.run(JdkbugApplication.class, args);
    }

}
