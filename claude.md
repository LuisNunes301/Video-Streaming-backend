# MiniStreaming Backend Upgrade Roadmap

## Objetivo

Evoluir o backend atual para suportar uma experiência mobile semelhante a plataformas modernas de streaming.

Arquitetura base já existente:

* Java 17
* Spring Boot
* PostgreSQL
* RabbitMQ
* MinIO
* FFmpeg
* JWT Authentication
* Clean Architecture
* DDD
* Event-Driven Architecture

---

# Prioridade 1 - Thumbnail Pública ✅

Implementado.

Objetivo:

* Exibir thumbnails sem autenticação.
* Permitir carregamento rápido da home.

---

# Prioridade 2 - Categorias Fortes ✅

Implementado.

Exemplo:

```java
public enum VideoCategory {
    MUSIC,
    TECHNOLOGY,
    EDUCATION,
    SPORTS,
    DOCUMENTARY,
    ENTERTAINMENT,
    ACTION
}
```

Benefícios:

* Home organizada
* Busca por categoria
* Trending por categoria
* Recomendações futuras

---

# Prioridade 3 - Busca ✅

Implementado.

Endpoints:

```http
GET /videos/search?q=java
```

Benefícios:

* Busca textual
* Busca por categoria
* Base para descoberta de conteúdo

---

# Prioridade 4 - Contas e Perfis ✅

## Objetivo

Separar autenticação de personalização.

---

## User

Responsável por:

```text
Login
Senha
JWT
Roles
Segurança
Autenticação
```

---

## UserProfile

Responsável por:

```text
Nickname
Avatar
Bio
Histórico
Favoritos
Continue Watching
Preferências
Controle Parental
```

---

## Relacionamento

```text
User
 |
 | 1:N
 |
UserProfile
```

---

## Modelo

```text
Conta:

Luis@email.com

Perfis:

👤 Luis
👤 Filho
👤 Kids
```

---

## Entidade

```java
public class UserProfile {

    private UUID id;

    private UUID userId;

    private String nickname;

    private String avatarKey;

    private String bio;

    private Boolean kidsProfile;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
```

---

## Fluxo de Registro

```text
Register User
        ↓
Persist User
        ↓
Create Default Profile
        ↓
nickname = user.name
```

---

## Endpoints

### Criar Perfil

```http
POST /profiles
```

### Listar Perfis

```http
GET /profiles
```

### Buscar Perfil

```http
GET /profiles/{profileId}
```

### Atualizar Perfil

```http
PUT /profiles/{profileId}
```

### Remover Perfil

```http
DELETE /profiles/{profileId}
```

---

# Prioridade 5 - Avatar via MinIO

## Objetivo

Permitir avatar customizado para cada perfil.

---

## Endpoint

```http
POST /profiles/{profileId}/avatar
```

multipart/form-data

```text
file
```

---

## Estrutura

```text
avatars/

profile-id/
 └── avatar.jpg
```

---

## Fluxo

```text
Upload
    ↓
MinIO
    ↓
Salvar avatarKey
    ↓
Gerar URL Pública
    ↓
Atualizar Perfil
```

---

# Prioridade 6 - Estatísticas de Consumo

## Objetivo

Exibir métricas por perfil.

---

## Fonte

Playback

---

## Endpoint

```http
GET /profiles/{profileId}/stats
```

---

## Response

```json
{
  "videosWatched": 42,
  "hoursWatched": 18,
  "completedVideos": 10
}
```

---

## Dados Derivados

Playback:

```text
currentTime
completed
lastUpdated
```

---

# Prioridade 7 - Favoritos

## Banco

```sql
CREATE TABLE profile_favorites (
    id UUID PRIMARY KEY,

    profile_id UUID NOT NULL,

    video_id UUID NOT NULL,

    created_at TIMESTAMP
);
```

---

## Endpoints

### Adicionar Favorito

```http
POST /profiles/{profileId}/favorites/{videoId}
```

### Remover Favorito

```http
DELETE /profiles/{profileId}/favorites/{videoId}
```

### Listar Favoritos

```http
GET /profiles/{profileId}/favorites
```

---

# Prioridade 8 - Histórico

## Objetivo

Separar Histórico Completo de Continue Watching.

---

## Fonte

Playback

---

## Endpoint

```http
GET /profiles/{profileId}/history
```

---

## Response

```json
[
  {
    "videoId": "...",
    "title": "...",
    "thumbnailUrl": "...",
    "watchedAt": "2026-06-04"
  }
]
```

---

# Prioridade 9 - Continue Watching Melhorado

## Situação Atual

Retorna:

```java
PlaybackState
```

---

## Problema

Frontend precisa buscar dados do vídeo separadamente.

---

## Novo DTO

```json
[
  {
    "videoId": "...",
    "title": "...",
    "thumbnailUrl": "...",
    "progressSeconds": 150,
    "duration": 372
  }
]
```

---

## Benefícios

Renderização imediata.

Menos chamadas HTTP.

---

# Prioridade 10 - Home Agregada

## Objetivo

Retornar toda a Home em uma única chamada.

---

## Endpoint

```http
GET /home?profileId={profileId}
```

---

## Response

```json
{
  "continueWatching": [],
  "music": [],
  "technology": [],
  "education": [],
  "sports": [],
  "documentary": [],
  "entertainment": [],
  "action": [],
  "trending": []
}
```

---

## Trending

Inicialmente baseado em:

```text
Mais assistidos
```

calculado através do Playback.

---

## Benefícios

Uma única chamada para montar a Home.

---

# Prioridade 11 - Controle Parental

## Objetivo

Permitir restrição de conteúdo por perfil.

---

## Nova Enum

```java
public enum ContentRating {
    FREE,
    AGE_10,
    AGE_12,
    AGE_14,
    AGE_16,
    AGE_18
}
```

---

## Alteração em VideoContent

```java
private ContentRating rating;
```

---

## Alteração em UserProfile

```java
private ContentRating maxAllowedRating;
```

---

## Exemplos

### Perfil Kids

```java
FREE
```

---

### Perfil Filho

```java
AGE_12
```

---

### Perfil Adulto

```java
AGE_18
```

---

## Banco

### Videos

```sql
ALTER TABLE videos
ADD COLUMN rating VARCHAR(20);
```

### Profiles

```sql
ALTER TABLE profiles
ADD COLUMN max_allowed_rating VARCHAR(20);
```

---

## Regra

Vídeo:

```text
John Wick
AGE_16
```

Perfil:

```text
Kids
FREE
```

Resultado:

```text
Não aparece.
```

---

## Impactados

Todos os endpoints de catálogo:

```text
Home
Busca
Trending
Favoritos
Histórico
Continue Watching
Recomendações Futuras
```

---

# Ordem Recomendada

## Sprint 1 ✅

* Thumbnail Pública
* Categorias Fortes
* Busca

---

## Sprint 2 ✅

* Perfis
* Avatar
* Gestão de Perfis

---

## Sprint 3

* Estatísticas
* Favoritos
* Histórico

---

## Sprint 4

* Continue Watching Melhorado
* Home Agregada
* Trending

---

## Sprint 5

* Controle Parental
* Classificação Etária
* Perfis Kids

---

# Resultado Final Esperado

Backend capaz de suportar:

* Streaming HLS
* Resume Playback
* Continue Watching
* Home estilo Netflix
* Busca
* Categorias fortes
* Perfis múltiplos
* Avatar
* Favoritos
* Histórico
* Estatísticas
* Trending
* Controle parental
* Classificação etária
* Home agregada

Mantendo:

* Clean Architecture
* DDD
* Event-Driven Architecture
* RabbitMQ
* PostgreSQL
* MinIO
* Spring Boot
* FFmpeg
* Docker
