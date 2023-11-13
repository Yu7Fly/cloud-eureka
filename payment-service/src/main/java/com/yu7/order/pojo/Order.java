package com.yu7.order.pojo;



import com.yu7.model.User;
import lombok.Data;

@Data
public class Order {
    private Long id;
    private Long price;
    private String name;
    private Integer num;
    private Long userId;
    /**
     * 这样依赖会不会出现以后实体类不同步的情况呢？
     */
    private User user;
}