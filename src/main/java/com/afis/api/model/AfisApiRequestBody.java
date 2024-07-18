package com.afis.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfisApiRequestBody {
    private String probe;
    private String candidate;
}
