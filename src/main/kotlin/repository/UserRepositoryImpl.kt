package com.example.repository

import com.example.security.JwtConfig
import com.example.service.CreateUserParams
import com.example.service.UserService
import com.example.utils.BaseResponse

class UserRepositoryImpl(
    private val userService: UserService
) : UserRepository {
    override suspend fun registerUser(params: CreateUserParams): BaseResponse<Any> {
        return if (isEmailExist(params.email)){
            BaseResponse.ErrorResponse(message = "Электронная почта уже существует")
        }else{
            val user = userService.registerUser(params)
            if(user != null){
                val token = JwtConfig.instance.createAccessToken(user.id)
                user.authToken = token
                BaseResponse.SuccessResponse(data = user)
            }else{
                BaseResponse.ErrorResponse()
            }
        }
    }

    override suspend fun loginUser(email: String, password: String): BaseResponse<Any> {
        TODO("Not yet implemented")
    }

    private suspend fun isEmailExist(email: String): Boolean{
        return userService.findUserByEmail(email) != null
    }
}