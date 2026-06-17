# MiniStreaming

MiniStreaming é uma plataforma backend de streaming inspirada em Netflix, Prime Video e Disney+, construída com Java e Spring Boot utilizando Clean Architecture, DDD e Event-Driven Architecture.

O projeto foi desenvolvido com foco em arquitetura escalável, desacoplamento entre contextos, processamento assíncrono e boas práticas de engenharia de software.

---

# Tecnologias

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* PostgreSQL
* RabbitMQ
* MinIO
* FFmpeg
* Docker
* JPA / Hibernate

---

# Arquitetura

O projeto segue os princípios de:

* Clean Architecture
* Domain Driven Design (DDD)
* Ports and Adapters
* Event-Driven Architecture
* Separação entre Domínio e Infraestrutura

Estrutura:

```text
domain
application
infrastructure
web
```

---

# Funcionalidades Implementadas

## Autenticação

* Registro de usuário
* Login JWT
* Roles USER e ADMIN
* Spring Security

Endpoints:

```http
POST /auth/register
POST /auth/login
```

---

## Upload de Vídeos

* Upload assíncrono
* Armazenamento no MinIO
* Processamento via RabbitMQ
* Conversão para HLS
* Extração de metadados
* Geração de thumbnail

Fluxo:

```text
Upload
↓
MinIO
↓
RabbitMQ
↓
FFmpeg
↓
Thumbnail
↓
HLS
↓
Catálogo
```

---

## Catálogo

* Listagem de vídeos
* Busca por título
* Busca por categoria
* Consulta individual

Endpoints:

```http
GET /videos

GET /videos/{id}

GET /videos/search?query=

GET /videos/category/{category}
```

---

## Categorias

Categorias fortemente tipadas:

```java
MUSIC
DOCUMENTARY
TECHNOLOGY
EDUCATION
SPORTS
NEWS
GAMING
ENTERTAINMENT
```

---

## Thumbnail Pública

Cada vídeo possui thumbnail pública gerada automaticamente.

Exemplo:

```text
/videos/previews/{videoId}/thumbnail.jpg
```

---

## Perfil

Separação entre:

```text
User
└── UserProfile
```

Preparado para futura evolução para múltiplos perfis.

---

## Avatar

Upload de avatar utilizando MinIO.

Funcionalidades:

* Bucket dedicado
* Upload
* Persistência da URL
* Associação ao perfil

Endpoints:

```http
POST /profiles/avatar

GET /profiles/me
```

---

## Favoritos

Cada perfil possui sua própria lista de favoritos.

Endpoints:

```http
POST /profiles/favorites/{videoId}

DELETE /profiles/favorites/{videoId}

GET /profiles/favorites
```

---

## Playback

Controle completo de reprodução.

Funcionalidades:

* Início de reprodução
* Recuperação de progresso
* Salvamento de progresso
* Marcação automática de conclusão

Endpoints:

```http
GET /playback/start/{contentId}

POST /playback/progress

GET /playback/{contentId}
```

---

## Continue Watching

Lista vídeos iniciados e ainda não concluídos.

Endpoint:

```http
GET /playback/continue
```

Retorna:

* Video ID
* Título
* Thumbnail
* Categoria
* Duração
* Tempo atual
* Percentual assistido

Exemplo:

```json
{
  "videoId": "...",
  "title": "...",
  "thumbnailUrl": "...",
  "duration": 109,
  "currentTime": 10,
  "progressPercent": 9,
  "category": "SPORTS"
}
```

---

## Home Netflix

Endpoint agregador da tela inicial.

```http
GET /home
```

Retorna:

```json
{
  "trending": [],
  "continueWatching": [],
  "categories": []
}
```

Seções:

* Trending
* Continue Watching
* Categorias

---

## Estatísticas de Consumo

Contexto separado de Analytics.

Funcionalidades:

* Visualizações totais
* Visualizações completas
* Segundos assistidos

Eventos:

```text
VideoCompletedEvent
```

RabbitMQ:

```text
video.completed
```

Endpoint:

```http
GET /statistics/video/{videoId}
```

Resposta:

```json
{
  "videoId": "...",
  "views": 10,
  "completedViews": 4,
  "watchedSeconds": 1500
}
```

---

# Modelo de Domínio

Principais agregados:

```text
VideoContent

PlaybackState

VideoStatistics

User

UserProfile
```

---

# Estados do Vídeo

Fluxo:

```text
UPLOADING
    ↓
PROCESSING
    ↓
READY

ou

PROCESSING
    ↓
FAILED
```

Estados:

```java
UPLOADING
PROCESSING
READY
FAILED
```

---

# Mensageria

RabbitMQ é utilizado para:

## Processamento de vídeo

```text
video.uploaded
```

Responsável por:

* Processamento FFmpeg
* Extração de metadata
* Geração de thumbnail
* Conversão HLS

## Analytics

```text
video.completed
```

Responsável por:

* Atualização das estatísticas de consumo

---

# Armazenamento

MinIO utilizado para:

* Vídeos originais
* Arquivos HLS
* Thumbnails
* Avatares

---

# Segurança

* JWT Authentication
* BCrypt
* Spring Security
* Controle por Roles

---
```
Linter 
executar
mvn spotless:apply

verificar
mvn spotless:check
```
___

# Executando com Docker

```bash
docker-compose up -d
```

Serviços:

* PostgreSQL
* RabbitMQ
* MinIO
* MiniStreaming

---

# Objetivo do Projeto

O MiniStreaming foi desenvolvido para demonstrar conhecimento em:

* Backend moderno
* Arquitetura limpa
* DDD
* Event-Driven Architecture
* RabbitMQ
* MinIO
* Processamento assíncrono
* Streaming HLS
* Integração entre serviços
* Projetos inspirados em plataformas reais de streaming

---

# Autor

Luis Nunes
