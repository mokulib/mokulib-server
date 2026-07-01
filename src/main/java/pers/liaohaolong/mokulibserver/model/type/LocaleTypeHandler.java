package pers.liaohaolong.mokulibserver.model.type;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.jspecify.annotations.NonNull;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.IllformedLocaleException;
import java.util.Locale;

@Slf4j
public class LocaleTypeHandler extends BaseTypeHandler<Locale> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Locale parameter, JdbcType jdbcType) throws SQLException {
        // 序列化
        ps.setString(i, parameter.getLanguage());
    }

    @Override
    public Locale getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 反序列化（从结果集中按列名获取值）
        return toLocale(rs.getString(columnName));
    }

    @Override
    public Locale getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 反序列化（从结果集中按列索引获取值）
        return toLocale(rs.getString(columnIndex));
    }

    @Override
    public Locale getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 反序列化（从存储过程中获取值）
        return toLocale(cs.getString(columnIndex));
    }

    /**
     * 将语言代码字符串转换为 Locale 对象
     *
     * @param code 语言代码字符串
     * @return 对应的 Locale 对象，任何无法转换的情况，均返回 {@link Locale#CHINESE}
     */
    private @NonNull Locale toLocale(String code) {
        if (code == null || code.isBlank())
            return Locale.CHINESE;

        Locale locale;

        try {
            locale = new Locale.Builder().setLanguage(code).build();
        } catch (IllformedLocaleException e) {
            log.error("Invalid locale code: {}", code);
            locale = Locale.CHINESE;
        }

        return locale;
    }

}
