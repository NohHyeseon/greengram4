package com.greengram.greengram4.common;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Getter
@Component
public class MyFileUtils {

    private final String uploadPrefixPath; //이값이


    public MyFileUtils(@Value("${file.dir}") String uploadPrefixPath) {
        this.uploadPrefixPath = uploadPrefixPath;
    }

    //폴더만들기
    public String makeFolder(String path) {
        File folder = new File(uploadPrefixPath, path);
        folder.mkdirs(); //폴더 만들기
        return folder.getAbsolutePath();//absoultePath에 두개값이 합쳐진게 들어감
    }

    //랜덤파일명 만들기
    public String getRandomFileNm() {
        return UUID.randomUUID().toString();// UUID->랜덤으로 만들어주고 문자열로 리턴
    }

    //확장자 얻어오기
    public String getExt(String fileNm) {

//        int idx = fileNm.lastIndexOf(".");
//        System.out.println("idx = " + idx);
//        String ext = fileNm.substring(idx);
//        System.out.println("file name : " + fileNm);
//        System.out.println("extension : " + ext);
        return fileNm.substring(fileNm.lastIndexOf("."));
    }

    public String getRandomFileNm(String originFileNm) {
        String nm = getExt(originFileNm);
        return getRandomFileNm() + nm; //문자열 문자열이라 + 로
    }

    //랜덤파일명 만들기 with 확장자 from multipartFile
    public String getRandomFileNm(MultipartFile mf) { //오버로딩 (파라미터만 다르면 상관없음)
        String fileNm = mf.getOriginalFilename();
        return getRandomFileNm(fileNm);
    }

    //메모리에 있는 내용 > 파일로 옮기는 메소드
    public String transferTo(MultipartFile mf, String target) { //mf 메모리에있는 사진
        String fileNm = getRandomFileNm(mf); //확장자는 바뀌지않음
        String folderPath = makeFolder(target); //user라는 폴더 생기는 순간
        File saveFile = new File(folderPath, fileNm); //순서 바뀌면안됨
        saveFile.exists();
        try {
            mf.transferTo(saveFile); //transferTo에 saveFile을 보내줌 메모리에 있던걸 파일로 옮겨줌
            return fileNm; //db에 저장하기 위해 랜덤한파일명 보냄 경로는 저장안함
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delFolderTrigger(String relativePath) {
        delFolder(uploadPrefixPath + relativePath);
    }

    public void delFolder(String folderPath) { //폴더 아래에 폴더 및 파일 삭제, 보냈는 폴더는 삭제안함 안쪽에있는것만
        File folder = new File(folderPath); //폴더 만들기
        if (folder.exists()) {
            File[] files = folder.listFiles(); //안에 파일과 사진이 존재함 파일객체를 지울때 또 한번 delFiles가 호출됨

            for (File file : files) {
                if (file.isDirectory()) { //디렉토리인지 확인하고 반복문 돌기
                    delFolder(file.getAbsolutePath()); //getAbsoultePath 절대주소값만 가져옴
                } else {
                    file.delete(); //디렉토리안 파일이 없을경우만 폴더가 삭제됨 그 후 다음 사진 즉 디렉토리가 아닌 사진을 지움
                }
            }
            folder.delete();
        }
    }
}
