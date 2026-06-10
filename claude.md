# MiniStreaming Backend Upgrade Roadmap

## Objetivo

Evoluir o backend atual para suportar uma experiência mobile semelhante a plataformas modernas de streaming como Netflix, Prime Video e Disney+.

O backend já possui:

* JWT Authentication
* Registro e Login
* Catálogo de vídeos
* HLS Streaming
* Continue Watching
* PostgreSQL
* RabbitMQ
* MinIO
* FFmpeg
* Clean Architecture
* DDD
* Event-Driven Architecture

O foco agora é fornecer recursos necessários para melhorar a experiência do aplicativo mobile.

---

# Prioridade 1 - Thumbnail Pública ✅

Implementado.

---

# Prioridade 2 - Categorias Fortes ✅

Implementado.

---

# Prioridade 3 - Busca ✅

Implementado.

---

# Prioridade 4 - Contas e Perfis

## Objetivo

Permitir que uma conta possua múltiplos perfis.

Cada perfil mantém seus próprios:

* favoritos
* histórico
* continue watching
* estatísticas
* avatar
* preferências futuras

Seguindo o padrão utilizado pelos principais serviços de streaming.

---

## Modelo

```text
Account
 ├─ Luis
 ├─ Filho
 ├─ Kids
 └─ Convidado
```

---

## Arquitetura

### User

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

### Profile

Responsável por:

```text
Nickname
Avatar
Bio
Histórico
Favoritos
Continue Watching
Preferências
```

---

## Nova Entidade de Domínio

```java
public class Profile {

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

## Relacionamento

```text
User
  |
  | 1:N
  |
Profiles
```

---

## Banco

```sql
CREATE TABLE profiles (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    nickname VARCHAR(100) NOT NULL,

    avatar_key TEXT,

    bio TEXT,

    kids_profile BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT fk_profile_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
```

---

## Fluxo Inicial

Ao registrar uma conta:

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

### Criar perfil

```http
POST /api/profiles
```

Request:

```json
{
  "nickname": "Luis"
}
```

---

### Listar perfis

```http
GET /api/profiles
```

Response:

```json
[
  {
    "id": "1",
    "nickname": "Luis",
    "avatarUrl": "..."
  },
  {
    "id": "2",
    "nickname": "Kids",
    "avatarUrl": "..."
  }
]
```

---

### Buscar perfil

```http
GET /api/profiles/{profileId}
```

---

### Atualizar perfil

```http
PUT /api/profiles/{profileId}
```

Request:

```json
{
  "nickname": "Luis Nunes",
  "bio": "Backend Developer"
}
```

---

### Remover perfil

```http
DELETE /api/profiles/{profileId}
```

---

## Upload de Avatar

```http
POST /api/profiles/{profileId}/avatar
```

Request:

```multipart/form-data
file
```

Response:

```json
{
  "avatarUrl": "http://..."
}
```

---

## Resultado

```text
Conta:
luis@email.com

Perfis:

👤 Luis

👤 Filho

👤 Kids
```

---

# Prioridade 5 - Estatísticas de Consumo

## Objetivo

Exibir métricas por perfil.

---

## Endpoint

```http
GET /api/profiles/{profileId}/stats
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

## Fonte

```text
Playback
```

relacionado ao Profile.

---

# Prioridade 6 - Favoritos

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

### Adicionar favorito

```http
POST /api/profiles/{profileId}/favorites/{videoId}
```

---

### Remover favorito

```http
DELETE /api/profiles/{profileId}/favorites/{videoId}
```

---

### Listar favoritos

```http
GET /api/profiles/{profileId}/favorites
```

---

## Resultado

```text
⭐ Favoritos
```

---

# Prioridade 7 - Histórico

## Objetivo

Separar:

```text
Continue Watching
```

de

```text
Histórico Completo
```

---

## Endpoint

```http
GET /api/profiles/{profileId}/history
```

---

## Response

```json
[
  {
    "videoId": "...",
    "title": "Beat It",
    "watchedAt": "2026-06-04"
  }
]
```

---

# Prioridade 8 - Homepage Inteligente

## Objetivo

Retornar toda a Home em uma única chamada.

---

## Endpoint

```http
GET /api/home?profileId={profileId}
```

---

## Response

```json
{
  "continueWatching": [],
  "music": [],
  "technology": [],
  "documentary": [],
  "action": [],
  "education": [],
  "entertainment": [],
  "sports": [],
  "trending": []
}
```

---

## Trending

Inicialmente:

```text
Mais assistidos
```

calculado pelo playback.

---

## Benefícios

Evita dezenas de chamadas do frontend.

Tudo chega através de:

```http
GET /api/home
```

---

# Prioridade 9 - Continue Watching Melhorado

## Hoje

Retorna:

```java
PlaybackState
```

---

## Problema

O frontend precisa buscar os vídeos individualmente.

---

## Novo DTO

```json
[
  {
    "videoId": "...",
    "title": "Beat It",
    "thumbnailUrl": "...",
    "progressSeconds": 150,
    "duration": 372
  }
]
```

---

## Benefício

Renderização imediata da seção:

```text
Continuar Assistindo
```

---

# Prioridade 10 - Avatar via MinIO

## Objetivo

Permitir avatar customizado para cada perfil.

---

## Fluxo

```text
Upload Avatar
       ↓
MinIO
       ↓
Salvar avatarKey
       ↓
Gerar URL Pública
       ↓
Atualizar Profile
```

---

## Estrutura no Bucket

```text
profiles/
 └─ profile-id/
     └─ avatar.jpg
```

---

# Ordem Recomendada

## Sprint 1 ✅

* Thumbnail Pública
* Categorias Fortes
* Busca

---

## Sprint 2

* Perfis
* Upload Avatar
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

# Resultado Final Esperado

Backend capaz de suportar:

* Home estilo Netflix
* Múltiplos perfis
* Continue Watching
* Busca
* Avatar
* Favoritos
* Histórico
* Estatísticas
* Trending
* Streaming HLS
* Resume Playback
* Categorias fortes
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
