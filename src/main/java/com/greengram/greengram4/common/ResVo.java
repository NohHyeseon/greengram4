package com.greengram.greengram4.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor //모든 멤버필드를 받을 수 있는 생성자 forexam>> int , string , long 다가능
@NoArgsConstructor// 기본생성자 ,@RequiredArgsConsturctor>> final이 필수

public class ResVo {
    private int result;

}
