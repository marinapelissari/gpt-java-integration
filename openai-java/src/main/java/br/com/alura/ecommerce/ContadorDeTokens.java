package br.com.alura.ecommerce;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;

import java.math.BigDecimal;

public class ContadorDeTokens {
    public static void main(String[] args) {
        var text = "Identifique o perfil de compra de cada cliente";

        int tokenCount = tokenCount(text);

        var cost = new BigDecimal(tokenCount).divide(new BigDecimal("1000")).multiply(new BigDecimal("0.0010"));

        System.out.println(cost);
    }

    public static int tokenCount(String text) {
        var registry = Encodings.newDefaultEncodingRegistry();

        var encoding = registry.getEncodingForModel(ModelType.GPT_4O_MINI);

        return encoding.countTokens(text);
    }
}
