package br.com.alura.ecommerce;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;

public class IdentificadorDePerfil {
    public static void main(String[] args) {
        final var system = """
                Identifique o perfil de compra de cada cliente.
                
                #####
                A resposta deve ser:
                                
                Cliente - descreva o perfil do cliente em três palavras
                #####
                """;

        final String clientes = carregarClientesDoArquivo();
        final var chave = System.getenv("OPENAI_API_KEY");
        final var service = new OpenAiService(chave, Duration.ofSeconds(60));

        var quantidadeTokens = contarTokens(clientes);

        var modelo = "gpt-3.5-turbo";
        var tamanhoRespostaEsperada = 2048;
        if (quantidadeTokens > 4096 - tamanhoRespostaEsperada) {
            modelo = "gpt-3.5-turbo-16k";
        }
        System.out.println("Quantidade de tokens " + quantidadeTokens);
        System.out.println("Modelo escolhido " + modelo);

        final var request = ChatCompletionRequest.builder()
                .model(modelo)
                .messages(Arrays.asList(new ChatMessage(ChatMessageRole.SYSTEM.value(), system),
                                        new ChatMessage(ChatMessageRole.USER.value(), clientes)))
                .build();

        System.out.println(service.createChatCompletion(request).getChoices().get(0).getMessage().getContent());
    }

    private static int contarTokens(final String prompt) {
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_3_5_TURBO);
        return enc.countTokens(prompt);
    }

    private static String carregarClientesDoArquivo() {
        try {
            final var path = Path.of(ClassLoader.getSystemResource("compras/lista_de_compras_10_clientes.csv").toURI());
            return Files.readAllLines(path).toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar arquivo", e);
        }
    }
}
