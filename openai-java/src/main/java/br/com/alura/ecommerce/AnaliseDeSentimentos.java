package br.com.alura.ecommerce;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class AnaliseDeSentimentos {
    public static void main(String[] args) {
        try {
            var system = """
                    Você é um analisador de sentimentos de avaliações de produtos.
                    Escreva um parágrafo com até 50 palavras resumindo as avaliações e depois atribua qual o sentimento geral para o produto.
                    Identifique também 3 pontos fortes e 3 pontos fracos identificados a partir das avaliações.

                    #### Formato de saída
                    Nome do produto:
                    Resumo das avaliações: [resuma em até 50 palavras]
                    Sentimento geral: [deve ser: POSITIVO, NEUTRO ou NEGATIVO]
                    Pontos fortes: [3 bullets points]
                    Pontos fracos: [3 bullets points]
                    """;

            var diretorioAvaliacoes = Path.of("src/main/resources/avaliacoes");

            var arquivosDeAvaliacoes = Files.walk(diretorioAvaliacoes, 1)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .toList();

            arquivosDeAvaliacoes.forEach(arquivo -> {
                System.out.println("Iniciando análise do produto: " + arquivo.getFileName());

                var user = carregarArquivo(arquivo);

                var model = "gpt-3.5-turbo";

                var chatRequest = ChatRequest.builder().model(model).message(ChatMessage.SystemMessage.of(user)).message(ChatMessage.UserMessage.of(system)).temperature(0.0).build();

                var openAI = SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();

                var futureChat = openAI.chatCompletions().create(chatRequest);
                var chatResponse = futureChat.join();

                salvarAnalise(arquivo.getFileName().toString().replace(".txt", ""), chatResponse.firstContent());

                System.out.println("Análise finalizada.");
            });
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao realizar as análises de sentimentos!");
        }
    }

    private static String carregarArquivo(Path arquivo) {
        try {
            return Files.readAllLines(arquivo).toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o arquivo!", e);
        }
    }

    private static void salvarAnalise(String arquivo, String analise) {
        try {
            var path = Path.of("src/main/resources/analises/analise-sentimentos-" + arquivo + ".txt");
            Files.writeString(path, analise, StandardOpenOption.CREATE_NEW);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o arquivo!", e);
        }
    }
}
