package com.yp;

import com.yp.interfaces.IUserApi;
import com.yp.interfaces.ProxyCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class WebfluxclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxclientApplication.class, args);
		System.out.println("WebfluxclientApplication端启动成功");
	}

	@Bean
	FactoryBean<IUserApi> createBean(ProxyCreator proxyCreator){
		return new FactoryBean<IUserApi>() {
			/**
			 * @return 代理对象
			 * @throws Exception
			 */
			@Override
			public IUserApi getObject() throws Exception {
				return (IUserApi)proxyCreator.createProxy(this.getObjectType());
			}

			@Override
			public Class<?> getObjectType() {
				return IUserApi.class;
			}
		};
	}
}
