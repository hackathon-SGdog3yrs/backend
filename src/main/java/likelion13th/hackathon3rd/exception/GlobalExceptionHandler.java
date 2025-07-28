package likelion13th.hackathon3rd.exception;

import likelion13th.hackathon3rd.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 모임을 찾을 수 없는 경우 (404)
    @ExceptionHandler(MeetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMeetNotFoundException(
            MeetNotFoundException ex, WebRequest request) {
        
        log.error("모임을 찾을 수 없음: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "API 없음",
                "서버에 문제가 발생했습니다. 잠시후 다시 시도해주세요.",
                getPath(request)
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // 잘못된 요청 (400)
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(
            InvalidRequestException ex, WebRequest request) {
        
        log.error("잘못된 요청: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "잘못된 실패",
                "가입되지 않은 계정입니다.",
                getPath(request)
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 인증 실패 (401)
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleSecurityException(
            SecurityException ex, WebRequest request) {
        
        log.error("인증 실패: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                "인증 실패",
                "인증되지 않았습니다. 올바른 인증 정보를 제공해주세요.",
                getPath(request)
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // 요청 값이 잘못된 경우 (419)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        
        log.error("잘못된 요청 파라미터: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                419,
                "인증 오류",
                "인증이 만료되었습니다. 다시 로그인해주세요.",
                getPath(request)
        );
        
        return ResponseEntity.status(419).body(errorResponse);
    }

    // 서버 내부 오류 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex, WebRequest request) {
        
        log.error("서버 내부 오류: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 오류",
                "서버에 문제가 발생했습니다. 잠시후 다시 시도해주세요.",
                getPath(request)
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // 서비스 일시 정지 (503)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        // 특정 런타임 예외가 아닌 경우에만 503으로 처리
        if (ex instanceof MeetNotFoundException || ex instanceof InvalidRequestException) {
            throw ex; // 다른 핸들러에서 처리하도록
        }
        
        log.error("서비스 일시 정지: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "서버 오류",
                "서비스 점검 중입니다. 잠시 후 다시 시도해주세요.",
                getPath(request)
        );
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    // 요청 경로 추출
    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}