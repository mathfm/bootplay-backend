## Projeto Backend 

##### [Frontend](https://github.com/mathfm/bootplay-frontend)

## Descrição 
Feito durante um bootcamp. utilizando o java17 com springboot, spring data jpa, spring security e o rabbitmq. O projeto consiste no backend de uma loja de albuns integrada com a api do spotify.




## Funcionalidades

- Adicionar e remover albums de musica
- Registro de usuário
- Buscar albuns


## Melhorias Futuras

Adicionar mais camadas de validações

Adicionar opção de editar e apagar usuário


    

## Instalação

Executar o seguinte comando na raiz do projeto

```bash
  docker-compose -f docker-compose.yml build
  docker-compose -f docker-compose.yml up
```



#### IntegrationAPI rodando na porta: 8081
#### Documentação http://localhost:8081/api/swagger-ui/index.html#/

#### UserAPI rodando na porta: 8080
#### Documentação http://localhost:8080/api/swagger-ui/index.html#/
