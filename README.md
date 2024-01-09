# Simple OpenAI Client

A **simple** Java library for seamless integration of your Java applications with OpenAI API.

## Supported Endpoints
- [Chat](https://platform.openai.com/docs/api-reference/chat)
- [Assistant](https://platform.openai.com/docs/api-reference/assistants)
- [Threads](https://platform.openai.com/docs/api-reference/threads)

In development:
- [Messages](https://platform.openai.com/docs/api-reference/messages)
- [Runs](https://platform.openai.com/docs/api-reference/runs)

## Installation

### Maven

```xml
<dependency>
    <groupId>br.com.rcaneppele</groupId>
    <artifactId>simple-openai-client</artifactId>
    <version>1.3.0</version>
</dependency>
```

### Gradle

`implementation 'br.com.rcaneppele:simple-openai-client:1.3.0'`

### Java version support

To utilize the library, your project needs to be built with Java 17 or a later version.

## Usage

### Chat Completion

```java
var client = new OpenAIClient("YOUR_API_KEY");

var request = new ChatCompletionRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .systemMessage("You are a sustainable product name generator")
    .userMessage("Please generate two product names")
    .build();

var response = client.chatCompletion(request);
System.out.println(response);
```

The response is an object of type [`ChatCompletion`](src/main/java/br/com/rcaneppele/openai/endpoints/chatcompletion/response/ChatCompletion.java).

If you only need to get the generated message content:

```java
// First choice
System.out.println(response.firstChoiceMessageContent());

// Last choice
System.out.println(response.lastChoiceMessageContent());

// All choices
response.choices().forEach(c -> System.out.println(c.messageContent()));
```

#### Chat Completion Parameters

The [`ChatCompletionRequestBuilder`](src/main/java/br/com/rcaneppele/openai/endpoints/chatcompletion/request/ChatCompletionRequestBuilder.java) object has methods to set API parameters:

```java
var request = new ChatCompletionRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .systemMessage("You are a sustainable product name generator")
    .userMessage("Please generate two product names")
    .n(3)
    .maxTokens(2048)
    .temperature(1.3)
    .frequencyPenalty(1.4)
    .seed(123)
    .user("user-id")
    .build();

var response = client.chatCompletion(request);
response.choices().forEach(c -> System.out.println(c.messageContent()));
```

#### Streaming

If you require Chat Completion [Streaming support](https://platform.openai.com/docs/api-reference/streaming), you can use the library as shown below:

```java
var request = new ChatCompletionRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .systemMessage("You are a sustainable product name generator")
    .userMessage("Please generate two product names")
    .build();

client.streamChatCompletion(request).subscribe(response -> {
    var message = response.firstChoiceMessageContent();
    if (message != null) {
        System.out.println(message);
    }
});
```

Optionally, you can listen to events such as **errors** and **completion** during streaming:

```java
client.streamChatCompletion(request).subscribe(response -> {
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

### Assistants

#### Create Assistant

```java
var request = new CreateAssistantRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .name("Assistant name")
    .description("Assistant description")
    .instructions("Assistant instructions")
    .retrieval()
    .codeInterpreter()
    .fileIds("fileId-1", "fileId-2", "fileId-3")
    .metadata(Map.of("metadata-key-1", "metadata-value-1", "metadata-key-2", "metadata-value-2"))
    .build();

var response = client.createAssistant(request);
System.out.println(response);
```

The response is an object of type [`Assistant`](src/main/java/br/com/rcaneppele/openai/endpoints/assistant/response/Assistant.java).

If you need to create an Assistant with [Function Calling](https://platform.openai.com/docs/guides/function-calling) support:

```java
// The third parameter is a Map<String, Object> representing the function parameters
var myFunction = new Function("function-name", "function description", Map.of("name", "string", "age", "number"));

var request = new CreateAssistantRequestBuilder()
    .model(OpenAIModel.GPT_4_1106_PREVIEW)
    .name("Assistant name")
    .description("Assistant description")
    .instructions("Assistant instructions")
    .function(myFunction)
    .build();
```

#### List Assistants

```java
var response = client.listAssistants();
System.out.println(response);
```

The response is an object of type [`ListOfAssistants`](src/main/java/br/com/rcaneppele/openai/endpoints/assistant/response/ListOfAssistants.java).

Optionally, you can change the default pagination/filter parameters:

```java
var parameters = new QueryParametersBuilder()
    .limit(5)
    .after("after-assistant-id")
    .before("before-assistant-id")
    .ascOrder()
    .build();

var response = client.listAssistants(parameters);
System.out.println(response);
```

#### Retrieve Assistant

```java
var assistant = client.retrieveAssistant("assistant_id");
System.out.println(assistant);
```

#### Modify Assistant

```java
var request = new ModifyAssistantRequestBuilder()
    .assistantId("assistant_id")
    .name("New Assistant name")
    .description("New Assistant description")
    .instructions("New Assistant instructions")
    .build();

var response = client.modifyAssistant(request);
System.out.println(response);
```

#### Delete Assistant

```java
var status = client.deleteAssistant("assistant_id");
System.out.println(status);
```

The response is an object of type [`DeletionStatus`](src/main/java/br/com/rcaneppele/openai/common/response/DeletionStatus.java).

### Threads

#### Create Thread

```java
var response = client.createThread();
System.out.println(response);
```

The response is an object of type [`Thread`](src/main/java/br/com/rcaneppele/openai/endpoints/threads/response/Thread.java).

Optionally, you can create a Thread with metadata and a list of messages:

```java
var messageMetadata = Map.of("message-metadata-key", "message-metadata-value");
var threadMetadata = Map.of("thread-metadata-key", "thread-metadata-value");
var fileIds = Set.of("fileId-1", "fileId-2");

var request = new CreateThreadRequestBuilder()
    .addUserMessage("message 1", null, null)
    .addUserMessage("message 2", fileIds, messageMetadata)
    .metadata(threadMetadata)
    .build();

var response = client.createThread(request);
System.out.println(response);
```

#### Retrieve Thread

```java
var thread = client.retrieveThread("thread_id");
System.out.println(thread);
```

#### Modify Thread

```java
var metadata = Map.of("metadata-key", "metadata-value");

var request = new ModifyThreadRequestBuilder()
    .threadId("thread_id")
    .metadata(metadata)
    .build();

var response = client.modifyThread(request);
System.out.println(response);
```

#### Delete Thread

```java
var status = client.deleteThread("thread_id");
System.out.println(status);
```
