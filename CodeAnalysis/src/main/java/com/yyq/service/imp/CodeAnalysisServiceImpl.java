package com.yyq.service.imp;

import com.yyq.entity.SDGResult;
import com.yyq.entity.fileContent;
import com.yyq.entity.keyword;
import com.yyq.mapper.CodeAnalysisMapper;
import com.yyq.service.CodeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Service
public class CodeAnalysisServiceImpl implements CodeAnalysisService {
    @Autowired
    private CodeAnalysisMapper codeAnalysisMapper;
    @Override
    public List<keyword> getAllKeywords() {
        List<keyword> keywords = codeAnalysisMapper.findAllKeywords();
        return keywords;
    }
   @Override
   public List<fileContent> getAllFilesContent(Path directory){
        List<fileContent> allFilesContent = new ArrayList<>();
       try {
           Files.walk(directory)
           .filter(path -> Files.isRegularFile(path))
           .filter(path -> isTextFile(path))
           .forEach(path -> {
               try {
                   String content = Files.readString(path);
                   fileContent fc = new fileContent(content, path);
                   allFilesContent.add(fc);
               } catch (IOException e) {
                   System.out.println("fail to read file" + path);
                   throw new RuntimeException(e);
               }
           });
       } catch (IOException e) {
           System.out.println("fail to walk directory" + e.getMessage());
           throw new RuntimeException(e);
       }
       return allFilesContent;
   }
    @Override
    public List<Map<String, Object>> ScanKeywords(Path directory) throws IOException {
        List<keyword> allKeywords = getAllKeywords();
        List<Map<String, Object>> result = new ArrayList<>();
       getAllFilesContent(directory).forEach(fileContent -> {
           for (keyword keyword : allKeywords) {
               if (fileContent.getContent().contains(keyword.getContent())) {
                   Map<String, Object> matchSDG = new HashMap<>();
                   matchSDG.put("keyword",keyword.getContent());
                   matchSDG.put("polarity",keyword.getPolarity());
                   matchSDG.put("file",fileContent.getPath().getFileName().toString());
                   result.add(matchSDG);
               }
           }
       });
        return result;
    }

    @Override
    public SDGResult ScanLoop(Path directory){
        // 定义死循环模式
        List loopList = new ArrayList<>();
        List<String> deadLoopPatterns = Arrays.asList(
                "while(1)", "while(true)", "for(;;)",
                "for(;1;)", "for(;true;)"
        );
        getAllFilesContent(directory).forEach(fileContent -> {
            String processedContent = fileContent.getContent().toLowerCase().replaceAll("\\s+", "");
            for (String pattern : deadLoopPatterns){
                if (processedContent.contains(pattern)){
                    if( processedContent.contains("break") || processedContent.contains("continue")){
                        break;
                    }
                    loopList.add(pattern);
                }
            }
        });
        return new SDGResult(loopList, "SDG7. Affordable and Clear Energy", "loop");
    }

    @Override
    public SDGResult ScanTestFile(Path directory){
        List<fileContent> allJavaContent = new ArrayList<>();
        List<String> testPath = new ArrayList<>();
        try {
            Files.walk(directory)
                 .filter(path -> path.toString().endsWith(".java"))
                 .forEach( path -> {
                     try {
                         String fileName = path.getFileName().toString();
                         String content = Files.readString(path);
                         fileContent fc = new fileContent(content, path);
                         allJavaContent.add(fc);
                         if (fileName.toLowerCase().contains("test")) {
                             testPath.add(path.toString());
                         }
                     } catch (IOException e) {
                         System.out.println("fail to read file" + path);
                         throw new RuntimeException(e);
                     }
                 });
        } catch (IOException e) {
            System.out.println("fail to walk directory" + e.getMessage());
            throw new RuntimeException(e);
        }
        allJavaContent.forEach(fileContent -> {
            String content = fileContent.getContent();
            if (content.toLowerCase().contains("@test")) {
                testPath.add(fileContent.getPath().toString());
            }
        });
        return new SDGResult(testPath, "SDG9. Industry, Innovation and Infrastructure","test");

    }

    @Override public SDGResult ScanMain(Path directory){
        List <String> longMain = new ArrayList<>();
        int l = 0;
        try {
            Files.walk(directory)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach( path -> {
                        try {
                            List <String> content = Files.readAllLines(path);
                            int mainLength = 0;
                            boolean isMain = false;
                            for (String line : content) {
                                if(line.contains("public static void main")){
                                    isMain = true;
                                }
                                if (isMain){
                                    mainLength ++;
                                }
                                if(line.trim().equals("}")){
                                    mainLength --;
                                }
                            }
                            if(mainLength > 10){
                                System.out.println(mainLength);
                                longMain.add(path.toString());
                            }
                        } catch (IOException e) {
                            System.out.println("fail to read file" + path);
                            throw new RuntimeException(e);
                        }

                    });
        } catch (IOException e) {
            System.out.println("fail to walk directory" + e.getMessage());
            throw new RuntimeException(e);
        }
        return new SDGResult(longMain, "SDG9. Industry, Innovation and Infrastructure","main");
    }
    @Override
    public SDGResult ScanSize(Path directory){
        String[] fontSizeProperties = {"font-size:", "fontsize:"};
        List<Map<String, Object>> sizeFile = new ArrayList<>();
        List <fileContent> allVuefiles = new ArrayList<>();
        try {
            Files.walk(directory)
                    .filter(path -> path.toString().endsWith(".vue"))
                    .forEach( path -> {
                        try {
                            String content = Files.readString(path).toLowerCase().replaceAll("\\s+", "");
                            fileContent fc = new fileContent(content, path);
                            allVuefiles.add(fc);

                        } catch (IOException e) {
                            System.out.println("fail to read file" + path);
                            throw new RuntimeException(e);
                        }

                    });
            for(String fontSizeProperty : fontSizeProperties){
                for (fileContent fileContent : allVuefiles){
                    String content = fileContent.getContent();
                    int index = 0;
                    while((index = content.indexOf(fontSizeProperty, index)) != -1 ){
                        StringBuilder numberStr = new StringBuilder();
                        int pxindex = content.indexOf("px",index + fontSizeProperty.length());
                        for (int i = pxindex; i >= 0; i--){
                            char c = content.charAt(i);
                            if (!Character.isDigit(c)){
                                numberStr.insert(0, c);
                            }else{
                                int pxNum = Integer.parseInt(numberStr.toString());
                                if (pxNum < 14){
                                    Map<String, Object> sizeFileInfo = new HashMap<>();
                                    sizeFileInfo.put("file", fileContent.getPath().toString());
                                    sizeFileInfo.put("size", pxNum);
                                    sizeFile.add(sizeFileInfo);
                                }
                                break;
                            }
                        }
                        index += fontSizeProperty.length();
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("fail to walk directory" + e.getMessage());
            throw new RuntimeException(e);
        }
        return new SDGResult(sizeFile, "SDG10. Reduced Inequality","size");
    }

    @Override
    public SDGResult ScanResource(Path directory) {
        List<fileContent> allJavaContent = new ArrayList<>();
        List<Map<String, Object>> resourceFile = new ArrayList<>();
        Set<String> resourceTypes = Set.of(
                "FileWriter", "FileReader", "FileInputStream", "FileOutputStream",
                "BufferedWriter", "BufferedReader", "BufferedInputStream", "BufferedOutputStream",
                "OutputStreamWriter", "InputStreamReader", "PrintWriter", "PrintStream",
                "Scanner", "ObjectInputStream", "ObjectOutputStream", "DataInputStream", "DataOutputStream"
        );
        try {
            Files.walk(directory)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach( path -> {
                        try {
                            String content = Files.readString(path);
                            fileContent fc = new fileContent(content, path);
                            allJavaContent.add(fc);
                        } catch (IOException e) {
                            System.out.println("fail to read file" + path);
                            throw new RuntimeException(e);
                        }
                    });
            allJavaContent.forEach(fileContent -> {
                String content = fileContent.getContent();
                String[] lines = content.split("\n");
                for (int i = 0; i < lines.length; i++) {
                    for (String type : resourceTypes) {
                        if (lines[i].contains(type)) {
                            boolean hasClose = false;
                            boolean hasFinally = false;
                            for (int j = i; j < lines.length; j++) {
                                if (lines[j].contains("close")) {
                                    hasClose = true;
                                }
                                if (lines[j].contains("finally")) {
                                    hasFinally = true;
                                }
                            }
                            if (!hasClose && !hasFinally) {
                                Map<String, Object> resourceFileInfo = new HashMap<>();
                                resourceFileInfo.put("file", fileContent.getPath().toString());
                                resourceFileInfo.put("resource",type);
                                resourceFile.add(resourceFileInfo);
                            }
                        }
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("fail to walk directory" + e.getMessage());
            throw new RuntimeException(e);
        }

        return new SDGResult(resourceFile, "SDG12. Responsible Consumption and Production","resource");

    }

    public boolean isTextFile(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".txt") || fileName.endsWith(".java") || fileName.endsWith(".md");
    }
}
