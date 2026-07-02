package org.crs.se2035jv_anhndhe200028_carrentingsystem;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Se2035JvAnhNdhe200028CarRentingSystemApplication.class);
    }

}
