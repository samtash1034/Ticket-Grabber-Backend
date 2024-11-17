package com.project.common.aspect;

import com.project.common.enums.CommonCode;
import com.project.common.enums.CommonStatus;
import com.project.common.exception.BaseException;
import com.project.common.response.ApiRes;
import com.project.common.util.MessageFormatterUtil;
import com.project.common.util.UUIDUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;

@Slf4j
@Aspect
@Component
public class ManageAspect {

    private static final String RES_ERROR_MESSAGE = "伺服器錯誤，請聯繫管理員，錯誤識別碼:%s";
    private static final String LOG_ERROR_MESSAGE = "錯誤識別碼: %s%n%s";

    // 修改 Pointcut，避免直接引用 user 和 article 模組的包名
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controllerPoint() {
    }

    /**
     * 1. 標準化回應
     * 2. 錯誤訊息處理
     * 3. 統計 API 使用時間
     * 4. 驗證 token
     *
     * @param pjp 切面連接點
     * @return 標準化的 API 回應
     * @throws Throwable 處理過程中的異常
     */
    @Around("controllerPoint()")
    @Order(9)
    public Object doAroundAccessCheck(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        ApiRes apiRes = new ApiRes();
        HttpServletResponse response = getCurrentHttpResponse();
        stopWatch.start();

        try {
            apiRes = handleProceedResult(apiRes, pjp.proceed());
            setSuccessResponse(apiRes);
        } catch (BaseException e) {
            log.error(e.getMessage(), e);
            setResponseStatusCode(e, response);

            String message = MessageFormatterUtil.formatMessage(e.getCode().getMsg(), e.getArgs());
            handleBaseExceptionResponse(apiRes, e, message);
        } catch (Exception e) {
            String errorUuid = UUIDUtil.generateUuid();
            String resErrorMessage = String.format(RES_ERROR_MESSAGE, errorUuid);
            log.error(String.format(LOG_ERROR_MESSAGE, errorUuid, e.getMessage()), e);

            response.setStatus(500);
            handleOtherExceptionResponse(apiRes, resErrorMessage);
        } finally {
            stopWatch.stop();
            log.info("[API Process] Controller 名稱: {}, 執行時間 (納秒): {}", pjp.getSignature().getName(), stopWatch.getNanoTime());
        }

        return apiRes;
    }

    private HttpServletResponse getCurrentHttpResponse() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return ((ServletRequestAttributes) attributes).getResponse();
    }

    private ApiRes handleProceedResult(ApiRes apiRes, Object result) {
        if (result instanceof ApiRes responseResult) {
            apiRes = responseResult;
        } else {
            apiRes.setResult(new HashMap<>());
        }
        return apiRes;
    }

    private void setSuccessResponse(ApiRes apiRes) {
        CommonCode commonCode = CommonCode.SUCCESS;
        CommonStatus commonStatus = CommonStatus.SUCCESS;
        apiRes.setStatus(commonStatus.getStatus());
        apiRes.setCode(commonCode.getCode());
        apiRes.setMessage(commonCode.getMsg());
    }

    private void handleBaseExceptionResponse(ApiRes apiRes, BaseException e, String msg) {
        handleExceptionResponse(apiRes, e.getCode().getCode(), msg, CommonStatus.ERROR.getStatus());
    }

    private void handleOtherExceptionResponse(ApiRes apiRes, String resErrorMessage) {
        handleExceptionResponse(apiRes, CommonCode.ERROR.getCode(), resErrorMessage, CommonStatus.ERROR.getStatus());
    }

    private void handleExceptionResponse(ApiRes apiRes, int errorCode, String message, String errorStatus) {
        apiRes.setStatus(errorStatus);
        apiRes.setCode(errorCode);
        apiRes.setMessage(message);
        apiRes.setResult(new HashMap<>());
    }

    private void setResponseStatusCode(BaseException e, HttpServletResponse response) {
        CommonCode code = e.getCode();
        int status = getFirstThreeDigits(code.getCode());
        response.setStatus(status);
    }

    private int getFirstThreeDigits(int number) {
        int absNumber = Math.abs(number);
        String numberStr = String.valueOf(absNumber);
        String firstThreeDigits = numberStr.length() > 3 ? numberStr.substring(0, 3) : numberStr;
        return Integer.parseInt(firstThreeDigits);
    }
}
