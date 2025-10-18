# 📋 API de Usuário - Agendador

## 📖 Sobre
API para gerenciamento de usuários do sistema Agendador, incluindo cadastro, autenticação e informações de contato.

## 🔗 URL Base
```
http://localhost:8083
```

## 🔐 Autenticação
A API utiliza autenticação Bearer Token (JWT):
```http
Authorization: Bearer <seu_token_jwt>
```

## 📝 Endpoints

### 👤 Usuários

#### 🔍 Buscar Usuário por Email
**GET** `/usuario?email={email}`

**Parâmetros:**
- `email` (query): Email do usuário

**Respostas:**
- `200`: Usuário encontrado
- `404`: Usuário não encontrado
- `500`: Erro de servidor

#### ➕ Criar Usuário
**POST** `/usuario`

**Body:**
```json
{
  "nome": "string",
  "email": "string",
  "senha": "string",
  "enderecos": [
    {
      "rua": "string",
      "numero": 0,
      "complemento": "string",
      "cidade": "string",
      "estado": "string",
      "cep": "string"
    }
  ],
  "telefones": [
    {
      "numero": "string",
      "ddd": "string"
    }
  ]
}
```

#### ✏️ Atualizar Usuário
**PUT** `/usuario`

**Body:** Mesmo schema de criação

#### 🗑️ Deletar Usuário
**DELETE** `/usuario/{email}`

#### 👥 Listar Usuários
**GET** `/usuario/listar`

### 🔐 Login
**POST** `/usuario/login`

**Body:**
```json
{
  "email": "string",
  "senha": "string"
}
```

### 📞 Telefones

#### Adicionar Telefone
**POST** `/usuario/telefone`

**Body:**
```json
{
  "numero": "string",
  "ddd": "string"
}
```

#### Atualizar Telefone
**PUT** `/usuario/telefone?id={id}`

### 🏠 Endereços

#### Adicionar Endereço
**POST** `/usuario/endereco`

**Body:**
```json
{
  "rua": "string",
  "numero": 0,
  "complemento": "string",
  "cidade": "string",
  "estado": "string",
  "cep": "string"
}
```

#### Atualizar Endereço
**PUT** `/usuario/endereco?id={id}`

#### Buscar Endereço por CEP
**GET** `/usuario/endereco/{cep}`

## 🚀 Exemplos de Uso

### Criar Usuário
```bash
curl -X POST "http://localhost:8083/usuario" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

### Login
```bash
curl -X POST "http://localhost:8083/usuario/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "123456"
  }'
```

### Buscar Usuário
```bash
curl -X GET "http://localhost:8083/usuario?email=joao@email.com" \
  -H "Authorization: Bearer <token>"
```

## 📊 Modelos

### Usuário
```typescript
{
  nome: string;
  email: string;
  senha: string;
  enderecos: Endereco[];
  telefones: Telefone[];
}
```

### Telefone
```typescript
{
  numero: string;
  ddd: string;
}
```

### Endereço
```typescript
{
  rua: string;
  numero: number;
  complemento: string;
  cidade: string;
  estado: string;
  cep: string;
}
```
