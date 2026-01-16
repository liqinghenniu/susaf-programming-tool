package com.yyq.controller;

import com.yyq.entity.JsonResult;
import com.yyq.entity.SDGResult;
import com.yyq.entity.keyword;
import com.yyq.service.CodeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
public class CodeAnalysisController {
    @Autowired
    private CodeAnalysisService codeAnalysisService;

    private Path getUnzipPath() {
        String unzipstr = FileController.unzipDirStr;
        if (unzipstr == null) {
            throw new RuntimeException("请先上传文件");
        }
        return Paths.get(unzipstr);
    }

    @GetMapping("/scan/keywords")
    public JsonResult ScanKeywords() throws IOException {

        List<Map<String, Object>> result = codeAnalysisService.ScanKeywords(getUnzipPath());
        return JsonResult.success(result);
    }

    @GetMapping("scan/loop")
    public JsonResult ScanLoop() {
        SDGResult result = codeAnalysisService.ScanLoop(getUnzipPath());
        return JsonResult.success(result);
    }

    @GetMapping("scan/test")
    public JsonResult ScanTest() {
        SDGResult result = codeAnalysisService.ScanTestFile(getUnzipPath());
        return JsonResult.success(result);

    }

    @GetMapping("scan/main")
    public JsonResult ScanMain() {
        SDGResult result = codeAnalysisService.ScanMain(getUnzipPath());
        return JsonResult.success(result);
    }

    @GetMapping("scan/size")
    public JsonResult ScanSize() {
        SDGResult result = codeAnalysisService.ScanSize(getUnzipPath());
        return JsonResult.success(result);
    }

    @GetMapping("scan/resource")
    public JsonResult ScanResource() {
        SDGResult result= codeAnalysisService.ScanResource(getUnzipPath());
        return JsonResult.success(result);
    }
}