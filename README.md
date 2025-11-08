# 🛡️ Sistema de Validação Antifraude - Backend

Este projeto é um backend desenvolvido em **Spring Boot** e **Python** que processa imagens de **biometria facial**, **biometria digital** e **documentoscopia**, detecta fraudes e notifica o sistema interno da **QUOD**. Os dados são persistidos em um banco **MongoDB**.

---

## 📦 Funcionalidades

- ✅ Validação de biometria facial e digital
- ✅ Verificação de documentos com análise facial
- ✅ Validação de metadados (data, localização, fabricante)
- ✅ Detecção de fraudes simuladas (Deepfake, máscara, etc.)
- ✅ Notificação automática de fraudes ao sistema interno
- ✅ Persistência dos dados em MongoDB
- ✅ Histórico de notificações com paginação
- ✅ Arquitetura baseada em containers Docker

---

## 🚀 Como rodar o projeto

## Back-end
### Pré-requisitos

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

---

1. Buildar os containers
docker-compose build
Isso irá compilar o backend Java e instalar as dependências do serviço Python.

2. Subir os containers
docker-compose up
A aplicação estará disponível em:

Backend Java: http://localhost:8080

Serviço de verificação facial (Python): http://localhost:5001

3. Parar os containers
docker-compose down

## APP Kotlin
### Pré-requisitos

- [Android Studio](https://developer.android.com/studio)
- Emulador Android ou dispositivo físico com Android 8.0 ou superior
- Conexão com a mesma rede local do backend (ou backend exposto via internet)

---

### Passos para rodar o app

1. Build e execute o projeto:
Clique em Run ▶️ no Android Studio
Escolha um emulador ou dispositivo conectado

---

🛠️ Estrutura dos serviços

backendantifraude/: Backend principal (Spring Boot)
face-verification-service/: Serviço Python para validação facial via DeepFace

📡 Endpoints principais

POST /api/biometria/processar: Validação de biometria facial e digital
POST /api/notificacoes/fraude: Recebe notificações de fraude
POST /face/verify : Validação das faces presentes em documentos comparado com selfies
POST /api/notificacoes/fraude: Notificações de tentativas de validações/biometrias (fraude ou sucesso)
GET /api/notificacoes: Stream de notificações (SSE)
GET /api/notificacoes/historico: Histórico paginado de notificações


☁️ MongoDB
A aplicação utiliza um cluster MongoDB Atlas. A URI de conexão está definida em docker-compose.yml, via SPRING_DATA_MONGODB_URI.
