package br.com.alura.ecommerce;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import java.nio.file.Files;
import java.nio.file.Path;

public class IdentificadorDePerfil {
    public static void main(String[] args) {
        var system = """
                Identifique o perfil de compra de cada cliente.
                                
                A resposta deve ser:
                                
                Cliente - descreva o perfil do cliente em três palavras
                """;

        var user = carregarClientesDoArquivo();

        disparaRequisicao(user, system);
    }

    private static void disparaRequisicao(String user, String system) {
        var totalTokens = ContadorDeTokens.tokenCount(user);

        String model;

        if (totalTokens > 16000) {
            model = "gpt-4-turbo";
        } else {
            model = "gpt-3.5-turbo";
        }

        System.out.println("Quantidade de tokens: " + totalTokens);
        System.out.println("Modelo escolhido: " + model);

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

    private static String carregarClientesDoArquivo() {
        try {
            var path = Path.of(ClassLoader
                    .getSystemResource("lista_de_compras_500_clientes.csv")
                    .toURI());
            return Files.readAllLines(path).toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o arquivo!", e);
        }
    }
}
