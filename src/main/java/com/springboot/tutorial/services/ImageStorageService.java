package com.springboot.tutorial.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStorageService {
    private final Path storageFolder = Paths.get("uploads");

    // Tham chiếu tới thư mục để upload ảnh
    // thư mục này nằm trong cùng project nếu chưa có sẽ tự tạo ra
    // Tạo ra ở đâu ??? => Tạo ra trong hàm Constructor
    // Constructor
    // Khi nào Constructor được gọi ?
    // => Khi Chúng ta inject service này thì sẽ được gọi 1 lần duy nhất khi app chạy
    // Mỗi lần chạy là 1 lần gọi
    public ImageStorageService() {
        try {
            System.out.println("Create a folder to save image file");
            Files.createDirectories(storageFolder);
        } catch (Exception exception) {
            throw new RuntimeException("Can not initialize storage", exception);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        // Let install FileNameUtils
        // bình thường phải kiểm tra đuôi file ảnh
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        // file.getOriginalFilename() để lấy ra xem đuổi file này là gì
        // chỉ cho phép nhận các đuôi {"png","jpeg","jpg","bmp"} là file ảnh
        return Arrays.asList(new String[]{"png", "jpeg", "jpg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("store image file in storage");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store an empty file");
            }

            // check file is image?
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image files");
            }

            // file must be <= 5MB
            float fileSizeInMegabytes = file.getSize() / 1_000_000.f;
//            float fileSizeInMegabytes = (float) (file.getSize() / Math.pow(2, 20));
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5MB");
            }

            // file must be rename Why ?
            // 1 file image đang có sẵn trong thư mục
            // người dùng upload 1 file image mới lên có filename gần giống file trên
            // Dẫn đến file cũ bị file mới ghi đè làm xoá mất file cũ
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                    Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("Can not store file image outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                // Copy file vừa rồi upload lên sau đó ghi vào thư mục đích, thư mục ta mặc định là chứa file
                // nếu nó đã tồn tại rồi thì ghi đè file mới lên
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;

        } catch (IOException exception) {
            throw new RuntimeException("Failed to store a file", exception);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            // list all files in storageFolder
            // Chỉ chứa tất cả các ảnh uploaded gần nhất không chứa cháu chắt gì hết
            // How to fix file have "_.asdsafasf...png" remove ._
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
// Câu lệnh 1 dòng thì debug không duyệt qua vậy nên cho vào 1 khối lệnh {} và thêm từ khoá return
//                    {
////                        return !path.equals(this.storageFolder) && path.toString().contains("._");
//                    }
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read stored files", exception);
        }

    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            // download file trả về kiểu dữ liệu là resolve còn nếu muốn xem file trả về kiểu dữ liệu là bytes
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
