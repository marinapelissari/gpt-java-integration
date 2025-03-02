package br.com.alura.ecommerce;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.base.RealtimeConfig;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

public class TestaIntegracao {
    public static void main(String[] args) {
        var user = "Gere 5 produtos";
        var system = "Você é um gerador de produtos fictícios para um ecommerce e deve gerar apenas o nome dos produtos solicitados pelo usuário";
        var model = "gpt-4o-mini";

        var chatRequest = ChatRequest.builder()
                .model(model)
                .message(ChatMessage.SystemMessage.of(user))
                .message(ChatMessage.UserMessage.of(system))
                .temperature(0.0)
                .maxCompletionTokens(300)
                .build();

        var openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();

        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }
}
