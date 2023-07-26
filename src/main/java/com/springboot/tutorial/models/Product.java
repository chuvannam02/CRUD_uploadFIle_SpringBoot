package com.springboot.tutorial.models;

import jakarta.persistence.*;

import java.util.Calendar;
import java.util.Objects;

// model or entity = Thực thể
//POJO = Plain Object Java Object -> convert into JSON in order that client can use,
// Java Object clients can not read and used
@Entity
// Thực thể
@Table(name = "tblProduct")
// Cho biết thực thể này tương ứng với bảng table "tblProduct trong cơ sở dữ liệu
public class Product {
    // gắn với database phải tìm tới "primary key" - khoá chính
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    // auto = auto_increment
    // id bản ghi ban đầu là 1 sau đó là 2 , 3 tự động tăng thêm 1 sau mỗi lần
    // You can also use "sequence"
    // Tạo ra quy tắc cho thứ tự của trường id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

    //Validate = Constraint - Các ràng buộc của database
    @Column(nullable = false, unique = true, length = 300)
    // nullable = false tức bắt buộc phải nhập productName không được để trống
    // unique = true tức nếu có productName = "Nam" thì không thể thêm productName khác = "Nam" được
    // length = 300 tức length của productName là varchar(300) maximum 300 ký tự
    private String productName;
    private int productYear;
    private double price;
    private String url;
    // calculated field = transient, not exist in mysql
    // Trường được tính từ các trường khác = transient = tạm thời
    // các trường không được lưu trong db nhưng được tính toán nhờ các trường khác
    @Transient
    private int age; //age is calculated from "year"

    //Default Constructor
    public Product() {
    }

    //Parameterized Constructor
    //hint right click then choose generate... After that, select all field you want to pass into Constructor
    public Product(String productName, int productYear, double price, String url) {
//        this.id = id;
        this.productName = productName;
        this.productYear = productYear;
        this.price = price;
        this.url = url;
    }

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - productYear;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productYear=" + productYear +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }

    //Getter and setter properties of object class
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductYear() {
        return productYear;
    }

    public void setProductYear(int productYear) {
        this.productYear = productYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    // Hàm equals
    // Định nghĩa 2 đối tượng Product như thế nào thì được gọi là khác nhau hoặc giống nhau về nội dung
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productYear == product.productYear
                && Double.compare(product.price, price) == 0
                && age == product.age
                && Objects.equals(id, product.id)
                && Objects.equals(productName, product.productName)
                && Objects.equals(url, product.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productYear, price, url, age);
    }
}

//Objects are created from this class are call POJO - Plain Object Java Object
