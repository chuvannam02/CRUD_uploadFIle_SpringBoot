package com.springboot.tutorial.database;

import com.springboot.tutorial.models.Product;
import com.springboot.tutorial.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Now connect with mysql using JPA = Java Persistence API

//this class has @Configuration annotation included method - Bean Method
//Bean method này sẽ được gọi ngay khi ứng dụng của ta chạy
@Configuration
public class Database {

    //logger
    //logger để thay thế cho System.out.println
    // Cũng là in ra màn hình chi tiết hơn infor, in ra error, in ra waring ...
    // getLogger() truyền vào class name -> hiển thông tin chi tiết của các function trong class nào
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    //Có thể khởi tạo database, khởi tạo biến môi trường ...
    // Tạo dữ liệu fake ban đầu cho database H2
    // Initialize data for H2 Database
    // Data ban đầu trắng trơn, insert một vài bản ghi vào trong database
    // Nếu chưa có table sẽ tự động tạo table --- khái niệm code first code có trước data từ đó mà ra
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        // CommandLineRunner là 1 interface
        // Dòng 33 là tạo ra một đối tượng object - instance cụ thể là thực thi hàm run trong đó
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

//                Product productA = new Product(1L, "Macbook pro 16 inch", 2020, 2400.0, "");
//                Product productB = new Product(2L, "Dell Vostro 5400", 2012, 500, "");
//                Product productA = new Product("Macbook pro 16 inch", 2020, 2400.0, "");
//                Product productB = new Product("Dell Vostro 5400", 2012, 500, "");
                // Sau đó save data into database
                //Ctrl D để duplicate dòng chứa con trỏ chuột
                // save 2 bản ghi - record vào trong database
//                System.out.println("insert data: " + productRepository.save(productA));
//                System.out.println("insert data: " + productRepository.save(productB));

// Chú ý khi lưu vào H2 DB và test thành công
// Chuyển sang dùng mysql sau khi lưu thành công tắt ứng dụng chạy lại
// Sẽ gặp lỗi vì duplicate productName trong khi productName phải unique
//                logger.info("insert data: " + productRepository.save(productA));
//                logger.info("insert data: " + productRepository.save(productB));
            }
        };
    }

}
