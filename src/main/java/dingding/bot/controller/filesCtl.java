package dingding.bot.controller;

import com.azure.core.annotation.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("file")
public class filesCtl {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("doc", "docx", "pdf", "pptx", "txt");

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = getFileExtension(filename);

        // 检查文件格式是否符合要求
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("只允许上传文件格式为word、pdf、pptx和txt");
        }

        // 保存文件逻辑
        // file.transferTo(new File("路径" + filename));

        return ResponseEntity.status(HttpStatus.OK).body("文件上传成功");
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

}
