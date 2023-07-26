package com.springboot.tutorial.controllers;

import com.springboot.tutorial.models.Product;
import com.springboot.tutorial.models.ResponseObject;
import com.springboot.tutorial.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
//import java.util.Optional;

@RestController
// @RestController annotation tell Java Spring that this class is a Controller
@RequestMapping(path = "/api/v1/Products")
//Routing It means that with link "../api/v1/Products" sent request to this Controller (ProductController)

public class ProductController {
    // DI = Dependency Injecttion
    @Autowired
    // Đối tượng repository sẽ được tạo ra ngay khi app của chúng ta được chạy
    // Tạo 1 lần sau đó cứ thế dùng - singleton pattern
    private ProductRepository repository;

    @GetMapping("getAllProducts")
        // this is GET method HTTP
        //this request is : http://localhost:8080/api/v1/Products/getAllProducts
        // v1= version 1

        //a list of String elements List<String>
        // but use Generics List<Product> List of object - instance (model Product)
    List<Product> getAllProducts() {
        //function List.of("","","",...); return list of String
//        return List.of("iphone", "ipad");

//        return List.of(
//                new Product(1L, "Macbook pro 16 inch", 2020, 2400.0, ""),
//                //id: 1L là số 1 nhưng kiểu dữ liệu là kiểu LONG
//                new Product(2L, "Del Vostro 5400 XS", 2012, 1500.0, "")
//        );
        //You must save this data to Database, Now we have H2 DB - In-memory Database
        // Using H2 - DB to test data can be save into database, save done you can get all data ?

        return repository.findAll(); //where is data?
        // method findALl(); có sẵn trong JpaRepository

        // Ctrl + Alt + SHift + L to reformat code
    }


    // Get detail product
    @GetMapping("/{id}")
    // Sử dụng Optional class thì giá trị trả về của hàm findById có thể null
    // Sử dụng orElseThrow thì không cần Optional Class nữa
    // Vì kiểu gì cũng trả về đối tượng khác null
    // Let's return an object with: data, message, status
    // Lúc này sử dụng ResponseEntity<ResponseObject> sau đó tạo Model/Entity định dạng cho object trả về
    // hàm findById() có sẵn trong JpaRepository
//    Product findById(@PathVariable Long id) {
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        // Viết lại dưới dạng Ternery Operator ? :
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS", "Query product successfully", foundProduct)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("FAILED", "Can not find product with id = " + id, "")
                );
        // Kiểm tra bên đối tượng Product bên trong Optional class khác null hay có chứa dữ liệu chưa nếu có thì...
//        if (foundProduct.isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS", "Query product successfully", foundProduct));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("FAILED","Can not find product with id = " + id,"")
//            );
//        }
//        return repository.findById(id).orElseThrow(()->new RuntimeException("Can not find product with id = "+id));
    }
    // khi vào link http://localhost:8080/api/v1/Products/5 vì chưa có product với id = 5
    // nên sẽ ném ra error như sau
//    {
//        "timestam" :" Thời gian gặp lỗi",
//        "status":"500" Mã lỗi,
//            "error":"Internal Server Error",
//        "path":"/api/v1/Products/5"
//    }
    // Hiển thị "Nội bộ lục đục - Internal Server Error" thì chả biết lỗi đang gặp là ở đâu
    // Nhưng chúng ta muốn trả về response "detail error" vậy phải làm thế nào?
    // Cụ thể chẳng hạn "Can not find the product with id = 5" thì biết lỗi ở đâu luôn
    // Vậy nên cần chuẩn hoá đối tượng trả về


    // Insert new Product with POST method HTTP
    // Postman: Raw, JSON
    @PostMapping("insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        // 2 products must not have the same name!!
        // There are to solution
        // The first method is use constraint at Database
        // The second one is write a method/ function check at JpaRepository
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        // trim() Để loại bỏ khoảng cách ở rìa trái và phải
        // Ví dụ "  Chu Nam   " trim() xong sẽ thành "Chu Nam"
//        if (foundProducts.size() > 0) {
//            // Nếu tồn tại ít nhất 1 product trùng với new product định insert thì sẽ thực thi như sau:
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("FAILED", "Product name is already taken in database", "")
//            );
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("SUCCESS", "Inserted new product successfully", repository.save(newProduct))
//            );
//        }

        // Viết lại dưới dạng Ternery Operator
        return foundProducts.size() > 0 ? ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("FAILED", "Product name is already taken in database", "")
        ) : ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("SUCCESS", "Inserted new product successfully", repository.save(newProduct))
        );
    }

    //update + insert = upsert = update if found, otherwise insert

    @PutMapping("/{id}")
//@PutMapping HTTP Status 405 - Request method 'PUT' not supported
// because you @PutMapping("/id") but we use @PathVariable
        // I mean link is http://localhost:8080/api/v1/Products/id not http://localhost:8080/api/v1/Products/1
//    @RequestMapping(value = "/{id}",
//            produces = "application/json",
//            method=RequestMethod.PUT)
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updatedProduct = repository.findById(id).map(product -> {
            product.setProductName(newProduct.getProductName());
            product.setProductYear(newProduct.getProductYear());
            product.setPrice(newProduct.getPrice());
            product.setUrl(newProduct.getUrl());
            return repository.save(product);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return repository.save(newProduct);
        });
        //orElseGet là nếu trường hợp ta không tìm thấy product với id truyền vào
        // Thì phải insert mới product
        // If product not found, create a new one
        // Khi sử dụng orElseGet bỏ Optional Class đi
        // bởi giá trị trả về của orElseGet không phải Optional nữa mà là 1 object rồi
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("SUCCESS", "Updated product successfully", updatedProduct));
        //Trường hợp id = 4 mà chưa có product với id = 4 nào trong database gặp bug
        // Vì trường id trong db mặc định là auto_increment nên nếu chỉ có 2 product
        // Nên khi insert new prouduct with id = 4 thì thực tế new product thêm vào có id = 3
    }


    // Delete a product => DELETE method HTTP
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        // method existsById() có sẵn trong JpaRepository
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            // method deleteById available in JpaRepository
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("SUCCESS","Delete a product with id = "+id + " successfully","")
            );
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("FAILED","Can not find the product to delete","")
            );
        }

//        // Rewite with using Ternery Operator
//        return exists ? ResponseEntity.status(HttpStatus.OK).body(repository.deleteById(id),
//                new ResponseObject("SUCCESS","Delete a product with id = "+id + " successfully","")
//        ) :  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                new ResponseObject("FAILED","Can not find the product to delete","")
//        );
    }
}
