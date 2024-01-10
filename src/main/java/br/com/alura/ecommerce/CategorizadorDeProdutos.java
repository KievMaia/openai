package br.com.alura.ecommerce;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import javax.swing.*;
import java.time.Duration;
import java.util.Arrays;

public class CategorizadorDeProdutos {
    public static void main(String[] args) {
        var categorias = JOptionPane.showInputDialog(
                null, "Digite as categorias válidas:", "Entrada de Texto", JOptionPane.QUESTION_MESSAGE);

        while (true) {
            var user = JOptionPane.showInputDialog(
                    null, "Digite o nome do produto:", "Entrada de Texto", JOptionPane.QUESTION_MESSAGE);
            var system = """
                    Você é um categorizador de produtos e deve gerar sugestões de nome da categoria do produto informado.
                                    
                    Escolha uma categoria dentre a lista abaixo:
                                    
                    %s
                                    
                    ###### exemplo de uso:
                                    
                    Pergunta: Bola de futebol
                    Resposta: Esportes
                                    
                    ###### regras a serem seguidas:
                    Caso o usuário pergunte algo que não seja de categorização de produtos você deve responder que não pode ajudar,
                    pois  seu papel é somente responder a categoria dos produtos.
                    """.formatted(categorias);

            dispararRequisicao(user, system);
        }
    }

    private static void dispararRequisicao(final String user, final String system) {
        var chave = System.getenv("OPENAI_API_KEY");
        var service = new OpenAiService(chave, Duration.ofSeconds(30));

        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-16k-0613")
                .messages(Arrays.asList(new ChatMessage(ChatMessageRole.USER.value(), user),
                                        new ChatMessage(ChatMessageRole.SYSTEM.value(), system)))
                .build();

        service
                .createChatCompletion(completionRequest)
                .getChoices()
                .forEach(c -> System.out.println(c.getMessage().getContent()));
    }
}
