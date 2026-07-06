package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.RegisterRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;
import org.springframework.validation.BindingResult;

public interface UserService {
    Account signin(LoginRequestDTO loginRequestDTO);
    void signup(RegisterRequestDTO registerRequestDTO, BindingResult bindingResult);
}
