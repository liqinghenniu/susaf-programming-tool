package com.yyq.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class fileContent {
    String content;
    Path path;
}
