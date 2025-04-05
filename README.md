# Americana Integration Service

Serviço de integração entre as bases de dados do Americana Delivery e Americana Restaurant.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring AMQP (RabbitMQ)
- Supabase
- Maven
- Lombok

## Configuração do Ambiente

1. Instale o Java 17
2. Instale o Maven
3. Instale e configure o RabbitMQ
4. Configure as variáveis de ambiente:
   - `SUPABASE_DELIVERY_URL`: URL do Supabase do Delivery
   - `SUPABASE_DELIVERY_KEY`: Chave do Supabase do Delivery
   - `SUPABASE_RESTAURANT_URL`: URL do Supabase do Restaurant
   - `SUPABASE_RESTAURANT_KEY`: Chave do Supabase do Restaurant

## Executando o Projeto

1. Clone o repositório
2. Configure as variáveis de ambiente
3. Execute o comando:
```bash
mvn spring-boot:run
```

## Endpoints da API

- `GET /api/integration/health`: Verifica o status do serviço
- `POST /api/integration/sync`: Força uma sincronização manual

## Funcionamento

O serviço realiza as seguintes operações:

1. A cada 30 segundos, verifica por novos pedidos ou atualizações no Supabase do Delivery
2. Envia os pedidos para uma fila no RabbitMQ
3. Consome os pedidos da fila e atualiza o Supabase do Restaurant
4. Verifica por atualizações de status no Supabase do Restaurant
5. Sincroniza as atualizações de volta para o Supabase do Delivery

## Monitoramento

O serviço utiliza logs para monitoramento das operações. Os principais eventos são:

- Recebimento de pedidos
- Processamento de pedidos
- Erros durante a sincronização
- Erros durante o processamento

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request 