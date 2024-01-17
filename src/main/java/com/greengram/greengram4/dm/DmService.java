package com.greengram.greengram4.dm;

import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.dm.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DmService {
    private final DmMapper mapper;

    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dmMsgSelDto) {
        return mapper.getMsgAll(dmMsgSelDto);
    }

    public List<DmSelVo> getDmAll(DmSelDto dmSelDto) {
        return mapper.getDmAll(dmSelDto, 0);
    }

    public DmSelVo postDm(DmInsDto dto) {
        if (mapper.checkEx(dto) != 0) return null;
        if (mapper.insDm(dto) == 1) {
            if (mapper.insDmUser(dto) != 0) {
                DmSelDto dmSelDto = new DmSelDto();
                dmSelDto.setLoginedIuser(dto.getLoginedIuser());
                List<DmSelVo> dmAll = mapper.getDmAll(dmSelDto, dto.getIdm());
                log.info("dmAll.get(0) = {}", dmAll.get(0));
                return dmAll.get(0);
            }
        }
        return null;
    }


    public ResVo postDmMsg(DmMsgInsDto msgInsDto) {
        mapper.postDmMsg(msgInsDto);
        return new ResVo(msgInsDto.getSeq());
    }

    LocalDateTime now=LocalDateTime.now();//현재날짜구하기
    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String createdAt=now.format(formatter);//포맷적용
    //상대방의 firevaseToken값 필요 나의 pic,iuser값 필요.


    public ResVo delDmMsg(DmMsgDelDto msgDelDto) {
        return new ResVo(mapper.delDmMsg(msgDelDto));
    }
}
