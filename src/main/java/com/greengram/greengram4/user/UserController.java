package com.greengram.greengram4.user;


import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.user.model.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    @PostMapping("/signup")
    public ResVo postSignup(@RequestBody UserSignupDto dto) {
        log.info("dto:{}", dto);
        return service.signup(dto);
    }


    @PatchMapping("/firebase-token")
    public ResVo patchUserFirebaseToken(@RequestBody UserFirebaseTokenPatchDto dto) {
        return service.patchUserFirebaseToken(dto);
    }

    @PatchMapping("/pic")
    public UserPicPatchDto patchUserPic(@RequestPart MultipartFile pic) {
        return service.patchUserPic(pic);
    }

    //pk값, nm ,pic ,
    @PostMapping("/signin")
    public UserSignInVo postUser(HttpServletResponse res,
                                 @RequestBody UserSignDto dto) {
        log.info("dto, {}", dto);
        return service.signin(res, dto);
    }

    @PostMapping("/signout")
    public ResVo postSignout(HttpServletResponse res) {
        return service.signout(res);
    }

    @GetMapping("/refresh-token")
    public UserSignInVo getRefreshToken(HttpServletRequest req){
        return service.getRefreshToken(req);
    }



    @GetMapping
    public UserInfoVo getUserInfo(UserInfoSelDto userInfoSelDto) {
        return service.getInfo(userInfoSelDto);
    }

    @PutMapping("/update")
    public ResVo upUserpic(int iuser) {
        return service.upUser(iuser);
    }

    //------------------------------follow
    //ResVo  - result: following:1, unfollowing:0
    @PostMapping("/follow")
    public ResVo toggleFollow(@RequestBody UserFollowDto dto) {
        return service.toggleFollow(dto);

    }

}
