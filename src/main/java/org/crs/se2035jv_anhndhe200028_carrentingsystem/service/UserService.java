package org.crs.se2035jv_anhndhe200028_carrentingsystem.service;

import org.crs.se2035jv_anhndhe200028_carrentingsystem.dto.LoginRequestDTO;
import org.crs.se2035jv_anhndhe200028_carrentingsystem.entity.Account;

public interface UserService {
    Account signin(LoginRequestDTO loginRequestDTO);

}
