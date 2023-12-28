# Simple OpenAI Client

A simple Java library for seamless integration of your Java applications with OpenAI API.

## Supported Endpoints
- [Chat Completion](https://platform.openai.com/docs/api-reference/chat/create)

## Installation

### Maven

```xml
<dependency>
    <groupId>br.com.rcaneppele</groupId>
    <artifactId>simple-openai-client</artifactId>
    <version>1.0.3</version>
</dependency>
```

### Gradle

`implementation 'br.com.rcaneppele:simple-openai-client:1.0.3'`

## Usage

### Chat Completion

```java
var client = new OpenAIClient("YOUR_API_KEY");

var request = new ChatCompletionRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .systemMessage("You are a Sustainable product name generator")
    .userMessage("Generate 2 products name")
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
response.choices().forEach(c -> System.out.println(c.messageContent()));
```

### Chat Completion Parameters

The [`ChatCompletionRequestBuilder`](src/main/java/br/com/rcaneppele/openai/chatcompletion/request/ChatCompletionRequestBuilder.java) object has methods to set API Parameters:

```java
var request = new ChatCompletionRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .systemMessage("You are a Sustainable product name generator")
    .userMessage("Generate 2 products name")
    .n(3)
    .maxTokens(2048)
    .temperature(1.3)
    .frequencyPenalty(1.4)
    .seed(123)
    .user("user-id")
    .build();

var response = client.sendChatCompletionRequest(request);

response.choices().forEach(c -> System.out.println(c.messageContent()));
```

### Timeout

The default OpenAI API response timeout is **15 seconds**. If you need to change it:

```java
// 30 seconds timeout
var client = new OpenAIClient("YOUR_API_KEY", 30);

// No timeout
var client = new OpenAIClient("YOUR_API_KEY", 0);
```

### API Key

To send requests, generate an [API KEY](https://platform.openai.com/api-keys)

**ATTENTION!** The API key is sensitive and confidential information; do not use it directly in your code. Instead, use **Environment Variables** to protect your key:

```java
// Assuming you have an environment variable named OPENAI_API_KEY
var apiKey = System.getenv("OPENAI_API_KEY");
var client = new OpenAIClient(apiKey);
```

### Streaming

If you require [Streaming support](https://platform.openai.com/docs/api-reference/streaming), you can use the library as shown below:

```java
var request = new ChatCompletionRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .systemMessage("You are a Sustainable product name generator")
    .userMessage("Generate 2 products name")
    .build();

client.sendStreamChatCompletionRequest(request).subscribe(response ->
    {
        var message = response.firstChoiceMessageContent();
        if (message != null) {
            System.out.println(message);
        }
    });
```

Optionally, you can listen to events such as **errors** and **completion** during streaming:

```java
client.sendStreamChatCompletionRequest(request).subscribe(response ->
    {
        var message = response.firstChoiceMessageContent();
        if (message != null) {
            System.out.println(message);
        }
    }, error -> {
        System.out.println("Error during streaming: " +error.getMessage());
    }, () -> {
        System.out.println("Streaming completed");
    });
```
