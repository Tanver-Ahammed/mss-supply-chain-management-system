package com.therap.supply.chain.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@Component
public class RequisitionProductHistoryBindDTO {

    private List<RequisitionProductHistoryDTO> rphDTO = new ArrayList<>();

    public void addRequisitionProductHistoryDTO(RequisitionProductHistoryDTO requisitionProductHistoryDTO) {
        rphDTO.add(requisitionProductHistoryDTO);
    }

}
