> **Aposentado (2026-11-11).** Este serviço foi substituído pelo [cthulhu-char-golang](https://github.com/lucasmagalhaees/cthulhu-char-golang), com as mesmas rotas e o mesmo JSON, que sobe em milissegundos em vez de ~10 s. O cthulhu-front e o Secret Cultist já usam o Go. Este repositório fica só de referência; para arquivar: Settings → General → Archive this repository.

# cthulhu.character.generator

Project made for randomly generating a Call of Cthulhu Character 

## Deploy na Vercel (container)

A Vercel não roda Java como função; este projeto sobe pelo runtime de container, a partir do `Dockerfile` da raiz.

1. Na Vercel: **Add New → Project**, importe este repositório. Sem framework detectado, a Vercel usa o `Dockerfile`.
2. Variáveis de ambiente (opcionais):
   - `PROFILE`: perfil do Spring (padrão `prod`).
3. O `vercel.json` força o preset de container. A Vercel manda o tráfego para a porta 80, a menos que `PORT` esteja nas variáveis do projeto; o app escuta em `PORT` (80 sem ela).

A API fica em `https://<projeto>.vercel.app/char-generator/random-char`.
