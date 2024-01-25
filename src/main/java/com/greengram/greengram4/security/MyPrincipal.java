package com.greengram.greengram4.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor //json에서 객체화되는데 그때 기본생성자가 필요함 토큰값때문인듯
@AllArgsConstructor
public class MyPrincipal { //토큰에 집어넣는용도 (메모리관리때문에 따로뻄)
    private int iuser;
}

