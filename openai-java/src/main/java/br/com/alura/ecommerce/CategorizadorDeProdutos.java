package br.com.alura.ecommerce;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import java.util.Scanner;

public class CategorizadorDeProdutos {
    public static void main(String[] args) {
        var leitor = new Scanner(System.in);

        System.out.println("Digite as categorias válidas.");
        var categorias = leitor.nextLine();

        while (true) {
            System.out.println("Digite o nome do produto");
            var user = leitor.nextLine();


            var system = """
                    Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado

                    Escolha uma categoria dentra a lista abaixo:

                    %s

                    ###### exemplo de uso:

                    Pergunta: Bola de futebol
                    Resposta: Esportes

                    ###### regras a serem seguidas:
                    Caso o usuario pergunte algo que nao seja de categorizacao de produtos, voce deve responder que nao pode ajudar pois o seu papel é apenas responder a categoria dos produtos
                    """.formatted(categorias);

            disparaRequisicao(user, system);
        }
    }

    private static void disparaRequisicao(String user, String system) {
        var chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(ChatMessage.SystemMessage.of(user))
                .message(ChatMessage.UserMessage.of(system))
                .temperature(0.0)
                .maxCompletionTokens(300)
                .n(5)
                .build();

        var openAI = SimpleOpenAI.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();

        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }
}
