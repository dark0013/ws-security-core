package com.darkross.wssecuritycore.service;

import com.darkross.wssecuritycore.dto.UserRequestDto;
import com.darkross.wssecuritycore.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto requestDto);

    UserResponseDto updateUser(Long id, UserRequestDto requestDto);

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    void deleteUser(Long id);

    UserResponseDto restoreUser(Long id);
}
