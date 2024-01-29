# How to contribute

Welcome! We're excited that you're considering contributing to **Simple OpenAI Client** library. Your help is essential for maintaining and improving the project. Please take a moment to review the guidelines outlined below to ensure a smooth and collaborative contribution process.

## Table of Contents
- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Submitting Issues](#submitting-issues)
- [Contributing Code](#contributing-code)
    - [Getting Started](#getting-started)
    - [Adding New OpenAI API Endpoint Implementations](#adding-new-openai-api-endpoint-implementations)
    - [Code Style](#code-style)
- [Review Process](#review-process)
- [Thanks for Contributing!](#thanks-for-contributing)

## Code of Conduct

This project follows a [Code of Conduct](CODE_OF_CONDUCT.md). Please review and adhere to it to ensure a welcoming and inclusive environment for everyone.

## How Can I Contribute?

There are several ways you can contribute to the project:

- Submitting bug reports
- Improving documentation
- Enhancing existing features
- Adding new OpenAI API endpoint implementations

## Submitting Issues

Before submitting a [new issue](https://github.com/rcaneppele/simple-openai-client/issues/new/choose), please check the existing issues to avoid duplicates. When submitting an issue, provide detailed information, including steps to reproduce and your environment.

## Contributing Code

### Getting Started

1. Fork the repository
2. Clone your fork to your local machine
3. Create a new branch for your changes
4. Make your changes and commit them
5. Push your branch to your fork
6. Open a pull request

### Adding New OpenAI API Endpoint Implementations

When adding a new OpenAI API endpoint implementation, it's essential to adhere to the established patterns within our codebase. Follow the example set by any existing endpoint implementation in the [endpoint package](../src/main/java/br/com/rcaneppele/openai/endpoints).

Here's a step-by-step guide:

1. **Create the new endpoint package**: Begin by creating a new package for the endpoint within the [endpoints package](../src/main/java/br/com/rcaneppele/openai/endpoints).
2. **Request Sender class**: Develop a class responsible for sending requests to the API. This class should handle the communication specifics of the new endpoint.
3. **Request Builder class (If Applicable)**: If the endpoint involves request body parameters, create a corresponding Request Builder class to facilitate the construction of requests with ease.
4. **Request record**: Establish a Request record to represent the structure of the request body parameters, ensuring clarity and consistency.
5. **Response record**: Define a Response record to encapsulate the structure of the response obtained from the API, providing a clear representation of the expected data.
6. **Update OpenAIClient class**: Integrate the new endpoint by creating a method in the [OpenAIClient class](../src/main/java/br/com/rcaneppele/openai/OpenAIClient.java).
7. **Unit Tests**: Enhance the reliability of your implementation by creating comprehensive unit tests for both the Request Builder and Request Sender classes.

### Code Style

Follow the existing code style conventions. Consistent code style ensures readability and maintainability.

## Review Process

All contributions will be reviewed by the maintainers. Be prepared to address feedback and iterate on your changes.

## Thanks for Contributing!

We appreciate your time and effort in contributing to Simple OpenAI Client library. Your contributions make this project better for everyone. Thank you for being part of our community!
