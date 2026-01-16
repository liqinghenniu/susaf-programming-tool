package com.yyq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yyq.entity.CarbonResult;

import java.nio.file.Path;

public interface CarbonAnalysisService {
    CarbonResult calculateCarbon(Path directory) throws JsonProcessingException;
}
