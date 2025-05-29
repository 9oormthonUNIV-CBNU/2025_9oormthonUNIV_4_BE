package goormthon_group4.backend.global.common.exception;

import org.springframework.http.HttpStatus;


public interface BaseErrorCode {
  HttpStatus getHttpStatus();
  String getCode();
  String getMessage();
}