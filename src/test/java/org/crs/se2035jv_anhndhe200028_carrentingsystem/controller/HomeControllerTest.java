package org.crs.se2035jv_anhndhe200028_carrentingsystem.controller;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.service.CarService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class HomeControllerTest {

    @Test
    void indexRedirectsToSignin() {
        HomeController controller = new HomeController(mock(CarService.class));

        assertThat(controller.index()).isEqualTo("redirect:/auth/signin");
    }
}
