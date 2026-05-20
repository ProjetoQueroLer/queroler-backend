
################### Fazer login de usuário ######################
 curl -v -X 'POST' \
  'http://localhost:8080/logins' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "user": "saulo3@db.tec.br",
  "senha": "Teste123@"
}'
############### Criar um livro com capa #######################
curl -v -X 'POST' \
  'http://localhost:8080/livros' \
  -H 'accept: */*' \
  -H 'Content-Type: multipart/form-data' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdWVyb19sZXIiLCJzdWIiOiJzYXVsbzNAZGIudGVjLmJyIiwidXNlcmlkIjozLCJyb2xlIjoiTEVJVE9SIiwiaWF0IjoxNzc5MzAzMjYzLCJqdGkiOiIyMjk5NDNlOC1iZjA2LTQxNWUtYTMyNi1kZTBlNTA3YjgxNDMiLCJleHAiOjE3NzkzMTA0NjN9.pSMIz76A93wHHyGAzGGqzDikQFmD9pkU1o1i7quUqm4' \
  -F 'imagem=/home/renanalves/Imagens/queroler/capa-dostoievski-Os-Irmaos-Karamazov.jpg;type=image/jpeg' \
  -F 'dados="titulo":"Os Irmãos Karamazov","isbn":"999","editora":"Generica","anoDePublicacao":"1999","numeroDePaginas":300,"idioma":"Portugues","sinopse":"Uma obra de romance do famoso Fiodor Dostoievsk","autores":[{"nome":"Fiodor Dostoievsk"}]'

##################### Criar um diario de leitura ####################3
curl -v -X 'POST' \
  'http://localhost:8080/leituras' \
  -H 'accept: */*' \
  -H 'Content-Type: multipart/form-data' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdWVyb19sZXIiLCJzdWIiOiJzYXVsbzNAZGIudGVjLmJyIiwidXNlcmlkIjozLCJyb2xlIjoiTEVJVE9SIiwiaWF0IjoxNzc5MzAzMjYzLCJqdGkiOiIyMjk5NDNlOC1iZjA2LTQxNWUtYTMyNi1kZTBlNTA3YjgxNDMiLCJleHAiOjE3NzkzMTA0NjN9.pSMIz76A93wHHyGAzGGqzDikQFmD9pkU1o1i7quUqm4' \
  -f '{
  "livroId": 1,
  "inicioDaLeitura": "2026-05-20T19:19:13.573Z",
  "paginasLidas": 1,
  "nota": 0,
  "tituloDaResenha": "Dostoiesvisk",
  "resenha": "Sem resenha"
}'

######################## consultar livros ###########################
curl -X 'GET' \
  'http://localhost:8080/livros?page=0&size=8&sort=titulo,asc' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxdWVyb19sZXIiLCJzdWIiOiJzYXVsbzNAZGIudGVjLmJyIiwidXNlcmlkIjozLCJyb2xlIjoiTEVJVE9SIiwiaWF0IjoxNzc5MzAzMjYzLCJqdGkiOiIyMjk5NDNlOC1iZjA2LTQxNWUtYTMyNi1kZTBlNTA3YjgxNDMiLCJleHAiOjE3NzkzMTA0NjN9.pSMIz76A93wHHyGAzGGqzDikQFmD9pkU1o1i7quUqm4'

########################## ##########################3333
############# STASH ###################
curl -v -X 'POST'   --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo2@db.tec.br",\n   "confirmarEmail":"saulo@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "28878332860",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/logins \\n  --header 'Content-Type: application/json' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --data '{\n"user": "saulo@db.tec.br",\n"senha": "Teste123@"\n}'

  curl --request POST \\n  --url http://localhost:8080/logins \\n  --header 'Content-Type: application/json' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --data '{\n"user": "saulo@db.tec.br",\n"senha": "Teste123@"\n}'
  
  curl --request POST \\n  --url http://localhost:8080/logins \\n  --header 'Content-Type: application/json' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --data '{\n"user": "saulo@db.tec.br",\n"senha": "Teste123@"\n}'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo@db.tec.br",\n   "confirmarEmail":"saulo@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "28878332860",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=@C:\Users\saulo.rodrigues\Desktop\Saulo de gravata2.jpg'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo@db.tec.br",\n   "confirmarEmail":"saulo@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "28878332860",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=@~/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo@db.tec.br",\n   "confirmarEmail":"saulo@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "28878332860",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=~/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo@db.tec.br",\n   "confirmarEmail":"saulo@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "28878332860",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'


  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo2@db.tec.br",\n   "confirmarEmail":"saulo2@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "11111111111",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo2@db.tec.br",\n   "confirmarEmail":"saulo2@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "11111111111",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo2@db.tec.br",\n   "confirmarEmail":"saulo2@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "11111111111",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo3@db.tec.br",\n   "confirmarEmail":"saulo3@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "11111111111",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo3@db.tec.br",\n   "confirmarEmail":"saulo3@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "21111111111",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl -X 'POST' \\n  'https://queroler-backend.onrender.com/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -X 'POST' \\n  'https://queroler-backend.onrender.com/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -X 'POST' \\n  'https://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -X 'POST' \\n  'https://localhost/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo3@db.tec.br",\n   "confirmarEmail":"saulo3@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "21111111111",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl --request POST \\n  --url http://localhost:8080/usuarios \\n  --header 'Content-Type: multipart/form-data' \\n  --header 'User-Agent: insomnia/12.3.1' \\n  --form 'dados={\n\t"nome":"Saulo",\n   "email": "saulo3@db.tec.br",\n   "confirmarEmail":"saulo3@db.tec.br",\n   "senha": "Teste123@",\n   "confirmarSenha": "Teste123@",\n   "cpf": "21111111111",\n  "dataDeNascimento": "2000-12-05",\n   "checkTermo": true\t\n}' \\n  --form 'imagem=/home/renanalves/imagem.png'

  curl -X 'POST' \\n  'https://localhost/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -X -v 'POST' \\n  'https://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -X -V 'POST' \\n  'https://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -v -X 'POST' \\n  'https://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -v -X 'POST' \\n  'http://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -v -X 'POST' \\n  'http://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'


  curl -v -X 'POST' \\n  'http://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

  curl -v -X 'POST' \\n  'http://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'


  curl -v -X 'POST' \\n  'http://localhost:8080/logins' \\n  -H 'accept: */*' \\n  -H 'Content-Type: application/json' \\n  -d '{\n  "user": "saulo3@db.tec.br",\n  "senha": "Teste123@"\n}'

