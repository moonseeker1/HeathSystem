package com.healthSystem.user.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SystemUserDiet {
    private String dietId;
    private String userId;
    private String userName;
    private String breakfast;
    private String lunch;
    private String dinner;

}
