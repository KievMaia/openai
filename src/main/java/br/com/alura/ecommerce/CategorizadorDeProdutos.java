package br.com.alura.ecommerce;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.Arrays;

public class CategorizadorDeProdutos {
    public static void main(String[] args) {
        var user = "Me dê sugestões de nomes de categorias para o produto Escova de Dentes";
        var system = "Você é um categorizador de produtos e deve gerar sugestões de nome de categoria. ";

        var chave = System.getenv("OPENAI_API_KEY");
        var service = new OpenAiService(chave);

        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-16k-0613")
                .messages(Arrays.asList(new ChatMessage(ChatMessageRole.USER.value(), user),
                                        new ChatMessage(ChatMessageRole.SYSTEM.value(), system)))
                .n(5)
                .build();

        service
                .createChatCompletion(completionRequest)
                .getChoices()
                .forEach(
                        c -> System.out.println(c.getMessage().getContent()));
    }
}
