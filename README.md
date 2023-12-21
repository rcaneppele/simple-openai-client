# Simple OpenAI Client

A simple Java library for seamless integration of your Java applications with OpenAI API.

## Supported Endpoints
- [Chat Completion](https://platform.openai.com/docs/api-reference/chat/create)

## Usage

### Chat Completion

```java
var client = new OpenAIClient("YOUR_API_KEY");

var request = new ChatCompletionRequestBuilder()
        .model(OpenAIModel.GPT_4_1106_PREVIEW)
        .systemMessage("You are a sustainable product generator")
        .userMessage("Generate 5 product names")
        .build();

var response = client.sendChatCompletionRequest(request);

System.out.println(response);
```

The response is an object of type [`ChatCompletionResponse`](src/main/java/br/com/rcaneppele/openai/chatcompletion/response/ChatCompletionResponse.java).

If you only need to print the generated message content:

```java
// First choice
System.out.println(response.firstChoiceMessageContent());

// Last choice
System.out.println(response.lastChoiceMessageContent());

// All choices
response.choices().forEach(c -> System.out.println(c.message().content()));
```

### Chat Completion Parameters

The [`ChatCompletionRequestBuilder`](src/main/java/br/com/rcaneppele/openai/chatcompletion/request/ChatCompletionRequestBuilder.java) object has methods to set API Parameters:

```java
var request = new ChatCompletionRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .userMessage("Generate 5 product name")
    .systemMessage("You are a Sustainable product name generator")
    .n(3)
    .maxTokens(2048)
    .temperature(1.3)
    .frequencyPenalty(1.4)
    .build();

var response = client.sendChatCompletionRequest(request);

response.choices().forEach(c -> System.out.println(c.message().content()));
```

### Timeout

The default OpenAI API response timeout is **15 seconds**. If you need to change it:

```java
var myTimeout = Duration.ofSeconds(30);
var client = new OpenAIClient("YOUR_API_KEY", myTimeout);
```

### API Key

To send requests, generate an [API KEY](https://platform.openai.com/api-keys)

**ATTENTION!** The API key is sensitive and confidential information; do not use it directly in your code. Instead, use **Environment Variables** to protect your key:

```java
// Assuming you have an environment variable named OPENAI_API_KEY
var apiKey = System.getenv("OPENAI_API_KEY");
var client = new OpenAIClient(apiKey);
```
