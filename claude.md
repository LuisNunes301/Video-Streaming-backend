# MiniStreaming Backend Upgrade Roadmap

## Objetivo

Evoluir o backend atual para suportar uma experiência mobile semelhante a plataformas modernas de streaming como Netflix, Prime Video e Disney+.

O backend já possui:

- JWT Authentication
- Registro e Login
- Catálogo de vídeos
- HLS Streaming
- Continue Watching
- PostgreSQL
- RabbitMQ
- MinIO
- FFmpeg
- Clean Architecture
- DDD
- Event-Driven Architecture

O foco agora é fornecer recursos necessários para melhorar a experiência do aplicativo mobile.

---

# Arquitetura de Usuário

## Situação Atual

Atualmente a entidade User possui:

```java
public class User {

    private final UUID id;

    private String name;
    private String email;
    private String passwordHash;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private final Set<UserRole> roles;
}
```

Essa entidade é responsável por:

- autenticação
- autorização
- credenciais
- permissões

---

## Nova Estratégia

Separar:

### User

Responsável por:

```text
Login
Senha
JWT
Roles
Segurança
```

---

### UserProfile

Responsável por:

```text
Avatar
Nickname
Bio
Preferências
Configurações futuras
```

---

## Nova Entidade de Domínio

```java
public class UserProfile {

    private UUID id;

    private UUID userId;

    private String nickname;

    private String avatarUrl;

    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
```

---

## Relacionamento

```text
User
  |
  | 1:1
  |
UserProfile
```

---

## Banco

```sql
CREATE TABLE user_profile (
    id UUID PRIMARY KEY,

    user_id UUID UNIQUE NOT NULL,

    nickname VARCHAR(100),

    avatar_url TEXT,

    bio TEXT,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT fk_profile_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
```

---

## Fluxo de Registro

Ao criar usuário:

```text
Register User
        ↓
Persist User
        ↓
Create Default UserProfile
        ↓
nickname = user.name
avatar = null
bio = null
```

---

## Benefícios

Permite evoluir futuramente:

```text
Foto de perfil

Preferências

Tema

Idioma

Configurações do app

Notificações

Controle parental
```

Sem poluir a entidade User.

---

# Prioridade 1 - Thumbnail Pública feito

## Problema

Hoje o catálogo retorna:

```json
{
  "thumbnailUrl": "previews/video-id/thumbnail.jpg"
}
```

O aplicativo mobile não consegue consumir diretamente essa informação.

---

## Objetivo

Retornar URL pública completa.

Exemplo:

```json
{
  "thumbnailUrl": "http://192.168.0.4/videos/previews/video-id/thumbnail.jpg"
}
```

---

## Solução

Durante o mapeamento para DTO:

```java
thumbnailUrl =
    publicBaseUrl
    + "/videos/"
    + thumbnailPath;
```

---

## Resultado

Frontend:

```tsx
<Image source={{ uri: video.thumbnailUrl }} />
```

Sem chamadas adicionais.

---

# Prioridade 2 - Categorias Fortes feito

## Problema

Usar String livre para categoria gera erros:

```text
Music
music
MUSIC
Musica
Musics
```

Além de permitir uploads incorretos.

---

## Solução

Criar enum de domínio.

---

## Enum

```java
public enum VideoCategory {

    MUSIC,

    DOCUMENTARY,

    TECHNOLOGY,

    ACTION,

    EDUCATION,

    ENTERTAINMENT
}
```

---

## Entidade

```java
private VideoCategory category;
```

---

## Banco

```sql
ALTER TABLE videos
ADD COLUMN category VARCHAR(50);
```

---

## DTO

```json
{
  "title": "Beat It",
  "category": "MUSIC"
}
```

---

## Upload

O frontend sempre envia:

```json
{
  "title": "Beat It",
  "category": "MUSIC"
}
```

---

## Benefícios

Impede:

```text
Typos
Categorias inválidas
Dados inconsistentes
```

---

## Evolução Futura

Criar tabela:

```sql
video_categories
```

caso o catálogo cresça muito.

---

# Prioridade 3 - Busca

## Endpoint

```http
GET /api/videos/search?q=beat
```

---

## Caso de Uso

```java
SearchVideosUseCase
```

---

## Repository Port

```java
List<VideoContent> searchByTitle(String query);
```

---

## Exemplo

```json
[
  {
    "id": "...",
    "title": "Beat It"
  }
]
```

---

## Resultado

Tela:

```text
🔍 Buscar

Beat
```

retornando resultados em tempo real.

---

# Prioridade 4 - Perfil do Usuário

## Objetivo

Permitir personalização da conta.

Sem múltiplos perfis.

Um perfil por usuário.

---

## Endpoints

### Buscar perfil

```http
GET /api/profile/me
```

---

### Atualizar perfil

```http
PUT /api/profile
```

---

## Request

```json
{
  "nickname": "Luis",
  "avatarUrl": "https://...",
  "bio": "Backend Developer"
}
```

---

## Response

```json
{
  "nickname": "Luis",
  "avatarUrl": "https://...",
  "bio": "Backend Developer"
}
```

---

## Resultado

Tela:

```text
👤 Luis

Backend Developer
```

---

# Prioridade 5 - Estatísticas de Consumo

## Objetivo

Exibir métricas pessoais.

---

## Endpoint

```http
GET /api/users/stats
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

Tabela:

```text
playback
```

---

## Resultado

```text
42 vídeos assistidos

18 horas consumidas

10 vídeos concluídos
```

---

# Prioridade 6 - Favoritos

## Nova Tabela

```sql
CREATE TABLE favorites (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    video_id UUID NOT NULL,

    created_at TIMESTAMP
);
```

---

## Endpoints

### Adicionar

```http
POST /api/favorites/{videoId}
```

---

### Remover

```http
DELETE /api/favorites/{videoId}
```

---

### Listar

```http
GET /api/favorites
```

---

## Resultado

Nova aba:

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
GET /api/history
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

## Fonte

Tabela playback.

---

# Prioridade 8 - Homepage Inteligente

## Objetivo

Evitar múltiplas chamadas do frontend.

---

## Endpoint

```http
GET /api/home
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
  "trending": []
}
```

---

## Exemplo de Construção

```java
HomeResponse {

    List<VideoCard> continueWatching;

    List<VideoCard> music;

    List<VideoCard> technology;

    List<VideoCard> documentary;

    List<VideoCard> action;

    List<VideoCard> education;

    List<VideoCard> entertainment;

    List<VideoCard> trending;
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

Evita:

```text
GET /videos

GET /continue

GET /videos/category/music

GET /videos/category/documentary

GET /videos/category/action
```

Tudo em:

```text
GET /home
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

O frontend precisa buscar vídeo por vídeo.

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

Renderização direta da faixa:

```text
Continuar Assistindo
```

---

# Prioridade 10 - Upload de Avatar

## Endpoint

```http
POST /api/profile/avatar
```

---

## Request

```multipart/form-data
file
```

---

## Fluxo

```text
Upload
      ↓
MinIO
      ↓
Salvar URL
      ↓
Atualizar UserProfile
```

---

## Response

```json
{
  "avatarUrl": "http://..."
}
```

---

# Ordem Recomendada

## Sprint 1

- Thumbnail pública feito
- Categorias fortes (enum)
- Busca

---

## Sprint 2

- UserProfile
- Avatar
- Perfil

---

## Sprint 3

- Estatísticas
- Favoritos
- Histórico

---

## Sprint 4

- Continue Watching melhorado
- Home agregada

---

# Resultado Final Esperado

Backend capaz de suportar:

- Home estilo Netflix
- Continue Watching
- Busca
- Perfil customizado
- Avatar
- Favoritos
- Histórico
- Estatísticas pessoais
- Streaming HLS
- Resume Playback
- Categorias fortes
- Home agregada
- Trending

Mantendo a arquitetura baseada em:

- Clean Architecture
- DDD
- Event-Driven Architecture
- RabbitMQ
- PostgreSQL
- MinIO
- Spring Boot
- FFmpeg
- Docker

---