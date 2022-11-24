package com.ssafy.board.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration // spring bean configuration (root-context.xml) 파일을 대신하는 자바, 스프링 객체 저장 창고
@MapperScan(basePackages = "com.ssafy.board.model.dao") // 인터페이스 매퍼 (dao) 스캔하기
public class DatabaseConfig {

}
