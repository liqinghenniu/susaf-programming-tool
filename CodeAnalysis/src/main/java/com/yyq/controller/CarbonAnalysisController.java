package com.yyq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yyq.entity.CarbonResult;
import com.yyq.entity.JsonResult;
import com.yyq.service.CarbonAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class CarbonAnalysisController {
    @Autowired
    private CarbonAnalysisService carbonAnalysisService;

    private Path getUnzipPath() {
        String unzipstr = FileController.unzipDirStr;
        if (unzipstr == null) {
            throw new RuntimeException("please upload file first");
        }
        return Paths.get(unzipstr);
    }

    @PostMapping("/scan/carbon")
    public JsonResult ScanCarbon() throws JsonProcessingException {
        CarbonResult cr = carbonAnalysisService.calculateCarbon(getUnzipPath());
        if (cr.getMessage() == "success"){
            return JsonResult.success(cr);
        }
        else {
            return JsonResult.error();
        }
    }
}
