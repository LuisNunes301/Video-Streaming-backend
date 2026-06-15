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

O objetivo do projeto é servir como um sistema realista de streaming moderno, focado em arquitetura escalável e boas práticas de engenharia de software.

---

# Arquitetura Atual

## Autenticação

* JWT Authentication
* Roles (USER / ADMIN)
* Spring Security

## Upload de Vídeos

* Upload assíncrono
* RabbitMQ
* Processamento via FFmpeg
* Armazenamento no MinIO

## Catálogo

* Listagem de vídeos
* Busca por título
* Busca por categoria
* Thumbnail pública

## Playback

* Registro de reprodução
* Controle de progresso
* Histórico de visualização

## Perfil

Separação entre:

User

e

UserProfile

Permitindo evolução futura para múltiplos perfis.

## Avatar

* Upload para MinIO
* Avatar associado ao perfil
* URL persistida no banco

## Favoritos

Cada perfil possui:

* Lista de favoritos
* Adição de favoritos
* Remoção de favoritos
* Consulta de favoritos

Endpoints:

POST /profiles/favorites/{videoId}

DELETE /profiles/favorites/{videoId}

GET /profiles/favorites

---

# Funcionalidades Concluídas

## Prioridade 1

### Thumbnail Pública

Status:

CONCLUÍDO

---

## Prioridade 2

### Categorias Fortes

VideoCategory como enum.

Status:

CONCLUÍDO

---

## Prioridade 3

### Busca de Vídeos

Busca textual por título.

Status:

CONCLUÍDO

---

## Prioridade 4

### Separação User / UserProfile

Estrutura preparada para evolução futura.

Status:

CONCLUÍDO

---

## Prioridade 5

### Estatísticas de Consumo

Métricas de visualização e progresso.

Status:

CONCLUÍDO

---

## Prioridade 6

### Favoritos

* Adicionar favorito
* Remover favorito
* Listar favoritos

Status:

CONCLUÍDO

---

## Prioridade 7

### Avatar via MinIO

* Bucket próprio
* Upload
* Persistência da URL
* Integração com perfil

Status:

CONCLUÍDO

---

# Próximas Prioridades

## Prioridade 8

### Histórico Completo

Objetivo:

Permitir que o usuário visualize tudo que assistiu.

Endpoints previstos:

GET /history

Possíveis dados:

* Vídeo
* Data da visualização
* Última posição
* Tempo assistido

---

## Prioridade 9

### Home Netflix

Endpoint agregador:

GET /home

Retornando:

* Trending
* Continue Watching
* Categorias
* Recomendados
* Últimos adicionados

Objetivo:

Reduzir múltiplas chamadas do frontend.

---

## Prioridade 10

### Continue Watching Enriquecido

Atualmente existe playback.

A evolução consiste em retornar:

* Thumbnail
* Título
* Categoria
* Percentual assistido
* Duração
* Tempo restante

Experiência semelhante à Netflix.

---

## Prioridade 11

### Múltiplos Perfis

Evolução do modelo atual.

Hoje:

User
└── UserProfile

Futuro:

User
├── Profile 1
├── Profile 2
├── Profile 3
└── Profile 4

Cada perfil possuirá:

* Histórico próprio
* Favoritos próprios
* Continue Watching próprio
* Recomendações próprias

---

## Prioridade 12

### Controle Parental

Dependência:

Prioridade 11

Recursos previstos:

* Perfil Infantil
* Restrição por classificação indicativa
* Limite de idade
* Bloqueio de conteúdo adulto

Exemplo:

KIDS

Permite apenas:

* Livre
* 10 anos

ADULT

Permite:

* Todo catálogo

---

# Evoluções Futuras (Longo Prazo)

## Recomendações

Motor de recomendação baseado em:

* Histórico
* Favoritos
* Categorias assistidas

---

## Trending Inteligente

Vídeos populares por:

* Dia
* Semana
* Mês

---

## Sistema de Avaliações

Curtir / Não Curtir

ou

Avaliação por estrelas.

---

## Notificações

Integração via RabbitMQ para:

* Novo vídeo
* Vídeo processado
* Recomendações

---

## Dashboard Administrativo

Métricas:

* Vídeos enviados
* Vídeos assistidos
* Usuários ativos
* Tempo médio de visualização

---

# Objetivo Final

Construir uma plataforma de streaming inspirada em arquiteturas utilizadas por Netflix, Prime Video e Disney+, aplicando:

* Clean Architecture
* DDD
* Event-Driven
* Mensageria
* Cloud Storage
* Processamento Assíncrono
* Escalabilidade
* Boas práticas de backend moderno
