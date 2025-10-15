package com.dophuong.demo_redis;

import org.springframework.boot.SpringApplication;

public class TestDemoRedisApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoRedisApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
