package com.greengram.greengram4.dm;

import com.greengram.greengram4.dm.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DmMapper {
    int insDm(DmInsDto dto);

    int insDmUser(DmInsDto dto);

    List<DmMsgSelVo> getMsgAll(DmMsgSelDto dmMsgSelDto);
    List<DmSelVo> getDmAll(@Param("dto") DmSelDto dmSelDto, @Param("idm") int idm);

    int checkEx(DmInsDto dto);


    int postDmMsg(DmMsgInsDto dmMsgInsDto);

    int delDmMsg(DmMsgDelDto dmMsgDelDto);
}
