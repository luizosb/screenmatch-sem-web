package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

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

        List <DadosEpisodio> dadosEpisodioList = temporadaList.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        dadosEpisodioList.stream()
                .filter(e-> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List <Episodio> episodios = temporadaList.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e-> new Episodio(t.numeroTemporada(), e)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite um trecho do titulo do episodio: ");
        var trechoTitulo = leitura.nextLine();

        /*
        O principal uso do Optional é fornecer um tipo de retorno alternativo quando um metodo pode não retornar um valor.
         */
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        /*
        Ao utilizar o findAny em uma coleção com threads, cada thread pode buscar um elemento da coleção de forma
        paralela, tornando a busca mais rápida e eficiente.
        É importante ressaltar que o findAny não garante que sempre será retornado o mesmo elemento, pois a ordem
        de busca pode variar entre as threads.
         */

        if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado: " );
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episodio não encontrado");
        }

//        System.out.println("A partir de qual ano gostaria de ver os episodios?");
//        var ano = leitura.nextInt();
//
//        LocalDate localDate = LocalDate.of(ano, 1, 1);
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e-> e.getDataLancamento() != null && e.getDataLancamento().isAfter(localDate))
//                .forEach(e-> System.out.println(
//                        "Temporada " + e.getTemporada() +
//                                " Episodio " + e.getTitulo() +
//                                " Data lançamento " + e.getDataLancamento())
//                );

        Map <Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                         Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        /*
        A classe DoubleSummaryStatistics é uma classe utilitária que permite calcular estatísticas como soma, média,
        valor mínimo e máximo, além do total de elementos em uma coleção de valores do tipo double.
        Ela possui métodos como getSum(), getAverage(), getMin(), getMax() e getCount(), que retornam os respectivos valores estatísticos.
         */

        System.out.println("Média: " + est.getAverage());

    }
}
