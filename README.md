# cthulhu.character.generator

Project made for randomly generating a Call of Cthulhu Character 

## Deploy na Vercel (container)

A Vercel não roda Java como função; este projeto sobe pelo runtime de container, a partir do `Dockerfile` da raiz.

1. Na Vercel: **Add New → Project**, importe este repositório. Sem framework detectado, a Vercel usa o `Dockerfile`.
2. Variáveis de ambiente (opcionais):
   - `CORS_ORIGINS`: domínios que podem chamar a API, separados por vírgula (padrão: qualquer um). Ex.: `https://cthulhu-front.vercel.app,https://secret-game-gamma.vercel.app`
   - `PROFILE`: perfil do Spring (padrão `prod`).
3. A porta vem da variável `PORT` que a plataforma define; sem ela, 8080.

A API fica em `https://<projeto>.vercel.app/char-generator/random-char`.
