package com.greengram.greengram4.dm;


import com.greengram.greengram4.common.Const;
import com.greengram.greengram4.common.ResVo;
import com.greengram.greengram4.dm.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dm")
@Slf4j
public class DmController {

    private final DmService service;

    @GetMapping("/msg")
    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dmMsgSelDto) {

        log.info("msgDto = {}", dmMsgSelDto);
        return service.getMsgAll(dmMsgSelDto);

    }

    @GetMapping
    public List<DmSelVo> getDmAll(DmSelDto dmSelDto) {
        dmSelDto.setRowCount(Const.DM_MSG_COUNT_PER_PAGE);
        dmSelDto.setStartIdx((dmSelDto.getPage() - 1) * dmSelDto.getRowCount());

        return service.getDmAll(dmSelDto);
    }

    @PostMapping
    public DmSelVo postDm(@RequestBody DmInsDto dto) {
        return service.postDm(dto);
    }

    @PostMapping("/msg")
    public ResVo postDmMsg(@RequestBody DmMsgInsDto dmMsgInsDto) {
        // seq 값 리턴
        return service.postDmMsg(dmMsgInsDto);
    }

    @DeleteMapping("/msg")
    public ResVo delDmMsg(DmMsgDelDto dmMsgDelDto) {
        return service.delDmMsg(dmMsgDelDto);
    }


}
