package com.darkross.wssecuritycore.service;

import com.darkross.wssecuritycore.dto.auth.LoginRequestDto;
import com.darkross.wssecuritycore.dto.auth.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequest);
}
