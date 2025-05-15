package com.clonecod.clone.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AddToCartRequest {
    private Long memberId;
    private List<Long> bookIds;
}
