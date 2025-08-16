/*
 * Copyright (c) 2025. Tobe Wang
 */

package cn.zhaofd.demorabbitmqackweb.modules.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 */
public class SysUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Spring框架注解，用于前端到后端的数据绑定（HTTP请求参数->Java对象）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Jackson库注解，用于JSON序列化和反序列化（Java对象<->JSON字符串）
    private LocalDateTime regtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getRegtime() {
        return regtime;
    }

    public void setRegtime(LocalDateTime regtime) {
        this.regtime = regtime;
    }
}
