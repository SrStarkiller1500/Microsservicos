# Microsserviços em Java com Spring Boot: Storefront & Warehouse

Projeto didático para estudar **microsserviços** usando **Spring Boot**, **Gradle**, **PostgreSQL** e **RabbitMQ**.

## Objetivo
Criar dois microsserviços que se comunicam de forma **síncrona** (REST) e **assíncrona** (RabbitMQ):
- **storefront-service**: expõe API REST, persiste pedidos, publica eventos de criação de pedido e consome respostas do warehouse.  
- **warehouse-service**: consome pedidos, simula reserva de estoque e publica evento de sucesso ou falha.

## Conceitos principais
- **Microsserviço**: serviço independente, com responsabilidade única, banco próprio e deploy isolado.  
- **Comunicação síncrona**: REST API, usada entre cliente e storefront.  
- **Comunicação assíncrona**: eventos via RabbitMQ (exchange `ecommerce`). Permite **consistência eventual**.  
- **Mensageria**: exchange do tipo `topic`, filas com binding keys específicas (`storefront.order.created`, `warehouse.product.reserved`, etc.).

## Estrutura do projeto
```
ecommerce-storefront-warehouse/
├─ storefront-service/       # API REST e produtor de eventos
├─ warehouse-service/       # Consumidor de eventos e lógica de estoque
├─ docker-compose.yml       # Postgres, RabbitMQ e serviços
└─ README.md                # Este arquivo resumido
```

## Fluxo de pedidos
1. Cliente envia `POST /orders` no storefront: JSON com userId e itens.  
2. Storefront persiste pedido com status `PENDING` e publica `storefront.order.created`.  
3. Warehouse consome evento, tenta reservar estoque (80% sucesso).  
4. Warehouse publica `warehouse.product.reserved` ou `warehouse.product.reservation_failed`.  
5. Storefront consome evento de resposta e atualiza status do pedido (`RESERVED` ou `RESERVATION_FAILED`).

## Executando com Docker (recomendado)
1. Abrir terminal na raiz do projeto.  
2. Subir serviços:  
```bash
docker-compose up --build -d
```
3. Acessar RabbitMQ UI: [http://localhost:15672](http://localhost:15672) (user: guest / pass: guest).

### Criar um pedido
```bash
curl -X POST http://localhost:8081/orders -H "Content-Type: application/json" -d '{
  "userId":"11111111-1111-1111-1111-111111111111",
  "items":[{"skuId":"sku-123","name":"Camiseta","quantity":2,"price":39.9}]
}'
```
### Consultar pedido
```bash
curl http://localhost:8081/orders/<order-id>
```
- Status inicial: `PENDING`  
- Após processamento do warehouse: `RESERVED` ou `RESERVATION_FAILED`

## Rodando no VS Code com Gradle
1. Abra a pasta do projeto no VS Code.  
2. Instale extensões recomendadas: Java Pack, Spring Boot Pack, Lombok.  
3. Rodar storefront:  
```bash
cd storefront-service
./gradlew bootRun
```
4. Rodar warehouse:  
```bash
cd warehouse-service
./gradlew bootRun
```
5. Use os mesmos comandos `curl` para testar pedidos.

