package pers.liaohaolong.mokulibserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tools.jackson.databind.ObjectMapper;

/**
 * <h3>结果数据传输对象</h3>
 *
 * <p>Controller -> Frontend</p>
 * <p>后端所有接口均统一返回此类的实体。</p>
 */
@Data
@AllArgsConstructor
public class ResultDTO {

    public static final String OK_STATUS = "OK";
    public static final String ERROR_STATUS = "ERROR";
    public static final String TOO_FREQUENT = "TOO_FREQUENT";

    private String status;

    private String businessType;

    private String message;

    private Object data;

    public String toJson(ObjectMapper objectMapper) {
        return objectMapper.writeValueAsString(this);
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

}
