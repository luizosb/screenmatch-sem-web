package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {


    private Scanner leitura = new Scanner(System.in);

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();

    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private static final String API_KEY = "&apikey=497dfcec";
    /*
     Ao declarar uma constante, é importante seguir algumas boas práticas.
     A nomenclatura das constantes deve ser clara e descritiva, utilizando letras maiúsculas e separando as palavras por underscore (_)
     */

    /*
    Além disso, é uma boa prática declarar as constantes como static caso elas pertençam a uma classe e sejam compartilhadas por vários objetos.
     */
    /*
     
     */

    public void exibeMenu(){
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = converteDados.obterDados(json,DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadaList = new ArrayList<>();

		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++){
			json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season="+ i + API_KEY);
			DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
			temporadaList.add(dadosTemporada);
		}
		temporadaList.forEach(System.out::println);

        temporadaList.forEach(t -> t.episodios().forEach(e-> System.out.println(e.titulo())));

        temporadaList.forEach(System.out::println);
    }
}
