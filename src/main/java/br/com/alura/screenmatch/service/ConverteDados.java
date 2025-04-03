package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados{

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        /*TIP Então, após return passamos mapper que é o objeto do Jackson que faz a
          conversão, e pediremos para realizar a leitura do json e tente transformar
          na classe que a pessoa passou. Então escrevemos .readValue(json, classe).*/

        /*TIP
          Notamos que como o readValue lança uma exceção,
          precisamos cobri-lo com um try/catch, ou jogar na assinatura do metodo.
         */
    }
}
