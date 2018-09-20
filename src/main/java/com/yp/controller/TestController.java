package com.yp.controller;

import com.yp.dataobject.User;
import com.yp.interfaces.IUserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author fengzheng
 * @create 2018-09-15 9:57
 * @desc 自己实现类似于Feign框架调用服务的功能
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private IUserApi iUserApi;

    @GetMapping("/getAllUser")
    public void getAllUser(){
        iUserApi.getAllUser();
        //iUserApi.deleteByUserId("1111");
        iUserApi.createUser(Mono.just(User.builder().name("haha").age(20).build()));
//        Flux<User> userFlux = iUserApi.getAllUser();
//        userFlux.subscribe(System.out::println);
    }
}