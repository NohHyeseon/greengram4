package com.greengram.greengram4.user;


import com.greengram.greengram4.common.*;
import com.greengram.greengram4.exception.AuthErrorCode;
import com.greengram.greengram4.exception.RestApiException;
import com.greengram.greengram4.feed.model.FeedPicsInsDto;
import com.greengram.greengram4.security.AuthenticationFacade;
import com.greengram.greengram4.security.JwtTokenProvider;
import com.greengram.greengram4.security.MyPrincipal;
import com.greengram.greengram4.security.MyUserDetails;
import com.greengram.greengram4.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder; //SecurityConfiguration안에 bean등록시켜둠
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils myFileUtils;



    public ResVo signup(UserSignupDto dto) {
        String hashedPw = passwordEncoder.encode(dto.getUpw());
        UserSignupProcDto dto1 = new UserSignupProcDto(dto);
        dto1.setUpw(hashedPw);
        mapper.insUser(dto1);
        ResVo vo = new ResVo(dto1.getIuser());

        //비밀번호 암호화
        return vo;//회원가입한 iuser pk값이 리턴

    }

    public UserSignInVo signin(HttpServletResponse res, UserSignDto dto) {


        UserSignProcDto savedVo = mapper.selUser(dto);
        UserSignInVo vo = new UserSignInVo();
        if (savedVo == null) {//아이디없음
            throw new RestApiException(AuthErrorCode.NOT_EXIST_USER_ID);
        } else if (!passwordEncoder.matches(dto.getUpw(), savedVo.getUpw())) {
            throw new RestApiException(AuthErrorCode.VALID_PASSWORD);
        }
        vo.setResult(Const.L0GIN_SUCCEED);
        vo.setIuser(savedVo.getIuser());
        vo.setNm(savedVo.getNm());
        vo.setPic(savedVo.getPic());


        MyPrincipal myPrincipal = MyPrincipal.builder()
                .iuser(savedVo.getIuser())
                .build();

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);

        //rt > cookie에 담을거임
        int rtCookieMaxAge = (int) appProperties.getJwt().getRefreshTokenExpiry() / 1000;
        cookieUtils.deleteCookie(res, "rt");
        cookieUtils.setCookie(res, "rt", rt, rtCookieMaxAge);

        vo.setAccessToken(at);
        vo.setFirebaseToken(savedVo.getFirebaseToken());
        return vo;
    }

    //1.쿠키를지움 2.성공시 at값 지움
    public ResVo signout(HttpServletResponse res) {
        cookieUtils.deleteCookie(res, "rt");

        return new ResVo(1);
    }


    public UserSignInVo getRefreshToken(HttpServletRequest req) {
        Cookie cookie = cookieUtils.getCookie(req, "rt");
        UserSignInVo vo = new UserSignInVo();
        if (cookie == null) {
            vo.setAccessToken(null);
            vo.setResult(Const.FAIL);
            return vo;
        }
        String token = cookie.getValue();


        if (!jwtTokenProvider.isValidateToken(token)) {
            vo.setResult(Const.FAIL);
            vo.setAccessToken(null);
            return vo;
        }
        MyUserDetails myUserDetails = (MyUserDetails) jwtTokenProvider.getUserDetailsFromToken(token);
        MyPrincipal myPrincipal = myUserDetails.getMyPrincipal();
        String at = jwtTokenProvider.generateAccessToken(myPrincipal);

        vo.setResult(Const.SUCCESS);
        vo.setAccessToken(at);


        return vo;
    }


    public UserInfoVo getInfo(UserInfoSelDto userInfoSelDto) {
        return mapper.userInfo(userInfoSelDto);
    }

    public ResVo upUser(int iuser) {
        ResVo vo = new ResVo(mapper.upUserPic(iuser));
        return vo;
    }


    public ResVo toggleFollow(UserFollowDto dto) {
        int del = mapper.delFollow(dto);
        if (del == 1) {
            return new ResVo(0);
        } else {
            return new ResVo(mapper.insFollow(dto));
        }
    }

    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
        int affectedRows = mapper.updUserFirebaseToken(dto);
        return new ResVo(affectedRows);
    }

    public UserPicPatchDto patchUserPic(MultipartFile pic) {
        UserPicPatchDto dto = new UserPicPatchDto();
        dto.setIuser(authenticationFacade.getLoginUserPk()); //로긴된 iuser값 가져오는거
//        myFileUtils.delFiles();
        String path = "/user/" + dto.getIuser();//user폴더가 생김
        myFileUtils.delFolderTrigger(path);
        String savedPicFileNm = myFileUtils.transferTo(pic, path);
        dto.setPic(savedPicFileNm); //여기서 사진을 넣어줌
        int affectedRows = mapper.updUserPic(dto);
        return dto;
    }



}


