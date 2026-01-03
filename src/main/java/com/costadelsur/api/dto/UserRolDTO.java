package com.costadelsur.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolDTO {
    private Integer  userId; 
    private Integer  roleId; 
}
