package com.yyq.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarbonResult {
        private double carbon;
        private double electricity;
        private String message;
        private String type;
        private String SDG;
}
