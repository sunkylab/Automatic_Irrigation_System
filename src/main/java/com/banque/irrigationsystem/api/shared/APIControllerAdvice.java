package com.banque.irrigationsystem.api.shared;

import com.banque.irrigationsystem.core.exceptions.AppBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@ControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler({AppBaseException.class,Exception.class,MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public final ResponseEntity<?> handleException(Exception ex, WebRequest request) {

        if (ex instanceof AppBaseException) {
            return new ResponseEntity<>(ApiResponse.builder()
                    .code(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.OK);
        }else if(ex instanceof MethodArgumentNotValidException){

            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;

            List<String> errors = e.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            return new ResponseEntity<>(ApiResponse.builder()
                    .code(-1)
                    .errors(errors)
                    .message("failed")
                    .build(), HttpStatus.BAD_REQUEST);
        }else if(ex instanceof HttpMessageNotReadableException){
            return new ResponseEntity<>(ApiResponse.builder()
                    .code(-1)
                    .message(ex.getCause().getLocalizedMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }else{
            log.error("APIControllerAdvice :: error is {}",ex.getMessage());

            return new ResponseEntity<>(ApiResponse.builder()
                    .code(-1)
                    .message(ex.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
