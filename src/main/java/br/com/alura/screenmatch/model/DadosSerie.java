package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
Precisamos informar o sistema que o que não foi mapeado não deve ser convertido.
Para isso, escrevemos @JsonIgnoreProperties(ignoreUnknown = true).
Assim, definimos que tudo o que não for encontrado seja ignorado.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating")String avaliacao   ) {
}
/*
Vamos apenas obter os dados e representá-los,
não precisaremos trabalhar com a classe e criar métodos, por exemplo.
 */

/*
JSON ALIAS
É importante sempre digitar exatamente como está no json de resposta.
Caso não, o Java não irá encontrar e não conseguiremos mapear a aplicação para apelido.
 */

/*
JSON PROPERTY
Pode ser utilizado tanto no processo de serialização quanto de desserialização.
Isso significa que, se no fim do código escrevermo o trecho @JsonProperty("imdbVotes") String votos,
ao gerar um json com dados sequenciais será incluído o nome "imdb votes". Então, tenta ler imdb votes e escrever também.
 */

/*
DIFERENÇA DE JSON ALIAS E PROPERY
Já o JsonAlias é utilizado apenas para ler o json.
Sendo assim, lerá o title, porém quando for escrever utilizará o nome original do atributo, que é título.
 */