package com.petlover.petsocial.payload.request;

import com.petlover.petsocial.model.entity.Apply;
import com.petlover.petsocial.model.entity.ApplyStatus;
import com.petlover.petsocial.websocket.utils.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplyDTO {
    private Long id;
    private Date applyDate;
    private ApplyStatus status;
    private ExchangeDTO exchange;
    private Long userId;

    public static ApplyDTO convertToDTO(Apply apply) {
        ApplyDTO applyDTO = new ApplyDTO();
        applyDTO.setId(apply.getId());
        applyDTO.setApplyDate(apply.getApply_date());
        applyDTO.setStatus(apply.getStatus());

        // Assuming you have appropriate getters in the Exchange and User entities
        applyDTO.setExchange(MapperUtils.mapperObject(apply.getExchange(), ExchangeDTO.class));
        applyDTO.setUserId(apply.getUser().getId());

        return applyDTO;
    }

}


