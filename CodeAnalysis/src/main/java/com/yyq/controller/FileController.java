package com.yyq.controller;

import com.yyq.entity.JsonResult;
import com.yyq.utils.ThreadLocalUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {
    public static String unzipDirStr;
    @PostMapping("/upload")
    public JsonResult uploadFile(MultipartFile file) throws IOException {
        try{
            Path root = Paths.get("E:\\assignment");
            File tempFile = File.createTempFile("assignment",".zip");
            Files.write(tempFile.toPath(),file.getBytes());
            Path unzipDir = Files.createTempDirectory(root,"unzip");
            ZipUtil.unpack(tempFile, unzipDir.toFile());
            unzipDirStr = unzipDir.toString();
            System.out.println(unzipDirStr);
            return JsonResult.success();
        }catch (IOException e){
            return JsonResult.error("上传失败",e.getMessage());
        }
    }

}
