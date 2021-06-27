package com.timberliu.im.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by liujie on 2021/6/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private String userId;

    private String username;
}
