# MiniStreaming - Roadmap Oficial

## Projeto

MiniStreaming é uma plataforma backend inspirada em Netflix, Prime Video e Disney+, construída utilizando:

* Java 17
* Spring Boot
* PostgreSQL
* RabbitMQ
* MinIO
* FFmpeg
* JWT Authentication
* Docker
* Clean Architecture
* DDD
* Event-Driven Architecture

O objetivo do projeto é servir como uma implementação realista de uma plataforma de streaming moderna, demonstrando arquitetura escalável, desacoplamento entre contextos e boas práticas de engenharia de software.

---

# Arquitetura Atual

## Autenticação

Implementado:

* JWT Authentication
* Spring Security
* Roles USER e ADMIN
* Filtro JWT customizado
* Endpoints protegidos

---

## Upload e Processamento de Vídeos

Implementado:

* Upload assíncrono
* RabbitMQ
* Processamento via FFmpeg
* Conversão para HLS
* Geração automática de thumbnails
* Armazenamento no MinIO

Fluxo:

Upload
→ RabbitMQ
→ Processamento FFmpeg
→ Thumbnail
→ HLS
→ Atualização do Catálogo

---

## Catálogo

Implementado:

* Listagem de vídeos
* Busca por título
* Busca por categoria
* Consulta individual
* Catálogo orientado ao domínio

```java
public interface VideoCatalogRepository {

    void save(VideoContent video);

    Optional<VideoContent> findById(String id);

    List<VideoContent> findAll();

    List<VideoContent> findByCategory(VideoCategory category);

    List<VideoContent> searchByTitle(String query);
}
```

---

## Categorias Fortes

Implementado através de enum de domínio:

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

Não existem categorias livres persistidas no banco.

---

## Thumbnail Pública

Implementado.

As thumbnails possuem URL pública gerada pelo MinIO.

Exemplo:

```text
/videos/previews/{videoId}/thumbnail.jpg
```

---

## Perfil

Estrutura atual:

```text
User
└── UserProfile
```

Preparada para futura evolução para múltiplos perfis.

---

## Avatar

Implementado.

Funcionalidades:

* Bucket dedicado
* Upload para MinIO
* Persistência da URL
* Associação ao UserProfile

Endpoints:

```http
POST /profiles/avatar

GET /profiles/me
```

---

## Favoritos

Implementado.

Funcionalidades:

* Adicionar favorito
* Remover favorito
* Consultar favoritos

Endpoints:

```http
POST /profiles/favorites/{videoId}

DELETE /profiles/favorites/{videoId}

GET /profiles/favorites
```

---

## Playback

Implementado.

Funcionalidades:

* Início de reprodução
* Recuperação de progresso
* Persistência de progresso
* Controle de conclusão
* Continue Watching

Endpoints:

```http
GET /playback/start/{contentId}

POST /playback/progress

GET /playback/{contentId}

GET /playback/continue
```

---

## Continue Watching Enriquecido

Implementado.

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

Implementado.

Endpoint:

```http
GET /home
```

Retorna:

* Trending
* Continue Watching
* Categorias

Estrutura:

```json
{
  "trending": [],
  "continueWatching": [],
  "categories": []
}
```

Objetivo:

Fornecer uma única chamada para montagem da Home do frontend.

---

## Estatísticas de Consumo

Implementado.

Arquitetura Event-Driven:

```text
Playback Completed
        ↓
VideoCompletedEvent
        ↓
RabbitMQ
        ↓
VideoCompletedConsumer
        ↓
VideoStatistics
```

Métricas disponíveis:

* Total de visualizações
* Visualizações completas
* Tempo total assistido

Endpoint:

```http
GET /statistics/video/{videoId}
```

Exemplo:

```json
{
  "videoId": "...",
  "views": 4,
  "completedViews": 4,
  "watchedSeconds": 436.07
}
```

---

# Funcionalidades Concluídas

## Prioridade 1 — Thumbnail Pública

✅ CONCLUÍDO

## Prioridade 2 — Categorias Fortes

✅ CONCLUÍDO

## Prioridade 3 — Busca de Vídeos

✅ CONCLUÍDO

## Prioridade 4 — Separação User / UserProfile

✅ CONCLUÍDO

## Prioridade 5 — Favoritos

✅ CONCLUÍDO

## Prioridade 6 — Avatar via MinIO

✅ CONCLUÍDO

## Prioridade 7 — Continue Watching

✅ CONCLUÍDO

## Prioridade 8 — Continue Watching Enriquecido

✅ CONCLUÍDO

## Prioridade 9 — Home Netflix

✅ CONCLUÍDO

## Prioridade 10 — Estatísticas de Consumo

✅ CONCLUÍDO

---

# Próximas Prioridades

## Prioridade 11 — Trending Inteligente

✅ CONCLUÍDO

---

## Prioridade 12 — Múltiplos Perfis

Evolução da estrutura atual:

```text
User
└── UserProfile
```

Para:

```text
User
├── Profile 1
├── Profile 2
├── Profile 3
└── Profile 4
```

Cada perfil possuirá:

* Continue Watching próprio
* Favoritos próprios
* Recomendações próprias
* Estatísticas próprias

---

## Prioridade 13 — Controle Parental

Dependência:

* Múltiplos Perfis

Funcionalidades:

* Perfil infantil
* Restrição etária
* Classificação indicativa
* Catálogo filtrado

---

# Evoluções Futuras

## Recomendações

Baseadas em:

* Histórico
* Favoritos
* Categorias consumidas

---

## Dashboard Administrativo

Métricas:

* Uploads
* Vídeos processados
* Vídeos assistidos
* Horas consumidas
* Usuários ativos

---

## Avaliações

Implementar:

* Like
* Dislike

ou

* Sistema de estrelas

---

## Notificações

RabbitMQ:

* Novo vídeo
* Vídeo processado
* Recomendações

---

## Busca Avançada

Filtros:

* Categoria
* Popularidade
* Data
* Duração

---

# Objetivo Final

Construir uma plataforma de streaming inspirada em arquiteturas utilizadas por Netflix, Prime Video e Disney+, aplicando:

* Clean Architecture
* DDD
* Event-Driven Architecture
* RabbitMQ
* MinIO
* Processamento assíncrono
* HLS Streaming
* Escalabilidade
* Observabilidade
* Boas práticas de backend moderno

Com foco em servir como projeto de portfólio profissional e demonstrar competências avançadas em backend distribuído.
