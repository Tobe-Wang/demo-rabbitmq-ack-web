package cn.zhaofd.demorabbitmqackweb.demo.web;

import cn.zhaofd.demorabbitmqackweb.demo.dto.SysUser;
import cn.zhaofd.demorabbitmqackweb.demo.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * RabbitMQ应用示例
 */
@RestController
@RequestMapping("/demo/rabbitmq")
public class RabbitMQController {
    private final RabbitMQService rabbitMQService;

    public RabbitMQController(@Autowired RabbitMQService rabbitMQService) {
        this.rabbitMQService = rabbitMQService;
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    @GetMapping("/sendMessage")
    public void sendMessage(String message) {
        rabbitMQService.sendMessage(message);
    }

    /**
     * 发送消息(用户)
     */
    @GetMapping("/sendSysUserMsg")
    public void sendSysUserMsg() {
        SysUser sysUser = new SysUser();
        sysUser.setId(1);
        sysUser.setName("张三");
        sysUser.setRegtime(LocalDateTime.now());

        rabbitMQService.sendMessage(sysUser);
    }
}
