package com.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

    private T data;
    private String errorMessage;
    private long timestamp;

    public ResponseData(T data) {
        this.data = data;
        this.errorMessage = "";
        this.timestamp = System.currentTimeMillis();
    }
    public ResponseData(String errorMessage) {
        this.data = null;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResponseEntity<ResponseData<T>> success200(T data) {
        return ResponseEntity.ok(new ResponseData<>(data));
    }

    public static <T> ResponseEntity<ResponseData<T>> success201(T data) {
        return new ResponseEntity<>(new ResponseData<>(data), HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ResponseData<T>> success202(T data) {
        return new ResponseEntity<>(new ResponseData<>(data), HttpStatus.ACCEPTED);
    }

    public static <T> ResponseEntity<ResponseData<T>> error500(String errorMessage) {
        return new ResponseEntity<>(new ResponseData<>(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<ResponseData<T>> errorStatus(String errorMessage, HttpStatus status) {
        return new ResponseEntity<>(new ResponseData<>(errorMessage), status);
    }

    public static <T> ResponseEntity<ResponseData<T>> notFoundData(String errorMessage) {
        return new ResponseEntity<>(new ResponseData<>(errorMessage), HttpStatus.NOT_FOUND);
    }

    public static <T> ResponseEntity<ResponseData<T>> alreadyExists(String errorMessage) {
        return new ResponseEntity<>(new ResponseData<>(errorMessage), HttpStatus.CONFLICT);
    }

}
