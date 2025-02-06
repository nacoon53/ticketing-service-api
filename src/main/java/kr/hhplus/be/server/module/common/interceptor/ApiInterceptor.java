package kr.hhplus.be.server.module.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.module.auth.application.usecase.WaitListTokenUsecase;
import kr.hhplus.be.server.module.auth.presentation.dto.WaitListTokenValidationResponseDTO;
import kr.hhplus.be.server.module.common.error.code.ErrorCode;
import kr.hhplus.be.server.module.common.error.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApiInterceptor implements HandlerInterceptor {
    private final WaitListTokenUsecase waitListTokenUsecase;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception{
        log.info("request Headers: {}", getHeadersAsString(request));
        String userId = (String) request.getHeader("USER_ID");
        String token = request.getHeader("WAITLIST_TOKEN");

        if(StringUtils.isEmpty(userId)) {
            throw new ApiException(ErrorCode.NOT_FOUND_USER);
        }

        //토큰이 없다면 토큰 발급
        if(StringUtils.isEmpty(token)) {
            token = waitListTokenUsecase.issueToken(userId);
            response.setHeader("WAITLIST_TOKEN", token);
        }

        //토큰의 대기열 순서 조회(+토큰 유효성 검사)
        WaitListTokenValidationResponseDTO waitListToken = waitListTokenUsecase.checkTokenAndGetWaitNumber(userId, token);
        if(waitListToken.isCanAccess()) {
            request.setAttribute("token", token);
            return true;
        }

        String errorMessage = String.format("%s(대기 순서: %d)", ErrorCode.NOT_YOUR_TURN.getMessage(), waitListToken.getWaitNumber());
        response.setStatus(ErrorCode.NOT_YOUR_TURN.getHttpStatus().value()); // 403 Forbidden
        response.setContentType("application/json");
        response.getWriter().write("{\"msg\":\""+ errorMessage + "}");

        return false;
    }

    private String getHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(header -> {
            headers.append(header).append(": ").append(request.getHeader(header)).append("\n");
        });
        return headers.toString();
    }
}
