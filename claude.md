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

O objetivo do projeto é servir como um sistema realista de streaming moderno, focado em arquitetura escalável, desacoplamento entre contextos e boas práticas de engenharia de software.

---

# Arquitetura Atual

## Autenticação

Implementado:

* JWT Authentication
* Spring Security
* Roles USER e ADMIN
* Filtros JWT customizados
* Endpoints protegidos

---

## Upload de Vídeos

Implementado:

* Upload assíncrono
* RabbitMQ
* Processamento via FFmpeg
* Conversão para HLS
* Armazenamento no MinIO
* Geração automática de thumbnails

Fluxo:

Upload
→ Evento RabbitMQ
→ FFmpeg
→ Thumbnail
→ HLS
→ Atualização do Catálogo

---

## Catálogo

Implementado:

* Listagem de vídeos
* Busca textual por título
* Busca por categoria
* Consulta individual
* Catálogo baseado em domínio

Repositório:

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

Não existem categorias livres no banco.

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

Separação concluída para permitir evolução futura para múltiplos perfis.

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

Cada perfil possui:

* Adição de favoritos
* Remoção de favoritos
* Consulta de favoritos

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
* Recuperação do progresso
* Salvamento de progresso
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

Modelo semelhante ao utilizado pela Netflix.

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

Reduzir chamadas do frontend e fornecer uma única resposta agregada para a tela inicial.

---

# Funcionalidades Concluídas

## Prioridade 1

### Thumbnail Pública

Status:

✅ CONCLUÍDO

---

## Prioridade 2

### Categorias Fortes

Status:

✅ CONCLUÍDO

---

## Prioridade 3

### Busca de Vídeos

Status:

✅ CONCLUÍDO

---

## Prioridade 4

### Separação User / UserProfile

Status:

✅ CONCLUÍDO

---

## Prioridade 5

### Favoritos

Status:

✅ CONCLUÍDO

---

## Prioridade 6

### Avatar via MinIO

Status:

✅ CONCLUÍDO

---

## Prioridade 7

### Continue Watching

Status:

✅ CONCLUÍDO

---

## Prioridade 8

### Continue Watching Enriquecido

Status:

✅ CONCLUÍDO

---

## Prioridade 9

### Home Netflix

Status:

✅ CONCLUÍDO

---

# Próximas Prioridades

## Prioridade 10

### Estatísticas de Consumo

Objetivo:

Registrar métricas reais de uso da plataforma.

Métricas previstas:

* Total de visualizações
* Horas assistidas
* Vídeos mais vistos
* Categorias mais consumidas
* Usuários ativos

Essa funcionalidade servirá como base para:

* Trending real
* Dashboard administrativo
* Recomendações

---

## Prioridade 11

### Trending Inteligente

Dependência:

Prioridade 10

Hoje:

```text
Trending = vídeos mais recentes
```

Futuro:

```text
Trending = vídeos mais assistidos
```

Critérios:

* Dia
* Semana
* Mês

---

## Prioridade 12

### Múltiplos Perfis

Evolução da estrutura atual.

Hoje:

```text
User
└── UserProfile
```

Futuro:

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
* Histórico próprio
* Recomendações próprias

---

## Prioridade 13

### Controle Parental

Dependência:

Prioridade 12

Funcionalidades:

* Perfil infantil
* Restrição etária
* Classificação indicativa
* Catálogo filtrado

Exemplo:

```text
KIDS
→ Livre
→ 10 anos

ADULT
→ Todo catálogo
```

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
