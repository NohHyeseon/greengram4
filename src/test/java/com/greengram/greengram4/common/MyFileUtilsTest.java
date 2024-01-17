package com.greengram.greengram4.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@ExtendWith(SpringExtension.class)
@Import({MyFileUtils.class})
@TestPropertySource(properties = {"file.dir=D:/home/download",}) //야믈에 있는 값이아니라  @Value
public class MyFileUtilsTest {

    @Autowired
    private MyFileUtils myFileUtils;

    @Test
    public void makeFoldersrTest() {
        String path = "/ggggd";
        File preFolder = new File(myFileUtils.getUploadPrefixPath(), path); //File->io하는 친구 directory,file Controll가능
        //MyFileUtils.와 path둘다 경로 첫번째가 D:/home이들어감 path에는 ggg가들어감 문자열자동합치기
        assertEquals(false, preFolder.exists()); //false를 기대

        String newPath = myFileUtils.makeFolder(path);
        File newFolder = new File(newPath);
        assertEquals(preFolder.getAbsolutePath(), newFolder.getAbsolutePath());
        assertEquals(true, newFolder.exists());
    }

    @Test
    public void getRandomFileNm() {
        String fileNm = myFileUtils.getRandomFileNm();
        System.out.println("fileNm: " + fileNm);
        assertNotNull(fileNm);
        assertNotEquals("", fileNm);


    }

    //확장자 얻어오기 테스트
    @Test
    public void getExtTest() {
        String fileNm = "abc.efg.eee.jpg";

        String ext = myFileUtils.getExt(fileNm);
        assertEquals(".jpg", ext);

        String fileNm2 = "jjj-asdfasdf.pnge";
        String ext2 = myFileUtils.getExt(fileNm2);
        assertEquals(".pnge", ext2);

    }

    @Test
    public void getRandomFileNm2() {
        String fileNm1 = "반갑다.친구야.돈갚아.jpg";
        String rFileNm1 = myFileUtils.getRandomFileNm(fileNm1);
        System.out.println("rFileNm1: " + rFileNm1);


        String fileNm2 = "반갑다sdfsd아.jpeg";
        String rFileNm2 = myFileUtils.getRandomFileNm(fileNm2);
        System.out.println("rFileNm1: " + rFileNm2);
    }

    @Test
    public void transferToTest() throws Exception {
        String fileNm = "운동장.jpg";
        String filepath = "C:/Users/Administrator/Desktop/짱구/" + fileNm;
        FileInputStream fis = new FileInputStream(filepath);
        MultipartFile mf = new MockMultipartFile("pic",fileNm,"png", fis);

        String saveFileNm = myFileUtils.transferTo(mf, "/feed/10");
        log.info("myFileUitls: {}", saveFileNm);
    }

}
