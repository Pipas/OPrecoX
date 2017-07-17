package software.pipas.oprecox.modules.categories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import software.pipas.oprecox.R;

public abstract class CategoryHandler
{
    private static ArrayList<String> selected = new ArrayList<>();
    private static ArrayList<ParentCategory> categories = new ArrayList<>();

    /*public String getName(int ID)
    {
        switch (ID)
        {
            case 1:
                return "Acessórios";
            case 2:
                return "Animais";
            case 3:
                return "Roupa";
            case 4:
                return "Calçado";
            case 5:
                return "Outros artigos";
            case 6:
                return "Carros";
            case 7:
                return "Peças";
            case 8:
                return "Outros veículos";
            case 9:
                return "Artigos desportivos";
            case 10:
                return "Bicicletas";
            case 11:
                return "Apartamentos";
            case 12:
                return "Moradias ou Casas";
            case 13:
                return "Outros imóveis";
            case 14:
                return "Brinquedos ou jogos";
            case 15:
                return "Instrumentos musicais";
            case 16:
                return "Livros ou Revistas";
            case 17:
                return "Colecções ou Antiguidades";
            case 18:
                return "DVDs ou Filmes";
            case 19:
                return "CDs, Vinis ou Música";
            case 20:
                return "Roupa";
            case 21:
                return "Calçado";
            case 22:
                return "Joias, Relógios e Bijuteria";
            case 23:
                return "Malas e Acessórios";
            case 24:
                return "Saúde e Beleza";
            case 25:
                return "Utilidades e Decoração";
            case 26:
                return "Electrodomésticos";
            case 27:
                return "Jardim e Bricolage";
            case 28:
                return "Móveis";
            case 29:
                return "Videojogos ou Consolas";
            case 30:
                return "Computadores ou Informática";
            case 31:
                return "Electrónica";
            case 32:
                return "TV, Som e Fotografia";
            case 33:
                return "Acessórios";
            case 34:
                return "Telemóveis ou Tablets";
            case 35:
                return "Outras Vendas";
            default:
                break;
        }
        return null;
    }*/

    private static String getURLEnd(int ID)
    {
        switch (ID)
        {
            case 1:
                return "animais/acessorios-animais";
            case 2:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(10418) + 1;
                if(randomNumber < 907)
                    return "animais/animais-de-quinta";
                else if(randomNumber >= 907 && randomNumber < 919)
                    return "animais/aranhas";
                else if(randomNumber >= 919 && randomNumber < 5158)
                    return "animais/aves";
                else if(randomNumber >= 5158 && randomNumber < 5812)
                    return "animais/cavalos";
                else if(randomNumber >= 5812 && randomNumber < 8516)
                    return "animais/caes";
                else if(randomNumber >= 8516 && randomNumber < 8673)
                    return "animais/gatos";
                else if(randomNumber >= 8673 && randomNumber < 8786)
                    return "animais/insectos";
                else if(randomNumber >= 8786 && randomNumber < 9673)
                    return "animais/peixes";
                else if(randomNumber >= 9673 && randomNumber < 10245)
                    return "animais/roedores";
                else if(randomNumber >= 10245)
                    return "animais/repteis";
            }
            case 3:
                return "bebes-criancas/roupinhas";
            case 4:
                return "bebes-criancas/calcado-bebes";
            case 5:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(44292) + 1;
                if(randomNumber < 10156)
                    return "bebes-criancas/passeio";
                else if(randomNumber >= 10156 && randomNumber < 17911)
                    return "bebes-criancas/refeicao";
                else if(randomNumber >= 17911 && randomNumber < 36562)
                    return "bebes-criancas/relaxar-e-dormir";
                else if(randomNumber >= 36562)
                    return "bebes-criancas/seguranca";
            }
            case 6:
                return "carros-motos-e-barcos/carros";
            case 7:
                return "carros-motos-e-barcos/pecas-e-acessorios-carros";
            case 8:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(16990) + 1;
                if(randomNumber < 5923)
                    return "carros-motos-e-barcos/motociclos-scooters";
                else if(randomNumber >= 5923 && randomNumber < 12635)
                    return "carros-motos-e-barcos/camioes-veiculos-comerciais";
                else if(randomNumber >= 12635 && randomNumber < 13432)
                    return "carros-motos-e-barcos/barcos-lanchas";
                else if(randomNumber >= 13432 && randomNumber < 15092)
                    return "carros-motos-e-barcos/autocaravanas-roulotes-reboques";
                else if(randomNumber >= 15092 && randomNumber < 16504)
                    return "carros-motos-e-barcos/outros-veiculos";
                else if(randomNumber >= 16504)
                    return "carros-motos-e-barcos/salvados";
            }
            case 9:
                return "desporto-e-lazer/artigos-desportivos";
            case 10:
                return "desporto-e-lazer/bicicletas";
            case 11:
                return "imoveis/apartamento-casa-a-venda";
            case 12:
                return "imoveis/casas-moradias-para-arrendar-vender";
            case 13:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(111597) + 1;
                if(randomNumber < 3185)
                    return "imoveis/quartos-para-aluguer";
                else if(randomNumber >= 3185 && randomNumber < 6524)
                    return "imoveis/casas-de-ferias";
                else if(randomNumber >= 6524 && randomNumber < 10020)
                    return "imoveis/permutas";
                else if(randomNumber >= 10020 && randomNumber < 14243)
                    return "imoveis/garagens-estacionamento";
                else if(randomNumber >= 14243 && randomNumber < 65739)
                    return "imoveis/terrenos-quintas";
                else if(randomNumber >= 65739 && randomNumber < 97915)
                    return "imoveis/escritorios-lojas";
                else if(randomNumber >= 97915)
                    return "imoveis/estabelecimentos-comerciais-para-alugar-vender";
            }
            case 14:
                return "lazer/jogos-brinquedos";
            case 15:
                return "lazer/instrumentos-musicais";
            case 16:
                return "lazer/livros-revistas";
            case 17:
                return "lazer/coleccoes-antiguidades";
            case 18:
                return "lazer/dvd-filmes";
            case 19:
                return "lazer/discos-vinil-cds-musica";
            case 20:
                return "moda/roupa-moda";
            case 21:
                return "moda/calcado";
            case 22:
                return "moda/joias-bijuteria-relogios";
            case 23:
                return "moda/malas-e-acessorios";
            case 24:
                return "moda/saude-beleza";
            case 25:
                return "moveis-casa-e-jardim/utilidades-e-decoracao";
            case 26:
                return "moveis-casa-e-jardim/electrodomesticos";
            case 27:
                return "moveis-casa-e-jardim/jardim-e-bricolage";
            case 28:
                return "moveis-casa-e-jardim/moveis-decoracao";
            case 29:
                return "tecnologia-e-informatica/videojogos-consolas";
            case 30:
                return "tecnologia-e-informatica/computadores-informatica";
            case 31:
                return "tecnologia-e-informatica/electronica";
            case 32:
                return "tecnologia-e-informatica/fotografia-tv-som";
            case 33:
                return "telemoveis-e-tablets/acessorios-telemoveis-tablets";
            case 34:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(37845) + 1;
                if(randomNumber < 34124)
                    return "telemoveis-e-tablets/telemoveis";
                else
                    return "telemoveis-e-tablets/tablets";
            }
            case 35:
                return "compra-venda/o-resto";
            default:
                break;
        }
        return null;
    }

    public static boolean checkSelected(int ID)
    {
        return selected.contains(Integer.toString(ID));
    }

    public static boolean checkAllSelected()
    {
        for(int i = 1; i < 36; i++)
            if(!checkSelected(i))
                return false;

        return true;
    }

    public static void add(int ID)
    {
        if(!checkSelected(ID))
            selected.add(Integer.toString(ID));
    }

    public static void remove(int ID)
    {
        if(checkSelected(ID))
            selected.remove(selected.indexOf(Integer.toString(ID)));
    }

    public static String generateURL()
    {
        Random rand = new Random();
        int categoryNumber = rand.nextInt(selected.size());
        return getURLEnd(Integer.parseInt(selected.get(categoryNumber)));
    }

    public static ArrayList<String> getSelected()
    {
        return selected;
    }

    public static void setSelected(ArrayList<String> s)
    {
        selected = s;
    }

    public static void selectAll()
    {
        for(int i = 1; i < 36; i++)
            add(i);
    }

    public static String writeString()
    {
        String s = "";
        for (int i = 0; i < selected.size(); i++)
        {
            s += selected.get(i) + " ";
        }
        return s;
    }

    public static void selectFromString(String input)
    {
        if(input.isEmpty())
            return;
        String[] elements = input.split(" ");
        selected.clear();
        for(int i = 0; i < elements.length; i++)
        {
            selected.add(elements[i]);
        }
    }

    public static void initiateCategories()
    {
        categories.add(new ParentCategory("Animais", new ArrayList<SubCategory>() {{
            add(new SubCategory("Animais", new String[]
                    {
                            "animais/cavalos",
                            "animais/peixes",
                            "animais/aves",
                            "animais/gatos",
                            "animais/animais-de-quinta",
                            "animais/caes",
                            "animais/roedores"
                    }, new Double[]
                    {
                            0.05d,
                            0.07d,
                            0.41d,
                            0.06d,
                            0.09d,
                            0.27d,
                            0.06d
                    }));
            add(new SubCategory("Acessórios", "animais/acessorios-animais"));
        }}, R.drawable.animals));
        categories.add(new ParentCategory("Bebé e Criança", new ArrayList<SubCategory>() {{
            add(new SubCategory("Roupa", "bebes-criancas/roupinhas"));
            add(new SubCategory("Calçado", "bebes-criancas/calcado-bebes"));
        }}, R.drawable.baby));
        categories.add(new ParentCategory("Carros, Motas e Barcos", new ArrayList<SubCategory>() {{
            add(new SubCategory("Carros", "carros-motos-e-barcos/carros"));
            add(new SubCategory("Peças", "carros-motos-e-barcos/pecas-e-acessorios-carros"));
            add(new SubCategory("Outros Veículos", new String[]
                    {
                            "carros-motos-e-barcos/motociclos-scooters",
                            "carros-motos-e-barcos/camioes-veiculos-comerciais",
                            "carros-motos-e-barcos/barcos-lanchas",
                            "carros-motos-e-barcos/autocaravanas-roulotes-reboques",
                            "carros-motos-e-barcos/outros-veiculos",
                            "carros-motos-e-barcos/salvados"
                    }, new Double[]
                    {
                            0.34d,
                            0.41d,
                            0.04d,
                            0.10d,
                            0.07d,
                            0.03d
                    }));
        }}, R.drawable.cars));
        categories.add(new ParentCategory("Desporto", new ArrayList<SubCategory>() {{
            add(new SubCategory("Artigos Desportivos", "desporto-e-lazer/artigos-desportivos"));
            add(new SubCategory("Bicicletas", "desporto-e-lazer/bicicletas"));
        }}, R.drawable.sports));
        categories.add(new ParentCategory("Imóveis", new ArrayList<SubCategory>() {{
            add(new SubCategory("Apartamentos", "imoveis/apartamento-casa-a-venda"));
            add(new SubCategory("Moradias", "imoveis/casas-moradias-para-arrendar-vender"));
            add(new SubCategory("Escritórios e Lojas", "imoveis/escritorios-lojas"));
            add(new SubCategory("Terrenos", "imoveis/terrenos-quintas"));
            add(new SubCategory("Outros Imóveis", new String[]
                    {
                            "imoveis/quartos-para-aluguer",
                            "imoveis/casas-de-ferias",
                            "imoveis/permutas",
                            "imoveis/garagens-estacionamento",
                            "imoveis/estabelecimentos-comerciais-para-alugar-vender"
                    }, new Double[]
                    {
                            0.15d,
                            0.18d,
                            0.13d,
                            0.13d,
                            0.41d
                    }));
        }}, R.drawable.houses));
        categories.add(new ParentCategory("Lazer", new ArrayList<SubCategory>() {{
            add(new SubCategory("Jogos e Brinquedos", "lazer/jogos-brinquedos"));
            add(new SubCategory("Instrumentos Musicais", "lazer/instrumentos-musicais"));
            add(new SubCategory("Livros e Revistas", "lazer/livros-revistas"));
            add(new SubCategory("Antiguidades", "lazer/coleccoes-antiguidades"));
            add(new SubCategory("DVDs e Filmes", "lazer/dvd-filmes"));
            add(new SubCategory("Discos de Vinil, CDs e Música", "lazer/discos-vinil-cds-musica"));
        }}, R.drawable.leisure));
        categories.add(new ParentCategory("Moda", new ArrayList<SubCategory>() {{
            add(new SubCategory("Roupa", "moda/roupa-moda"));
            add(new SubCategory("Calçado", "moda/calcado"));
            add(new SubCategory("Joias, Bijuteria e Relógios", "moda/joias-bijuteria-relogios"));
            add(new SubCategory("Malas e Acessórios", "moda/malas-e-acessorios"));
            add(new SubCategory("Saúde e Beleza", "moda/saude-beleza"));
        }}, R.drawable.fashion));
        categories.add(new ParentCategory("Móveis, Casa e Jardim", new ArrayList<SubCategory>() {{
            add(new SubCategory("Utilidades e Decoração", "moveis-casa-e-jardim/utilidades-e-decoracao"));
            add(new SubCategory("Electrodomésticos", "moveis-casa-e-jardim/electrodomesticos"));
            add(new SubCategory("Jardim e Bricolage", "moveis-casa-e-jardim/jardim-e-bricolage"));
            add(new SubCategory("Móveis e Decoração", "moveis-casa-e-jardim/moveis-decoracao"));
        }}, R.drawable.furniture));
        categories.add(new ParentCategory("Tecnologia e Informática", new ArrayList<SubCategory>() {{
            add(new SubCategory("Videojogos e Consolas", "tecnologia-e-informatica/videojogos-consolas"));
            add(new SubCategory("Computadores e Informática", "tecnologia-e-informatica/computadores-informatica"));
            add(new SubCategory("Electrónica", "tecnologia-e-informatica/electronica"));
            add(new SubCategory("Fotografia, TV e Som", "tecnologia-e-informatica/fotografia-tv-som"));
        }}, R.drawable.technology));
        categories.add(new ParentCategory("Telemoveis e Tablets", new ArrayList<SubCategory>() {{
            add(new SubCategory("Acessórios", "telemoveis-e-tablets/acessorios-telemoveis-tablets"));
            add(new SubCategory("Telemoveis e Tablets", new String[]
                    {
                            "telemoveis-e-tablets/telemoveis",
                            "telemoveis-e-tablets/tablets"
                    }, new Double[]
                    {
                            0.9d,
                            0.1d
                    }));
        }}, R.drawable.phones));
        categories.add(new ParentCategory("Outras Vendas", new ArrayList<SubCategory>() {{
            add(new SubCategory("Outras Vendas", "compra-venda/o-resto"));
        }}, R.drawable.others));
    }

    public static ArrayList<ParentCategory> getCategories()
    {
        return categories;
    }

    public static void setCategories(ArrayList<ParentCategory> categories)
    {
        CategoryHandler.categories = categories;
    }
}
