package com.springboot.tutorial.controllers;

import com.springboot.tutorial.models.ResponseObject;
import com.springboot.tutorial.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
//Cho biết class này là controller
@RequestMapping(path = "/api/v1/FileUpload")
// ánh xạ đường dẫn, router
public class FileUploadController {
    // Inject Service = create object (only 1 time) which implements IStorageService interface
    // Inject Storage Service here
    @Autowired
    private IStorageService storageService;

    // Tạo ra đối tượng service 1 lần duy nhất khi mà app chạy
    //Thís controller receive file/image from client
    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file) {
        // Tránh trường hợp truy cập vào các file ảnh không truy cập được do không có quyền hoặc không tìm thấy ...
        try {
            // save files to a folder in server => use a service
            // Gọi hàm storeFile của service
            String generatedFileName = storageService.storeFile(file);
            // Khi nhận được request là 1 file gửi qua postman, web, moble,...
            // Sau đó gọi hàm storeFile và trả về giá trị là fileName
            // Đó là file lưu vào trong server và được generated 1 cái tên mới
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("SUCCESS", "Upload file image successfully", generatedFileName)
            );
            // 0f9256e441d349cfb3dc35c6fca2b6e6.png => how to open this image (this file) in Web Browser?
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("FAILED", exception.getMessage(), "")
            );
        }
    }

    // get image's url
    @GetMapping("/files/{fileName:.+}")
    // /files/0f9256e441d349cfb3dc35c6fca2b6e6.png
    public ResponseEntity<byte[]> readDetailedFile(@PathVariable String fileName) {
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        } catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }

    // How to load all uploaded files? tải tất cả các files đã upload
    @GetMapping("all")
    public ResponseEntity<ResponseObject> getUploadedFiles() {
        try {
            List<String> urls = storageService.loadAll()
                    .map(path -> {
                        // convert fileName to URL (send request to "readDetailedFile")
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class
                                , "readDetailedFile", path.getFileName().toString()).build().toUri().toString();
                        return urlPath;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(
                    new ResponseObject("SUCCESS", "List files successfully", urls));
        } catch (Exception exception) {
            return ResponseEntity.ok(
                    new ResponseObject("FAILED", "List files failed", new String[]{}));
        }
    }
}
