package studentmanagement;

import lk.dialog.dds.common.ComponentConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ComponentConfiguration.class) // Common lib components loading to context
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
