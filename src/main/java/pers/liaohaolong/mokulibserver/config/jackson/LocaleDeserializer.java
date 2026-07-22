package pers.liaohaolong.mokulibserver.config.jackson;

import com.fasterxml.jackson.core.JsonTokenId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JacksonComponent;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.type.LogicalType;

import java.util.IllformedLocaleException;
import java.util.Locale;

@Slf4j
@JacksonComponent
public class LocaleDeserializer extends StdDeserializer<Locale> {

    public LocaleDeserializer() {
        super(Locale.class);
    }

    @Override
    public LogicalType logicalType() {
        return LogicalType.Textual;
    }

    @Override
    public Object getEmptyValue(DeserializationContext ctxt) {
        // 默认中文
        return Locale.CHINESE;
    }

    @Override
    public Locale deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        String text;
        if (p.currentTokenId() == JsonTokenId.ID_STRING) {
            text = p.getString();
        } else {
            return (Locale) ctxt.handleUnexpectedToken(getValueType(ctxt), p);
        }

        final CoercionAction act = _checkFromStringCoercion(ctxt, text);
        if (act == CoercionAction.AsNull) {
            return (Locale) getNullValue(ctxt);
        }
        if (act == CoercionAction.AsEmpty) {
            return (Locale) getEmptyValue(ctxt);
        }
        text = text.trim();
        if (_hasTextualNull(text)) {
            return (Locale) getNullValue(ctxt);
        }

        // 使用 Builder 模式严格校验
        try {
            return new Locale.Builder().setLanguage(text).build();
        } catch (IllformedLocaleException ignore) {
        }

        return (Locale) ctxt.handleWeirdStringValue(_valueClass, text,
                "not a valid representation");
    }

}
