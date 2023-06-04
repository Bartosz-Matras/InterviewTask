package pl.matrasbartosz.zadanieatipera.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.matrasbartosz.zadanieatipera.entity.GitHubResponse;
import pl.matrasbartosz.zadanieatipera.exceptions.UsernameNotFoundException;

@ControllerAdvice
public class GitHubExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<GitHubResponse> handleUsernameNotExistsException(UsernameNotFoundException e) {
        return new ResponseEntity<>(new GitHubResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<GitHubResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(new GitHubResponse(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

}
