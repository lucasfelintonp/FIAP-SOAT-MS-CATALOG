# language: pt
Funcionalidade: Testar os endpoints de categorias dos produtos
  São testados os endpoints que constroem a definição da categoria dos produtos.

  Cenario: Consultar todas as categorias existentes
    Dado que existem as seguintes categorias cadastradas
      | id | name            |
      | 1  | Lanche          |
      | 2  | Bebida          |
      | 3  | Sobremesa       |
      | 4  | Acompanhamentos |
    Quando eu realizar um requisicao GET para "/api/v1/product-categories"
    Entao deve retornar status 200
    E deve retornar um JSON com o schema "product-categories-list-schema.json"
