package com.project.common.exception.helper;


import com.project.common.annotation.FrontendColumnName;
import com.project.common.enums.CommonCode;
import com.project.common.enums.CommonStatus;
import com.project.common.response.ApiRes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationExceptionHelper {

    public static final String NOT_BLANK = "NotBlank";
    public static final String NOT_NULL = "NotNull";
    public static final String SIZE = "Size";
    public static final String DIGITS = "Digits";
    public static final String PATTERN = "Pattern";

    // RequestBody 的驗證會來這邊
    public static <T extends ApiRes> T methodArgumentNotValidExceptionHandle(T resp, MethodArgumentNotValidException e) {
        FieldError fieldError = getFirstFieldErrors(e);

        String fieldName = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();
        String validateAnnotation = fieldError.getCode();
        Object[] arguments = fieldError.getArguments();

        String message = "";
        CommonCode commonCode = null;
        CommonStatus commonStatus = null;

        fieldName = getFieldName(e.getParameter().getParameterType(), fieldName);

        if (NOT_BLANK.equals(validateAnnotation) || (NOT_NULL.equals(validateAnnotation))) {
            commonCode = CommonCode.N40001;
            commonStatus = CommonStatus.ERROR;
            message = commonCode.makeMessage(new String[]{fieldName});
        } else if (DIGITS.equals(validateAnnotation)) {
            commonCode = CommonCode.N40002;
            commonStatus = CommonStatus.ERROR;
            List<String> digits = extractDigits(defaultMessage);
            message = commonCode.makeMessage(new String[]{fieldName + "做多只能 " + digits.get(0) + " 位數整數與 " + digits.get(1) + " 位數小數"});
        } else if (SIZE.equals(validateAnnotation)) {
            commonCode = CommonCode.N40002;
            commonStatus = CommonStatus.ERROR;
            // arguments[1]：max
            // arguments[2]：min
            if (arguments[1] == arguments[2]) {
                message = commonCode.makeMessage(new String[]{fieldName + " " + "長度必須等於" + " " + arguments[1]});
            } else if (arguments[1] != arguments[2] && !arguments[2].toString().equals("0") && !arguments[2].toString().equals("2147483647")) {
                message = commonCode.makeMessage(new String[]{fieldName + " " + "長度必須介於" + " " + arguments[2] + " 至 " + arguments[1]});
            } else if (arguments[1] != arguments[2] && !arguments[2].toString().equals("0")) {
                message = commonCode.makeMessage(new String[]{fieldName + " " + "長度必須大於" + " " + arguments[2]});
            } else {
                message = commonCode.makeMessage(new String[]{fieldName + " " + "長度必須小於" + " " + arguments[1]});
            }
        } else if (PATTERN.equals(validateAnnotation)) {
            commonCode = CommonCode.N40002;
            commonStatus = CommonStatus.ERROR;
            message = commonCode.makeMessage(new String[]{fieldName});
        } else {
            commonCode = CommonCode.N40002;
            commonStatus = CommonStatus.ERROR;
            message = commonCode.makeMessage(new String[]{fieldName + " " + defaultMessage});
        }

        setCommonResponse(resp, commonCode, commonStatus, message);

        return resp;
    }

    /**
     * 抓取順序：
     * 1.FrontendColumnName
     * 2.schema.description
     * 3.變數名稱
     */
    private static String getFieldName(Class<?> parameterType, String fieldName) {
        try {
            if (fieldName.contains("[")) {
                return handleCollectionField(parameterType, fieldName);
            } else {
                return resolveFieldName(parameterType, fieldName);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String handleCollectionField(Class<?> parameterType, String fieldName) throws NoSuchFieldException {
        String collectionFieldName = fieldName.substring(0, fieldName.indexOf('['));
        String remainingPath = fieldName.substring(fieldName.indexOf(']') + 2);

        Field collectionField = parameterType.getDeclaredField(collectionFieldName);

        if (Collection.class.isAssignableFrom(collectionField.getType())) {
            Class<?> elementClass = extractGenericType(collectionField);
            return getFieldName(elementClass, remainingPath);
        }
        return null;
    }

    private static Class<?> extractGenericType(Field collectionField) {
        Type genericFieldType = collectionField.getGenericType();
        if (genericFieldType instanceof ParameterizedType pt) {
            return (Class<?>) pt.getActualTypeArguments()[0];
        }
        return null;
    }

    private static String resolveFieldName(Class<?> parameterType, String fieldName) throws NoSuchFieldException {
        Field field = parameterType.getDeclaredField(fieldName);
        if (field.isAnnotationPresent(FrontendColumnName.class)) {
            return field.getAnnotation(FrontendColumnName.class).value();
        } else if(field.isAnnotationPresent(Schema.class)) {
            return field.getAnnotation(Schema.class).description();
        }
        return fieldName;
    }

    // RequestParam 的驗證會來這邊 (參數有帶才會抓)
    public static <T extends ApiRes> T constraintViolationExceptionHandle(T resp, ConstraintViolationException e) {
        //取出違反約束的對象
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = null;
        //只針對第一個進行處理,方便控管自定例外訊息
        violation = violations.stream().findFirst().orElse(null);

        if (violation == null) {
            CommonCode code = CommonCode.ERROR;
            CommonStatus commonStatus = CommonStatus.ERROR;
            resp.setStatus(commonStatus.getStatus());
            resp.setCode(code.getCode());
            resp.setMessage(code.getMsg());
            resp.setResult(new HashMap<>());
            return resp;
        }
        //propertyPath="呼叫的方法名" + "." + "參數名"
        //將參數名取出,以及對傳入list時進行特別處理
        String propertyPath = violation.getPropertyPath().toString();
        String propertyName = propertyPath.substring(propertyPath.lastIndexOf(".") + 1);
        int lastIndex = propertyPath.lastIndexOf(".<list element>");
        if (lastIndex != -1) {
            propertyName = propertyPath.substring(0, lastIndex);
            propertyName = propertyName.substring(propertyName.lastIndexOf(".") + 1);
        }
        //取出違反的註解名稱
        String constraintType = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
        //自訂例外邏輯,使用註解名稱判斷
        String message = "";
        CommonCode code = null;
        if (NOT_BLANK.equals(constraintType)) {
            code = CommonCode.N40001;
            message = code.makeMessage(new String[]{propertyName});
        } else {
            code = CommonCode.N40002;
            message = code.makeMessage(new String[]{propertyName + " " + violation.getMessage()});
        }
        CommonStatus commonStatus = CommonStatus.ERROR;
        resp.setStatus(commonStatus.getStatus());
        resp.setCode(code.getCode());
        resp.setMessage(message);
        resp.setResult(new HashMap<>());
        return resp;
    }

    /**
     * Spring validation 預設會抓全部沒有驗證過的，我們這邊只抓第一個
     *
     */
    private static FieldError getFirstFieldErrors(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.get(0);
    }

    private static List<String> extractDigits(String input) {
        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("<(.*?) digits>");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            resultList.add(matcher.group(1));
        }
        return resultList;
    }

    private static void setCommonResponse(ApiRes<HashMap<Object, Object>> resp, CommonCode code, CommonStatus commonStatus, String message) {
        resp.setStatus(commonStatus.getStatus());
        resp.setCode(code.getCode());
        resp.setMessage(message);
        resp.setResult(new HashMap<>());
    }
}
