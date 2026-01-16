package com.yyq.service;

import com.yyq.entity.SDGResult;
import com.yyq.entity.fileContent;
import com.yyq.entity.keyword;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.nio.file.Path;

public interface CodeAnalysisService {
    List<fileContent> getAllFilesContent(Path directory) throws IOException;
    List<keyword> getAllKeywords();
    List<Map<String,Object>> ScanKeywords(Path directory) throws IOException;
    SDGResult ScanLoop(Path directory);
    SDGResult ScanTestFile(Path directory);
    SDGResult ScanMain(Path directory);
    SDGResult ScanSize(Path directory);
    SDGResult ScanResource(Path directory);
}
