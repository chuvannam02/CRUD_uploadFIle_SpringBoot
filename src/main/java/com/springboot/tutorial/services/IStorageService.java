package com.springboot.tutorial.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

// Đã có interface phải có phần thực thi
// Ví dụ như có ông nói phải có ông làm

// interface để định nghĩa các hàm được thực thi trong service đó
// sau đó inject service vào trong controller
// Trong Service có thể gọi tới Repository tuyệt đối không được gọi ngược lại
// Trong Controller gọi Service không có chiều ngược lại
public interface IStorageService {
    public String storeFile(MultipartFile file);
    public Stream<Path> loadAll(); //load all file inside a folder
    // Cần tạo ra 1 thư mục để chứa danh sách các ảnh

    // Tạo mảng các byte từ mảng các byte mới xem được ảnh
// Request gửi tới server, server trả về mảng các bytes thì chúng ta mới xem được ảnh một cách chi tiết
    // trên trình duyệt

    public byte[] readFileContent(String fileName);

    public void deleteAllFiles();
}
