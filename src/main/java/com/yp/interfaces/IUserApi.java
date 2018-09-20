package com.yp.interfaces;

import com.yp.dataobject.User;
import com.yp.service.ApiServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author fengzheng
 * @create 2018-09-15 9:26
 **/
@ApiServer("http://localhost:8090/user")
public interface IUserApi {

    @GetMapping("/getAllUser")
    Flux<User> getAllUser();

    @PostMapping("/createUser")
    Mono<User> createUser(@RequestBody Mono<User> user);

    @RequestMapping("/deleteByUserId/{id}")
    Mono<ResponseEntity<Void>> deleteByUserId(@PathVariable("id") String id);

    @PostMapping("/updateUserById/{id}")
    Mono<ResponseEntity<User>> updateUserById(@PathVariable("id") String id, @RequestBody User user);

    @GetMapping("/findByRangeAge/{start}/{end}")
    Flux<User> findByRangeAge(@PathVariable("start") Integer start, @PathVariable("end") Integer end);
}
