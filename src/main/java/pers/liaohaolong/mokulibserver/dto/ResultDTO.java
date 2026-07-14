package pers.liaohaolong.mokulibserver.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <h3>结果数据传输对象</h3>
 *
 * <p>后端 -> 前端</p>
 * <p>后端所有接口均统一返回此类的实体。</p>
 */
@Data
@AllArgsConstructor
public class ResultDTO {

    public static final String OK_STATUS = "OK";
    public static final String ERROR_STATUS = "ERROR";
    public static final String TOO_FREQUENT = "TOO_FREQUENT";

    public static final ResultDTO OK = new ResultDTO(OK_STATUS, "", "", null);
    public static final ResultDTO ERROR = new ResultDTO(ERROR_STATUS, "", "", null);
    public static final ResultDTO BAD_REQUEST = new ResultDTO(ERROR_STATUS, "", "请求错误", null);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String status;

    private String businessType;

    private String message;

    private Object data;

    public String toJson() throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(this);
    }

    public static ResultBuilder builder() {
        return new ResultBuilder();
    }

    public static ResultBuilder ok() {
        return new ResultBuilder().status(OK_STATUS);
    }

    public static ResultBuilder error() {
        return new ResultBuilder().status(ERROR_STATUS);
    }

    /**
     * 构建器
     */
    public static class ResultBuilder {

        private String status;
        private String businessType;
        private String message;
        private Object data;

        public ResultBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ResultBuilder businessType(String businessType) {
            this.businessType = businessType;
            return this;
        }

        public ResultBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResultBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public ResultDTO build() {
            return new ResultDTO(status, businessType, message, data);
        }

    }

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

}
