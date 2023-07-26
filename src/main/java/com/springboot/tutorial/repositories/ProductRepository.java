package com.springboot.tutorial.repositories;

import com.springboot.tutorial.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Repositories là nơi chưa dữ liệu - data
// Nơi chứa các method, các hàm để lấy dữ liệu về
//Data nằm ở in - memory, trong file text, trong database
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository<" Entity's type ", " primary key's type ">
    // Tận dụng các hàm CRUD - thêm bớt sửa xoá của database
    // có sẵn trong JpaRepository không cần viết lại chỉ cần extends
    // Hay ở chỗ những hàm cơ bản không cần viết phần thực thi


    // Viết đúng theo format, convention - quy ước ở dưới
    // thì sẽ tìm đúng theo productName - property của Product entity
    List<Product> findByProductName(String productName);
}
